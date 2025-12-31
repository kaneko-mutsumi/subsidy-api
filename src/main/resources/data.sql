INSERT INTO subsidy_applications
  (applicant_name, application_date, amount, status)

VALUES
  ('山田太郎', '2024-06-01', 1000000, 'APPLIED');

INSERT INTO subsidy_applications
  (applicant_name, application_date, amount, status)

 VALUES
  ('鈴木花子', '2024-06-02', 500000, 'APPROVED');

INSERT INTO subsidy_applications
  (applicant_name, application_date, amount, status)
VALUES
  ('佐藤次郎', '2024-06-03', 750000, 'PAID');

INSERT INTO staff_users (name, email, role)
VALUES ('管理者 太郎', 'admin@example.com', 'ADMIN');

INSERT INTO staff_users (name, email, role)
VALUES ('職員 花子', 'staff1@example.com', 'STAFF');

INSERT INTO staff_users (name, email, role)
VALUES ('職員 次郎', 'staff2@example.com', 'STAFF');

