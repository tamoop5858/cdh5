/**
 *
 */
package jp.co.fd.hadoop.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import jp.co.fd.hadoop.common.CommonConst;

/**
 * @author YIDATEC
 * @param <T>
 *
 */
public class GeneralKeyBean extends BaseKeyBean<Object> {

	private String billGroupId = "";
	private String billMonth = "";
	private String billCycleId = "";
	@Override
	public void clearDataEx() {
		this.billGroupId = "";
		this.billMonth = "";
		this.billCycleId = "";

	}
	@Override
	public void writeEx(DataOutput out) throws IOException {
		out.writeUTF(billGroupId);
		out.writeUTF(billMonth);
		out.writeUTF(billCycleId);
	}
	@Override
	public void readFieldsEx(DataInput in) throws IOException {
		billGroupId = checkReaUTF(in);
		billMonth = checkReaUTF(in);
		billCycleId = checkReaUTF(in);
	}
	@Override
	public int compareTo(Object o) {
		if (o == null)
			return -1;
		if (!(o instanceof GeneralKeyBean))
			return -1;

		GeneralKeyBean another = (GeneralKeyBean) o;

		int result = -1;
		switch (getSortType()) {
		case CommonConst.SORT_TYPE_BILL_GROUP_ID:
			result = compareToBillGroupId(another);
			break;
		case CommonConst.SORT_TYPE_CALL_DETAIL:
			result = compareToCallDetail(another);
			break;
		default:
			result = 0;
			break;
		}
		int resultValue = 0;
		if (0 < result) {
			resultValue = 1;
		} else if (result < 0) {
			resultValue = -1;
		}
		return resultValue;

	}

	private int compareToBillGroupId(GeneralKeyBean another) {

		int result = -1;
		result = compareToStringPair(billGroupId, another.billGroupId);
		if (result != 0) {
			return result;
		}
		result = getRecordType() - another.getRecordType();

		if (result != 0) {
			return result;
		}
		return result;
	}

	private int compareToCallDetail(GeneralKeyBean another) {

		int result = -1;
		result = compareToStringPair(billGroupId, another.billGroupId);
System.out.println("#--beanid---#" + getBeanId() + ":" + another.getBeanId() + "$--billGroupId---" + billGroupId + ":" + another.billGroupId);
		if (result != 0) {
			return result;
		}
		result = compareToStringPair(billMonth, another.billMonth);
System.out.println("#--beanid---#" + getBeanId() + ":" + another.getBeanId() + "$--billMonth---" + billMonth + ":" + another.billMonth);
		if (result != 0) {
			return result;
		}
		result = compareToStringPair(billCycleId, another.billCycleId);
System.out.println("#--beanid---#" + getBeanId() + ":" + another.getBeanId() + "$--billCycleId---" + billCycleId + ":" + another.billCycleId);
		if (result != 0) {
			return result;
		}
		result = getRecordType() - another.getRecordType();

		if (result != 0) {
			return result;
		}
		return result;
	}
	private int compareToStringPair(String self, String other) {

		int result = -1;
		if ((self != null) && (other != null)) {
			result = self.compareTo(other);

		} else if ((self != null) && (other == null)) {
			result = -1;

		} else if ((self == null) && (other != null)) {
			result = 1;

		} else {
			result = 0;
		}

		return result;

	}


	public String getBillGroupId() {
        if (billGroupId == null) {
        	billGroupId = "";
        }
		return billGroupId;
	}
	public void setBillGroupId(String billGroupId) {
        if (billGroupId == null) {
        	billGroupId = "";
        }
		this.billGroupId = billGroupId;
	}
	public void setBillMonth(String billMonth) {
        if (billMonth == null) {
        	billMonth = "";
        }
		this.billMonth = billMonth;
	}
	public String getBillMonth() {
        if (billMonth == null) {
        	billMonth = "";
        }
		return billMonth;
	}
	public void setBillCycleId(String billCycleId) {
        if (billCycleId == null) {
        	billCycleId = "";
        }
		this.billCycleId = billCycleId;
	}
	public String getBillCycleId() {
        if (billCycleId == null) {
        	billCycleId = "";
        }
		return billCycleId;
	}


}
