package com.fxiaoke.fhc.bean;

import java.util.Date;

/**
 * refunds
 * Created by jief on 2017/3/22.
 */
public class Refunds {

  private Integer eid;
  private Integer refundId;
  private Integer subRefundId;
  private Double refundAmount;
  private Integer orderId;
  private String refundCreateTime;
  private Integer productId;
  private Double subRefundAmount;

  public Refunds(){}
  public Refunds(Integer eid,
                 Integer refundId,
                 Integer subRefundId,
                 Double refundAmount,
                 Integer orderId,
                 String refundCreateTime,
                 Integer productId,
                 Double subRefundAmount){
    this.eid=eid;
    this.refundId=refundId;
    this.subRefundId=subRefundId;
    this.refundAmount=refundAmount;
    this.orderId=orderId;
    this.refundCreateTime=refundCreateTime;
    this.productId=productId;
    this.subRefundAmount=subRefundAmount;
  }
  public Integer getEid() {
    return eid;
  }

  public void setEid(Integer eid) {
    this.eid = eid;
  }

  public Integer getRefundId() {
    return refundId;
  }

  public void setRefundId(Integer refundId) {
    this.refundId = refundId;
  }

  public Integer getSubRefundId() {
    return subRefundId;
  }

  public void setSubRefundId(Integer subRefundId) {
    this.subRefundId = subRefundId;
  }

  public Double getRefundAmount() {
    return refundAmount;
  }
  public void setRefundAmount(Double refundAmount) {
    this.refundAmount = refundAmount;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public String getRefundCreateTime() {
    return refundCreateTime;
  }

  public void setRefundCreateTime(String refundCreateTime) {
    this.refundCreateTime = refundCreateTime;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Double getSubRefundAmount() {
    return subRefundAmount;
  }

  public void setSubRefundAmount(Double subRefundAmount) {
    this.subRefundAmount = subRefundAmount;
  }

  @Override
  public String toString() {
    return "Refunds{" +
            "eid=" + eid +
            ", refundId=" + refundId +
            ", subRefundId=" + subRefundId +
            ", refundAmount=" + refundAmount +
            ", orderId=" + orderId +
            ", refundCreateTime='" + refundCreateTime + '\'' +
            ", productId=" + productId +
            ", subRefundAmount=" + subRefundAmount +
            '}';
  }
}
