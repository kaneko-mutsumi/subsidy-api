CREATE TABLE subsidy_applications (
  id                BIGINT         PRIMARY KEY,
  applicant_name    VARCHAR(100)   NOT NULL,
  application_date  DATE           NOT NULL,
  amount            DECIMAL(15, 0) NOT NULL,
  status            VARCHAR(20)    NOT NULL
);
