-- Fee structure, Scholarships, Invoices, Payments
CREATE TABLE IF NOT EXISTS fee_structure (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id VARCHAR(100) NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  category VARCHAR(50) NOT NULL,
  UNIQUE KEY uq_fee_course_category (course_id, category),
  INDEX idx_fee_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS scholarships (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL UNIQUE,
  type VARCHAR(20) NOT NULL,         -- PERCENT | AMOUNT
  value DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS invoices (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  currency VARCHAR(10) NOT NULL DEFAULT 'USD',
  category VARCHAR(50) NOT NULL,
  scholarship_code VARCHAR(50) NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'PENDING',  -- PENDING | PAID | OVERDUE | CANCELLED
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  paid_at TIMESTAMP NULL,
  INDEX idx_invoices_student (student_id),
  INDEX idx_invoices_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  invoice_id BIGINT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED', -- COMPLETED | FAILED | REFUNDED
  provider VARCHAR(50) NULL,     -- DUMMY, STRIPE, RAZORPAY
  reference VARCHAR(100) NULL,   -- external reference or token
  receipt_url VARCHAR(500) NULL,
  paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_payments_invoice (invoice_id),
  INDEX idx_payments_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;