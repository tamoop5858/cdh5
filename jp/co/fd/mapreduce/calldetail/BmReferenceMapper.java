/**
 *
 */
package jp.co.fd.mapreduce.calldetail;


import jp.co.fd.hadoop.base.BaseMapperText;
import jp.co.fd.hadoop.bean.BmReferenceValueBean;
import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;

import org.apache.hadoop.conf.Configuration;


/**
 * @author YIDATEC
 *
 */
public class BmReferenceMapper extends BaseMapperText {
	private static final int BM_PARAMETER_RFRNC_NUM = 29;
	private static final String BM_PARAMETER_RFRNC_ID = "080000021";
	private BmReferenceValueBean valueBean = new BmReferenceValueBean();
	public BmReferenceMapper() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);
	}
	@Override
	public void setupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{
		setRecordInfo(CommonConst.STRING_COMMA, BM_PARAMETER_RFRNC_NUM);
	}
	@Override
	public boolean parseRecordText(Context context, String[] values, GeneralKeyBean keyBean, GeneralValueBean generalValueBean)
			throws ProcessException, IllegalDataException {

		valueBean.clearData();
		String id = values[0];
		String parameter_value_desc = values[17];
		if (id.equals(BM_PARAMETER_RFRNC_ID)) {
			if (parameter_value_desc != null) {
				valueBean.setParameter_id(id);
				valueBean.setParameter_value_desc(parameter_value_desc);
			} else {
				return false;
			}
		} else {
			return false;
		}

		byte beanid = CommonConst.BEAN_ID_BM_PARAMETER_RFRNC;
		keyBean.setBeanId(beanid);

		valueBean.setRecordType(CommonConst.RECORD_TYPE_BM_PARAMETER_RFRNC);
		generalValueBean.setBmReferenceValueBean(valueBean);
		generalValueBean.setBeanId(beanid);
		return true;
	}
	@Override
	public void cleanupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{

	}
}
