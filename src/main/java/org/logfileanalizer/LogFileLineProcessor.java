package org.logfileanalizer;

import java.time.LocalDateTime;

public interface LogFileLineProcessor {
  LocalDateTime parseLineForError(String line);
}
