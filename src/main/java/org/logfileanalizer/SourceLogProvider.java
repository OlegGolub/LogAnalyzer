package org.logfileanalizer;

import java.io.File;
import java.util.List;

public interface SourceLogProvider {
  List<File> getLogFiles();
}
