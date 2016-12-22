package jp.co.fd.hadoop.common.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jp.co.fd.hadoop.common.CommonConst;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;




public class TextFileOutputFormatExt<K, V> extends FileOutputFormat<K, V> {

	public TextFileOutputFormatExt() {

	}

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		Configuration conf = job.getConfiguration();
		String keyvalueSeparator = conf.get(CommonConst.TEXTOUTPUT_FORMAT_SEPARATOR, CommonConst.STRING_COMMA);
		String newline  = conf.get(CommonConst.TEXTOUTPUT_NEWLINE, CommonConst.LINE_SEPARATER);
		boolean isCompressed = getCompressOutput(job);

		CompressionCodec codec = null;
		String extension = "";

		if (isCompressed) {
			Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(job, GzipCodec.class);
			codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
		}
		Path file = getDefaultWorkFile(job, extension);
		FileSystem fs = file.getFileSystem(conf);

		if (!isCompressed) {
			FSDataOutputStream fileOut = fs.create(file, false);
			return new LineRecordWriter<K, V>(fileOut, keyvalueSeparator, newline);
		} else {
			FSDataOutputStream fileOut = fs.create(file, false);
			return new LineRecordWriter<K, V>(new DataOutputStream(codec.createOutputStream(fileOut)),
					keyvalueSeparator, newline);
		}
	}

	public static class  LineRecordWriter<K, V> extends RecordWriter<K, V>{
		public DataOutputStream out = null;
		private byte[] newline = null;
		private byte[] keyvalueSeparator = null;

		public LineRecordWriter(DataOutputStream out, String keyvalueSeparator, String newline) throws IllegalArgumentException {
			this.out = out;
			try {
				if (keyvalueSeparator != null) {
					this.keyvalueSeparator = keyvalueSeparator.getBytes(CommonConst.CHARSET_UTF8);
				}
				if (newline != null) {

					this.newline = newline.getBytes(CommonConst.CHARSET_UTF8);
				}
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("can not encoding");
			}
		}
		public LineRecordWriter(DataOutputStream out) {
			this(out, CommonConst.STRING_COMMA, CommonConst.LINE_SEPARATER);
		}
		private void writeObject(Object object) throws IOException {
			if (object instanceof Text) {
				Text text = (Text) object;
				out.write(text.getBytes(), 0, text.getLength());
			} else {
				out.write(object.toString().getBytes(CommonConst.CHARSET_UTF8));
			}
		}
		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			out.close();
		}
		@Override
		public void write(K key, V value) throws IOException,
				InterruptedException {
			boolean nullkey = false;
			boolean nullvalue = false;

			if (key == null || key instanceof NullWritable) {
				nullkey = true;
			}
			if (value == null || value instanceof NullWritable) {
				nullvalue = true;
			}

			if (nullkey && nullvalue) {
				return;
			}
			if (!nullkey) {
				writeObject(key);
			}
			if (!(nullkey || nullvalue)) {
				out.write(keyvalueSeparator);
			}
			if (!nullvalue) {
				writeObject(value);
			}
			out.write(newline);
		}
	}

}

