/**
 *
 */
package jp.co.fd.mapreduce.calldetail;


import jp.co.fd.hadoop.base.BaseMapperText;
import jp.co.fd.hadoop.bean.CdrSbtmValueBean;
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
public class CdrSbtmMapper extends BaseMapperText {
	private static final int CYCLE_BILL_GROUP_RECORD_NUM = 138;
	private String treatmentSerialNumberPara = "";
	private CdrSbtmValueBean valueBean = new CdrSbtmValueBean();

	public CdrSbtmMapper() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);
	}
	@Override
	public void setupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{
		setRecordInfo(CommonConst.STRING_COMMA, CYCLE_BILL_GROUP_RECORD_NUM);
		treatmentSerialNumberPara = conf.get(CommonConst.PRAA_TREATMENT_SERIAL_NUMBER);
	}
	@Override
	public boolean parseRecordText(Context context, String[] values, GeneralKeyBean keyBean, GeneralValueBean generalValueBean)
			throws ProcessException, IllegalDataException {
		valueBean.clearData();
		String billGroupId = values[2];
		String treatmentSerialNumber = values[137];
		String billingInd = values[55];
		String deleteInd = values[136];

		if (treatmentSerialNumber.equals(treatmentSerialNumberPara) && billingInd.equals("1") &&
				deleteInd.equals(CommonConst.STRING_NOT)) {

			valueBean.setBillMonth(values[0]);
			valueBean.setBillCycleId(values[1]);
			valueBean.setBillGroupId(values[2]);
			valueBean.setServiceId(values[3]);
			valueBean.setCdrRecordSeqNbr(values[7]);
			valueBean.setConnectDt(values[9]);
			valueBean.setConnectTm(values[10]);
			valueBean.setDisconnectDt(values[11]);
			valueBean.setDisconnectTm(values[12]);
			valueBean.setCdrRatingTmstmp(values[13]);
			valueBean.setRecycleCnt(values[14]);
			valueBean.setOrigNbr(values[16]);
			valueBean.setTermNbr(values[17]);
			valueBean.setExtensionNbr(values[18]);
			valueBean.setDayOfWeekCde(values[19]);
			valueBean.setHolidayCde(values[20]);
			valueBean.setDelayedFlg(values[23]);
			valueBean.setSubstractionPrimCallCharge(values[24]);
			valueBean.setOrigCountryCde(values[51]);
			valueBean.setTermCountryCde(values[52]);
			valueBean.setMafDivisionNbr(values[53]);
			valueBean.setFrsDivisionNbr(values[54]);
			valueBean.setBillingInd(values[55]);
			valueBean.setMaDistance(values[56]);
			valueBean.setRealDistance(values[57]);
			valueBean.setCommMode(values[58]);
			valueBean.setSummaryId(values[59]);
			valueBean.setSummaryIdType(values[60]);
			valueBean.setRatingId(values[61]);
			valueBean.setRatingIdType(values[62]);
			valueBean.setBusinessAreaType(values[63]);
			valueBean.setCallTypeCde(values[64]);
			valueBean.setCallCategoryCde(values[65]);
			valueBean.setPriceableItemId(values[66]);
			valueBean.setProductId(values[67]);
			valueBean.setTimeZoneType(values[69]);
			valueBean.setPrimPpCde(values[70]);
			valueBean.setBasicCallCharge(values[72]);
			valueBean.setPrimCallCharge(values[74]);
			valueBean.setConvertCallingTime(values[75]);
			valueBean.setSplitInd(values[76]);
			valueBean.setOrigMaCde(values[77]);
			valueBean.setTermMaCde(values[78]);
			valueBean.setAccountMaCde(values[79]);
			valueBean.setOrigExchangeNbrDelimit(values[80]);
			valueBean.setTermExchangeNbrDelimit(values[81]);
			valueBean.setAccountExchangeNbrDelimit(values[82]);
			valueBean.setProductTypeCde(values[83]);
			valueBean.setOrigNetworkTypeCde(values[84]);
			valueBean.setTermNetworkTypeCde(values[85]);
			valueBean.setProcessingType(values[86]);
			valueBean.setDepartureType(values[87]);
			valueBean.setInvUserDialNbr(values[88]);
			valueBean.setInvAccountCde(values[89]);
			valueBean.setInvInternalLine1(values[90]);
			valueBean.setInvInternalLine2(values[91]);
			valueBean.setInvServiceNbr(values[92]);
			valueBean.setInvPasswordMngNbr(values[93]);
			valueBean.setInvJrTelNbr(values[94]);
			valueBean.setInvNpaDialLength(values[95]);
			valueBean.setInvUomInd(values[96]);
			valueBean.setAccountAheadInd(values[97]);
			valueBean.setInvJwswObjectInd(values[99]);
			valueBean.setInvExtensionDiscountInd(values[100]);
			valueBean.setInvJwswInternationalInd(values[101]);
			valueBean.setInvJwswIntegrationInd(values[102]);
			valueBean.setServiceInfo5(values[118]);
			valueBean.setDeleteInd(values[136]);
			valueBean.setProcessSequenceNbr(values[137]);

		} else {
			return false;
		}
		keyBean.setBeanId(CommonConst.BEAN_ID_CDR_SBTM);
		keyBean.setRecordType(CommonConst.RECORD_TYPE_CDR_SBTM);
		keyBean.setSortType(CommonConst.SORT_TYPE_CALL_DETAIL);
		keyBean.setBillGroupId(billGroupId);
		keyBean.setBillMonth(values[0]);
		keyBean.setBillCycleId(values[1]);


		valueBean.setRecordType(CommonConst.RECORD_TYPE_CDR_SBTM);
		generalValueBean.setCdrSbtmValueBean(valueBean);
		generalValueBean.setBeanId(CommonConst.BEAN_ID_CDR_SBTM);
		return true;
	}
	@Override
	public void cleanupEx(Context context, Configuration conf) throws ProcessException, IllegalDataException{

	}
}
