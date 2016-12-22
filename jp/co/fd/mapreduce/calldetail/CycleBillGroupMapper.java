/**
 *
 */
package jp.co.fd.mapreduce.calldetail;


import jp.co.fd.hadoop.base.BaseMapperText;
import jp.co.fd.hadoop.bean.CycleBillGroupValueBean;
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
public class CycleBillGroupMapper extends BaseMapperText {
	private static final int CYCLE_BILL_GROUP_RECORD_NUM = 40;
	private String treatmentModePara = "";
	private String billMonthPara = "";
	private String billCycleIDPara = "";
	private CycleBillGroupValueBean valueBean = new CycleBillGroupValueBean();

	public CycleBillGroupMapper() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);
	}
	@Override
	public void setupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{
		setRecordInfo(CommonConst.STRING_COMMA, CYCLE_BILL_GROUP_RECORD_NUM);
		treatmentModePara = conf.get(CommonConst.PRAA_CALLDETAIL_TREATMENT_MODE);
		billMonthPara = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_MONTH);
		billCycleIDPara = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_CYCLE_ID);
	}
	@Override
	public boolean parseRecordText(Context context, String[] values, GeneralKeyBean keyBean, GeneralValueBean generalValueBean)
			throws ProcessException, IllegalDataException {
		valueBean.clearData();
		String billGroupId = values[0];
		String billingInterfaceKbn = values[23];
		String summarizeUnbillInd = values[29];

		if (treatmentModePara.equals("6")) {
			valueBean.setBillGroupId(billGroupId);
			valueBean.setBillMonth(billMonthPara);
			valueBean.setBillCycleId(billCycleIDPara);
		} else {
			if (!billingInterfaceKbn.equals("01") || !summarizeUnbillInd.equals(CommonConst.STRING_YES)) {
				valueBean.setBillGroupId(billGroupId);
			} else {
				return false;
			}
		}
		keyBean.setBeanId(CommonConst.BEAN_ID_CYCLE_BILL_GROUP);
		keyBean.setRecordType(CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP);
		keyBean.setSortType(CommonConst.SORT_TYPE_BILL_GROUP_ID);
		keyBean.setBillGroupId(billGroupId);

		valueBean.setRecordType(CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP);
		generalValueBean.setCycleBillGroupValueBean(valueBean);
		generalValueBean.setBeanId(CommonConst.BEAN_ID_CYCLE_BILL_GROUP);
		return true;
	}
	@Override
	public void cleanupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{

	}
}
