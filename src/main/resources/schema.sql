CREATE TABLE subsidy_applications (
  id                BIGINT         PRIMARY KEY,
  applicant_name    VARCHAR(100)   NOT NULL,
  application_date  DATE           NOT NULL,
  amount            DECIMAL(15, 0) NOT NULL,
  status            VARCHAR(20)    NOT NULL
);

CREATE TABLE staff_users (
  id          BIGINT        PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(100)  NOT NULL,
  email       VARCHAR(100)  NOT NULL UNIQUE,
  role        VARCHAR(20)   NOT NULL,
  created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP     DEFAULT NULL
);
