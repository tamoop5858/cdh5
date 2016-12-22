/**
 *
 */
package jp.co.fd.hadoop.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import jp.co.fd.hadoop.bean.BmReferenceValueBean;
import jp.co.fd.hadoop.bean.CallDetailValueBean;
import jp.co.fd.hadoop.bean.CdrSbtmValueBean;
import jp.co.fd.hadoop.bean.CycleBillGroupValueBean;
import jp.co.fd.hadoop.bean.SummarizeBillGroupValueBean;
import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.ProcessException;

import org.apache.hadoop.io.Writable;

/**
 * @author YIDATEC
 *
 */
public class GeneralValueBean implements Writable {

	private byte beanId = 0;
	private BmReferenceValueBean bmReferenceValueBean = null;
	private CycleBillGroupValueBean cycleBillGroupValueBean = null;
	private SummarizeBillGroupValueBean summarizeBillGroupValueBean = null;
	private CdrSbtmValueBean cdrSbtmValueBean = null;
	private CallDetailValueBean callDetailValueBean = null;

	public GeneralValueBean() {

	}

	public void clearData() {

		beanId = 0;
		setBmReferenceValueBean(null);
		setCycleBillGroupValueBean(null);
		setSummarizeBillGroupValueBean(null);
		setCdrSbtmValueBean(null);
		setCallDetailValueBean(null);

	}

	@Override
	public void readFields(DataInput in) throws IOException {
		if (in == null) {
			IOException exception = new IOException("generalValueBean readFields error : in is null");
			throw exception;
		}
		clearData();
		beanId = in.readByte();
		switch (beanId) {
			case CommonConst.BEAN_ID_BM_PARAMETER_RFRNC:
				bmReferenceValueBean = new BmReferenceValueBean();
				bmReferenceValueBean.readFields(in);
				break;
			case CommonConst.BEAN_ID_CYCLE_BILL_GROUP:
				cycleBillGroupValueBean = new CycleBillGroupValueBean();
				cycleBillGroupValueBean.readFields(in);
				break;
			case CommonConst.BEAN_ID_SUMMARIZE_BILL_GROUP:
				summarizeBillGroupValueBean = new SummarizeBillGroupValueBean();
				summarizeBillGroupValueBean.readFields(in);
				break;
			case CommonConst.BEAN_ID_CDR_SBTM:
				cdrSbtmValueBean = new CdrSbtmValueBean();
				cdrSbtmValueBean.readFields(in);
				break;
			case CommonConst.BEAN_ID_CALL_DETAIL:
				callDetailValueBean = new CallDetailValueBean();
				callDetailValueBean.readFields(in);
				break;
			default:
				ProcessException processException = new ProcessException();
				processException.setMessage("generalValueBean readFields error beanId is :" + beanId);
				throw new IOException(processException);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		BaseValueBean valueBean = null;
		if (out == null) {
			IOException exception = new IOException("generalValueBean readFields write : out is null");
			throw exception;
		}
		out.writeByte(beanId);
		valueBean = getHoldBean();
		if (valueBean != null) {
			valueBean.write(out);
		} else {
			ProcessException processException = new ProcessException();
			processException.setMessage("generalValueBean readFields write : valueBean is null");
			throw new IOException(processException);
		}
	}

	public byte getRecordType() {

		BaseValueBean baseValueBean = getHoldBean();

		byte recordType = 0;
		if (baseValueBean != null) {
			recordType = baseValueBean.getRecordType();
		}

		return recordType;

	}
	private BaseValueBean getHoldBean() {

		BaseValueBean holdBean = null;

		switch (beanId) {
			case CommonConst.BEAN_ID_BM_PARAMETER_RFRNC:
				holdBean = bmReferenceValueBean;
				break;
			case CommonConst.BEAN_ID_CYCLE_BILL_GROUP:
				holdBean = cycleBillGroupValueBean;
				break;
			case CommonConst.BEAN_ID_SUMMARIZE_BILL_GROUP:
				holdBean = summarizeBillGroupValueBean;
				break;
			case CommonConst.BEAN_ID_CDR_SBTM:
				holdBean = cdrSbtmValueBean;
				break;
			case CommonConst.BEAN_ID_CALL_DETAIL:
				holdBean = callDetailValueBean;
				break;
			default:
				holdBean = null;
				break;
		}
		return holdBean;
	}
	public byte getBeanId() {
		return beanId;

	}

	public void setBeanId(byte beanId) {
		this.beanId = beanId;

	}

	public void setBmReferenceValueBean(BmReferenceValueBean bmReferenceValueBean) {
		this.bmReferenceValueBean = bmReferenceValueBean;
	}

	public BmReferenceValueBean getBmReferenceValueBean() {
		return bmReferenceValueBean;
	}

	public void setCycleBillGroupValueBean(CycleBillGroupValueBean cycleBillGroupValueBean) {
		this.cycleBillGroupValueBean = cycleBillGroupValueBean;
	}

	public CycleBillGroupValueBean getCycleBillGroupValueBean() {
		return cycleBillGroupValueBean;
	}

	public void setSummarizeBillGroupValueBean(
			SummarizeBillGroupValueBean summarizeBillGroupValueBean) {
		this.summarizeBillGroupValueBean = summarizeBillGroupValueBean;
	}

	public SummarizeBillGroupValueBean getSummarizeBillGroupValueBean() {
		return summarizeBillGroupValueBean;
	}

	public void setCdrSbtmValueBean(CdrSbtmValueBean cdrSbtmValueBean) {
		this.cdrSbtmValueBean = cdrSbtmValueBean;
	}

	public CdrSbtmValueBean getCdrSbtmValueBean() {
		return cdrSbtmValueBean;
	}

	public void setCallDetailValueBean(CallDetailValueBean callDetailValueBean) {
		this.callDetailValueBean = callDetailValueBean;
	}

	public CallDetailValueBean getCallDetailValueBean() {
		return callDetailValueBean;
	}

}
