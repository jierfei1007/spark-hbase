package com.fxiaoke.fhc.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.Date;


@JSONType
public class Employee implements Serializable{
    private static final long serialVersionUID = 4104027423678585239L;
    @JSONField(name="id")
    private String id;
    @JSONField(name="enterpriseId")
    private int enterpriseId;
    @JSONField(name="employeeId")
    private int employeeId;
    @JSONField(name="versionType")
    private String versionType;
    @JSONField(name="trialVersionExpireTime")
    private String trialVersionExpireTime;
    @JSONField(name="freePackages")
    private String freePackages;
    @JSONField(name="createTime")
    private Date createTime;
    @JSONField(name="updateTime")
    private String updateTime;
    @JSONField(name="isStop")
    private String isStop;
    @JSONField(name="oldPassword")
    private String oldPassword;
    @JSONField(name="password")
    private String password;
    @JSONField(name="passwordSalt")
    private String passwordSalt;
    @JSONField(name="passwordUpdateTime")
    private String passwordUpdateTime;
    @JSONField(name="employeeAccount")
    private String employeeAccount;
    @JSONField(name="isInitialPassword")
    private String isInitialPassword;
    @JSONField(name="isPauseLogin")
    private String isPauseLogin;
    @JSONField(name="stopUpdateTime")
    private String stopUpdateTime;
    @JSONField(name="isActivated")
    private String isActivated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getTrialVersionExpireTime() {
        return trialVersionExpireTime;
    }

    public void setTrialVersionExpireTime(String trialVersionExpireTime) {
        this.trialVersionExpireTime = trialVersionExpireTime;
    }

    public String getFreePackages() {
        return freePackages;
    }

    public void setFreePackages(String freePackages) {
        this.freePackages = freePackages;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsStop() {
        return isStop;
    }

    public void setIsStop(String isStop) {
        this.isStop = isStop;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordUpdateTime() {
        return passwordUpdateTime;
    }

    public void setPasswordUpdateTime(String passwordUpdateTime) {
        this.passwordUpdateTime = passwordUpdateTime;
    }

    public String getEmployeeAccount() {
        return employeeAccount;
    }

    public void setEmployeeAccount(String employeeAccount) {
        this.employeeAccount = employeeAccount;
    }

    public String getIsInitialPassword() {
        return isInitialPassword;
    }

    public void setIsInitialPassword(String isInitialPassword) {
        this.isInitialPassword = isInitialPassword;
    }

    public String getIsPauseLogin() {
        return isPauseLogin;
    }

    public void setIsPauseLogin(String isPauseLogin) {
        this.isPauseLogin = isPauseLogin;
    }

    public String getStopUpdateTime() {
        return stopUpdateTime;
    }

    public void setStopUpdateTime(String stopUpdateTime) {
        this.stopUpdateTime = stopUpdateTime;
    }

    public String getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }
}
