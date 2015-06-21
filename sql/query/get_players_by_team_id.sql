SELECT id,
       team_id,
       "name",
       alive
FROM player
WHERE team_id = :team_id;
