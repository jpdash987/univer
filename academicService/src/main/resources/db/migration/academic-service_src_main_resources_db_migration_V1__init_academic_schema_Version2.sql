-- Exams & Grades & Transcript storage (PDF S3 reference)
CREATE TABLE IF NOT EXISTS exams (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id VARCHAR(100) NOT NULL,
  exam_date DATE NOT NULL,
  exam_type VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_exams_course (course_id),
  INDEX idx_exams_date (exam_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS grades (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  course_id VARCHAR(100) NOT NULL,
  semester VARCHAR(50) NOT NULL,
  marks DECIMAL(5,2) NOT NULL,
  grade VARCHAR(5) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_grades_student (student_id),
  INDEX idx_grades_semester (semester),
  INDEX idx_grades_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS transcripts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  s3_bucket VARCHAR(100) NOT NULL,
  s3_key VARCHAR(500) NOT NULL,
  gpa DECIMAL(4,3) NULL,
  cgpa DECIMAL(4,3) NULL,
  generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_transcripts_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;