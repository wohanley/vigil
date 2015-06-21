DROP TABLE IF EXISTS team;

CREATE TABLE team
(
  id serial NOT NULL,
  game_id int NOT NULL references game(id),
  "name" varchar(50) NOT NULL,
  CONSTRAINT team_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE team
  OWNER TO postgres;
