package com.fxiaoke.fhc.test;

import java.io.Serializable;

/**
 * Created by jief on 2017/3/31.
 */
public class People implements Serializable{
  private String name;
  private int age;
  public People(){}
  public People(String name,int age){
    this.name=name;
    this.age=age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
