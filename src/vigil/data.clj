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

(defn query-file-path [file-name]
  "Get the path for a SQL file to be used with yesql."
  (format "query/%s" file-name))

(defquery get-player "get_player.sql")

;; (def get-players-by-team-id (partial get-by :player :team_id))

;; (def get-team (partial get-by-id :team))

;; (def get-teams-by-game-id (partial get-by :team :game_id))

;; (defn add-players-to-team [team]
;;   (assoc team :players (get-players-by-team-id (:id team))))

;; (def get-game (partial get-by-id :game))

;; (defn get-full-game [game-id]
;;   (let [game (get-game game-id)]
;;     (assoc game
;;       :teams
;;       (map add-players-to-team (get-teams-by-game-id game-id)))))

;; (def insert-game (partial db-insert :game [:started]))

;; (defn get-full-game-by-player-id [player-id]
;;   (get-full-game (:game-id (get-team (:team-id (get-player player-id))))))

;; (def insert-team (partial db-insert :team [:game-id :name]))

;; (def insert-player (partial db-insert :player [:team-id :name :alive]))

;; (defn kill [player-id]
;;   (update :player
;;           (set-fields {:alive false})
;;           (where {:id [= player-id]})))
