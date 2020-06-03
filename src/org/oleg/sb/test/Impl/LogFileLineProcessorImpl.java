package org.oleg.sb.test.Impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;
import org.oleg.sb.test.LogFileLineProcessor;
import org.oleg.sb.test.LogLevel;
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
    logger.info("Start Parsing line: {} for {}", line, loglevel);
    if (line!=null || !line.isEmpty() && StringUtils.containsIgnoreCase(line, loglevel.toString())) {
      String[] parts = line.split(";", 3);
      try {
        LocalDateTime localDateTime = LocalDateTime.parse(parts[0]);
        logger.info("Date: {}", localDateTime);
        return localDateTime;
      } catch (DateTimeParseException e) {
        logger
            .error("Skip[ string: {} cannot convert {} to date: ", line, parts[0], e.getMessage());
        return null;
      }
    }
    else {
      return null;
    }
  }
}
