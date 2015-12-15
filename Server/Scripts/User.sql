-- Table: "User"

DROP TABLE IF EXISTS "User" CASCADE;

CREATE TABLE "User"
(
  "userId" character varying(50) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY ("userId")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "User"
  OWNER TO ift604;
