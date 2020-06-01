package org.oleg.sb.test.Impl;

import org.apache.commons.lang3.StringUtils;
import org.oleg.sb.test.LogFileLineProcessor;
import org.oleg.sb.test.LogLevel;

public class LogFileLineProcessorImpl implements LogFileLineProcessor {

  private LogLevel loglevel;

  public LogFileLineProcessorImpl(LogLevel logLevel){
    this.loglevel = logLevel;

  }

  @Override
  public boolean parseLine(String line) {
    if (line!=null || !line.isEmpty() && StringUtils.containsIgnoreCase(line, loglevel.toString())){

      return true;
    }else {
      return false;
    }
  }
}
