-- Notification logs table
CREATE TABLE IF NOT EXISTS notification (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type VARCHAR(20) NOT NULL,           -- EMAIL | SMS | EVENT
  recipient VARCHAR(255) NULL,
  status VARCHAR(30) NOT NULL,         -- SENT | FAILED | RECEIVED
  payload TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_notification_type (type),
  INDEX idx_notification_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;