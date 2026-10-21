-- Run this from MySQL Workbench while connected as a MySQL administrator.
-- It creates/resets the local application account used by Spring Boot and the
-- "Interview Tracker (Local)" Workbench connection.
CREATE DATABASE IF NOT EXISTS interview_tracker
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'interview_tracker'@'localhost'
  IDENTIFIED BY 'interview_tracker';
ALTER USER 'interview_tracker'@'localhost'
  IDENTIFIED BY 'interview_tracker';
GRANT ALL PRIVILEGES ON interview_tracker.* TO 'interview_tracker'@'localhost';

-- Some MySQL installations resolve localhost TCP clients as 127.0.0.1.
CREATE USER IF NOT EXISTS 'interview_tracker'@'127.0.0.1'
  IDENTIFIED BY 'interview_tracker';
ALTER USER 'interview_tracker'@'127.0.0.1'
  IDENTIFIED BY 'interview_tracker';
GRANT ALL PRIVILEGES ON interview_tracker.* TO 'interview_tracker'@'127.0.0.1';
