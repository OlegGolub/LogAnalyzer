package org.logfileanalizer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface StatisticKeeper {

  class Statistic{

    private LocalDateTime dateTime;
    private StatisticInterval statisticInterval;
    private int factsCount;


    public Statistic(LocalDateTime startDateTime,  StatisticInterval statisticInterval, int count){
      this.dateTime = startDateTime;
      this.factsCount = count;
      this.statisticInterval=statisticInterval;
    }
    public int getFactsCount() {
      return factsCount;
    }

    public LocalDateTime getDateTime() {
      return dateTime;
    }

    public String getDateAsString(){

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm-");
      StringBuilder sb = new StringBuilder(formatter.format(dateTime));

      if(StatisticInterval.HOUR ==statisticInterval){
        sb.append(dateTime.plusHours(1).getHour()+".00");
      }else if(StatisticInterval.MINUTE ==statisticInterval){
        LocalDateTime endDateTime = dateTime.plusMinutes(1);
        sb.append(endDateTime.getHour()+":"+endDateTime.getMinute());
      }
      return sb.toString();
    }
  }

  /**
   * Count Error at the moment
   * */
  void registerTheFact(LocalDateTime moment);

  /**
   * This method will return Statistic
   * */
  List<Statistic> getStatistics();
  StatisticLogLevel getStatisticLogLevel();

  int getErrorStatisticForDateAndInterval(LocalDateTime startDate, StatisticInterval  statisticInterval);
}
