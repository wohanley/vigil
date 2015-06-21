DROP TABLE IF EXISTS player;

CREATE TABLE player
(
  id serial NOT NULL,
  team_id int NOT NULL references team(id),
  "name" varchar(50) NOT NULL,
  alive bool NOT NULL,
  CONSTRAINT player_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE player
  OWNER TO postgres;
