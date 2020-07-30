package org.logfileanalizer.impl;

import org.apache.commons.lang3.StringUtils;
import org.logfileanalizer.StatisticProcessor;
import org.logfileanalizer.StatisticWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StatisticWriterToFile implements StatisticWriter {

    String rootDirectory;
    String ERROR_COUNT = "Количество ошибок: ";
    String DATE_PATTERN = "yyyyMMddHHmm'.txt'";
    String FILE_NAME_PATTERN = "reportFile_";

    final Logger logger = LoggerFactory.getLogger(StatisticWriterToFile.class);

    public StatisticWriterToFile(String rootDirectory){
        this.rootDirectory=rootDirectory;
    }

    public StatisticWriterToFile(){

    }

    protected String getFileName(){
        String datePart= new SimpleDateFormat(DATE_PATTERN).format(new Date());
        return FILE_NAME_PATTERN + datePart;
    }

    protected String getRootDirectoryName(){
        return !StringUtils.isEmpty(rootDirectory) ? rootDirectory : System.getProperty("user.dir");
    }

    @Override
    public long writeStatistic( List<StatisticProcessor.Statistic> statisticList ) {
        return writeStatistic(statisticList, getFileName());
    }

    @Override
    public long writeStatistic( List<StatisticProcessor.Statistic> statisticList, String fileName) {

       if (StringUtils.isEmpty(fileName)){
           fileName = getFileName();
       }

       String absoluteFilePath = getRootDirectoryName() +"\\"+ fileName;

       logger.info("StatisticWriterToFile printing to file: " + absoluteFilePath);

       try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absoluteFilePath))){
           for (StatisticProcessor.Statistic statistic : statisticList) {
                String line = String.format("%s %s %d", statistic.getDateAsString(), ERROR_COUNT, statistic.getErrorCount());
                bufferedWriter.write(line);
                bufferedWriter.append('\n');
           }
           bufferedWriter.flush();
       }
       catch( IOException ex){
              System.out.println(ex.getMessage());
       }
       File file = new File(absoluteFilePath);
       if( file.exists()){
           logger.info("File {} successfully written, size {} bytes", absoluteFilePath, file.length());
           return file.length();
       }else {
           logger.error("File {} not found", absoluteFilePath);
           return 0;
       }
    }
}
