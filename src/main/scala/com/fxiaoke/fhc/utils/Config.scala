package com.fxiaoke.fhc.utils

import java.util.Map

import com.github.autoconf.ConfigFactory

/**
  * Created by bill on 16/7/18.
  */
object Config {
  /**
    * 获取配置文件
    *
    * @param name  配置名称
    * @param group 配置组
    * @return
    */
  def getConfig(name: String, group: String): Map[String, String] = {
    System.setProperty("spring.profiles.active", group)
    val esConfig: Map[String, String] = ConfigFactory.getConfig(name).getAll()
    esConfig
  }
}