package org.logfileanalizer;

public interface StatisticWriter {
    long writeStatistic(StatisticKeeper statisticProccessor, String fileName);
    long writeStatistic(StatisticKeeper statisticProccessor);
}
