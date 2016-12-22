/**
 *
 */
package jp.co.fd.hadoop.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CdrSbtmValueBean extends BaseValueBean {
	private String billMonth="";
	private String billCycleId="";
	private String billGroupId="";
	private String serviceId="";
	private String cdrRecordSeqNbr="";
	private String connectDt="";
	private String connectTm="";
	private String disconnectDt="";
	private String disconnectTm="";
	private String cdrRatingTmstmp="";
	private String recycleCnt="";
	private String origNbr="";
	private String termNbr="";
	private String extensionNbr="";
	private String dayOfWeekCde="";
	private String holidayCde="";
	private String delayedFlg="";
	private String origCountryCde="";
	private String termCountryCde="";
	private String mafDivisionNbr="";
	private String frsDivisionNbr="";
	private String billingInd="";
	private String maDistance="";
	private String realDistance="";
	private String commMode="";
	private String summaryId="";
	private String summaryIdType="";
	private String ratingId="";
	private String ratingIdType="";
	private String businessAreaType="";
	private String callTypeCde="";
	private String callCategoryCde="";
	private String priceableItemId="";
	private String productId="";
	private String timeZoneType="";
	private String primPpCde="";
	private String basicCallCharge="";
	private String primCallCharge="";
	private String convertCallingTime="";
	private String splitInd="";
	private String origMaCde="";
	private String termMaCde="";
	private String accountMaCde="";
	private String origExchangeNbrDelimit="";
	private String termExchangeNbrDelimit="";
	private String accountExchangeNbrDelimit="";
	private String productTypeCde="";
	private String origNetworkTypeCde="";
	private String termNetworkTypeCde="";
	private String processingType="";
	private String departureType="";
	private String invUserDialNbr="";
	private String invAccountCde="";
	private String invInternalLine1="";
	private String invInternalLine2="";
	private String invServiceNbr="";
	private String invPasswordMngNbr="";
	private String invJrTelNbr="";
	private String invNpaDialLength="";
	private String invUomInd="";
	private String accountAheadInd="";
	private String serviceInfo5="";
	private String invJwswObjectInd="";
	private String invExtensionDiscountInd="";
	private String invJwswInternationalInd="";
	private String invJwswIntegrationInd="";
	private String deleteInd="";
	private String processSequenceNbr="";
	private String substractionPrimCallCharge="";


    public CdrSbtmValueBean() {
    }
	@Override
	public void clearDataEx() {
		billMonth="";
		billCycleId="";
		billGroupId="";
		serviceId="";
		cdrRecordSeqNbr="";
		connectDt="";
		connectTm="";
		disconnectDt="";
		disconnectTm="";
		cdrRatingTmstmp="";
		recycleCnt="";
		origNbr="";
		termNbr="";
		extensionNbr="";
		dayOfWeekCde="";
		holidayCde="";
		delayedFlg="";
		origCountryCde="";
		termCountryCde="";
		mafDivisionNbr="";
		frsDivisionNbr="";
		billingInd="";
		maDistance="";
		realDistance="";
		commMode="";
		summaryId="";
		summaryIdType="";
		ratingId="";
		ratingIdType="";
		businessAreaType="";
		callTypeCde="";
		callCategoryCde="";
		priceableItemId="";
		productId="";
		timeZoneType="";
		primPpCde="";
		basicCallCharge="";
		primCallCharge="";
		convertCallingTime="";
		splitInd="";
		origMaCde="";
		termMaCde="";
		accountMaCde="";
		origExchangeNbrDelimit="";
		termExchangeNbrDelimit="";
		accountExchangeNbrDelimit="";
		productTypeCde="";
		origNetworkTypeCde="";
		termNetworkTypeCde="";
		processingType="";
		departureType="";
		invUserDialNbr="";
		invAccountCde="";
		invInternalLine1="";
		invInternalLine2="";
		invServiceNbr="";
		invPasswordMngNbr="";
		invJrTelNbr="";
		invNpaDialLength="";
		invUomInd="";
		accountAheadInd="";
		serviceInfo5="";
		invJwswObjectInd="";
		invExtensionDiscountInd="";
		invJwswInternationalInd="";
		invJwswIntegrationInd="";
		deleteInd="";
		processSequenceNbr="";
		substractionPrimCallCharge="";


	}

	@Override
	public void readFieldsEx(DataInput in) throws IOException {
		clearDataEx();
		billMonth=checkReadUTF(in);
		billCycleId=checkReadUTF(in);
		billGroupId=checkReadUTF(in);
		serviceId=checkReadUTF(in);
		cdrRecordSeqNbr=checkReadUTF(in);
		connectDt=checkReadUTF(in);
		connectTm=checkReadUTF(in);
		disconnectDt=checkReadUTF(in);
		disconnectTm=checkReadUTF(in);
		cdrRatingTmstmp=checkReadUTF(in);
		recycleCnt=checkReadUTF(in);
		origNbr=checkReadUTF(in);
		termNbr=checkReadUTF(in);
		extensionNbr=checkReadUTF(in);
		dayOfWeekCde=checkReadUTF(in);
		holidayCde=checkReadUTF(in);
		delayedFlg=checkReadUTF(in);
		origCountryCde=checkReadUTF(in);
		termCountryCde=checkReadUTF(in);
		mafDivisionNbr=checkReadUTF(in);
		frsDivisionNbr=checkReadUTF(in);
		billingInd=checkReadUTF(in);
		maDistance=checkReadUTF(in);
		realDistance=checkReadUTF(in);
		commMode=checkReadUTF(in);
		summaryId=checkReadUTF(in);
		summaryIdType=checkReadUTF(in);
		ratingId=checkReadUTF(in);
		ratingIdType=checkReadUTF(in);
		businessAreaType=checkReadUTF(in);
		callTypeCde=checkReadUTF(in);
		callCategoryCde=checkReadUTF(in);
		priceableItemId=checkReadUTF(in);
		productId=checkReadUTF(in);
		timeZoneType=checkReadUTF(in);
		primPpCde=checkReadUTF(in);
		basicCallCharge=checkReadUTF(in);
		primCallCharge=checkReadUTF(in);
		convertCallingTime=checkReadUTF(in);
		splitInd=checkReadUTF(in);
		origMaCde=checkReadUTF(in);
		termMaCde=checkReadUTF(in);
		accountMaCde=checkReadUTF(in);
		origExchangeNbrDelimit=checkReadUTF(in);
		termExchangeNbrDelimit=checkReadUTF(in);
		accountExchangeNbrDelimit=checkReadUTF(in);
		productTypeCde=checkReadUTF(in);
		origNetworkTypeCde=checkReadUTF(in);
		termNetworkTypeCde=checkReadUTF(in);
		processingType=checkReadUTF(in);
		departureType=checkReadUTF(in);
		invUserDialNbr=checkReadUTF(in);
		invAccountCde=checkReadUTF(in);
		invInternalLine1=checkReadUTF(in);
		invInternalLine2=checkReadUTF(in);
		invServiceNbr=checkReadUTF(in);
		invPasswordMngNbr=checkReadUTF(in);
		invJrTelNbr=checkReadUTF(in);
		invNpaDialLength=checkReadUTF(in);
		invUomInd=checkReadUTF(in);
		accountAheadInd=checkReadUTF(in);
		serviceInfo5=checkReadUTF(in);
		invJwswObjectInd=checkReadUTF(in);
		invExtensionDiscountInd=checkReadUTF(in);
		invJwswInternationalInd=checkReadUTF(in);
		invJwswIntegrationInd=checkReadUTF(in);
		deleteInd=checkReadUTF(in);
		processSequenceNbr=checkReadUTF(in);
		substractionPrimCallCharge=checkReadUTF(in);


	}
	@Override
	public void writeEx(DataOutput out) throws IOException {
		out.writeUTF(billMonth);
		out.writeUTF(billCycleId);
		out.writeUTF(billGroupId);
		out.writeUTF(serviceId);
		out.writeUTF(cdrRecordSeqNbr);
		out.writeUTF(connectDt);
		out.writeUTF(connectTm);
		out.writeUTF(disconnectDt);
		out.writeUTF(disconnectTm);
		out.writeUTF(cdrRatingTmstmp);
		out.writeUTF(recycleCnt);
		out.writeUTF(origNbr);
		out.writeUTF(termNbr);
		out.writeUTF(extensionNbr);
		out.writeUTF(dayOfWeekCde);
		out.writeUTF(holidayCde);
		out.writeUTF(delayedFlg);
		out.writeUTF(origCountryCde);
		out.writeUTF(termCountryCde);
		out.writeUTF(mafDivisionNbr);
		out.writeUTF(frsDivisionNbr);
		out.writeUTF(billingInd);
		out.writeUTF(maDistance);
		out.writeUTF(realDistance);
		out.writeUTF(commMode);
		out.writeUTF(summaryId);
		out.writeUTF(summaryIdType);
		out.writeUTF(ratingId);
		out.writeUTF(ratingIdType);
		out.writeUTF(businessAreaType);
		out.writeUTF(callTypeCde);
		out.writeUTF(callCategoryCde);
		out.writeUTF(priceableItemId);
		out.writeUTF(productId);
		out.writeUTF(timeZoneType);
		out.writeUTF(primPpCde);
		out.writeUTF(basicCallCharge);
		out.writeUTF(primCallCharge);
		out.writeUTF(convertCallingTime);
		out.writeUTF(splitInd);
		out.writeUTF(origMaCde);
		out.writeUTF(termMaCde);
		out.writeUTF(accountMaCde);
		out.writeUTF(origExchangeNbrDelimit);
		out.writeUTF(termExchangeNbrDelimit);
		out.writeUTF(accountExchangeNbrDelimit);
		out.writeUTF(productTypeCde);
		out.writeUTF(origNetworkTypeCde);
		out.writeUTF(termNetworkTypeCde);
		out.writeUTF(processingType);
		out.writeUTF(departureType);
		out.writeUTF(invUserDialNbr);
		out.writeUTF(invAccountCde);
		out.writeUTF(invInternalLine1);
		out.writeUTF(invInternalLine2);
		out.writeUTF(invServiceNbr);
		out.writeUTF(invPasswordMngNbr);
		out.writeUTF(invJrTelNbr);
		out.writeUTF(invNpaDialLength);
		out.writeUTF(invUomInd);
		out.writeUTF(accountAheadInd);
		out.writeUTF(serviceInfo5);
		out.writeUTF(invJwswObjectInd);
		out.writeUTF(invExtensionDiscountInd);
		out.writeUTF(invJwswInternationalInd);
		out.writeUTF(invJwswIntegrationInd);
		out.writeUTF(deleteInd);
		out.writeUTF(processSequenceNbr);
		out.writeUTF(substractionPrimCallCharge);

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
	public String getBillGroupId() {
		return billGroupId;
	}
	public void setBillGroupId(String billGroupId) {
		if (billGroupId != null) {
			this.billGroupId = billGroupId;
		}
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		if (serviceId != null) {
			this.serviceId = serviceId;
		}
	}
	public String getCdrRecordSeqNbr() {
		return cdrRecordSeqNbr;
	}
	public void setCdrRecordSeqNbr(String cdrRecordSeqNbr) {
		if (cdrRecordSeqNbr != null) {
			this.cdrRecordSeqNbr = cdrRecordSeqNbr;
		}
	}
	public String getConnectDt() {
		return connectDt;
	}
	public void setConnectDt(String connectDt) {
		if (connectDt != null) {
			this.connectDt = connectDt;
		}
	}
	public String getConnectTm() {
		return connectTm;
	}
	public void setConnectTm(String connectTm) {
		if (connectTm != null) {
			this.connectTm = connectTm;
		}
	}
	public String getDisconnectDt() {
		return disconnectDt;
	}
	public void setDisconnectDt(String disconnectDt) {
		if (disconnectDt != null) {
			this.disconnectDt = disconnectDt;
		}
	}
	public String getDisconnectTm() {
		return disconnectTm;
	}
	public void setDisconnectTm(String disconnectTm) {
		if (disconnectTm != null) {
			this.disconnectTm = disconnectTm;
		}
	}
	public String getCdrRatingTmstmp() {
		return cdrRatingTmstmp;
	}
	public void setCdrRatingTmstmp(String cdrRatingTmstmp) {
		if (cdrRatingTmstmp != null) {
			this.cdrRatingTmstmp = cdrRatingTmstmp;
		}
	}
	public String getRecycleCnt() {
		return recycleCnt;
	}
	public void setRecycleCnt(String recycleCnt) {
		if (recycleCnt != null) {
			this.recycleCnt = recycleCnt;
		}
	}
	public String getOrigNbr() {
		return origNbr;
	}
	public void setOrigNbr(String origNbr) {
		if (origNbr != null) {
			this.origNbr = origNbr;
		}
	}
	public String getTermNbr() {
		return termNbr;
	}
	public void setTermNbr(String termNbr) {
		if (termNbr != null) {
			this.termNbr = termNbr;
		}
	}
	public String getExtensionNbr() {
		return extensionNbr;
	}
	public void setExtensionNbr(String extensionNbr) {
		if (extensionNbr != null) {
			this.extensionNbr = extensionNbr;
		}
	}
	public String getDayOfWeekCde() {
		return dayOfWeekCde;
	}
	public void setDayOfWeekCde(String dayOfWeekCde) {
		if (dayOfWeekCde != null) {
			this.dayOfWeekCde = dayOfWeekCde;
		}
	}
	public String getHolidayCde() {
		return holidayCde;
	}
	public void setHolidayCde(String holidayCde) {
		if (holidayCde != null) {
			this.holidayCde = holidayCde;
		}
	}
	public String getDelayedFlg() {
		return delayedFlg;
	}
	public void setDelayedFlg(String delayedFlg) {
		if (delayedFlg != null) {
			this.delayedFlg = delayedFlg;
		}
	}
	public String getOrigCountryCde() {
		return origCountryCde;
	}
	public void setOrigCountryCde(String origCountryCde) {
		if (origCountryCde != null) {
			this.origCountryCde = origCountryCde;
		}
	}
	public String getTermCountryCde() {
		return termCountryCde;
	}
	public void setTermCountryCde(String termCountryCde) {
		if (termCountryCde != null) {
			this.termCountryCde = termCountryCde;
		}
	}
	public String getMafDivisionNbr() {
		return mafDivisionNbr;
	}
	public void setMafDivisionNbr(String mafDivisionNbr) {
		if (mafDivisionNbr != null) {
			this.mafDivisionNbr = mafDivisionNbr;
		}
	}
	public String getFrsDivisionNbr() {
		return frsDivisionNbr;
	}
	public void setFrsDivisionNbr(String frsDivisionNbr) {
		if (frsDivisionNbr != null) {
			this.frsDivisionNbr = frsDivisionNbr;
		}
	}
	public String getBillingInd() {
		return billingInd;
	}
	public void setBillingInd(String billingInd) {
		if (billingInd != null) {
			this.billingInd = billingInd;
		}
	}
	public String getMaDistance() {
		return maDistance;
	}
	public void setMaDistance(String maDistance) {
		if (maDistance != null) {
			this.maDistance = maDistance;
		}
	}
	public String getRealDistance() {
		return realDistance;
	}
	public void setRealDistance(String realDistance) {
		if (realDistance != null) {
			this.realDistance = realDistance;
		}
	}
	public String getCommMode() {
		return commMode;
	}
	public void setCommMode(String commMode) {
		if (commMode != null) {
			this.commMode = commMode;
		}
	}
	public String getSummaryId() {
		return summaryId;
	}
	public void setSummaryId(String summaryId) {
		if (summaryId != null) {
			this.summaryId = summaryId;
		}
	}
	public String getSummaryIdType() {
		return summaryIdType;
	}
	public void setSummaryIdType(String summaryIdType) {
		if (summaryIdType != null) {
			this.summaryIdType = summaryIdType;
		}
	}
	public String getRatingId() {
		return ratingId;
	}
	public void setRatingId(String ratingId) {
		if (ratingId != null) {
			this.ratingId = ratingId;
		}
	}
	public String getRatingIdType() {
		return ratingIdType;
	}
	public void setRatingIdType(String ratingIdType) {
		if (ratingIdType != null) {
			this.ratingIdType = ratingIdType;
		}
	}
	public String getBusinessAreaType() {
		return businessAreaType;
	}
	public void setBusinessAreaType(String businessAreaType) {
		if (businessAreaType != null) {
			this.businessAreaType = businessAreaType;
		}
	}
	public String getCallTypeCde() {
		return callTypeCde;
	}
	public void setCallTypeCde(String callTypeCde) {
		if (callTypeCde != null) {
			this.callTypeCde = callTypeCde;
		}
	}
	public String getCallCategoryCde() {
		return callCategoryCde;
	}
	public void setCallCategoryCde(String callCategoryCde) {
		if (callCategoryCde != null) {
			this.callCategoryCde = callCategoryCde;
		}
	}
	public String getPriceableItemId() {
		return priceableItemId;
	}
	public void setPriceableItemId(String priceableItemId) {
		if (priceableItemId != null) {
			this.priceableItemId = priceableItemId;
		}
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		if (productId != null) {
			this.productId = productId;
		}
	}
	public String getTimeZoneType() {
		return timeZoneType;
	}
	public void setTimeZoneType(String timeZoneType) {
		if (timeZoneType != null) {
			this.timeZoneType = timeZoneType;
		}
	}
	public String getPrimPpCde() {
		return primPpCde;
	}
	public void setPrimPpCde(String primPpCde) {
		if (primPpCde != null) {
			this.primPpCde = primPpCde;
		}
	}
	public String getBasicCallCharge() {
		return basicCallCharge;
	}
	public void setBasicCallCharge(String basicCallCharge) {
		if (basicCallCharge != null) {
			this.basicCallCharge = basicCallCharge;
		}
	}
	public String getPrimCallCharge() {
		return primCallCharge;
	}
	public void setPrimCallCharge(String primCallCharge) {
		if (primCallCharge != null) {
			this.primCallCharge = primCallCharge;
		}
	}
	public String getConvertCallingTime() {
		return convertCallingTime;
	}
	public void setConvertCallingTime(String convertCallingTime) {
		if (convertCallingTime != null) {
			this.convertCallingTime = convertCallingTime;
		}
	}
	public String getSplitInd() {
		return splitInd;
	}
	public void setSplitInd(String splitInd) {
		if (splitInd != null) {
			this.splitInd = splitInd;
		}
	}
	public String getOrigMaCde() {
		return origMaCde;
	}
	public void setOrigMaCde(String origMaCde) {
		if (origMaCde != null) {
			this.origMaCde = origMaCde;
		}
	}
	public String getTermMaCde() {
		return termMaCde;
	}
	public void setTermMaCde(String termMaCde) {
		if (termMaCde != null) {
			this.termMaCde = termMaCde;
		}
	}
	public String getAccountMaCde() {
		return accountMaCde;
	}
	public void setAccountMaCde(String accountMaCde) {
		if (accountMaCde != null) {
			this.accountMaCde = accountMaCde;
		}
	}
	public String getOrigExchangeNbrDelimit() {
		return origExchangeNbrDelimit;
	}
	public void setOrigExchangeNbrDelimit(String origExchangeNbrDelimit) {
		if (origExchangeNbrDelimit != null) {
			this.origExchangeNbrDelimit = origExchangeNbrDelimit;
		}
	}
	public String getTermExchangeNbrDelimit() {
		return termExchangeNbrDelimit;
	}
	public void setTermExchangeNbrDelimit(String termExchangeNbrDelimit) {
		if (termExchangeNbrDelimit != null) {
			this.termExchangeNbrDelimit = termExchangeNbrDelimit;
		}
	}
	public String getAccountExchangeNbrDelimit() {
		return accountExchangeNbrDelimit;
	}
	public void setAccountExchangeNbrDelimit(String accountExchangeNbrDelimit) {
		if (accountExchangeNbrDelimit != null) {
			this.accountExchangeNbrDelimit = accountExchangeNbrDelimit;
		}
	}
	public String getProductTypeCde() {
		return productTypeCde;
	}
	public void setProductTypeCde(String productTypeCde) {
		if (productTypeCde != null) {
			this.productTypeCde = productTypeCde;
		}
	}
	public String getOrigNetworkTypeCde() {
		return origNetworkTypeCde;
	}
	public void setOrigNetworkTypeCde(String origNetworkTypeCde) {
		if (origNetworkTypeCde != null) {
			this.origNetworkTypeCde = origNetworkTypeCde;
		}
	}
	public String getTermNetworkTypeCde() {
		return termNetworkTypeCde;
	}
	public void setTermNetworkTypeCde(String termNetworkTypeCde) {
		if (termNetworkTypeCde != null) {
			this.termNetworkTypeCde = termNetworkTypeCde;
		}
	}
	public String getProcessingType() {
		return processingType;
	}
	public void setProcessingType(String processingType) {
		if (processingType != null) {
			this.processingType = processingType;
		}
	}
	public String getDepartureType() {
		return departureType;
	}
	public void setDepartureType(String departureType) {
		if (departureType != null) {
			this.departureType = departureType;
		}
	}
	public String getInvUserDialNbr() {
		return invUserDialNbr;
	}
	public void setInvUserDialNbr(String invUserDialNbr) {
		if (invUserDialNbr != null) {
			this.invUserDialNbr = invUserDialNbr;
		}
	}
	public String getInvAccountCde() {
		return invAccountCde;
	}
	public void setInvAccountCde(String invAccountCde) {
		if (invAccountCde != null) {
			this.invAccountCde = invAccountCde;
		}
	}
	public String getInvInternalLine1() {
		return invInternalLine1;
	}
	public void setInvInternalLine1(String invInternalLine1) {
		if (invInternalLine1 != null) {
			this.invInternalLine1 = invInternalLine1;
		}
	}
	public String getInvInternalLine2() {
		return invInternalLine2;
	}
	public void setInvInternalLine2(String invInternalLine2) {
		if (invInternalLine2 != null) {
			this.invInternalLine2 = invInternalLine2;
		}
	}
	public String getInvServiceNbr() {
		return invServiceNbr;
	}
	public void setInvServiceNbr(String invServiceNbr) {
		if (invServiceNbr != null) {
			this.invServiceNbr = invServiceNbr;
		}
	}
	public String getInvPasswordMngNbr() {
		return invPasswordMngNbr;
	}
	public void setInvPasswordMngNbr(String invPasswordMngNbr) {
		if (invPasswordMngNbr != null) {
			this.invPasswordMngNbr = invPasswordMngNbr;
		}
	}
	public String getInvJrTelNbr() {
		return invJrTelNbr;
	}
	public void setInvJrTelNbr(String invJrTelNbr) {
		if (invJrTelNbr != null) {
			this.invJrTelNbr = invJrTelNbr;
		}
	}
	public String getInvNpaDialLength() {
		return invNpaDialLength;
	}
	public void setInvNpaDialLength(String invNpaDialLength) {
		if (invNpaDialLength != null) {
			this.invNpaDialLength = invNpaDialLength;
		}
	}
	public String getInvUomInd() {
		return invUomInd;
	}
	public void setInvUomInd(String invUomInd) {
		if (invUomInd != null) {
			this.invUomInd = invUomInd;
		}
	}
	public String getAccountAheadInd() {
		return accountAheadInd;
	}
	public void setAccountAheadInd(String accountAheadInd) {
		if (accountAheadInd != null) {
			this.accountAheadInd = accountAheadInd;
		}
	}
	public String getServiceInfo5() {
		return serviceInfo5;
	}
	public void setServiceInfo5(String serviceInfo5) {
		if (serviceInfo5 != null) {
			this.serviceInfo5 = serviceInfo5;
		}
	}
	public String getInvJwswObjectInd() {
		return invJwswObjectInd;
	}
	public void setInvJwswObjectInd(String invJwswObjectInd) {
		if (invJwswObjectInd != null) {
			this.invJwswObjectInd = invJwswObjectInd;
		}
	}
	public String getInvExtensionDiscountInd() {
		return invExtensionDiscountInd;
	}
	public void setInvExtensionDiscountInd(String invExtensionDiscountInd) {
		if (invExtensionDiscountInd != null) {
			this.invExtensionDiscountInd = invExtensionDiscountInd;
		}
	}
	public String getInvJwswInternationalInd() {
		return invJwswInternationalInd;
	}
	public void setInvJwswInternationalInd(String invJwswInternationalInd) {
		if (invJwswInternationalInd != null) {
			this.invJwswInternationalInd = invJwswInternationalInd;
		}
	}
	public String getInvJwswIntegrationInd() {
		return invJwswIntegrationInd;
	}
	public void setInvJwswIntegrationInd(String invJwswIntegrationInd) {
		if (invJwswIntegrationInd != null) {
			this.invJwswIntegrationInd = invJwswIntegrationInd;
		}
	}
	public String getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(String deleteInd) {
		if (deleteInd != null) {
			this.deleteInd = deleteInd;
		}
	}
	public String getProcessSequenceNbr() {
		return processSequenceNbr;
	}
	public void setProcessSequenceNbr(String processSequenceNbr) {
		if (processSequenceNbr != null) {
			this.processSequenceNbr = processSequenceNbr;
		}
	}
	public String getSubstractionPrimCallCharge() {
		return substractionPrimCallCharge;
	}
	public void setSubstractionPrimCallCharge(String substractionPrimCallCharge) {
		if (substractionPrimCallCharge != null) {
			this.substractionPrimCallCharge = substractionPrimCallCharge;
		}
	}
}
