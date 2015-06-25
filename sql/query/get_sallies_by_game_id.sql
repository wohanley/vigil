SELECT sally.id,
       attacking_player_id,
       target_team_id,
       started,
       intercepted_by_player_id
FROM team, sally
WHERE team.game_id = :id AND sally.target_team_id = team.id;
