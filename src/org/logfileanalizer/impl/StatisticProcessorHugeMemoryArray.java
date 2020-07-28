package org.logfileanalizer.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import org.logfileanalizer.StatisticCountProcessor;
import org.logfileanalizer.StatisticInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatisticProcessorHugeMemoryArray implements StatisticCountProcessor {

  static final int MONTH_IN_YEAR = 12;
  static final int DAYS_IN_MONTH = 31;
  static final int HOURS_IN_DAY = 24;
  static final int MINUTES_IN_HOUR = 60;
  static final String ERROR_COUNT = "Количество ошибок:";
  static final int MONTH_OFFSET = DAYS_IN_MONTH * HOURS_IN_DAY * MINUTES_IN_HOUR;
  static final int DAY_OFFSET = HOURS_IN_DAY * MINUTES_IN_HOUR;

  LongAdder[] atomicCounterArray;

  private final Logger logger = LoggerFactory.getLogger(StatisticProcessorHugeMemoryArray.class);

  public StatisticProcessorHugeMemoryArray() {

    long timeStampStart = System.currentTimeMillis();
    atomicCounterArray = new LongAdder[MONTH_IN_YEAR * MONTH_OFFSET];// 535,680
    for (int i = 0; i < atomicCounterArray.length; i++) {
      atomicCounterArray[i] = new LongAdder();
    }
    long timeStampEnd = System.currentTimeMillis();
    logger.info("StatisticProcessorHugeMemoryArray  is used as StatisticProcessor" +
                "\nArray with size {} initialized, time spent {} ms", atomicCounterArray.length,
        (timeStampEnd - timeStampStart));
  }

  @Override
  public void countErrorStatistic(LocalDateTime moment) {
    int index = getIndexForMoment(moment);
    atomicCounterArray[index].increment();
  }

  @Override
  public List<Statistic> getStatistics() {

    finalize();

    List<Statistic> statisticList = new LinkedList<>();

    for (int i =0; i<atomicCounterArray.length; i++) {
      int value = atomicCounterArray[i].intValue();
      if (value != 0) {
        statisticList.add(new Statistic(getDateFromIndex(i), value));
      }
    }
    return statisticList;
  }

  @Override
  public int getErrorStatisticForDateAndInterval(LocalDateTime startDate, StatisticInterval statisticInterval) {
    throw new UnsupportedOperationException("Not supported");
  }

  public void printToFile(String reportFileName){

    try(FileWriter writer = new FileWriter(reportFileName, false))
    {
      for(int i =0; i<atomicCounterArray.length; i++){
        int value = atomicCounterArray[i].intValue();
        if (value!=0){
          String line = String.format("%s %s %d", getStringDateFromIndex(i), ERROR_COUNT, value );
          writer.write(line);
          writer.append('\n');
          logger.info(line);
        }
      }
      writer.flush();
    }
    catch(IOException ex){
      System.out.println(ex.getMessage());
    }
  }

  public void finalize(){
    for(int i =0; i<atomicCounterArray.length; i++){
      int value = (int) atomicCounterArray[i].sum();
      if (value>0){
        logger.info("error detected for index " + i);
      }
    }
  }

  private static LocalDateTime getDateFromIndex(int index) {

    LocalDateTime date = LocalDateTime.of(2020,
            index / MONTH_OFFSET - 1,
            (index % MONTH_OFFSET) / DAY_OFFSET,
            (index % MONTH_OFFSET) / HOURS_IN_DAY / MINUTES_IN_HOUR,
            ((index % MONTH_OFFSET) / DAY_OFFSET) % MINUTES_IN_HOUR);

    return date;
  }

  private String getStringDateFromIndex(int index){
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    GregorianCalendar calendar = new GregorianCalendar(2020,
            index/MONTH_OFFSET-1,
            (index % MONTH_OFFSET)/DAY_OFFSET,
            (index % MONTH_OFFSET)/HOURS_IN_DAY/MINUTES_IN_HOUR,
            ((index % MONTH_OFFSET)/DAY_OFFSET) % MINUTES_IN_HOUR);
    return dateFormat.format(calendar.getTime());
  }

  private int getIndexForMoment(LocalDateTime startDate){
    int index =  startDate.getMonthValue() * MONTH_OFFSET +
                 startDate.getDayOfMonth() * DAY_OFFSET +
                 startDate.getHour() * MINUTES_IN_HOUR +
                 startDate.getMinute();
    return index;
  }
}