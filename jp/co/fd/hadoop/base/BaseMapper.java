/**
 *
 */
package jp.co.fd.hadoop.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;
import jp.co.fd.hadoop.common.log.HDFSLogger;
import jp.co.fd.hadoop.common.util.CounterID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;


/**
 *
 * @author Administrator
 *
 * @param <KEYCLASS>
 * @param <VALUECLASS>
 */
abstract public  class BaseMapper<KEYCLASS, VALUECLASS> extends
		Mapper<KEYCLASS, VALUECLASS, GeneralKeyBean, GeneralValueBean> {

	private String mapReduceId = null;
	private String mapReduceName = null;

	GeneralKeyBean generalKeyBean = new GeneralKeyBean();
	GeneralValueBean generalValueBean = new GeneralValueBean();

	private HDFSLogger hdfsLogger = null;
	private Text currentRecord = null;
	private long currentRecordPosition = 0;

	private Map<Integer, Counter> counterMap = null;
	private String baseName = null;
	private String basePath = null;


	public BaseMapper(String mapReduceId, String mapReduceName) {
		this.setMapReduceId(mapReduceId);
		this.setMapReduceName(mapReduceName);
	}
	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();

		int maxLines = 0;
		maxLines = conf.getInt("mapReduce.map.log.max.lines", 10000);

		baseName = conf.get("mapReduce.map.base.name","");
		basePath = "/hdfs/tem/log/".concat(baseName);

		StringBuffer sb = new StringBuffer();
		sb.append(basePath);
		sb.append("/");
		sb.append(baseName);
		hdfsLogger = new HDFSLogger(context, sb.toString(), maxLines, false);

		counterMap = new HashMap<Integer, Counter>();

		try {
			setupEx(context, conf);
		} catch (ProcessException e) {
			throw new IOException(e);
		} catch (IllegalDataException e) {
			throw new IOException(e);
		}
	}

	abstract public void setupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException;

	@Override
	abstract public void map(KEYCLASS key, VALUECLASS value, Context context) throws IOException, InterruptedException;
	@Override
	public void cleanup(Context context) throws IOException,
			InterruptedException {
		Configuration conf = context.getConfiguration();
		if (conf == null) {
			ProcessException exception = new ProcessException();
			outputAppLog(exception, context, false);
			throw new IOException(exception);
		}
		try {
			cleanupEx(context, conf);
		} catch (ProcessException e) {
			outputAppLog(e, context, false);
			throw new IOException(e);
		} catch (IllegalDataException e) {
			outputAppLog(e, context, false);
			throw new IOException(e);
		} finally {
			if (hdfsLogger != null) {
				hdfsLogger.close();
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

	public void outputAppLog(Exception exception, Context context, boolean isCheckMaxLine) {
		if (exception == null) {
			return;
		}
		boolean iswrited = false;
		if (hdfsLogger != null) {
			String errorMessage = "Mapper�����ɂُ͈킪�������܂����B";
			iswrited = hdfsLogger.write(errorMessage, isCheckMaxLine);
		}
		if (!iswrited) {
			try {
				incrementCount(context, CounterID.LOG_OUTPUT_FAIL_COUNTER,1);
			} catch (ProcessException e) {

			}
		}
	}

	public void setMapReduceId(String mapReduceId) {
		this.mapReduceId = mapReduceId;
	}
	public void setMapReduceName(String mapReduceName) {
		this.mapReduceName = mapReduceName;
	}
	public void setCurrentRecord(Text currentRecord) {
		this.currentRecord = currentRecord;
	}
	public Text getCurrentRecord() {
		return currentRecord;
	}
	public void setCurrentRecordPosition(long currentRecordPosition) {
		this.currentRecordPosition = currentRecordPosition;
	}
	public long getCurrentRecordPosition() {
		return currentRecordPosition;
	}

}
