package org.oleg.sb.test.Impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.oleg.sb.test.SourceLogProvider;
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
      System.out.print("Введите root directory path:");
      root = scan.nextLine();
      logger.debug("Entering the root {}", root);
      Path path = Paths.get(root);
      if( !Files.exists(path)){
        System.out.print(String.format("The directory with name '\'%s\' does not exist", path));
        continue;
      }

      if (!Files.isDirectory(path)){
        System.out.println(String.format("The \'%s\' is not a directory", path));
        continue;
      }

      logger.debug("Accepted path {}", path);

      File dir  = new File(root);
      List<File> files = Arrays.asList(dir.listFiles());
      logger.info("Accepted the path: {}, and there are {} files in it", path, files.size());
      if (files.size()>MAX_LOG_FILES){
          System.out.println("There are to many files in the directory: " + files.size());
          throw new RuntimeException("There are to many files in the directory");
      }

      System.out.println(String.format("There are %d files in the dir %s", files.size(), dir));

      return files;
    } while(!root.equals(""));
    return Collections.emptyList();
  }

}
