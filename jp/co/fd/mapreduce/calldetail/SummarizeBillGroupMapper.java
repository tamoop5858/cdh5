/**
 *
 */
package jp.co.fd.mapreduce.calldetail;


import jp.co.fd.hadoop.base.BaseMapperText;
import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.bean.SummarizeBillGroupValueBean;
import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;

import org.apache.hadoop.conf.Configuration;


/**
 * @author YIDATEC
 *
 */
public class SummarizeBillGroupMapper extends BaseMapperText {

	private static final int SUMMARIZE_BILL_GROUPRECORD_NUM = 21;
	private String billMonthPara = "";
	private String billCycleIDPara = "";
	private SummarizeBillGroupValueBean valueBean = new SummarizeBillGroupValueBean();
	public SummarizeBillGroupMapper() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);
	}
	@Override
	public void setupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{
		setRecordInfo(CommonConst.STRING_COMMA, SUMMARIZE_BILL_GROUPRECORD_NUM);
		billMonthPara = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_MONTH);
		billCycleIDPara = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_CYCLE_ID);
	}
	@Override
	public boolean parseRecordText(Context context, String[] values, GeneralKeyBean keyBean, GeneralValueBean generalValueBean)
			throws ProcessException, IllegalDataException {

		valueBean.clearData();
		String billGroupId = values[0];
		String billMonth = values[11];
		String billCycleId = values[12];
		String billTargetInd = values[13];
		String deleteInd = values[19];

		String summarizeStatType1 = values[5];
		String summarizeBillMonth1 = values[3];
		String summarizeBillCycleid1 = values[4];
		String summarizeStatType2 = values[9];
		String summarizeBillMonth2 = values[7];
		String summarizeBillCycleid2 = values[8];

		if (billMonth.equals(billMonthPara) && billCycleId.equals(billCycleIDPara) &&
				billTargetInd.equals(CommonConst.STRING_YES) && deleteInd.equals(CommonConst.STRING_NOT)) {
			valueBean.setBillGroupId(billGroupId);
			valueBean.setSummarizeStatType1(summarizeStatType1);
			valueBean.setSummarizeBillMonth1(summarizeBillMonth1);
			valueBean.setSummarizeBillCycleid1(summarizeBillCycleid1);
			valueBean.setSummarizeStatType2(summarizeStatType2);
			valueBean.setSummarizeBillMonth2(summarizeBillMonth2);
			valueBean.setSummarizeBillCycleid2(summarizeBillCycleid2);
		} else {
			return false;
		}
		keyBean.setBeanId(CommonConst.BEAN_ID_SUMMARIZE_BILL_GROUP);
		keyBean.setRecordType(CommonConst.RECORD_TYPE_SUMMARIZE_BILL_GROUP);
		keyBean.setSortType(CommonConst.SORT_TYPE_BILL_GROUP_ID);
		keyBean.setBillGroupId(billGroupId);

		valueBean.setRecordType(CommonConst.RECORD_TYPE_SUMMARIZE_BILL_GROUP);
		generalValueBean.setSummarizeBillGroupValueBean(valueBean);
		generalValueBean.setBeanId(CommonConst.BEAN_ID_SUMMARIZE_BILL_GROUP);
		return true;
	}
	@Override
	public void cleanupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{

	}
}
