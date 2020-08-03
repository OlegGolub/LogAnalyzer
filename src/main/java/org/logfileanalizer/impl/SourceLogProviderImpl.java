package org.logfileanalizer.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.logfileanalizer.SourceLogProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This file works with sourc log files
 * Input the source Directory
 * The onley 10 files is processed
 * */
public class SourceLogProviderImpl implements SourceLogProvider {
  public static int MAX_LOG_FILES = 10;
  final Logger logger = LoggerFactory.getLogger(SourceLogProviderImpl.class);

  @Override
  public List<File> getLogFiles(){
    Scanner scan = new Scanner(System.in);
    String root;
    do{
      System.out.print("Введите root directory path or press <Enter> for default dir<resources>:");
      root = scan.nextLine();
      logger.debug("Entering the root {}", root);
      Path path;
      if("".compareToIgnoreCase(root)==0){
        root= "d:\\Work\\CodeTest\\SmartB_Test\\src\\resources\\";
      }
      if("l".compareToIgnoreCase(root)==0){
        root= "d:\\Work\\CodeTest\\SmartB_Test\\src\\resources\\long";
      }
      if("s".compareToIgnoreCase(root)==0){
        root= "d:\\Work\\CodeTest\\SmartB_Test\\src\\resources\\short";
      }

      path = Paths.get(root);
      if( !Files.exists(path)){
        System.out.print(String.format("The directory with name '\'%s\' does not exist", path));
        continue;
      }

      if (!Files.isDirectory(path)){
        System.out.println(String.format("The \'%s\' is not a directory", path));
        continue;
      }

      File dir  = new File(root);
      List<File> parts = Arrays.asList(dir.listFiles());
      final List<File> collectedFiles = parts.stream().filter(f->!f.isDirectory()).collect(Collectors.toList());

      logger.info("Accepted the path: {}, and there are {} files in it", path, collectedFiles.size());
      if (collectedFiles.size()>MAX_LOG_FILES){
          System.out.println("There are to many files in the directory: " + collectedFiles.size());
          throw new RuntimeException("There are to many files in the directory");
      }
      return collectedFiles;
    } while(!root.equals(""));
    return Collections.emptyList();
  }

}
