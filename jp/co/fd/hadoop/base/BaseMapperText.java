/**
 *
 */
package jp.co.fd.hadoop.base;

import java.io.UnsupportedEncodingException;

import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;



/**
 *
 * @author Administrator
 *
 * @param <KEYCLASS>
 * @param <VALUECLASS>
 */
abstract public  class BaseMapperText extends BaseMapperBinary {

	private String recordSplitChar = ",";
	private int recordColumnNumber = 0;
	private boolean isSkipHeaderRecord = false;
	private boolean isCheckHB = false;

	public BaseMapperText(String mapReduceId, String mapReduceName) {
		super(mapReduceId,mapReduceName);
	}

	public void setRecordInfo(String recordSplitChar, int recordColumnNumber) throws ProcessException{

		if (recordSplitChar == null || recordColumnNumber ==0) {
			ProcessException exception = new ProcessException();
			throw exception;
		}
		this.recordSplitChar = recordSplitChar;
		this.recordColumnNumber = recordColumnNumber;
	}
	@Override
	public boolean mapExecute(LongWritable key, Text value, Context context) throws ProcessException,
		IllegalDataException {
		//��s�`�F�b�N
		if (value.getLength() == 0) {
			return false;
		}
		byte[] recordBytes = value.getBytes();
		String valueByte = "";
		try {
			valueByte = new String(recordBytes, 0, value.getLength(), "UTF-8");
		} catch (UnsupportedEncodingException exception) {
			IllegalDataException illegalDataException = new IllegalDataException(exception);
			throw illegalDataException;
		}
		String[] values = valueByte.split(recordSplitChar, -1);
		if (isSkipHeaderRecord) {
			if ("#".equals(values[0])) {
				return false;
			}
		}
		if (recordColumnNumber != 0) {
			if (values.length != recordColumnNumber) {
				IllegalDataException illegalDataException = new IllegalDataException();
				throw illegalDataException;
			}
		}
		if (isCheckHB) {
			//TODO
		}
		generalKeyBean.clearData();
		generalValueBean.clearData();
		return parseRecordText(context, values, generalKeyBean, generalValueBean);
	}

	abstract public boolean parseRecordText(Context context, String[] values,GeneralKeyBean keyBean, GeneralValueBean valueBean)
		throws ProcessException, IllegalDataException;
	final public boolean parseRecordBinary(Context context, Text value,GeneralKeyBean keyBean, GeneralValueBean valueBean)
	throws ProcessException, IllegalDataException {
		return false;
	}
	public void setSkipHeaderRecord(boolean isSkipHeaderRecord) {
		this.isSkipHeaderRecord = isSkipHeaderRecord;
	}

	public void setCheckHB(boolean isCheckHB) {
		this.isCheckHB = isCheckHB;
	}

}
