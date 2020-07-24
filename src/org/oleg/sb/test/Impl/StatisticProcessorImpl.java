package org.oleg.sb.test.Impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

import org.oleg.sb.test.StatisticCountProcessor;
import org.oleg.sb.test.StatisticInterval;
import org.oleg.sb.test.StatisticResultProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticProcessorImpl implements StatisticCountProcessor, StatisticResultProcessor {

  static final int MONTH_IN_YEAR = 12;
  static final int DAYS_IN_MONTH = 31;
  static final int HOURS_IN_DAY = 24;
  static final int MINUTES_IN_HOUR = 60;

  static final int MONTH_OFFSET = DAYS_IN_MONTH * HOURS_IN_DAY * MINUTES_IN_HOUR;
  static final int DAY_OFFSET = HOURS_IN_DAY * MINUTES_IN_HOUR;

  final Logger logger = LoggerFactory.getLogger(StatisticProcessorImpl.class);
  LongAdder[] atomicCounterArray;

  public StatisticProcessorImpl() {
    long timeStampStart = System.currentTimeMillis();
    atomicCounterArray = new LongAdder[MONTH_IN_YEAR * MONTH_OFFSET];// 535,680
    for (int i = 0; i < atomicCounterArray.length; i++) {
      atomicCounterArray[i] = new LongAdder();
    }
    long timeStampEnd = System.currentTimeMillis();
    logger.info("Array with size {} initialized, time spent {} ms", atomicCounterArray.length,
        (timeStampEnd - timeStampStart));
  }

  @Override
  public void countErrorStatistic(LocalDateTime moment) {
    int index = getIndexForMoment(moment);
    atomicCounterArray[index].increment();
    logger.info("Incremented index for moment(offset) {}/{} to {}",moment, index, atomicCounterArray[getIndexForMoment(moment)].intValue());
  }

  public LongAdder[] getResultArray(){
    finalize();
    return atomicCounterArray;

  }

  public void printRezult(){
    for(int i =0; i<atomicCounterArray.length; i++){
      int value = atomicCounterArray[i].intValue();
      if(value!=0){
        System.out.println(String.format("Moment: %d Errors: %d",i, value ));
      }
    }
  }

  public void finalize(){
    for(int i =0; i<atomicCounterArray.length; i++){
      int value = (int) atomicCounterArray[i].sum();
      if(value>0){
        System.out.println("error detected for index "+i);
      }
    }
  }

  private int getIndexForMoment(LocalDateTime startDate){
    int index =  startDate.getMonthValue() * MONTH_OFFSET + startDate.getDayOfMonth()*DAY_OFFSET + startDate.getHour() * MINUTES_IN_HOUR + startDate.getMinute();
    System.out.println(String.format("Index for moment %s(%d/%d/%d%d) is %d", startDate,
            startDate.getMonthValue(),
            startDate.getDayOfMonth(),
            startDate.getHour(),
            startDate.getMinute(),
            index));
    return index;
  }

//  private LongAdder getCounterForMoment(int monthIndex, int hourIndex, int minuteIndex) {
//    int index = monthIndex * MONTH_OFFSET + hourIndex * DAY_OFFSET + minuteIndex;
//    return atomicCounterArray[index];
//  }

  @Override
  public int getErrorStatistic(LocalDateTime startDate, StatisticInterval statisticInterval) {
  //  getCounterForMoment(startDate.getMonthValue(), startDate.getHour(), startDate.getMinute());
    return 1;
  }


//  private List<LongAdder> getCountersForInterval(LocalDateTime start, StatisticInterval  statisticInterval) {
//    int indexStart = getIndexForMoment(start);
//
//    int distance = (StatisticInterval.HOUR==statisticInterval) ? HOURS_IN_DAY : 1;
//    return atomicCounterArray[index];
//  }
}