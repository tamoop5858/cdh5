/**
 *
 */
package jp.co.fd.mapreduce.calldetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.fd.hadoop.base.BaseReducer;
import jp.co.fd.hadoop.bean.BmReferenceValueBean;
import jp.co.fd.hadoop.bean.CdrSbtmValueBean;
import jp.co.fd.hadoop.bean.CycleBillGroupValueBean;
import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;


/**
 * @author YIDATEC
 *
 */
public class CdrSbtmReducer extends BaseReducer<GeneralKeyBean, GeneralValueBean> {

	private final static String referencePrefix = "rfc";
	private static final String innerPrefix = "inner";
	private static final String outerPrefix = "outer";

	private String outTempPathReference = "";
	private String billMonth = "";
	private String billCycleId = "";

	List<String> referenceInfoList = null;
	private Text outputText = new Text();
	public CdrSbtmReducer() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);

	}
	@Override
	public void setupEx(Context context, Configuration conf)
			throws IOException, InterruptedException, ProcessException,
			IllegalDataException {
		outTempPathReference = conf.get(CommonConst.OUT_TEMP_PATH_REFERENCE, "");
		billMonth = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_MONTH);
		billCycleId = conf.get(CommonConst.PRAA_CALLDETAIL_BILL_CYCLE_ID);
		referenceInfoList = getInfoList(conf, outTempPathReference, referencePrefix);
	}

	@Override
	public void reduceEx(GeneralKeyBean key, Iterable<GeneralValueBean> values, Context context)
			throws IOException, InterruptedException, ProcessException,
			IllegalDataException {
		int errorRecordCount = 0;
		boolean flg = false;
		for (GeneralValueBean generalValueBean : values) {
			switch (generalValueBean.getRecordType()) {
			case CommonConst.RECORD_TYPE_CYCLE_BILL_GROUP:
				CycleBillGroupValueBean cycleBillGroupValueBean = generalValueBean.getCycleBillGroupValueBean();
				flg = true;
				break;
			case CommonConst.RECORD_TYPE_CDR_SBTM:
				CdrSbtmValueBean cdrSbtmValueBean = generalValueBean.getCdrSbtmValueBean();
				String businessAreaType = cdrSbtmValueBean.getBusinessAreaType();
				StringBuilder outputData = new StringBuilder();

				outputData.append("20");
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getBusinessAreaType());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getCdrRecordSeqNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getMafDivisionNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getFrsDivisionNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(billCycleId);
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(billMonth);
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getSummaryId());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getSummaryIdType());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getRatingId());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getRatingIdType());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getCallCategoryCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getBillCycleId());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getOrigNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getTermNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getConnectDt()+cdrSbtmValueBean.getConnectTm());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getDisconnectDt()+cdrSbtmValueBean.getDisconnectTm());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getSplitInd());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getOrigMaCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getTermMaCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getAccountMaCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getOrigExchangeNbrDelimit());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getTermExchangeNbrDelimit());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getAccountExchangeNbrDelimit());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getProcessingType());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getCommMode());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getDepartureType());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getProductTypeCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getOrigNetworkTypeCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getTermNetworkTypeCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getOrigCountryCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getTermCountryCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvUserDialNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvAccountCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvInternalLine1());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvInternalLine2());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvServiceNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvPasswordMngNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvJrTelNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvNpaDialLength());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getConvertCallingTime());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvUomInd());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getTimeZoneType());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getSubstractionPrimCallCharge());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getBasicCallCharge());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getHolidayCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getCallTypeCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getDayOfWeekCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getMaDistance());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getRealDistance());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getPrimPpCde());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getPriceableItemId());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getProductId());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getExtensionNbr());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getAccountAheadInd());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getBillingInd());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getDelayedFlg());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getCdrRatingTmstmp().replace("-", ""));
				outputData.append(CommonConst.STRING_TAB);
		        if (cdrSbtmValueBean.getServiceInfo5() != null && cdrSbtmValueBean.getServiceInfo5().startsWith("W")) {
		        	outputData.append("1");
		        } else {
		        	outputData.append("0");
		        }
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvJwswObjectInd());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvExtensionDiscountInd());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvJwswIntegrationInd());
				outputData.append(CommonConst.STRING_TAB);
				outputData.append(cdrSbtmValueBean.getInvJwswInternationalInd());



				outputText.set(outputData.toString());
				if (flg) {
					if(referenceInfoList.contains(businessAreaType)) {
						multiOutputs.write(innerPrefix, null, outputText, innerPrefix);
					} else {
						multiOutputs.write(outerPrefix, null, outputText, outerPrefix);
					}
				}
				break;
			default:
				errorRecordCount++;
				IllegalDataException idEx = new IllegalDataException();
				idEx.setMessage("SummarizeBillGroupReducer error data");

				break;
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

	private List<String> getInfoList(Configuration conf,
			String basePath, String fileName) throws IllegalDataException,
			ProcessException {
		List<String> infoList = new ArrayList<String>();
		StringBuffer jn2 = new StringBuffer();
		jn2.append(basePath);
		jn2.append(fileName);
		List<Writable> writableList = getSequenceFileObject(conf, jn2.toString());
		for (Writable writable : writableList) {
			if (writable instanceof GeneralValueBean) {
				GeneralValueBean valueBean = (GeneralValueBean) writable;
				BmReferenceValueBean bean = valueBean.getBmReferenceValueBean();
				infoList.add(bean.getParameter_value_desc());
			}
		}
		return infoList;
	}
}
