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
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;



/**
 *
 * @author Administrator
 *
 * @param <KEYCLASS>
 * @param <VALUECLASS>
 */
abstract public  class BaseMapperBinary extends BaseMapper<LongWritable, Text>  {


	public BaseMapperBinary(String mapReduceId, String mapReduceName) {
		super(mapReduceId,mapReduceName);
	}

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (context == null) {
			ProcessException exception = new ProcessException();
			throw new IOException(exception);
		}
		//���̓��R�[�h���̃J�E���g�A�b�v
		try {
			incrementCount(context, CounterID.INPUT_TOTAL_RECORD_MAP, 1);
		} catch (ProcessException e) {
			outputAppLog(e, context, false);
			throw new IOException(e);
		}
		//�p�����[�^�`�F�b�N
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
			setCurrentRecordPosition(key.get());
			setCurrentRecord(value);
			boolean isValidRecord = mapExecute(key, value, context);
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
			context.write(generalKeyBean, generalValueBean);
		} catch (IOException ioException) {
			ProcessException exception = new ProcessException(ioException);
			outputAppLog(exception, context, false);
			throw new IOException(exception);
		}
	}
	public boolean mapExecute(LongWritable key, Text value, Context context) throws ProcessException,
		IllegalDataException {
		generalKeyBean.clearData();
		generalValueBean.clearData();
		return parseRecordBinary(context, value, generalKeyBean, generalValueBean);
	}
	abstract public boolean parseRecordBinary(Context context, Text value,GeneralKeyBean keyBean, GeneralValueBean valueBean)
		throws ProcessException, IllegalDataException;

	public boolean setBeanInfo(GeneralKeyBean key, GeneralValueBean value, Context context) throws ProcessException,
	IllegalDataException {
	return true;
}
	public void cleanupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{

	}
}
