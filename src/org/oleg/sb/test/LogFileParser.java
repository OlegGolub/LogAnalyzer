package org.oleg.sb.test;

import java.io.File;
import java.io.FileNotFoundException;

public interface LogFileParser {
  void parseLogFile(File file) throws FileNotFoundException;
}
