DROP TABLE IF EXISTS sally;

CREATE TABLE sally
(
  id serial NOT NULL,
  player_id int NOT NULL references player(id),
  started timestamp without time zone NOT NULL,
  CONSTRAINT sally_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sally
  OWNER TO postgres;
