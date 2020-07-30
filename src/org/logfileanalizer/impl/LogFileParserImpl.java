package org.logfileanalizer.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

import org.logfileanalizer.LogFileLineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.logfileanalizer.StatisticProcessor;

public class LogFileParserImpl implements Runnable {

  private File file;
  private LogFileLineProcessor logFileLineProcessor;
  private StatisticProcessor statisticProcessor;

  final Logger logger = LoggerFactory.getLogger(LogFileParserImpl.class);

  public LogFileParserImpl(File file,
                           LogFileLineProcessor logFileLineProcessor,
                           StatisticProcessor statisticCountProcessor
  ){
    this.logFileLineProcessor = logFileLineProcessor;
    this.statisticProcessor = statisticCountProcessor;
    this.file=file;
  }

  @Override
  public void run() {
    try {
        parseLogFile(file);
    }catch (IOException e){
      logger.error("Exception : "+ e.getMessage());
    }
  }

  public void parseLogFile(File file) throws IOException {

      long startTime = System.currentTimeMillis();

      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
          String line;
          int countError=0;
          //logger.info("Start process log file: {}", file.getName());
          while ((line = br.readLine()) != null) {
            //logger.debug("File: {}, line : {}", file.getName(), line);
            LocalDateTime momentWithError = logFileLineProcessor.parseLineForError(line);
            if(momentWithError!=null){
              statisticProcessor.registerTheFact(momentWithError);
              countError++;
            }
      }

      logger.info("File \'{}\' processed in {}ms by thread \'{}\': there are {} records",
              file.getName(),
              (System.currentTimeMillis()-startTime),
              Thread.currentThread().getName(),
              countError);
    }
  }
}
