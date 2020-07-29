package org.logfileanalizer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.logfileanalizer.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  public static void main(String[] args) {

      final Logger logger = LoggerFactory.getLogger(Main.class);
      logger.info("Start working");

      SourceLogProvider sourceLogProvider = new SourceLogProviderImpl();
      logger.debug("For getting log files is used "+ sourceLogProvider.getClass().getSimpleName());

      List<File> files = sourceLogProvider.getLogFiles();
      int filesNumber = files.size();

      StatisticCountProcessor statisticProcessor = new StatisticProcessorConcurrentSkipListMap();
      //StatisticCountProcessor statisticProcessor = new StatisticProcessorHugeMemoryArray();
      ExecutorService executorService = Executors.newFixedThreadPool(filesNumber);
      files.forEach( file->
        executorService.submit(new LogFileParserImpl(file,
                                                    new LogFileLineProcessorImpl(LogLevel.ERROR),
                                                    statisticProcessor)));

      //wait all threads to stop
      System.out.println("Working...");
      executorService.shutdown();
      try {
          if(executorService.awaitTermination(4, TimeUnit.SECONDS)){
              System.out.println("All files have been processed");
              //statisticProcessor.printResult();
              String datePart= new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
              statisticProcessor.printToFile("reportFile_"+datePart);
          }
          else {
              System.out.println("Not ready in 4 sec");
          }
      } catch (InterruptedException e) {
          executorService.shutdownNow();
//          Thread.currentThread().interrupt();
      }
  }
}
