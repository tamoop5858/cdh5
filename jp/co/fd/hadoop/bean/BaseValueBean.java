/**
 *
 */
package jp.co.fd.hadoop.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;


/**
 * @author YIDATEC
 * @param <T>
 *
 */
public abstract class BaseValueBean implements Writable{

	private byte recordType = 0;


	public void clearData() {
		this.recordType = 0;
		clearDataEx();
	}
	public abstract void clearDataEx();
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(recordType);
		writeEx(out);
	}
	public abstract void writeEx(DataOutput out) throws IOException;
	@Override
	public void readFields(DataInput in) throws IOException {
		recordType = in.readByte();
		readFieldsEx(in);
	}
	public abstract void readFieldsEx(DataInput in) throws IOException;

	public String checkReadUTF(DataInput in) throws IOException {
		String result = in.readUTF();
		if (result == null) {
			result = "";
		}
		return result;
	}
	/**
	 * @return recordType
	 */
	public byte getRecordType() {
		return recordType;
	}
	/**
	 * @param recordType �Z�b�g���� recordType
	 */
	public void setRecordType(byte recordType) {
		this.recordType = recordType;
	}
}
