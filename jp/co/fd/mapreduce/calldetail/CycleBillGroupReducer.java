/**
 *
 */
package jp.co.fd.mapreduce.calldetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.fd.hadoop.base.BaseReducer;
import jp.co.fd.hadoop.bean.CycleBillGroupValueBean;
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
public class CycleBillGroupReducer extends BaseReducer<GeneralKeyBean, GeneralValueBean> {

	private final String cycleBillGroupPrefix = "cbg";
	private String treatmentModePara = "";
	private String billMonthPara = "";
	private String billCycleIDPara = "";
	private GeneralKeyBean summarizeBillGroupKeyBean = new GeneralKeyBean();
	List<SummarizeBillGroupValueBean> SummarizeBillGroupList = null;

	public CycleBillGroupReducer() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);

	}
	@Override
	public void setupEx(Context context, Configuration conf)
			throws IOException, InterruptedException, ProcessException,
			IllegalDataException {
		treatmentModePara = conf.get(CommonConst.PRAA_CALLDETAIL_TREATMENT_MODE);
		billMonthPara = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_MONTH);
		billCycleIDPara = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_CYCLE_ID);

        summarizeBillGroupKeyBean.clearData();
        summarizeBillGroupKeyBean.setBeanId(CommonConst.BEAN_ID_CYCLE_BILL_GROUP);
        summarizeBillGroupKeyBean.setRecordType(CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP);
        summarizeBillGroupKeyBean.setSortType(CommonConst.SORT_TYPE_BILL_GROUP_ID);
	}

	@Override
	public void reduceEx(GeneralKeyBean key, Iterable<GeneralValueBean> values, Context context)
			throws IOException, InterruptedException, ProcessException,
			IllegalDataException {
		int errorRecordCount = 0;
//		int outputTotalRecordCount = 0;
		if (treatmentModePara.equals("6")) {
			for (GeneralValueBean generalValueBean : values) {
				switch (generalValueBean.getRecordType()) {
				case CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP:
					CycleBillGroupValueBean cycleBillGroupValueBean = generalValueBean.getCycleBillGroupValueBean();
					String billGroupId = cycleBillGroupValueBean.getBillGroupId();
					summarizeBillGroupKeyBean.clearData();
			        summarizeBillGroupKeyBean.setBeanId(CommonConst.BEAN_ID_CYCLE_BILL_GROUP);
			        summarizeBillGroupKeyBean.setRecordType(CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP);
			        summarizeBillGroupKeyBean.setSortType(CommonConst.SORT_TYPE_BILL_GROUP_ID);
					summarizeBillGroupKeyBean.setBillGroupId(billGroupId);

					cycleBillGroupValueBean.setBillMonth(billMonthPara);
					cycleBillGroupValueBean.setBillCycleId(billCycleIDPara);
					generalValueBean.setCycleBillGroupValueBean(cycleBillGroupValueBean);
					multiOutputs.write(cycleBillGroupPrefix, key, generalValueBean, cycleBillGroupPrefix);
				default:
					errorRecordCount++;
					IllegalDataException idEx = new IllegalDataException();
					idEx.setMessage("SummarizeBillGroupReducer error data");

					break;
				}
			}
		} else {
			SummarizeBillGroupList = new ArrayList<SummarizeBillGroupValueBean>();
			for (GeneralValueBean generalValueBean : values) {
				switch (generalValueBean.getRecordType()) {
				case CommonConst.RECORD_TYPE_SUMMARIZE_BILL_GROUP:
					SummarizeBillGroupValueBean summarizeBillGroupValueBean = generalValueBean.getSummarizeBillGroupValueBean();
					SummarizeBillGroupList.add(summarizeBillGroupValueBean);
					break;
				case CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP:
					CycleBillGroupValueBean cycleBillGroupValueBean = generalValueBean.getCycleBillGroupValueBean();
					String billGroupId = cycleBillGroupValueBean.getBillGroupId();
					summarizeBillGroupKeyBean.clearData();
			        summarizeBillGroupKeyBean.setBeanId(CommonConst.BEAN_ID_CYCLE_BILL_GROUP);
			        summarizeBillGroupKeyBean.setRecordType(CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP);
			        summarizeBillGroupKeyBean.setSortType(CommonConst.SORT_TYPE_BILL_GROUP_ID);
					summarizeBillGroupKeyBean.setBillGroupId(billGroupId);
					if (SummarizeBillGroupList.size() == 0) {
						summarizeBillGroupKeyBean.setBillMonth(billMonthPara);
						summarizeBillGroupKeyBean.setBillCycleId(billCycleIDPara);

						cycleBillGroupValueBean.setBillMonth(billMonthPara);
						cycleBillGroupValueBean.setBillCycleId(billCycleIDPara);
						generalValueBean.setCycleBillGroupValueBean(cycleBillGroupValueBean);
						multiOutputs.write(cycleBillGroupPrefix, summarizeBillGroupKeyBean, generalValueBean, cycleBillGroupPrefix);
					} else if (SummarizeBillGroupList.size() == 1) {
						SummarizeBillGroupValueBean summarizeBillGroupValueBeanFrom = SummarizeBillGroupList.get(0);
						if (summarizeBillGroupValueBeanFrom.getSummarizeStatType1().equals("1")) {
							summarizeBillGroupKeyBean.setBillMonth(summarizeBillGroupValueBeanFrom.getSummarizeBillMonth1());
							summarizeBillGroupKeyBean.setBillCycleId(summarizeBillGroupValueBeanFrom.getSummarizeBillCycleid1());

							cycleBillGroupValueBean.setBillMonth(summarizeBillGroupValueBeanFrom.getSummarizeBillMonth1());
							cycleBillGroupValueBean.setBillCycleId(summarizeBillGroupValueBeanFrom.getSummarizeBillCycleid1());
							generalValueBean.setCycleBillGroupValueBean(cycleBillGroupValueBean);
							multiOutputs.write(cycleBillGroupPrefix, summarizeBillGroupKeyBean, generalValueBean, cycleBillGroupPrefix);
						}
						if (summarizeBillGroupValueBeanFrom.getSummarizeStatType2().equals("1")) {
							summarizeBillGroupKeyBean.setBillMonth(summarizeBillGroupValueBeanFrom.getSummarizeBillMonth2());
							summarizeBillGroupKeyBean.setBillCycleId(summarizeBillGroupValueBeanFrom.getSummarizeBillCycleid2());

							cycleBillGroupValueBean.setBillMonth(summarizeBillGroupValueBeanFrom.getSummarizeBillMonth2());
							cycleBillGroupValueBean.setBillCycleId(summarizeBillGroupValueBeanFrom.getSummarizeBillCycleid2());
							generalValueBean.setCycleBillGroupValueBean(cycleBillGroupValueBean);
							multiOutputs.write(cycleBillGroupPrefix, summarizeBillGroupKeyBean, generalValueBean, cycleBillGroupPrefix);
						}
						summarizeBillGroupKeyBean.setBillMonth(billMonthPara);
						summarizeBillGroupKeyBean.setBillCycleId(billCycleIDPara);
						cycleBillGroupValueBean.setBillMonth(billMonthPara);
						cycleBillGroupValueBean.setBillCycleId(billCycleIDPara);
						generalValueBean.setCycleBillGroupValueBean(cycleBillGroupValueBean);
						multiOutputs.write(cycleBillGroupPrefix, summarizeBillGroupKeyBean, generalValueBean, cycleBillGroupPrefix);
					} else {
						ProcessException exception = new ProcessException();
						exception.setMessage("SummarizeBillGroupReducer record ");
						throw exception;
					}
					break;
				default:
					errorRecordCount++;
					IllegalDataException idEx = new IllegalDataException();
					idEx.setMessage("SummarizeBillGroupReducer error data");

					break;
				}
			}
		}

//        incrementCount(context, CounterID.OUTPUT_TOTAL_RECORD, outputTotalRecordCount);
//        incrementCount(context, CounterID.ERROR_TOTAL_RECORD_REDUCE, errorRecordCount);
	}
	@Override
	public void cleanupEx(Context context, Configuration conf)
			throws ProcessException, IllegalDataException {

		System.out.println("cleanupEx===========reducer===========");
	}
}
