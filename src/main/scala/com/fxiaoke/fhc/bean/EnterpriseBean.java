package com.fxiaoke.fhc.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;


public class EnterpriseBean implements Serializable {
    private static final long serialVersionUID = 1504210866146853976L;
    @JSONField(name="EnterpriseID")
    private int enterpriseId;
    @JSONField(name="EnterpriseName")
    private String  enterpriseName;
    @JSONField(name="EnterpriseShortName")
    private String enterpriseShortName;
    @JSONField(name="EnterpriseAccount")
    private String enterpriseAccount;
    @JSONField(name="VendorID")
    private String vendorId;
    @JSONField(name="KeyContactName")
    private String keycontactName;
    @JSONField(name = "keycontactPhone")
    private String keycontactPhone;
    @JSONField(name="KeyContactEmail")
    private String keyContactEmail;
    @JSONField(name="ContactName")
    private String contactName;
    @JSONField(name="ContactPhone")
    private String contactPhone;
    @JSONField(name="ContactEmail")
    private String contactEmail;
    @JSONField(name="ContactIM")
    private String contactIm;
    @JSONField(name="Address")
    private String address;
    @JSONField(name="Remark")
    private String remark;
    @JSONField(name="ProductID")
    private String productId;
    @JSONField(name="ProductChangeTime")
    private String productChangeTime;
    @JSONField(name="RunStatus")
    private String runStatus;
    @JSONField(name="AccountTotalAmount")
    private String accounttotalAmount;
    @JSONField(name="AccountUsedAmount")
    private String accountusedAmount;
    @JSONField(name="SmsTotalAmount")
    private String smsTotalAmount;
    @JSONField(name="SmsUsedAmount")
    private String smsUsedAmount;
    @JSONField(name="StorageTotalSpace")
    private String storageTotalSpace;
    @JSONField(name="StorageUsedSpace")
    private String storageUsedSpace;
    @JSONField(name="FirstPayTime")
    private String firstPayTime;
    @JSONField(name="EndTime")
    private String endTime;
    @JSONField(name="AppStartTime")
    private String appStartTime;
    @JSONField(name="DbServerIP")
    private String dbServerIp;
    @JSONField(name="EncryptString")
    private String encryptString;
    @JSONField(name="AppVersion")
    private String appVersion;
    @JSONField(name="AppUpdaterID")
    private String appUpdaterId;
    @JSONField(name="AppUpdateTime")
    private String appUpdateTime;
    @JSONField(name="IsUpdating")
    private String isUpdating;
    @JSONField(name="CreatorID")
    private String creatorId;
    @JSONField(name="CreateTime")
    private Date createTime;
    @JSONField(name="ModifierID")
    private String modifierId;
    @JSONField(name="ModifyTime")
    private String modifyTime;
    @JSONField(name="IsAgreementSigned")
    private String isagreementSigned;
    @JSONField(name="SmsSubPort")
    private String smssubPort;
    @JSONField(name="Modules")
    private String modules;
    @JSONField(name="Activity")
    private String activity;
    @JSONField(name="Industry")
    private int industry;
    @JSONField(name="Source")
    private int source;
    @JSONField(name="Province")
    private int province;
    @JSONField(name="GroupType")
    private String groupType;
    @JSONField(name="KnowSource")
    private String knowSource;
    @JSONField(name="RegisterMotive")
    private String registerMotive;
    @JSONField(name="LoginIn3Days")
    private String loginin3days;
    @JSONField(name="LoginIn3DaysDate")
    private String loginin3daysdate;
    @JSONField(name="LoginIn7Days")
    private String loginin7days;
    @JSONField(name="LoginIn7DaysDate")
    private String loginin7daysdate;
    @JSONField(name="LoginIn30Days")
    private String loginin30days;
    @JSONField(name="LoginIn30DaysDate")
    private String loginin30daysdate;
    @JSONField(name="LoginIn60Days")
    private String loginin60days;
    @JSONField(name="LoginIn60DaysDate")
    private String loginin60daysdate;
    @JSONField(name="LoginIn180Days")
    private String loginin180days;
    @JSONField(name="LoginIn180DaysDate")
    private String loginin180daysdate;
    @JSONField(name="ActiveIn30Days")
    private String activein30days;
    @JSONField(name="ActiveIn30DaysDate")
    private String activein30daysdate;
    @JSONField(name="ActiveIn60Days")
    private String activein60days;
    @JSONField(name="ActiveIn60DaysDate")
    private String activein60daysdate;
    @JSONField(name="LastTraceType")
    private String lasttraceType;
    @JSONField(name="LastTraceDate")
    private String lastTraceDate;
    @JSONField(name="LastTraceAccountID")
    private String lastTraceAccountId;
    @JSONField(name="IsMarketingStimulationEnabled")
    private String isMarketingstimulationEnabled;
    @JSONField(name="IsLoginPagePersonalization")
    private String isLoginPagePersonalization;
    @JSONField(name="BusinessCardUseType")
    private String businessCarduseType;
    @JSONField(name="BusinessCardTotalAmount")
    private String businessCardtotalAmount;
    @JSONField(name="BusinessCardUsedAmount")
    private String businessCardusedAmount;
    @JSONField(name="IsPayed")
    private String isPayed;
    @JSONField(name="IsAttach")
    private String isAttach;
    @JSONField(name="IsMaintaining")
    private String isMaintaining;
    @JSONField(name="DFSType")
    private String dfsType;
    @JSONField(name="DFSAddress")
    private String dfsAddress;
    @JSONField(name="DFSGroup")
    private String dfsGroup;
    @JSONField(name="ActivityType")
    private String activityType;
    @JSONField(name="ActivityTypeChangeTime")
    private String activityTypechangeTime;
    @JSONField(name="V3Database")
    private String v3database;
    @JSONField(name="CompanyScale")
    private int companyScale;
    @JSONField(name="IsSaleTeam")
    private String isSaleteam;
    @JSONField(name="SaleTeamScale")
    private String saleTeamscale;
    @JSONField(name="IsFirstmeetingSign")
    private String isFirstMeetingsign;
    @JSONField(name="IsFastSign")
    private String isFastsign;
    @JSONField(name="DealDays")
    private String dealDays;
    @JSONField(name="IsWillPin")
    private String isWillpin;
    @JSONField(name="IsStrangerVisits")
    private String isStrangervisits;
    @JSONField(name="IsAutoclave")
    private String isAutoclave;
    @JSONField(name="AutoclaveDays")
    private String autoClaveDays;
    @JSONField(name="IsReferral")
    private String isReferral;
    @JSONField(name="IsJiuCiFang")
    private String isJiucifang;
    @JSONField(name="BaichuanTotalAmount")
    private String baichuanTotalAmount;
    @JSONField(name="BaichuanUsedAmount")
    private String baichuanusedAmount;
    @JSONField(name="City")
    private String city;
    @JSONField(name="AppVersionUpdateWeight")
    private String appVersionUpdateWeight;
    @JSONField(name="BaichuanEndTime")
    private String baichuanendTime;
    @JSONField(name="AdvertisementFrom")
    private String advertisementFrom;
    private int enterpriseType;
    private int enterpriseGroup;
    private String enterpriseGroupDesc;


    public int getEnterpriseGroup() {
        return enterpriseGroup;
    }

    public void setEnterpriseGroup(int enterpriseGroup) {
        this.enterpriseGroup = enterpriseGroup;
    }

    public String getEnterpriseGroupDesc() {
        return enterpriseGroupDesc;
    }

    public void setEnterpriseGroupDesc(String enterpriseGroupDesc) {
        this.enterpriseGroupDesc = enterpriseGroupDesc;
    }

    public int getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(int enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseShortName() {
        return enterpriseShortName;
    }

    public void setEnterpriseShortName(String enterpriseShortName) {
        this.enterpriseShortName = enterpriseShortName;
    }

    public String getEnterpriseAccount() {
        return enterpriseAccount;
    }

    public void setEnterpriseAccount(String enterpriseAccount) {
        this.enterpriseAccount = enterpriseAccount;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getKeycontactName() {
        return keycontactName;
    }

    public void setKeycontactName(String keycontactName) {
        this.keycontactName = keycontactName;
    }

    public String getKeycontactPhone() {
        return keycontactPhone;
    }

    public void setKeycontactPhone(String keycontactPhone) {
        this.keycontactPhone = keycontactPhone;
    }

    public String getKeyContactEmail() {
        return keyContactEmail;
    }

    public void setKeyContactEmail(String keyContactEmail) {
        this.keyContactEmail = keyContactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactIm() {
        return contactIm;
    }

    public void setContactIm(String contactIm) {
        this.contactIm = contactIm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductChangeTime() {
        return productChangeTime;
    }

    public void setProductChangeTime(String productChangeTime) {
        this.productChangeTime = productChangeTime;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getAccounttotalAmount() {
        return accounttotalAmount;
    }

    public void setAccounttotalAmount(String accounttotalAmount) {
        this.accounttotalAmount = accounttotalAmount;
    }

    public String getAccountusedAmount() {
        return accountusedAmount;
    }

    public void setAccountusedAmount(String accountusedAmount) {
        this.accountusedAmount = accountusedAmount;
    }

    public String getSmsTotalAmount() {
        return smsTotalAmount;
    }

    public void setSmsTotalAmount(String smsTotalAmount) {
        this.smsTotalAmount = smsTotalAmount;
    }

    public String getSmsUsedAmount() {
        return smsUsedAmount;
    }

    public void setSmsUsedAmount(String smsUsedAmount) {
        this.smsUsedAmount = smsUsedAmount;
    }

    public String getStorageTotalSpace() {
        return storageTotalSpace;
    }

    public void setStorageTotalSpace(String storageTotalSpace) {
        this.storageTotalSpace = storageTotalSpace;
    }

    public String getStorageUsedSpace() {
        return storageUsedSpace;
    }

    public void setStorageUsedSpace(String storageUsedSpace) {
        this.storageUsedSpace = storageUsedSpace;
    }

    public String getFirstPayTime() {
        return firstPayTime;
    }

    public void setFirstPayTime(String firstPayTime) {
        this.firstPayTime = firstPayTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAppStartTime() {
        return appStartTime;
    }

    public void setAppStartTime(String appStartTime) {
        this.appStartTime = appStartTime;
    }

    public String getDbServerIp() {
        return dbServerIp;
    }

    public void setDbServerIp(String dbServerIp) {
        this.dbServerIp = dbServerIp;
    }

    public String getEncryptString() {
        return encryptString;
    }

    public void setEncryptString(String encryptString) {
        this.encryptString = encryptString;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppUpdaterId() {
        return appUpdaterId;
    }

    public void setAppUpdaterId(String appUpdaterId) {
        this.appUpdaterId = appUpdaterId;
    }

    public String getAppUpdateTime() {
        return appUpdateTime;
    }

    public void setAppUpdateTime(String appUpdateTime) {
        this.appUpdateTime = appUpdateTime;
    }

    public String getIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(String isUpdating) {
        this.isUpdating = isUpdating;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getIsagreementSigned() {
        return isagreementSigned;
    }

    public void setIsagreementSigned(String isagreementSigned) {
        this.isagreementSigned = isagreementSigned;
    }

    public String getSmssubPort() {
        return smssubPort;
    }

    public void setSmssubPort(String smssubPort) {
        this.smssubPort = smssubPort;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getKnowSource() {
        return knowSource;
    }

    public void setKnowSource(String knowSource) {
        this.knowSource = knowSource;
    }

    public String getRegisterMotive() {
        return registerMotive;
    }

    public void setRegisterMotive(String registerMotive) {
        this.registerMotive = registerMotive;
    }

    public String getLoginin3days() {
        return loginin3days;
    }

    public void setLoginin3days(String loginin3days) {
        this.loginin3days = loginin3days;
    }

    public String getLoginin3daysdate() {
        return loginin3daysdate;
    }

    public void setLoginin3daysdate(String loginin3daysdate) {
        this.loginin3daysdate = loginin3daysdate;
    }

    public String getLoginin7days() {
        return loginin7days;
    }

    public void setLoginin7days(String loginin7days) {
        this.loginin7days = loginin7days;
    }

    public String getLoginin7daysdate() {
        return loginin7daysdate;
    }

    public void setLoginin7daysdate(String loginin7daysdate) {
        this.loginin7daysdate = loginin7daysdate;
    }

    public String getLoginin30days() {
        return loginin30days;
    }

    public void setLoginin30days(String loginin30days) {
        this.loginin30days = loginin30days;
    }

    public String getLoginin30daysdate() {
        return loginin30daysdate;
    }

    public void setLoginin30daysdate(String loginin30daysdate) {
        this.loginin30daysdate = loginin30daysdate;
    }

    public String getLoginin60days() {
        return loginin60days;
    }

    public void setLoginin60days(String loginin60days) {
        this.loginin60days = loginin60days;
    }

    public String getLoginin60daysdate() {
        return loginin60daysdate;
    }

    public void setLoginin60daysdate(String loginin60daysdate) {
        this.loginin60daysdate = loginin60daysdate;
    }

    public String getLoginin180days() {
        return loginin180days;
    }

    public void setLoginin180days(String loginin180days) {
        this.loginin180days = loginin180days;
    }

    public String getLoginin180daysdate() {
        return loginin180daysdate;
    }

    public void setLoginin180daysdate(String loginin180daysdate) {
        this.loginin180daysdate = loginin180daysdate;
    }

    public String getActivein30days() {
        return activein30days;
    }

    public void setActivein30days(String activein30days) {
        this.activein30days = activein30days;
    }

    public String getActivein30daysdate() {
        return activein30daysdate;
    }

    public void setActivein30daysdate(String activein30daysdate) {
        this.activein30daysdate = activein30daysdate;
    }

    public String getActivein60days() {
        return activein60days;
    }

    public void setActivein60days(String activein60days) {
        this.activein60days = activein60days;
    }

    public String getActivein60daysdate() {
        return activein60daysdate;
    }

    public void setActivein60daysdate(String activein60daysdate) {
        this.activein60daysdate = activein60daysdate;
    }

    public String getLasttraceType() {
        return lasttraceType;
    }

    public void setLasttraceType(String lasttraceType) {
        this.lasttraceType = lasttraceType;
    }

    public String getLastTraceDate() {
        return lastTraceDate;
    }

    public void setLastTraceDate(String lastTraceDate) {
        this.lastTraceDate = lastTraceDate;
    }

    public String getLastTraceAccountId() {
        return lastTraceAccountId;
    }

    public void setLastTraceAccountId(String lastTraceAccountId) {
        this.lastTraceAccountId = lastTraceAccountId;
    }

    public String getIsMarketingstimulationEnabled() {
        return isMarketingstimulationEnabled;
    }

    public void setIsMarketingstimulationEnabled(String isMarketingstimulationEnabled) {
        this.isMarketingstimulationEnabled = isMarketingstimulationEnabled;
    }

    public String getIsLoginPagePersonalization() {
        return isLoginPagePersonalization;
    }

    public void setIsLoginPagePersonalization(String isLoginPagePersonalization) {
        this.isLoginPagePersonalization = isLoginPagePersonalization;
    }

    public String getBusinessCarduseType() {
        return businessCarduseType;
    }

    public void setBusinessCarduseType(String businessCarduseType) {
        this.businessCarduseType = businessCarduseType;
    }

    public String getBusinessCardtotalAmount() {
        return businessCardtotalAmount;
    }

    public void setBusinessCardtotalAmount(String businessCardtotalAmount) {
        this.businessCardtotalAmount = businessCardtotalAmount;
    }

    public String getBusinessCardusedAmount() {
        return businessCardusedAmount;
    }

    public void setBusinessCardusedAmount(String businessCardusedAmount) {
        this.businessCardusedAmount = businessCardusedAmount;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    public String getIsAttach() {
        return isAttach;
    }

    public void setIsAttach(String isAttach) {
        this.isAttach = isAttach;
    }

    public String getIsMaintaining() {
        return isMaintaining;
    }

    public void setIsMaintaining(String isMaintaining) {
        this.isMaintaining = isMaintaining;
    }

    public String getDfsType() {
        return dfsType;
    }

    public void setDfsType(String dfsType) {
        this.dfsType = dfsType;
    }

    public String getDfsAddress() {
        return dfsAddress;
    }

    public void setDfsAddress(String dfsAddress) {
        this.dfsAddress = dfsAddress;
    }

    public String getDfsGroup() {
        return dfsGroup;
    }

    public void setDfsGroup(String dfsGroup) {
        this.dfsGroup = dfsGroup;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityTypechangeTime() {
        return activityTypechangeTime;
    }

    public void setActivityTypechangeTime(String activityTypechangeTime) {
        this.activityTypechangeTime = activityTypechangeTime;
    }

    public String getV3database() {
        return v3database;
    }

    public void setV3database(String v3database) {
        this.v3database = v3database;
    }

    public int getCompanyScale() {
        return companyScale;
    }

    public void setCompanyScale(int companyScale) {
        this.companyScale = companyScale;
    }

    public String getIsSaleteam() {
        return isSaleteam;
    }

    public void setIsSaleteam(String isSaleteam) {
        this.isSaleteam = isSaleteam;
    }

    public String getSaleTeamscale() {
        return saleTeamscale;
    }

    public void setSaleTeamscale(String saleTeamscale) {
        this.saleTeamscale = saleTeamscale;
    }

    public String getIsFirstMeetingsign() {
        return isFirstMeetingsign;
    }

    public void setIsFirstMeetingsign(String isFirstMeetingsign) {
        this.isFirstMeetingsign = isFirstMeetingsign;
    }

    public String getIsFastsign() {
        return isFastsign;
    }

    public void setIsFastsign(String isFastsign) {
        this.isFastsign = isFastsign;
    }

    public String getDealDays() {
        return dealDays;
    }

    public void setDealDays(String dealDays) {
        this.dealDays = dealDays;
    }

    public String getIsWillpin() {
        return isWillpin;
    }

    public void setIsWillpin(String isWillpin) {
        this.isWillpin = isWillpin;
    }

    public String getIsStrangervisits() {
        return isStrangervisits;
    }

    public void setIsStrangervisits(String isStrangervisits) {
        this.isStrangervisits = isStrangervisits;
    }

    public String getIsAutoclave() {
        return isAutoclave;
    }

    public void setIsAutoclave(String isAutoclave) {
        this.isAutoclave = isAutoclave;
    }

    public String getAutoClaveDays() {
        return autoClaveDays;
    }

    public void setAutoClaveDays(String autoClaveDays) {
        this.autoClaveDays = autoClaveDays;
    }

    public String getIsReferral() {
        return isReferral;
    }

    public void setIsReferral(String isReferral) {
        this.isReferral = isReferral;
    }

    public String getIsJiucifang() {
        return isJiucifang;
    }

    public void setIsJiucifang(String isJiucifang) {
        this.isJiucifang = isJiucifang;
    }

    public String getBaichuanTotalAmount() {
        return baichuanTotalAmount;
    }

    public void setBaichuanTotalAmount(String baichuanTotalAmount) {
        this.baichuanTotalAmount = baichuanTotalAmount;
    }

    public String getBaichuanusedAmount() {
        return baichuanusedAmount;
    }

    public void setBaichuanusedAmount(String baichuanusedAmount) {
        this.baichuanusedAmount = baichuanusedAmount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAppVersionUpdateWeight() {
        return appVersionUpdateWeight;
    }

    public void setAppVersionUpdateWeight(String appVersionUpdateWeight) {
        this.appVersionUpdateWeight = appVersionUpdateWeight;
    }

    public String getBaichuanendTime() {
        return baichuanendTime;
    }

    public void setBaichuanendTime(String baichuanendTime) {
        this.baichuanendTime = baichuanendTime;
    }

    public String getAdvertisementFrom() {
        return advertisementFrom;
    }

    public void setAdvertisementFrom(String advertisementFrom) {
        this.advertisementFrom = advertisementFrom;
    }
}
