CREATE DOMAIN "Id" AS BIGINT;
CREATE DOMAIN "VarChar255" AS varchar(255);
CREATE DOMAIN "VarChar500" AS varchar(500);
CREATE DOMAIN "DateTime" AS timestamp without time zone;
CREATE DOMAIN "Decimal" AS numeric(16,2);

CREATE DOMAIN "Version" AS timestamp without time zone;

CREATE TABLE operator_profile (
  id "Id" NOT NULL,
  id_operator "Id" NOT NULL,
  version "Version",
  key_name "VarChar255" NOT NULL,
  key_value "VarChar255"
) WITHOUT OIDS;

ALTER TABLE ONLY operator_profile ADD CONSTRAINT "operator_profile_pkey" PRIMARY KEY (id);

CREATE SEQUENCE s_operator_profile
    START WITH 1000
    INCREMENT BY 10
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE TABLE bank_transaction (
  id "Id" NOT NULL,
  operation_date "DateTime" NOT NULL,
  order_date "DateTime" NOT NULL,
  note "VarChar255" NOT NULL,
  quota "Decimal" NOT NULL
) WITHOUT OIDS;

ALTER TABLE ONLY bank_transaction ADD CONSTRAINT "bank_transaction_pkey" PRIMARY KEY (id);

CREATE SEQUENCE s_bank_transaction
    START WITH 1000
    INCREMENT BY 10
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;