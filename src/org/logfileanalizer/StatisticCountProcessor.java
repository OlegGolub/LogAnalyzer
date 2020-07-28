package org.logfileanalizer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface StatisticCountProcessor {
  String ERROR_COUNT = "Количество ошибок:";

  class Statistic{

    private LocalDateTime dateTime;
    private int errorCount;

    public Statistic(LocalDateTime dateTime, int count){
      this.dateTime = dateTime;
      this.errorCount = count;
    }
    public int getErrorCount() {
      return errorCount;
    }
    public LocalDateTime getDateTime() {
      return dateTime;
    }
    public String getDateAsString(){
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
      return formatter.format(dateTime);
    }
  }
  /**
   * Count Error at the moment
   * */
  void countErrorStatistic(LocalDateTime moment);

  /**
   * This method will return Statistic
   * */
  List<Statistic> getStatistics();

  void printToFile(String fileName);

  int getErrorStatisticForDateAndInterval(LocalDateTime startDate, StatisticInterval  statisticInterval);
}
