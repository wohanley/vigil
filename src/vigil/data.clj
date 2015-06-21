(ns vigil.data
  (:require [vigil.data.util :as dutil]
            [environ.core :refer [env]]
            [vigil.heroku :as heroku]
            [korma.core :refer :all]
            [korma.db :refer :all]))

; Initialize the database. There is a special case for tests because I don't
; know how to do that kind of config thing right yet and I'm bored with trying
; to figure it out.
;(if (not (= (env :database-url) "test"))
;  (defdb korma-db (postgres (heroku/convert-database-url (env :database-url)))))

(defdb korma-db (postgres (heroku/convert-database-url "postgres://vigil_app:password@localhost:5432/vigil")))

(defn sql-now [] (java.sql.Timestamp. (.getTime (java.util.Date.))))

(defn db-prepare [item]
  (dutil/replace-rename-keys "-" "_" item))

(defn db-transform [item]
  (dutil/replace-rename-keys "_" "-" item))

(defn db-insert [table insert-keys item]
  (insert table (values (db-prepare (dutil/filter-keys item insert-keys)))))

(defn get-by [table key-name key-value]
  (map db-transform (select table (where {key-name [= key-value]}))))

(defn get-by-id [table key-value]
  "Returns the first row of a get-by on id, which by convention ought to be the
  primary key and therefore should be unique."
  (first (get-by table :id key-value)))

(def get-player (partial get-by-id :player))

(def get-players-by-team-id (partial get-by :player :team_id))

(def get-team (partial get-by-id :team))

(def get-teams-by-game-id (partial get-by :team :game_id))

(defn add-players-to-team [team]
  (assoc team :players (get-players-by-team-id (:id team))))

(def get-game (partial get-by-id :game))

(defn get-full-game [game-id]
  (let [game (get-game game-id)]
    (assoc game
      :teams
      (map add-players-to-team (get-teams-by-game-id game-id)))))

(def insert-game (partial db-insert :game [:started]))

(defn get-full-game-by-player-id [player-id]
  (get-full-game (:game-id (get-team (:team-id (get-player player-id))))))

(def insert-team (partial db-insert :team [:game-id :name]))

(def insert-player (partial db-insert :player [:team-id :name :alive]))

(defn kill [player-id]
  (update :player
          (set-fields {:alive false})
          (where {:id [= player-id]})))
