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
public class SummarizeBillGroupValueBean extends BaseValueBean {

	private String billGroupId = "";
	private String summarizeStatType1 = "";
	private String summarizeBillMonth1 = "";
	private String summarizeBillCycleid1 = "";
	private String summarizeStatType2 = "";
	private String summarizeBillMonth2 = "";
	private String summarizeBillCycleid2 = "";

    public SummarizeBillGroupValueBean() {
    }
	@Override
	public void clearDataEx() {
		billGroupId = "";
		summarizeStatType1 = "";
		summarizeBillMonth1 = "";
		summarizeBillCycleid1 = "";
		summarizeStatType2 = "";
		summarizeBillMonth2 = "";
		summarizeBillCycleid2 = "";
	}

	@Override
	public void readFieldsEx(DataInput in) throws IOException {
		clearDataEx();
		billGroupId = checkReadUTF(in);
		summarizeStatType1 = checkReadUTF(in);
		summarizeBillMonth1 = checkReadUTF(in);
		summarizeBillCycleid1 = checkReadUTF(in);
		summarizeStatType2 = checkReadUTF(in);
		summarizeBillMonth2 = checkReadUTF(in);
		summarizeBillCycleid2 = checkReadUTF(in);
	}

	@Override
	public void writeEx(DataOutput out) throws IOException {
		out.writeUTF(billGroupId);
		out.writeUTF(summarizeStatType1);
		out.writeUTF(summarizeBillMonth1);
		out.writeUTF(summarizeBillCycleid1);
		out.writeUTF(summarizeStatType2);
		out.writeUTF(summarizeBillMonth2);
		out.writeUTF(summarizeBillCycleid2);
	}

	public String getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(String billGroupId) {
        if (billGroupId != null) {
            this.billGroupId = billGroupId;
        }
	}

	public String getSummarizeStatType1() {
		return summarizeStatType1;
	}

	public void setSummarizeStatType1(String summarizeStatType1) {
        if (summarizeStatType1 != null) {
            this.summarizeStatType1 = summarizeStatType1;
        }
	}

	public String getSummarizeBillMonth1() {
		return summarizeBillMonth1;
	}

	public void setSummarizeBillMonth1(String summarizeBillMonth1) {
        if (summarizeBillMonth1 != null) {
            this.summarizeBillMonth1 = summarizeBillMonth1;
        }
	}

	public String getSummarizeBillCycleid1() {
		return summarizeBillCycleid1;
	}

	public void setSummarizeBillCycleid1(String summarizeBillCycleid1) {
        if (summarizeBillCycleid1 != null) {
            this.summarizeBillCycleid1 = summarizeBillCycleid1;
        }
	}

	public String getSummarizeStatType2() {
		return summarizeStatType2;
	}

	public void setSummarizeStatType2(String summarizeStatType2) {
        if (summarizeStatType2 != null) {
            this.summarizeStatType2 = summarizeStatType2;
        }
	}

	public String getSummarizeBillMonth2() {
		return summarizeBillMonth2;
	}

	public void setSummarizeBillMonth2(String summarizeBillMonth2) {
        if (summarizeBillMonth2 != null) {
            this.summarizeBillMonth2 = summarizeBillMonth2;
        }
	}

	public String getSummarizeBillCycleid2() {
		return summarizeBillCycleid2;
	}

	public void setSummarizeBillCycleid2(String summarizeBillCycleid2) {
        if (summarizeBillCycleid2 != null) {
            this.summarizeBillCycleid2 = summarizeBillCycleid2;
        }
	}

}
