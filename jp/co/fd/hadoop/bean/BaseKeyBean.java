/**
 *
 */
package jp.co.fd.hadoop.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * @author YIDATEC
 * @param <T>
 *
 */
public abstract class BaseKeyBean<T> implements WritableComparable<T> {

	private byte beanId = 0;
	private byte sortType = 0;
	private byte recordType = 0;


	public void clearData() {
		this.beanId = 0;
		this.sortType = 0;
		clearDataEx();
	}
	public abstract void clearDataEx();
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(beanId);
		out.writeByte(sortType);
		out.writeByte(recordType);
		writeEx(out);
	}
	public abstract void writeEx(DataOutput out) throws IOException;
	@Override
	public void readFields(DataInput in) throws IOException {
		beanId = in.readByte();
		sortType = in.readByte();
		recordType = in.readByte();
		readFieldsEx(in);
	}
	public abstract void readFieldsEx(DataInput in) throws IOException;
	@Override
	public abstract int compareTo(Object o) ;

	public String checkReaUTF(DataInput in) throws IOException {
		String result = in.readUTF();
		if (result == null) {
			result = "";
		}
		return result;
	}

	public byte getBeanId() {
		return beanId;
	}
	public void setBeanId(byte beanId) {
		this.beanId = beanId;
	}
	public void setRecordType(byte recordType) {
		this.recordType = recordType;
	}
	public byte getRecordType() {
		return recordType;
	}
	public void setSortType(byte sortType) {
		this.sortType = sortType;
	}

	public byte getSortType() {
		return sortType;
	}
}
