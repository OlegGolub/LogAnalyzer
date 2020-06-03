package org.oleg.sb.test;

import java.time.LocalDateTime;

public interface StatisticCountProcessor {
  void countErrorStatistic(LocalDateTime moment);
}
