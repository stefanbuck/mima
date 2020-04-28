package com.typesafe.tools.mima.core.util.log

private[mima] trait Logging {
  def verbose(msg: String): Unit
  def debug(msg: String): Unit
  def warn(msg: String): Unit
  def error(msg: String): Unit
}
