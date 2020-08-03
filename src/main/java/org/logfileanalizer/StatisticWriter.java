package org.logfileanalizer;

public interface StatisticWriter {
    long writeStatistic(StatisticKeeper statisticProcessor, String fileName);
    long writeStatistic(StatisticKeeper statisticProcessor);
}
