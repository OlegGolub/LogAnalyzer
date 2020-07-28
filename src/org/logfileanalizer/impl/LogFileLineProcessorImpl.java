package org.logfileanalizer.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;
import org.logfileanalizer.LogFileLineProcessor;
import org.logfileanalizer.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFileLineProcessorImpl implements LogFileLineProcessor {

  private LogLevel loglevel;
  final Logger logger = LoggerFactory.getLogger(LogFileLineProcessorImpl.class);

  public LogFileLineProcessorImpl(LogLevel logLevel){
    this.loglevel = logLevel;
  }

  @Override
  public LocalDateTime parseLineForError(String line) {
    if (line!=null && !line.isEmpty() && StringUtils.containsIgnoreCase(line, loglevel.toString())) {
      String[] parts = line.split(";", 3);
      try {
        LocalDateTime localDateTime = LocalDateTime.parse(parts[0]);
        //logger.debug("Line: {} analyzed. {} - Detected at {}", line, loglevel, localDateTime);
        return localDateTime;
      } catch (DateTimeParseException e) {
        logger.error("Skip[ string: {}-cannot convert {} to date: ", line, parts[0], e.getMessage());
        return null;
      }
    }
    else {
      //logger.debug("Line: {} analyzed. {} - Not detected", line, loglevel);
      return null;
    }
  }
}
