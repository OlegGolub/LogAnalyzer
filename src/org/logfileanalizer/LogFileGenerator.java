package org.logfileanalizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LogFileGenerator {

    final Logger logger = LoggerFactory.getLogger(LogFileGenerator.class);

    private LocalDateTime currentDateTime;
    private DateTimeFormatter dateFormatFull = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private DateTimeFormatter dateFormatShort = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    private Random random = new Random();
    private String fileName;
    private boolean changeDay;

    private  String FILE_NAME = "log_system_";
    private  String FILE_EXTENSION =".log";
    private  String WARN  = "WARN";
    private  String ПРЕДУПРЕЖДЕНИЕ = "Предупреждение ";
    private  String ERROR = "ERROR";
    private  String ОШИБКА = "Ошибка ";

    public LogFileGenerator(String systemName){
        currentDateTime = LocalDateTime.now();
        this.changeDay = false;
        createFileName(systemName);
    }

    public LogFileGenerator(String systemName, int year, int month, int day){
        this(systemName, year, month, day, false);
    }
    public LogFileGenerator(String systemName, int year, int month, int day, boolean changeDay){
        currentDateTime = LocalDateTime.of(year, month, day,0,0);
        this.changeDay = changeDay;
        createFileName(systemName);
    }

    public void printToFile(){

        long startTime = System.currentTimeMillis();

        try(FileWriter writer = new FileWriter(fileName, false))
        {
            for(int i=0; i<10000;i++){
                writer.write(generateLogString());
                writer.append('\n');
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        logger.info("Log File {} created in {} ms", fileName, (System.currentTimeMillis()-startTime));
    }

    private void createFileName(String systemName){
        fileName = FILE_NAME + systemName + "_"+ dateFormatShort.format(currentDateTime)+ FILE_EXTENSION;
    }

    private String generateLogString(){

        boolean isWarning = random.nextBoolean() ? true :
                (random.nextBoolean() ? true : (random.nextBoolean() ? true : false));

         StringBuilder sb = new StringBuilder(getNextDateString(changeDay));
         sb.append(";").
            append(isWarning? WARN: ERROR).
            append(";").
            append(isWarning? ПРЕДУПРЕЖДЕНИЕ : ОШИБКА).
            append(random.nextInt(5)+1);
         return sb.toString();
    }

    private String getNextDateString(boolean changeDay){
        int randomDayOffset = random.nextInt(5)+1;
        if (changeDay && randomDayOffset<3){
            //change day
            currentDateTime = currentDateTime.plusDays(randomDayOffset);
        }
        //Always change hh:mm:ss:SSS
        int randomHH = random.nextInt(24);
        currentDateTime = currentDateTime.plusHours(randomHH).plusNanos(randomHH*123456789);

        return dateFormatFull.format(currentDateTime);
    }
}
