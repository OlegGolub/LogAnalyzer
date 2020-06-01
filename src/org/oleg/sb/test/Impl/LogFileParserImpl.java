package org.oleg.sb.test.Impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.oleg.sb.test.LogFileLineProcessor;

public class LogFileParserImpl implements Runnable{

  private File file;
  LogFileLineProcessor logFileLineProcessor;

  public LogFileParserImpl(File file, LogFileLineProcessor logFileLineProcessor){
    this.logFileLineProcessor = logFileLineProcessor;
    this.file=file;
  }

  @Override
  public void run() {
    try {
      parseLogFile(file);
    }catch (IOException e){
     System.out.println("Exception : "+ e.getMessage());
    }
  }

  public void parseLogFile(File file) throws IOException {
      if (file==null || !file.exists() || !file.isFile()){
        throw new RuntimeException("");
      }

      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        logFileLineProcessor.parseLine(line);
      }
    }
  }

}
