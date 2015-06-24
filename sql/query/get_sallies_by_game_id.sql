SELECT id,
       attacking_player_id,
       target_team_id,
       started,
       intercepted_by_player_id
FROM game, team, sally
WHERE team.game_id = game.id AND sally.target_team_id = team.id;
