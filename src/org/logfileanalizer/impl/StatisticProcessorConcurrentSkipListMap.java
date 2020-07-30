package org.logfileanalizer.impl;

import org.apache.commons.lang3.StringUtils;
import org.logfileanalizer.StatisticInterval;
import org.logfileanalizer.StatisticProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;


public class StatisticProcessorConcurrentSkipListMap  implements StatisticProcessor {

    private ConcurrentNavigableMap<LocalDateTime, Integer> map = new ConcurrentSkipListMap();
    private StatisticInterval  statisticInterval;

    final Logger logger = LoggerFactory.getLogger(StatisticProcessorConcurrentSkipListMap.class);

    public StatisticProcessorConcurrentSkipListMap(StatisticInterval  statisticInterval){
        this.statisticInterval = statisticInterval;
    }
    
    @Override
    /**
     * Count this fact according to statisticInterval
     * */
    public void registerTheError(LocalDateTime moment) {
        LocalDateTime localDateTime = moment.withNano(0);
        if(statisticInterval==StatisticInterval.HOUR) {
            localDateTime = localDateTime.withMinute(0);
        }
        map.merge(localDateTime, 1, Integer::sum);
    }

    @Override
    public List<Statistic> getStatistics() {
        return map.keySet().stream().
                            map(key->new Statistic(key, statisticInterval, map.get(key))).
                            collect(Collectors.toList());
    }



    @Override
    public int getErrorStatisticForDateAndInterval(LocalDateTime theDate, StatisticInterval statisticInterval) {
        return map.get(theDate);
    }
}
