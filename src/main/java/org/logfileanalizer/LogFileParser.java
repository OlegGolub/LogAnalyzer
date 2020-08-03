package org.logfileanalizer;

import java.io.File;
import java.io.IOException;

public interface LogFileParser {
    void parseLogFile(File file) throws IOException;
}
