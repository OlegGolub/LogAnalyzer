package org.oleg.sb.test.Impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import org.oleg.sb.test.LogFileLineProcessor;
import org.oleg.sb.test.StatisticCountProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFileParserImpl implements Runnable{

  private File file;
  LogFileLineProcessor    logFileLineProcessor;
  StatisticCountProcessor statisticProcessor;

  final Logger logger = LoggerFactory.getLogger(LogFileParserImpl.class);

  public LogFileParserImpl(File file,
      LogFileLineProcessor logFileLineProcessor,
      StatisticCountProcessor statisticCountProcessor
  ){
    this.logFileLineProcessor = logFileLineProcessor;
    this.statisticProcessor = statisticCountProcessor;
    this.file=file;
    logger.info("Created LogFileProcessor for file {}", file);
  }

  @Override
  public void run() {
    try {
      logger.info("Start parse log file: {} in thread: {}", file, Thread.currentThread().getName());
      parseLogFile(file);
    }catch (IOException e){
      logger.error("Exception : "+ e.getMessage());
    }
    logger.info("Finished parse log file: {} in thread: {}", file, Thread.currentThread().getName());
  }

  public void parseLogFile(File file) throws IOException {
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      int countError=0;
      while ((line = br.readLine()) != null) {
        logger.info("Get line : {}",line);
        LocalDateTime momentWithError = logFileLineProcessor.parseLineForError(line);
        if(momentWithError!=null){
          statisticProcessor.countErrorStatistic(momentWithError);
          countError++;
        }
      }
      logger.info("File {}  processed: there are {} errors", file, countError);
    }
  }
}
