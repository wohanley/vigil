DROP TABLE IF EXISTS sally;

CREATE TABLE sally
(
  id serial NOT NULL,
  attacking_player_id int NOT NULL references player(id),
  target_team_id int NOT NULL references team(id),
  started timestamp with time zone NOT NULL,
  intercepted_by_player_id int NULL references player(id),
  CONSTRAINT sally_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sally
  OWNER TO postgres;
