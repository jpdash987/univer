CREATE TABLE admissions_approval (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     student_id BIGINT NOT NULL,
                                     admission_id BIGINT NOT NULL,
                                     status VARCHAR(50) NOT NULL,
                                     approved_by VARCHAR(100),
                                     approved_at TIMESTAMP NULL
);