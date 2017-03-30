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
  private Double refundableAmount;
  private Integer orderId;
  private String refundCreateTime;
  private Integer productId;
  private Double subRefundableTotalAmount;

  public Refunds(){}
  public Refunds(Integer eid,
                 Integer refundId,
                 Integer subRefundId,
                 Double refundableAmount,
                 Integer orderId,
                 String refundCreateTime,
                 Integer productId,
                 Double subRefundableTotalAmount){
    this.eid=eid;
    this.refundId=refundId;
    this.subRefundId=subRefundId;
    this.refundableAmount=refundableAmount;
    this.orderId=orderId;
    this.refundCreateTime=refundCreateTime;
    this.productId=productId;
    this.subRefundableTotalAmount=subRefundableTotalAmount;
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

  public Double getRefundableAmount() {
    return refundableAmount;
  }

  public void setRefundableAmount(Double refundableAmount) {
    this.refundableAmount = refundableAmount;
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

  public Double getSubRefundableTotalAmount() {
    return subRefundableTotalAmount;
  }

  public void setSubRefundableTotalAmount(Double subRefundableTotalAmount) {
    this.subRefundableTotalAmount = subRefundableTotalAmount;
  }
}
