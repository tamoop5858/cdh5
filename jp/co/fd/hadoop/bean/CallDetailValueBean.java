/**
 *
 */
package jp.co.fd.hadoop.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CallDetailValueBean extends BaseValueBean {
	private String datakbn="";
	private String businessAreaType="";
	private String cdrRecordSeqNbr="";
	private String mafDivisionNbr="";
	private String frsDivisionNbr="";
	private String billGroupId="";
	private String billMonth="";
	private String summaryId="";
	private String summaryIdType="";
	private String ratingId="";
	private String ratingIdType="";
	private String callCategoryCde="";
	private String billCycleId="";
	private String origNbr="";
	private String termNbr="";
	private String connectDttm="";
	private String tmdisconnectDt="";
	private String splitInd="";
	private String origMaCde="";
	private String termMaCde="";
	private String accountMaCde="";
	private String origExchangeNbrDelimit="";
	private String termExchangeNbrDelimit="";
	private String accountExchangeNbrDelimit="";
	private String processingType="";
	private String commMode="";
	private String departureType="";
	private String productTypeCde="";
	private String origNetworkTypeCde="";
	private String termNetworkTypeCde="";
	private String origCountryCde="";
	private String termCountryCde="";
	private String invUserDialNbr="";
	private String invAccountCde="";
	private String invInternalLine1="";
	private String invInternalLine2="";
	private String invServiceNbr="";
	private String invPasswordMngNbr="";
	private String invJrTelNbr="";
	private String invNpaDialLength="";
	private String callingtime="";
	private String invUomInd="";
	private String timeZoneType="";
	private String primCallCharge="";
	private String basicCallCharge="";
	private String holidayCde="";
	private String callTypeCde="";
	private String dayOfWeekCde="";
	private String maDistance="";
	private String realDistance="";
	private String primPpCde="";
	private String priceableItemId="";
	private String productId="";
	private String extensionNbr="";
	private String accountAheadInd="";
	private String billingInd="";
	private String delayedFlg="";
	private String cdrRatingTmstmp="";
	private String invSpecialOtherPartyInd="";
	private String invJwswObjectInd="";
	private String invExtensionDiscountInd="";
	private String invJwswIntegrationInd="";
	private String invJwswInternationalInd="";

	public CallDetailValueBean() {
    }

	@Override
	public void clearDataEx() {
		datakbn="";
		businessAreaType="";
		cdrRecordSeqNbr="";
		mafDivisionNbr="";
		frsDivisionNbr="";
		billGroupId="";
		billMonth="";
		summaryId="";
		summaryIdType="";
		ratingId="";
		ratingIdType="";
		callCategoryCde="";
		billCycleId="";
		origNbr="";
		termNbr="";
		connectDttm="";
		tmdisconnectDt="";
		splitInd="";
		origMaCde="";
		termMaCde="";
		accountMaCde="";
		origExchangeNbrDelimit="";
		termExchangeNbrDelimit="";
		accountExchangeNbrDelimit="";
		processingType="";
		commMode="";
		departureType="";
		productTypeCde="";
		origNetworkTypeCde="";
		termNetworkTypeCde="";
		origCountryCde="";
		termCountryCde="";
		invUserDialNbr="";
		invAccountCde="";
		invInternalLine1="";
		invInternalLine2="";
		invServiceNbr="";
		invPasswordMngNbr="";
		invJrTelNbr="";
		invNpaDialLength="";
		callingtime="";
		invUomInd="";
		timeZoneType="";
		primCallCharge="";
		basicCallCharge="";
		holidayCde="";
		callTypeCde="";
		dayOfWeekCde="";
		maDistance="";
		realDistance="";
		primPpCde="";
		priceableItemId="";
		productId="";
		extensionNbr="";
		accountAheadInd="";
		billingInd="";
		delayedFlg="";
		cdrRatingTmstmp="";
		invSpecialOtherPartyInd="";
		invJwswObjectInd="";
		invExtensionDiscountInd="";
		invJwswIntegrationInd="";
		invJwswInternationalInd="";
	}

	@Override
	public void readFieldsEx(DataInput in) throws IOException {
		clearDataEx();
		datakbn=checkReadUTF(in);
		businessAreaType=checkReadUTF(in);
		cdrRecordSeqNbr=checkReadUTF(in);
		mafDivisionNbr=checkReadUTF(in);
		frsDivisionNbr=checkReadUTF(in);
		billGroupId=checkReadUTF(in);
		billMonth=checkReadUTF(in);
		summaryId=checkReadUTF(in);
		summaryIdType=checkReadUTF(in);
		ratingId=checkReadUTF(in);
		ratingIdType=checkReadUTF(in);
		callCategoryCde=checkReadUTF(in);
		billCycleId=checkReadUTF(in);
		origNbr=checkReadUTF(in);
		termNbr=checkReadUTF(in);
		connectDttm=checkReadUTF(in);
		tmdisconnectDt=checkReadUTF(in);
		splitInd=checkReadUTF(in);
		origMaCde=checkReadUTF(in);
		termMaCde=checkReadUTF(in);
		accountMaCde=checkReadUTF(in);
		origExchangeNbrDelimit=checkReadUTF(in);
		termExchangeNbrDelimit=checkReadUTF(in);
		accountExchangeNbrDelimit=checkReadUTF(in);
		processingType=checkReadUTF(in);
		commMode=checkReadUTF(in);
		departureType=checkReadUTF(in);
		productTypeCde=checkReadUTF(in);
		origNetworkTypeCde=checkReadUTF(in);
		termNetworkTypeCde=checkReadUTF(in);
		origCountryCde=checkReadUTF(in);
		termCountryCde=checkReadUTF(in);
		invUserDialNbr=checkReadUTF(in);
		invAccountCde=checkReadUTF(in);
		invInternalLine1=checkReadUTF(in);
		invInternalLine2=checkReadUTF(in);
		invServiceNbr=checkReadUTF(in);
		invPasswordMngNbr=checkReadUTF(in);
		invJrTelNbr=checkReadUTF(in);
		invNpaDialLength=checkReadUTF(in);
		callingtime=checkReadUTF(in);
		invUomInd=checkReadUTF(in);
		timeZoneType=checkReadUTF(in);
		primCallCharge=checkReadUTF(in);
		basicCallCharge=checkReadUTF(in);
		holidayCde=checkReadUTF(in);
		callTypeCde=checkReadUTF(in);
		dayOfWeekCde=checkReadUTF(in);
		maDistance=checkReadUTF(in);
		realDistance=checkReadUTF(in);
		primPpCde=checkReadUTF(in);
		priceableItemId=checkReadUTF(in);
		productId=checkReadUTF(in);
		extensionNbr=checkReadUTF(in);
		accountAheadInd=checkReadUTF(in);
		billingInd=checkReadUTF(in);
		delayedFlg=checkReadUTF(in);
		cdrRatingTmstmp=checkReadUTF(in);
		invSpecialOtherPartyInd=checkReadUTF(in);
		invJwswObjectInd=checkReadUTF(in);
		invExtensionDiscountInd=checkReadUTF(in);
		invJwswIntegrationInd=checkReadUTF(in);
		invJwswInternationalInd=checkReadUTF(in);


	}
	@Override
	public void writeEx(DataOutput out) throws IOException {
		out.writeUTF(datakbn);
		out.writeUTF(businessAreaType);
		out.writeUTF(cdrRecordSeqNbr);
		out.writeUTF(mafDivisionNbr);
		out.writeUTF(frsDivisionNbr);
		out.writeUTF(billGroupId);
		out.writeUTF(billMonth);
		out.writeUTF(summaryId);
		out.writeUTF(summaryIdType);
		out.writeUTF(ratingId);
		out.writeUTF(ratingIdType);
		out.writeUTF(callCategoryCde);
		out.writeUTF(billCycleId);
		out.writeUTF(origNbr);
		out.writeUTF(termNbr);
		out.writeUTF(connectDttm);
		out.writeUTF(tmdisconnectDt);
		out.writeUTF(splitInd);
		out.writeUTF(origMaCde);
		out.writeUTF(termMaCde);
		out.writeUTF(accountMaCde);
		out.writeUTF(origExchangeNbrDelimit);
		out.writeUTF(termExchangeNbrDelimit);
		out.writeUTF(accountExchangeNbrDelimit);
		out.writeUTF(processingType);
		out.writeUTF(commMode);
		out.writeUTF(departureType);
		out.writeUTF(productTypeCde);
		out.writeUTF(origNetworkTypeCde);
		out.writeUTF(termNetworkTypeCde);
		out.writeUTF(origCountryCde);
		out.writeUTF(termCountryCde);
		out.writeUTF(invUserDialNbr);
		out.writeUTF(invAccountCde);
		out.writeUTF(invInternalLine1);
		out.writeUTF(invInternalLine2);
		out.writeUTF(invServiceNbr);
		out.writeUTF(invPasswordMngNbr);
		out.writeUTF(invJrTelNbr);
		out.writeUTF(invNpaDialLength);
		out.writeUTF(callingtime);
		out.writeUTF(invUomInd);
		out.writeUTF(timeZoneType);
		out.writeUTF(primCallCharge);
		out.writeUTF(basicCallCharge);
		out.writeUTF(holidayCde);
		out.writeUTF(callTypeCde);
		out.writeUTF(dayOfWeekCde);
		out.writeUTF(maDistance);
		out.writeUTF(realDistance);
		out.writeUTF(primPpCde);
		out.writeUTF(priceableItemId);
		out.writeUTF(productId);
		out.writeUTF(extensionNbr);
		out.writeUTF(accountAheadInd);
		out.writeUTF(billingInd);
		out.writeUTF(delayedFlg);
		out.writeUTF(cdrRatingTmstmp);
		out.writeUTF(invSpecialOtherPartyInd);
		out.writeUTF(invJwswObjectInd);
		out.writeUTF(invExtensionDiscountInd);
		out.writeUTF(invJwswIntegrationInd);
		out.writeUTF(invJwswInternationalInd);
	}

	public String getDatakbn() {
		return datakbn;
	}

	public void setDatakbn(String datakbn) {
		if (datakbn != null) {
			this.datakbn = datakbn;
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

	public String getCdrRecordSeqNbr() {
		return cdrRecordSeqNbr;
	}

	public void setCdrRecordSeqNbr(String cdrRecordSeqNbr) {
		if (cdrRecordSeqNbr != null) {
			this.cdrRecordSeqNbr = cdrRecordSeqNbr;
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

	public String getCallCategoryCde() {
		return callCategoryCde;
	}

	public void setCallCategoryCde(String callCategoryCde) {
		if (callCategoryCde != null) {
			this.callCategoryCde = callCategoryCde;
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

	public String getConnectDttm() {
		return connectDttm;
	}

	public void setConnectDttm(String connectDttm) {
		if (connectDttm != null) {
			this.connectDttm = connectDttm;
		}
	}

	public String getTmdisconnectDt() {
		return tmdisconnectDt;
	}

	public void setTmdisconnectDt(String tmdisconnectDt) {
		if (tmdisconnectDt != null) {
			this.tmdisconnectDt = tmdisconnectDt;
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

	public String getProcessingType() {
		return processingType;
	}

	public void setProcessingType(String processingType) {
		if (processingType != null) {
			this.processingType = processingType;
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

	public String getDepartureType() {
		return departureType;
	}

	public void setDepartureType(String departureType) {
		if (departureType != null) {
			this.departureType = departureType;
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

	public String getCallingtime() {
		return callingtime;
	}

	public void setCallingtime(String callingtime) {
		if (callingtime != null) {
			this.callingtime = callingtime;
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

	public String getTimeZoneType() {
		return timeZoneType;
	}

	public void setTimeZoneType(String timeZoneType) {
		if (timeZoneType != null) {
			this.timeZoneType = timeZoneType;
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

	public String getBasicCallCharge() {
		return basicCallCharge;
	}

	public void setBasicCallCharge(String basicCallCharge) {
		if (basicCallCharge != null) {
			this.basicCallCharge = basicCallCharge;
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

	public String getCallTypeCde() {
		return callTypeCde;
	}

	public void setCallTypeCde(String callTypeCde) {
		if (callTypeCde != null) {
			this.callTypeCde = callTypeCde;
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

	public String getPrimPpCde() {
		return primPpCde;
	}

	public void setPrimPpCde(String primPpCde) {
		if (primPpCde != null) {
			this.primPpCde = primPpCde;
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

	public String getExtensionNbr() {
		return extensionNbr;
	}

	public void setExtensionNbr(String extensionNbr) {
		if (extensionNbr != null) {
			this.extensionNbr = extensionNbr;
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

	public String getBillingInd() {
		return billingInd;
	}

	public void setBillingInd(String billingInd) {
		if (billingInd != null) {
			this.billingInd = billingInd;
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

	public String getCdrRatingTmstmp() {
		return cdrRatingTmstmp;
	}

	public void setCdrRatingTmstmp(String cdrRatingTmstmp) {
		if (cdrRatingTmstmp != null) {
			this.cdrRatingTmstmp = cdrRatingTmstmp;
		}
	}

	public String getInvSpecialOtherPartyInd() {
		return invSpecialOtherPartyInd;
	}

	public void setInvSpecialOtherPartyInd(String invSpecialOtherPartyInd) {
		if (invSpecialOtherPartyInd != null) {
			this.invSpecialOtherPartyInd = invSpecialOtherPartyInd;
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

	public String getInvJwswIntegrationInd() {
		return invJwswIntegrationInd;
	}

	public void setInvJwswIntegrationInd(String invJwswIntegrationInd) {
		if (invJwswIntegrationInd != null) {
			this.invJwswIntegrationInd = invJwswIntegrationInd;
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
}

