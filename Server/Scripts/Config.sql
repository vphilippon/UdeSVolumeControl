-- Table: "Config"

-- DROP TABLE "Config";

CREATE TABLE "Config"
(
  "configId" serial NOT NULL,
  "configName" character varying(50) NOT NULL,
  zone circle NOT NULL,
  "volumeRingtone" integer NOT NULL,
  "volumeNotification" integer NOT NULL,
  "userId" character varying(50) NOT NULL,
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
