(ns vigil.core
  (:require [vigil.util :refer :all]
            [vigil.data :as data]
            [vigil.sally :as sally]))

(defrecord sally [player-id started])

(defrecord player [team-id name alive])

(defn new-player [team-id name]
  (->player team-id name true))

(defrecord team [game-id name])

(defrecord game [sally-duration started])

(defn new-game [sally-duration] (->game sally-duration nil))

(defn with-id [id item]
  "The presence of an ID differentiates records that only exist in the
  application from those that have been persisted."
  (assoc item :id id))

(defn set-up-game [player-name team-name sally-duration]
  "Create a game, add a team, and add a player to the team. This function makes
  the database calls you need for a minimal working game. What is ultimately
  returned is the last thing inserted into the database - the new player."
  (data/insert-player<!
   (->player (:id (data/insert-team<!
                   (->team (:id (data/insert-game<!
                                 (new-game sally-duration)))
                           team-name)))
             player-name
             true)))

(defn killed-by [game player]
  "Return the player that killed player in game, or nil if player is still
  alive."
  (first
   (sort-by :started
            (filter (and (partial sally/against-team? (:team-id player))
                         (partial sally/overdue? (:sally-duration game)))
                    (:sallies game)))))
