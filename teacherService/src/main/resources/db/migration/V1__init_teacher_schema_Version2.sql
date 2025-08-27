-- Teacher service: classes, attendance, grade items, grades
CREATE TABLE IF NOT EXISTS classes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(255) NOT NULL,
  term VARCHAR(50) NOT NULL,
  teacher_user_id BIGINT NOT NULL,
  UNIQUE KEY uq_classes_code_term (code, term),
  INDEX idx_classes_teacher (teacher_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS attendance_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  class_id BIGINT NOT NULL,
  session_date DATE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_attendance_class_date (class_id, session_date),
  INDEX idx_attendance_class (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS attendance_entries (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  record_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  present TINYINT(1) NOT NULL,
  notes VARCHAR(500) NULL,
  UNIQUE KEY uq_attendance_record_student (record_id, student_id),
  INDEX idx_attendance_entries_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS grade_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  class_id BIGINT NOT NULL,
  item_type VARCHAR(30) NOT NULL,  -- ASSIGNMENT | QUIZ | EXAM
  item_id VARCHAR(100) NOT NULL,
  max_score DECIMAL(10,2) NOT NULL,
  assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_grade_item (class_id, item_type, item_id),
  INDEX idx_grade_items_class (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS grades (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  grade_item_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  score DECIMAL(10,2) NOT NULL,
  comment VARCHAR(500) NULL,
  graded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_grade_one_per_item (grade_item_id, student_id),
  INDEX idx_grades_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;