(ns vigil.data
  (:require [vigil.util :as util]
            [vigil.data.util :as dutil]
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
  "Convert SQL-style names to Clojure-style on the way out of the database.
  parameters is a map containing named query parameters, e.g.
  {:name wohanley :alive true}"
  (fn [parameters]
    (util/map-or-apply db-transform (query-fn (db-prepare parameters)))))

(defn single [query-fn]
  (fn [id] (first ((db-wrap query-fn) id))))

;;; defquery requires the SQL files it references to be on the classpath, which
;;; is handled in project.clj with :reference-paths.

(defquery get-player-raw "get_player.sql" {:connection db-spec})
(def get-player (single get-player-raw))

(defquery get-players-by-team-id-raw "get_players_by_team_id.sql"
  {:connection db-spec})
(def get-players-by-team-id (db-wrap get-players-by-team-id-raw))

(defquery get-team-raw "get_team.sql" {:connection db-spec})
(def get-team (single get-team-raw))

(defquery get-teams-by-game-id-raw "get_teams_by_game_id.sql"
  {:connection db-spec})
(def get-teams-by-game-id (db-wrap get-teams-by-game-id-raw))

(defquery get-sallies-by-game-id-raw "get_sallies_by_game_id.sql"
  {:connection db-spec})
(def get-sallies-by-game-id (db-wrap get-sallies-by-game-id-raw))

(defn add-players-to-team [team]
  (assoc team :players (get-players-by-team-id team)))

(defquery get-game-raw "get_game.sql" {:connection db-spec})
(def get-game (single get-game-raw))

(defn get-full-game [params]
  "params must be a map containing the key :id, specifying the game to be got."
  (let [game (get-game params)]
    (assoc game
           :teams (map add-players-to-team (get-teams-by-game-id game))
           :sallies (get-sallies-by-game-id game))))

(defn get-full-game-by-player-id [player-id]
  (get-full-game (:game-id (get-team (:team-id (get-player player-id))))))

(defquery insert-game-raw<! "insert_game.sql" {:connection db-spec})
(def insert-game<! (db-wrap insert-game-raw<!))

(defquery insert-team-raw<! "insert_team.sql" {:connection db-spec})
(def insert-team<! (db-wrap insert-team-raw<!))

(defquery insert-player-raw<! "insert_player.sql" {:connection db-spec})
(def insert-player<! (db-wrap insert-player-raw<!))

(defquery kill-player-raw! "kill_player.sql" {:connection db-spec})
(def kill-player! (db-wrap kill-player-raw!))
