package com.fxiaoke.fhc.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *  员工动态表bean：dw_dim.dim_pub_employee_info_dynamic_daily
 */
public class EmployeeInfo implements Serializable {

    private static final long serialVersionUID = -8416457354892518571L;
    private int eid;
    private int uid;
    private Date registeTime;
    private Date firstLoginTime;
    private Date lastLoginTime;
    private boolean lostStatus;
    private Date lostTime;
    private int lastLoginPf;
    private int lastLoginProv;
    private String lastLoginPfProv;
    private int lastEnterpriseType;
    private int lostEnterpriseType;
    private int firstLoginEnterpriseType;
    private int registeEnterpriseType;
    private int enterpriseType;
    private int enterpriseAccountNumber;
    private int lastEnterpriseAccountNumber;
    private int lostEnterpriseAccountNumber;
    private int firstLoginEnterpriseAccountNumber;
    private int registeEnterpriseAccountNumber;
    private int isStop;
    private String isStopDesc;

    public int getIsStop() {
        return isStop;
    }

    public void setIsStop(int isStop) {
        this.isStop = isStop;
    }

    public String getIsStopDesc() {
        return isStopDesc;
    }

    public void setIsStopDesc(String isStopDesc) {
        this.isStopDesc = isStopDesc;
    }

    public int getEnterpriseAccountNumber() {
        return enterpriseAccountNumber;
    }

    public void setEnterpriseAccountNumber(int enterpriseAccountNumber) {
        this.enterpriseAccountNumber = enterpriseAccountNumber;
    }

    public int getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(int enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public int getFirstLoginEnterpriseAccountNumber() {
        return firstLoginEnterpriseAccountNumber;
    }

    public void setFirstLoginEnterpriseAccountNumber(int firstLoginEnterpriseAccountNumber) {
        this.firstLoginEnterpriseAccountNumber = firstLoginEnterpriseAccountNumber;
    }

    public int getLastEnterpriseAccountNumber() {
        return lastEnterpriseAccountNumber;
    }

    public void setLastEnterpriseAccountNumber(int lastEnterpriseAccountNumber) {
        this.lastEnterpriseAccountNumber = lastEnterpriseAccountNumber;
    }

    public int getLostEnterpriseAccountNumber() {
        return lostEnterpriseAccountNumber;
    }

    public void setLostEnterpriseAccountNumber(int lostEnterpriseAccountNumber) {
        this.lostEnterpriseAccountNumber = lostEnterpriseAccountNumber;
    }

    public int getRegisteEnterpriseAccountNumber() {
        return registeEnterpriseAccountNumber;
    }

    public void setRegisteEnterpriseAccountNumber(int registeEnterpriseAccountNumber) {
        this.registeEnterpriseAccountNumber = registeEnterpriseAccountNumber;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getRegisteTime() {
        return registeTime;
    }

    public void setRegisteTime(Date registeTime) {
        this.registeTime = registeTime;
    }

    public Date getFirstLoginTime() {
        return firstLoginTime;
    }

    public void setFirstLoginTime(Date firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isLostStatus() {
        return lostStatus;
    }

    public void setLostStatus(boolean lostStatus) {
        this.lostStatus = lostStatus;
    }

    public Date getLostTime() {
        return lostTime;
    }

    public void setLostTime(Date lostTime) {
        this.lostTime = lostTime;
    }

    public int getLastLoginPf() {
        return lastLoginPf;
    }

    public void setLastLoginPf(int lastLoginPf) {
        this.lastLoginPf = lastLoginPf;
    }

    public int getLastLoginProv() {
        return lastLoginProv;
    }

    public void setLastLoginProv(int lastLoginProv) {
        this.lastLoginProv = lastLoginProv;
    }

    public String getLastLoginPfProv() {
        return lastLoginPfProv;
    }

    public void setLastLoginPfProv(String lastLoginPfProv) {
        this.lastLoginPfProv = lastLoginPfProv;
    }

    public int getFirstLoginEnterpriseType() {
        return firstLoginEnterpriseType;
    }

    public void setFirstLoginEnterpriseType(int firstLoginEnterpriseType) {
        this.firstLoginEnterpriseType = firstLoginEnterpriseType;
    }

    public int getLastEnterpriseType() {
        return lastEnterpriseType;
    }

    public void setLastEnterpriseType(int lastEnterpriseType) {
        this.lastEnterpriseType = lastEnterpriseType;
    }

    public int getLostEnterpriseType() {
        return lostEnterpriseType;
    }

    public void setLostEnterpriseType(int lostEnterpriseType) {
        this.lostEnterpriseType = lostEnterpriseType;
    }

    public int getRegisteEnterpriseType() {
        return registeEnterpriseType;
    }

    public void setRegisteEnterpriseType(int registeEnterpriseType) {
        this.registeEnterpriseType = registeEnterpriseType;
    }

    @Override
    public String toString() {
        return "EmployeeInfo{" +
                "eid=" + eid +
                ", uid=" + uid +
                ", registeTime=" + registeTime +
                ", firstLoginTime=" + firstLoginTime +
                ", lastLoginTime=" + lastLoginTime +
                ", lostStatus=" + lostStatus +
                ", lostTime=" + lostTime +
                ", lastLoginPf=" + lastLoginPf +
                ", lastLoginProv=" + lastLoginProv +
                ", lastLoginPfProv='" + lastLoginPfProv + '\'' +
                ", lastEnterpriseType=" + lastEnterpriseType +
                ", lostEnterpriseType=" + lostEnterpriseType +
                ", firstLoginEnterpriseType=" + firstLoginEnterpriseType +
                ", registeEnterpriseType=" + registeEnterpriseType +
                ", enterpriseType=" + enterpriseType +
                ", enterpriseAccountNumber=" + enterpriseAccountNumber +
                ", lastEnterpriseAccountNumber=" + lastEnterpriseAccountNumber +
                ", lostEnterpriseAccountNumber=" + lostEnterpriseAccountNumber +
                ", firstLoginEnterpriseAccountNumber=" + firstLoginEnterpriseAccountNumber +
                ", registeEnterpriseAccountNumber=" + registeEnterpriseAccountNumber +
                ", isStop=" + isStop +
                ", isStopDesc='" + isStopDesc + '\'' +
                '}';
    }
}
