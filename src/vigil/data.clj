(ns vigil.data
  (:require [vigil.data.util :as dutil]
            [environ.core :refer [env]]
            [vigil.heroku :as heroku]
            [yesql.core :refer [defquery]]))


(def db-spec
  (heroku/convert-database-url (env :database-url)))

(defn sql-now [] (java.sql.Timestamp. (.getTime (java.util.Date.))))

(defn db-prepare [item]
  (dutil/replace-rename-keys "-" "_" item))

(defn db-transform [item]
  (dutil/replace-rename-keys "_" "-" item))

(defn db-wrap [query-fn]
  "Convert SQL-style names to Clojure-style on the way out of the database."
  (fn [& parameters] (map db-transform (apply (partial query-fn db-spec)
                                              parameters))))

(defn single [query-fn]
  (fn [id] (first ((db-wrap query-fn) id))))

;;; defquery requires the SQL files it references to be on the classpath, which
;;; is handled in project.clj with :reference-paths.

(defquery get-player-raw "get_player.sql")
(def get-player (single get-player-raw))

(defquery get-players-by-team-id-raw "get_players_by_team_id.sql")
(def get-players-by-team-id (db-wrap get-players-by-team-id-raw))

(defquery get-team-raw "get_team.sql")
(def get-team (single get-team-raw))

(defquery get-teams-by-game-id-raw "get_teams_by_game_id.sql")
(def get-teams-by-game-id (db-wrap get-teams-by-game-id-raw))

(defn add-players-to-team [team]
  (assoc team :players (get-players-by-team-id (:id team))))

(defquery get-game-raw "get_game.sql")
(def get-game (single get-game-raw))

(defn get-full-game [game-id]
  (let [game (get-game game-id)]
    (assoc game
      :teams
      (map add-players-to-team (get-teams-by-game-id game-id)))))

(defn get-full-game-by-player-id [player-id]
  (get-full-game (:game-id (get-team (:team-id (get-player player-id))))))

(defquery insert-game-raw<! "insert_game.sql")
(def insert-game (partial insert-game-raw<! db-spec))

(defquery insert-team-raw<! "insert_team.sql")
(def insert-team (partial insert-team-raw<! db-spec))

(defquery insert-player-raw<! "insert_player.sql")
(def insert-player (partial insert-player-raw<! db-spec))

(defquery kill-player-raw! "kill_player.sql")
(def kill-player (partial kill-player-raw! db-spec))
