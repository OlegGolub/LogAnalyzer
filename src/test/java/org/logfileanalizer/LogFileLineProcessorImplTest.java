package org.logfileanalizer;

import org.junit.Assert;
import org.junit.Test;
import org.logfileanalizer.impl.LogFileLineProcessorImpl;

import java.time.LocalDateTime;

public class LogFileLineProcessorImplTest {
    private static String LOCAL_DATE_TIME_STRING = "2020-01-01T12:17:01.001";
    private static String LOG_LINE_WITH_ERROR = LOCAL_DATE_TIME_STRING+";ERROR; Ошибка 1";
    private static String LOG_LINE_WITH_WARN = "2020-01-01T12:17:01.001;ERROR; Ошибка 1";
    private static LocalDateTime LOCAL_DATE_TIME =  LocalDateTime.parse(LOCAL_DATE_TIME_STRING).withSecond(0).withNano(0);

    LogFileLineProcessor logFileLineProcessor = new LogFileLineProcessorImpl(StatisticLogLevel.ERROR);

    @Test
    public void testParseLineForError(){
        System.out.println("testParseLineForError");
        LocalDateTime localDateTime = logFileLineProcessor.parseLineForError(LOG_LINE_WITH_ERROR);
        Assert.assertNotNull("Error fact does not detected", localDateTime);
        Assert.assertEquals("Error fact DateTime does not detected correct", LOCAL_DATE_TIME, localDateTime);
    }

    @Test
    public void testParseLineForWarn(){
        System.out.println("testParseLineForError");
        LocalDateTime localDateTime = logFileLineProcessor.parseLineForError(LOG_LINE_WITH_WARN);
        Assert.assertNotNull("Error fact does not detected", localDateTime);
        Assert.assertEquals("Error fact DateTime does not detected correct", LOCAL_DATE_TIME, localDateTime);
    }

}
