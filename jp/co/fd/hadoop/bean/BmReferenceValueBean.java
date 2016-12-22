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
public class BmReferenceValueBean extends BaseValueBean {
	private String parameter_id = "";
	private String parameter_value_desc = "";

    public BmReferenceValueBean() {
    }
	@Override
	public void clearDataEx() {
		parameter_id = "";
		parameter_value_desc = "";
	}

	@Override
	public void readFieldsEx(DataInput in) throws IOException {
		clearDataEx();
		parameter_id = checkReadUTF(in);
		parameter_value_desc = checkReadUTF(in);

	}

	@Override
	public void writeEx(DataOutput out) throws IOException {
		out.writeUTF(parameter_id);
		out.writeUTF(parameter_value_desc);
	}
	public void setParameter_id(String parameter_id) {
        if (parameter_id != null) {
            this.parameter_id = parameter_id;
        }
	}
	public String getParameter_id() {
		return parameter_id;
	}
	public void setParameter_value_desc(String parameter_value_desc) {
        if (parameter_value_desc != null) {
            this.parameter_value_desc = parameter_value_desc;
        }
	}
	public String getParameter_value_desc() {
		return parameter_value_desc;
	}


}
