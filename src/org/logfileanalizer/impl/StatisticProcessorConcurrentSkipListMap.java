package org.logfileanalizer.impl;

import org.logfileanalizer.StatisticInterval;
import org.logfileanalizer.StatisticCountProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;


public class StatisticProcessorConcurrentSkipListMap  implements StatisticCountProcessor {

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
    public void countErrorStatistic(LocalDateTime moment) {
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
    public void printToFile(String reportFileName) {
        logger.info("StatisticProcessorConcurrentSkipListMap printing to file: "+reportFileName);

        try(FileWriter writer = new FileWriter(reportFileName, false))
        {
            List<Statistic> statisticList = getStatistics();
            for (Statistic statistic : statisticList) {
                String line = String.format("%s %s %d", statistic.getDateAsString(), ERROR_COUNT, statistic.getErrorCount());
                writer.write(line);
                writer.append('\n');
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public int getErrorStatisticForDateAndInterval(LocalDateTime theDate, StatisticInterval statisticInterval) {
        return map.get(theDate);
    }
}
