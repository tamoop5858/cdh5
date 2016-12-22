/**
 *
 */
package jp.co.fd.hadoop.base;

import java.io.IOException;

import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;
import jp.co.fd.hadoop.common.util.CounterID;

import org.apache.hadoop.conf.Configuration;



/**
 *
 * @author Administrator
 *
 * @param <KEYCLASS>
 * @param <VALUECLASS>
 */
public  class SequenceMapper extends BaseMapper<GeneralKeyBean, GeneralValueBean>  {

	protected  boolean isSetupInfo = false;

	public SequenceMapper() {
		super("","");
		isSetupInfo = false;
	}
	public SequenceMapper(String mapReduceId, String mapReduceName) {
		super(mapReduceId,mapReduceName);
		isSetupInfo = true;
	}

	@Override
	public void setupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{
		if (!isSetupInfo) {
			setMapReduceId(conf.get("mapred.config.mapReduce.id"));
			setMapReduceName(conf.get("mapred.config.mapReduce.name"));
		}
	}
	@Override
	public void map(GeneralKeyBean key, GeneralValueBean value, Context context) throws IOException, InterruptedException {
		if (context == null) {
			ProcessException exception = new ProcessException();
			throw new IOException(exception);
		}
		try {
			incrementCount(context, CounterID.INPUT_TOTAL_RECORD_MAP, 1);
		} catch (ProcessException e) {
			outputAppLog(e, context, false);
			throw new IOException(e);
		}
		if (key == null || value == null) {
			try {
				incrementCount(context, CounterID.ERROR_TOTAL_MAP_RECORD, 1);
			} catch (ProcessException e) {
				outputAppLog(e, context, false);
				throw new IOException(e);
			}
			//�ȍ~�̏������Ȃ�
			return;
		}
		try {
			boolean isValidRecord = setBeanInfo( key, value, context);
			if (!isValidRecord) {
				//�j���f�[�^�̃J�E���g
				try {
					incrementCount(context, CounterID.INVALID_TOTAL_MAP_RECORD, 1);
				} catch (ProcessException e) {
					outputAppLog(e, context, false);
					throw new IOException(e);
				}
				return;
			}
		} catch (ProcessException e) {
			outputAppLog(e, context, true);
			try {
				incrementCount(context, CounterID.ERROR_TOTAL_MAP_RECORD, 1);
			} catch (ProcessException processException) {
				outputAppLog(processException, context, false);
				throw new IOException(processException);
			}
			return;
		} catch (IllegalDataException e) {
			outputAppLog(e, context, true);
			try {
				incrementCount(context, CounterID.ERROR_TOTAL_MAP_RECORD, 1);
			} catch (ProcessException processException) {
				outputAppLog(processException, context, false);
				throw new IOException(processException);
			}
			return;
		}

		//�f�[�^�o��
		try {
			context.write(key, value);
		} catch (IOException ioException) {
			ProcessException exception = new ProcessException(ioException);
			outputAppLog(exception, context, false);
			throw new IOException(exception);
		}


	}
	public boolean setBeanInfo(GeneralKeyBean key, GeneralValueBean value, Context context) throws ProcessException,
		IllegalDataException {
		return true;
	}
	@Override
	public void cleanupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{

	}
}
