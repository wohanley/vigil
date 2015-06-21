SELECT id,
       started,
       sally_duration
FROM game
WHERE id = :game_id;
