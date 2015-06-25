SELECT id,
       team_id,
       "name"
FROM player
WHERE team_id = :id;
