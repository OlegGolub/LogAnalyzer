package org.oleg.sb.test;

import java.time.LocalDateTime;

public interface LogFileLineProcessor {
  LocalDateTime parseLineForError(String line);
}
