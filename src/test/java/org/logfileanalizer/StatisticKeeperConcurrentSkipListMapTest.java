package org.logfileanalizer;

import org.junit.Assert;
import org.junit.Test;
import org.logfileanalizer.impl.StatisticKeeperConcurrentSkipListMap;

import java.time.LocalDateTime;

public class StatisticKeeperConcurrentSkipListMapTest {
    private static StatisticKeeper statisticKeeper = new StatisticKeeperConcurrentSkipListMap(StatisticInterval.HOUR, StatisticLogLevel.ERROR);

    @Test
    public void registerTheFactTest(){
        Assert.assertEquals("Statistic must be empty on creation",0, statisticKeeper.getStatistics().size());
        LocalDateTime localDateTime = LocalDateTime.of(2020,1,5,15,00);
        statisticKeeper.registerTheFact(localDateTime);
        Assert.assertNotNull("Statistic must have not null output", statisticKeeper.getStatistics());
        Assert.assertEquals("Statistic must have one record",1, statisticKeeper.getStatistics().size());

        StatisticKeeper.Statistic statistic  = new StatisticKeeper.Statistic(localDateTime, StatisticInterval.HOUR, 1);
        Assert.assertEquals("Statistic must have right record",1, statisticKeeper.getStatistics().get(0).getFactsCount());

        statisticKeeper.registerTheFact(localDateTime.plusMinutes(30));
        Assert.assertEquals("Statistic must have right number of records",1, statisticKeeper.getStatistics().size());
        Assert.assertEquals("Statistic must have right number of events",2, statisticKeeper.getStatistics().get(0).getFactsCount());
        Assert.assertEquals("Statistic must have right event Date",localDateTime, statisticKeeper.getStatistics().get(0).getDateTime());

        statisticKeeper.registerTheFact(localDateTime.plusMinutes(80));
        Assert.assertEquals("Statistic must have right number of records",2, statisticKeeper.getStatistics().size());
        Assert.assertEquals("Statistic must have right number of events",2, statisticKeeper.getStatistics().get(0).getFactsCount());
        Assert.assertEquals("Statistic must have right number of events",1, statisticKeeper.getStatistics().get(1).getFactsCount());
        Assert.assertEquals("Statistic must have right event Date",localDateTime.plusMinutes(80).withMinute(0), statisticKeeper.getStatistics().get(1).getDateTime());
    }
}
