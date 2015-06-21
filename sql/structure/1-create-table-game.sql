-- Table: game

DROP TABLE IF EXISTS game;

CREATE TABLE game
(
  id serial NOT NULL,
  started timestamp without time zone NULL,
  CONSTRAINT game_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE game
  OWNER TO postgres;
