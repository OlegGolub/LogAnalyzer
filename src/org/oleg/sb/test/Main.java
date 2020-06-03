package org.oleg.sb.test;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.oleg.sb.test.Impl.LogFileLineProcessorImpl;
import org.oleg.sb.test.Impl.LogFileParserImpl;
import org.oleg.sb.test.Impl.SourceLogProviderImpl;
import org.oleg.sb.test.Impl.StatisticProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  public static void main(String[] args) {

      final Logger logger = LoggerFactory.getLogger(Main.class);
      logger.info("Start working");

      logger.debug("SourceLogProviderImpl  is used for getting log files");
      SourceLogProvider sourceLogProvider = new SourceLogProviderImpl();

      List<File> files = sourceLogProvider.getLogFiles();
      int filesNumber = files.size();
      logger.info("There are {} log files", filesNumber);


      ExecutorService executorService = Executors.newFixedThreadPool(filesNumber);
      files.forEach( file->
        executorService.submit(new LogFileParserImpl(
                                        file,
                                        new LogFileLineProcessorImpl(LogLevel.ERROR),
                                        new StatisticProcessorImpl()
                                        )
                               ));
    }
}
