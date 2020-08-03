package org.logfileanalizer.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;
import org.logfileanalizer.LogFileLineProcessor;
import org.logfileanalizer.StatisticLogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFileLineProcessorImpl implements LogFileLineProcessor {

  private StatisticLogLevel loglevel;

  final Logger logger = LoggerFactory.getLogger(LogFileLineProcessorImpl.class);

  public LogFileLineProcessorImpl(StatisticLogLevel statisticLogLevel){
    this.loglevel = statisticLogLevel;
  }

  @Override
  public LocalDateTime parseLineForError(String line) {
    if (!StringUtils.isEmpty(line) && StringUtils.containsIgnoreCase(line, loglevel.toString())) {
      String[] parts = line.split(";", 3);
      try {
        LocalDateTime localDateTime = LocalDateTime.parse(parts[0].trim());
        return localDateTime.withSecond(0).withNano(0);// not save second and mill sec
      } catch (DateTimeParseException e) {
        logger.error("Skip string: /'{}/'-cannot convert {} to date: ", line, parts[0], e.getMessage());
        return null;
      }
    }
    else {
      return null;
    }
  }
}
