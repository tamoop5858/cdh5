/**
 *
 */
package jp.co.fd.hadoop.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;
import jp.co.fd.hadoop.common.log.HDFSLogger;
import jp.co.fd.hadoop.common.util.CounterID;
import jp.co.fd.hadoop.common.util.GeneralOutputsUtil;
import jp.co.fd.hadoop.common.util.HDFSUtil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * @author YIDATEC
 * @param <GeneralKeyBean>
 * @param <GeneralValueBean>
 * @param <KEYIN>
 * @param <VALUEIN>
 * @param <KEYOUT>
 * @param <VALUEOUT>
 *
 */
abstract public  class BaseReducer<OUTKEY, OUTVALUE> extends
		ReducerExt<GeneralKeyBean, GeneralValueBean, OUTKEY, OUTVALUE> {

	private String mapReduceId = null;
	private String mapReduceName = null;
	private HDFSLogger hdfsLogger = null;
	private Map<CounterID, Long> correctCount = null;
	private Map<Integer, Counter> counterMap = null;
	private String baseName = null;
	private String basePath = null;

	protected GeneralOutputsUtil<GeneralKeyBean, GeneralValueBean> multiOutputs = null;

	public BaseReducer(String mapReduceId, String mapReduceName) {
		this.setMapReduceId(mapReduceId);
		this.setMapReduceName(mapReduceName);
	}
	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		int maxLines = 0;
		maxLines = conf.getInt("mapReduce.reduce.log.max.lines", 10000);
		baseName = conf.get("mapReduce.reduce.base.name","");
		basePath = "/hdfs/tem/log/".concat(baseName);

		multiOutputs = new GeneralOutputsUtil(context);
		StringBuffer sb = new StringBuffer();
		sb.append(basePath);
		sb.append("/");
		sb.append(baseName);
		hdfsLogger = new HDFSLogger(context, sb.toString(), maxLines, false);

		correctCount = new HashMap<CounterID, Long>();
		counterMap = new HashMap<Integer, Counter>();

		try {
			setupEx(context, conf);
		} catch (ProcessException e) {
			throw new IOException(e);
		} catch (IllegalDataException e) {
			throw new IOException(e);
		}
	}

	public abstract void setupEx(Context context, Configuration conf) throws IOException,
			InterruptedException, ProcessException, IllegalDataException;

	@Override
	public void reduce(GeneralKeyBean key, Iterable<GeneralValueBean> values, Context context) throws
		IOException, InterruptedException {
		try {
			reduceEx(key, values, context);
		} catch (IllegalDataException e) {
			outputAppLog(e, context, true);
			try {
				incrementCount(context, CounterID.ERROR_TOTAL_RECORD_RECORD, 1);
			} catch (ProcessException processException) {
				outputAppLog(processException, context, false);
				throw new IOException(processException);
			}
		} catch (ProcessException e) {
			outputAppLog(e, context, true);
			try {
				incrementCount(context, CounterID.ERROR_TOTAL_RECORD_RECORD, 1);
			} catch (ProcessException processException) {
				outputAppLog(processException, context, false);
				throw new IOException(processException);
			}
		} catch (IOException e) {
			outputAppLog(e, context, false);
			throw new IOException(e);
		} catch (InterruptedException e) {
			outputAppLog(e, context, false);
			throw new IOException(e);
		}
	}
	public abstract void reduceEx(GeneralKeyBean key, Iterable<GeneralValueBean> values, Context context) throws IOException,
	InterruptedException, ProcessException, IllegalDataException;
	@Override
	public void cleanup(Context context) throws IOException,
			InterruptedException {
		Configuration conf = context.getConfiguration();
		try {
			cleanupEx(context, conf);
		} catch (ProcessException e) {
			throw new IOException(e);
		} catch (IllegalDataException e) {
			throw new IOException(e);
		} finally {
			if (hdfsLogger != null) {
				hdfsLogger.close();
			}
			if (multiOutputs != null) {
				multiOutputs.close();
			}
		}
	}

	public abstract void cleanupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException;

	/**
	 *
	 * @param context
	 * @param counterid
	 * @param addValue
	 * @throws ProcessException
	 */
	public void incrementCount(Context context, CounterID counterid, int addValue) throws ProcessException{
		Counter counter = null;
		if (counterid == null) {
			throw new ProcessException();
		}
		if (counterMap.containsKey(counterid.getId())) {
			counter = counterMap.get(counterid.getId());
		} else {
			counter = context.getCounter(counterid);
			counterMap.put(counterid.getId(), counter);
		}
		counter.increment(addValue);
	}
	public void incrementCountCorrect(CounterID counterid, int addValue) {
		if (correctCount.containsKey(counterid)) {
			Long count = correctCount.get(counterid);
			correctCount.put(counterid, Long.valueOf(count + addValue));
		} else {
			correctCount.put(counterid, Long.valueOf(addValue));
		}
	}
	public void outputAppLog(Exception exception, Context context, boolean isCheckMaxLine) {
		if (exception == null) {
			return;
		}
		boolean iswrited = false;
		if (hdfsLogger != null) {
			String errorMessage = "Reducer�����ɂُ͈킪�������܂����B";
			iswrited = hdfsLogger.write(errorMessage, isCheckMaxLine);
		}
		if (!iswrited) {
			try {
				incrementCount(context, CounterID.LOG_OUTPUT_FAIL_COUNTER,1);
			} catch (ProcessException e) {

			}
		}
	}

	public void setCount(Context context, CounterID counterid, long  setValue, int countType) throws ProcessException {
		if (context == null || counterid == null) {
			new ProcessException();
		}
		StringBuffer fileName = new StringBuffer();
		fileName.append(counterid.getId()).append("_");
		fileName.append(context.getTaskAttemptID());
		fileName.append("seq");
		Writer writer = null;
		String fullPath = null;

		try {
			Path tempDirPath = FileOutputFormat.getWorkOutputPath(context);
			fullPath = tempDirPath.toUri().toString();
			Configuration conf = context.getConfiguration();
			writer = SequenceFile.createWriter(FileSystem.get(conf), conf, new Path(tempDirPath, fileName.toString()),
					IntWritable.class, LongWritable.class);
			writer.append(new IntWritable(counterid.getId()), new LongWritable((long)setValue));
		} catch (IOException e) {
			new ProcessException(e);
		} catch (InterruptedException e) {
			new ProcessException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					new ProcessException(e);
				}
			}
		}

	}
	public void fixCountCorrect(Context context, CounterID counterid) throws ProcessException {
		if (context == null || counterid == null) {
			new ProcessException();
		}
		long counterValue = 0;
		if (correctCount.containsKey(counterid)) {
			counterValue = correctCount.get(counterid).longValue();
		}
		setCount(context, counterid, counterValue, 3);
	}
	public void setMapReduceId(String mapReduceId) {
		this.mapReduceId = mapReduceId;
	}
	public void setMapReduceName(String mapReduceName) {
		this.mapReduceName = mapReduceName;
	}

	protected List<Writable> getSequenceFileObject(Configuration conf,
			String target) throws ProcessException {

		if (conf == null || target == null || target.isEmpty()) {
			ProcessException exception = new ProcessException();
			exception.setMessage("BaseReducer getSequenceFileObject error");
			throw exception;
		}
		List<String> fileList = new ArrayList<String>();
		List<Writable> dataList = new ArrayList<Writable>();
		try {
			fileList = HDFSUtil.getFileList(conf, target);
		} catch (IOException ioException) {
			ProcessException exception = new ProcessException(ioException);
			exception.setMessage("BaseReducer getSequenceFileObject error");
			throw exception;
		}
		for (String fileName : fileList) {
			List<Writable> getData = new ArrayList<Writable>();
			try {
				getData = HDFSUtil.readSequenceFileList(conf, fileName);
				dataList.addAll(getData);
			} catch (IOException ioException) {

				ProcessException exception = new ProcessException(ioException);
				exception.setMessage("BaseReducer getSequenceFileObject error");
				throw exception;
			}
		}
		return dataList;
	}
    protected List<String> getFileRecord(Configuration conf, String basePath, String fileName) throws ProcessException {

            if ((conf == null) || (basePath == null) || (basePath.isEmpty())
                || (fileName == null) || (fileName.isEmpty())) {
                ProcessException exception = new ProcessException();
                exception.setMessage("BaseReducer getFileRecord error");
                throw exception;
            }

            if (!(basePath.endsWith(CommonConst.FILE_SEPARATER))) {
                basePath = basePath.concat(CommonConst.FILE_SEPARATER);
            }

            List<String> fileLists = null;
            try {
                fileLists = HDFSUtil.getFileList(conf, basePath.concat(fileName));
            } catch (IOException ioException) {
                ProcessException exception = new ProcessException(ioException);
                exception.setMessage("BaseReducer getFileRecord error");
                throw exception;

            }
            if (fileLists.size() != 1) {
                ProcessException exception = new ProcessException();
                exception.setMessage("BaseReducer getFileRecord error");

                throw exception;
            }

            String filePath = fileLists.get(0);
            List<String> fileRecords = null;
            try {

                fileRecords = HDFSUtil.readRecords(conf, filePath);

            } catch (IOException ioException) {

                ProcessException exception = new ProcessException(ioException);
                exception.setMessage("BaseReducer getFileRecord error");

                throw exception;
            }

            if (fileRecords == null) {
                ProcessException exception = new ProcessException();
                exception.setMessage("BaseReducer getFileRecord error");
                throw exception;
            }
            return fileRecords;
        }
}
