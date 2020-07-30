package org.logfileanalizer;

import java.io.File;
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

      StatisticLogLevel statisticLogLevel = StatisticLogLevel.ERROR;

      logger.info("Start analysing log files for Log Level \'{}\'", statisticLogLevel);

      SourceLogProvider sourceLogProvider = new SourceLogProviderImpl();
      logger.debug("For getting log files is used "+ sourceLogProvider.getClass().getSimpleName());

      List<File> files = sourceLogProvider.getLogFiles();
      int filesNumber = files.size();

      StatisticKeeper statisticKeeper = new StatisticKeeperConcurrentSkipListMap(
                                            StatisticInterval.MINUTE, statisticLogLevel);

      ExecutorService executorService = Executors.newFixedThreadPool(filesNumber);
      files.forEach( file->
        executorService.submit(new LogFileParserImpl(file,
                                                    new LogFileLineProcessorImpl(statisticLogLevel),
                                                    statisticKeeper)));

      //wait all threads to stop
      System.out.println("Working...");
      executorService.shutdown();
      try {
          if(executorService.awaitTermination(4, TimeUnit.SECONDS)){
              System.out.println("All files have been processed");

              StatisticWriter statisticWriter  = new StatisticWriterToFile();
              statisticWriter.writeStatistic(statisticKeeper);
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
