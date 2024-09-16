CREATE DOMAIN "Id" AS BIGINT;
CREATE DOMAIN "VarChar255" AS varchar(255);
CREATE DOMAIN "VarChar500" AS varchar(500);
CREATE DOMAIN "DateTime" AS timestamp without time zone;
CREATE DOMAIN "Decimal" AS numeric(16,2);

CREATE TABLE bank_transaction (
  id "Id" NOT NULL,
  operation_date "DateTime" NOT NULL,
  order_date "DateTime" NOT NULL,
  note "VarChar255" NOT NULL,
  quota "Decimal" NOT NULL
) WITHOUT OIDS;

ALTER TABLE ONLY bank_transaction ADD CONSTRAINT "bank_transaction_pkey" PRIMARY KEY (id);