-- Table: "Config"

-- DROP TABLE "Config";

CREATE TABLE "Config"
(
  "configId" serial NOT NULL,
  zone circle NOT NULL,
  "volumeMain" integer NOT NULL,
  "userId" character varying(50),
  CONSTRAINT pk_config PRIMARY KEY ("configId"),
  CONSTRAINT fk_user_config FOREIGN KEY ("userId")
      REFERENCES "User" ("userId") MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "Config"
  OWNER TO ift604;
