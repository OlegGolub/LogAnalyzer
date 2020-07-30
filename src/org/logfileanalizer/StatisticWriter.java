package org.logfileanalizer;

import java.util.List;

public interface StatisticWriter {
    long writeStatistic(StatisticProcessor statisticProccessor, String fileName);
    long writeStatistic(StatisticProcessor statisticProccessor);
}
