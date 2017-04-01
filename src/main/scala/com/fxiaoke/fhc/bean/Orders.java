package com.fxiaoke.fhc.bean;

import java.util.Date;

/**
 *  order
 * Created by jief on 2017/3/22.
 */
public class Orders {
  private Integer eid;
  private Integer orderId;
  private Integer subOrderId;
  private String orderCreateTime;
  private Double totalAmount;
  private Integer productId;
  private Double purchaseAmount;
  private String subOrderCreateTime;
  private String productEndTime;

  public Orders(){}

  public Orders(Integer eid,
                Integer orderId,
                Integer subOrderId,
                String orderCreateTime,
                Double totalAmount,
                Integer productId,
                Double purchaseAmount,
                String subOrderCreateTime, String productEndTime){
    this.eid=eid;
    this.orderId=orderId;
    this.subOrderId=subOrderId;
    this.orderCreateTime=orderCreateTime;
    this.totalAmount=totalAmount;
    this.productId=productId;
    this.purchaseAmount=purchaseAmount;
    this.subOrderCreateTime=subOrderCreateTime;
    this.productEndTime=productEndTime;
  }
  public Integer getEid() {
    return eid;
  }

  public void setEid(Integer eid) {
    this.eid = eid;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public Integer getSubOrderId() {
    return subOrderId;
  }

  public void setSubOrderId(Integer subOrderId) {
    this.subOrderId = subOrderId;
  }

  public String getOrderCreateTime() {
    return orderCreateTime;
  }

  public void setOrderCreateTime(String orderCreateTime) {
    this.orderCreateTime = orderCreateTime;
  }

  public Double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Double getPurchaseAmount() {
    return purchaseAmount;
  }

  public void setPurchaseAmount(Double purchaseAmount) {
    this.purchaseAmount = purchaseAmount;
  }

  public String getSubOrderCreateTime() {
    return subOrderCreateTime;
  }

  public void setSubOrderCreateTime(String subOrderCreateTime) {
    this.subOrderCreateTime = subOrderCreateTime;
  }
  public String getProductEndTime() {
    return productEndTime;
  }

  public void setProductEndTime(String productEndTime) {
    this.productEndTime = productEndTime;
  }

  @Override
  public String toString() {
    return "Orders{" +
            "eid=" + eid +
            ", orderId=" + orderId +
            ", subOrderId=" + subOrderId +
            ", orderCreateTime='" + orderCreateTime + '\'' +
            ", totalAmount=" + totalAmount +
            ", productId=" + productId +
            ", purchaseAmount=" + purchaseAmount +
            ", subOrderCreateTime='" + subOrderCreateTime + '\'' +
            ", productEndTime='" + productEndTime + '\'' +
            '}';
  }
}
