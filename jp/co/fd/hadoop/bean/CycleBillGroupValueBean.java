/**
 *
 */
package jp.co.fd.hadoop.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author YIDATEC
 * @param <T>
 *
 */
public class CycleBillGroupValueBean extends BaseValueBean {
	private String billGroupId = "";
	private String billMonth = "";
	private String billCycleId = "";
    public CycleBillGroupValueBean() {
    }
	@Override
	public void clearDataEx() {
		billGroupId = "";
		billMonth = "";
		billCycleId = "";
	}

	@Override
	public void readFieldsEx(DataInput in) throws IOException {
		clearDataEx();
		billGroupId = checkReadUTF(in);
		billMonth = checkReadUTF(in);
		billCycleId = checkReadUTF(in);
	}

	@Override
	public void writeEx(DataOutput out) throws IOException {
		out.writeUTF(billGroupId);
		out.writeUTF(billMonth);
		out.writeUTF(billCycleId);
	}

	public String getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(String billGroupId) {
        if (billGroupId != null) {
            this.billGroupId = billGroupId;
        }
	}
	public String getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(String billMonth) {
        if (billMonth != null) {
            this.billMonth = billMonth;
        }
	}

	public String getBillCycleId() {
		return billCycleId;
	}

	public void setBillCycleId(String billCycleId) {
        if (billCycleId != null) {
            this.billCycleId = billCycleId;
        }
	}
}
