package jp.co.fd.hadoop.common.util;

import java.io.IOException;

import jp.co.fd.hadoop.common.CommonConst;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;



public class SequenceFileOutputFormatExt<K, V> extends FileOutputFormat<K, V> {

	public SequenceFileOutputFormatExt() {

	}

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();

		CompressionCodec codec = null;
		CompressionType compressionType = CompressionType.NONE;
		if (getCompressOutputExt(context)) {
			compressionType = getOuutputCompressionType(context);
			Class<?> codecClass = getOutputCompressorClass(context, DefaultCodec.class);
			codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
		}

		Path file = getDefaultWorkFile(context, "");
		FileSystem fs = file.getFileSystem(conf);

		final SequenceFile.Writer out = SequenceFile.createWriter(fs, conf, file,
				context.getOutputKeyClass(), context.getOutputValueClass(), compressionType, codec, context);

		return new RecordWriter<K,V>() {

			@Override
			public void close(TaskAttemptContext context) throws IOException,
					InterruptedException {
				out.close();

			}
			@Override
			public void write(K key, V value) throws IOException,
					InterruptedException {
				out.append(key, value);

			}};
	}

	public static CompressionType getOuutputCompressionType(JobContext job) {
		String type = job.getConfiguration().get(CommonConst.MAPRED_OUTPUT_COMPRESS_TYPE, CompressionType.RECORD.toString());
		return CompressionType.valueOf(type);
	}
	public static void setOuutputCompressionType(Job job, CompressionType type) {
		setCompressOutputExt(job, true);
		job.getConfiguration().get(CommonConst.MAPRED_OUTPUT_COMPRESS_TYPE, type.toString());

	}
	public static boolean getCompressOutputExt(JobContext job) {
		Configuration conf = job.getConfiguration();
		String compress = conf.get(CommonConst.MAPRED_OUTPUT_COMPRESS_EXT);
		if (compress == null) {
			return conf.getBoolean(CommonConst.MAPRED_OUTPUT_COMPRESS, false);
		}
		return Boolean.valueOf(compress);
	}
	public static void setCompressOutputExt(Job job, boolean  compress) {

		job.getConfiguration().setBoolean(CommonConst.MAPRED_OUTPUT_COMPRESS_EXT, compress);

	}
}

