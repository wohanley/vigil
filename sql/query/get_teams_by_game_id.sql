SELECT id,
       game_id,
       "name"
FROM team
WHERE game_id = :id;
