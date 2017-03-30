package com.fxiaoke.fhc.bean;

import java.io.Serializable;

/**
 * Created by jiangxd on 2016/10/13.
 */
public class IndustryBean implements Serializable {
    private static final long serialVersionUID = -6754544817854369085L;

    private int industryID;
    private int parentIndustryID;
    private int industryType;
    private String industryName;

    public int getIndustryID() {
        return industryID;
    }

    public void setIndustryID(int industryID) {
        this.industryID = industryID;
    }

    public int getParentIndustryID() {
        return parentIndustryID;
    }

    public void setParentIndustryID(int parentIndustryID) {
        this.parentIndustryID = parentIndustryID;
    }

    public int getIndustryType() {
        return industryType;
    }

    public void setIndustryType(int industryType) {
        this.industryType = industryType;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public IndustryBean(int industryID, int parentIndustryID, int industryType, String industryName) {
        this.industryID = industryID;
        this.parentIndustryID = parentIndustryID;
        this.industryType = industryType;
        this.industryName = industryName;
    }

    public IndustryBean() {
    }
}
