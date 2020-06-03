package org.oleg.sb.test;

import java.time.LocalDateTime;

public interface StatisticResultProcessor {
  int getErrorStatistic(LocalDateTime startDate, StatisticInterval  statisticInterval);
}
