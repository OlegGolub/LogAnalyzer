package org.logfileanalizer;

import java.util.List;

public interface StatisticWriter {
    long writeStatistic(List<StatisticProcessor.Statistic> statisticList, String fileName);
    long writeStatistic(List<StatisticProcessor.Statistic> statisticList);
}
