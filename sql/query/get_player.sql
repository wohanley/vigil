SELECT id,
       team_id,
       "name",
       alive
FROM player
WHERE id = :id;
