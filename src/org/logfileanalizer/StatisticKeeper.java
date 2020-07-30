package org.logfileanalizer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface StatisticKeeper {

  class Statistic{

    private static DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
    private static DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("-HH:mm");

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

      StringBuilder sb = new StringBuilder(formatterStart.format(dateTime));

      if(StatisticInterval.HOUR ==statisticInterval){
        sb.append(formatterEnd.format(dateTime.plusHours(1)));
      }else if(StatisticInterval.MINUTE ==statisticInterval){
        sb.append(sb.append(formatterEnd.format(dateTime.plusMinutes(1))));
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
