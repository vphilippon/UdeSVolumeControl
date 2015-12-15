-- Table: "VolumeConfig"

-- DROP TABLE "VolumeConfig";

CREATE TABLE "VolumeConfig"
(
  "id" serial NOT NULL,
  "name" character varying(50) NOT NULL,
  zone circle NOT NULL,
  "profile" integer NOT NULL,
  "userId" character varying(50) NOT NULL,
  CONSTRAINT pk_config PRIMARY KEY ("id"),
  CONSTRAINT fk_user_volumeconfig FOREIGN KEY ("userId")
      REFERENCES "User" ("userId") MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "VolumeConfig"
  OWNER TO ift604;
