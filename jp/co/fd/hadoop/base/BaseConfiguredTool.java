/**
 *
 */
package jp.co.fd.hadoop.base;

import java.io.IOException;

import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.BaseException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.util.Tool;

/**
 * @author YIDATEC
 *
 */
public abstract class BaseConfiguredTool extends Configured implements Tool {



	public static int RETURN_CODE_OK = 0;

	public static int RETURN_CODE_ERROR = 1;

	public boolean compress = true;

	public boolean useGzipCodec = true;

	private int currentCount = 0;


	private int execCount = 0;

	private JobID jobId = null;

	public BaseConfiguredTool(){

	}
	/**
	 *
	 * @param args
	 * @return
	 * @throws BaseException
	 * @throws IOException
	 */
	public abstract Job configJob(String[] args) throws BaseException, IOException;
	/**
	 *
	 * @param args
	 * @return
	 * @throws BaseException
	 */
	public boolean preProcess(String[] args) throws BaseException {
		return true;
	}
	/**
	 *
	 * @param job
	 * @return
	 * @throws BaseException
	 */
	public boolean postProcess(Job job) throws BaseException {
		return false;
	}
	/**
	 *
	 */
	@Override
	public int run(String[] args) throws BaseException, IOException, InterruptedException, ClassNotFoundException {
		int result = RETURN_CODE_OK;

		Job job = null;

		boolean preResult = this.preProcess(args);

		if (!preResult) {
			return RETURN_CODE_OK;
		}


		for (int i = 0; i < execCount; i++) {
			currentCount = i + 1;
			job = configJob(args);

			if (job == null) {
				continue;
			}
			if (compress) {
				setOutputCompress(job);
			}
			if (!execute(job)){
				result = RETURN_CODE_ERROR;
				break;
			}
			jobId = job.getJobID();
			if (!postProcess(job)) {
				break;
			}
		}

		return result;

	}
	/**
	 *
	 * @param job
	 */
	public void setOutputCompress(Job job) {
		Configuration conf = job.getConfiguration();
		conf.setBoolean(CommonConst.OUTPUT_COMPRESS, true);
		if (useGzipCodec) {
			conf.setClass(CommonConst.OUTPUT_COMPRESSION_CODEC, GzipCodec.class, CompressionCodec.class);
		}

	}
	/**
	 *
	 * @param job
	 */
	public void setMapOutputCompress(Job job) {
		Configuration conf = job.getConfiguration();
		conf.setBoolean(CommonConst.COMPRESS_MAP_OUTPUT, true);
		if (useGzipCodec) {
			conf.setClass(CommonConst.MAP_OUTPUT_COMPRESSION_CODEC, GzipCodec.class, CompressionCodec.class);
		}
	}
	/**
	 *
	 * @param job
	 */
	public void setSpeculativeOff(Job job) {
		Configuration conf = job.getConfiguration();
		conf.setBoolean(CommonConst.MAP_TASKS_SPECULATIVE_EXECUTION, false);
		conf.setBoolean(CommonConst.REDUCE_TASKS_SPECULATIVE_EXECUTION, false);
	}
	/**
	 *
	 * @param job
	 * @param queueName
	 */
	public void setQueueName(Job job, String queueName) {
		Configuration conf = job.getConfiguration();
		conf.set(CommonConst.JOB_QUEUE_NAME, queueName);
	}
	/**
	 *
	 * @param job
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean execute(Job job) throws ClassNotFoundException, IOException,
			InterruptedException {

		boolean isSuccessful = job.waitForCompletion(true);
		return isSuccessful;
	}
	public void addMultipleInputsPath(Job job, Path path, Class<? extends InputFormat> inputFormat,
			Class<? extends Mapper> mapper) {
		MultipleInputs.addInputPath(job, path, inputFormat, mapper);
	}
	/**
	 *
	 * @return
	 */
	public String  getJobId() {
		String id = null;
		if (jobId != null) {
			id = jobId.toString();
		}
		return id;
	}

	/**
	 * @return currentCount
	 */
	public int getCurrentCount() {
		return currentCount;
	}
	/**
	 * @param execCount �Z�b�g���� execCount
	 */
	public void setExecCount(int execCount) {
		this.execCount = execCount;
	}

}
