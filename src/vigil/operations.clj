(ns vigil.operations
  (:require [vigil.core :as core]
            [vigil.data :as data]))

(defn get-full-game [game-id]
  (data/get-full-game game-id))

(defn- kill-attackers [player-id]
  (map (comp :id data/kill-player!)
       (core/check (data/get-player player-id)
                   (data/get-full-game-by-player-id player-id))))

(defn check [player-id]
  "1. Check for attackers and kill them if we catch any.
   2. Gather all the data a player needs for a view of their game, including the
  deaths of attackers in 1."
  (do
    (kill-attackers player-id)
    {:game (data/get-full-game-by-player-id player-id)
     :current-player (data/get-player player-id)}))

(defn new-game [player-name team-name sally-duration]
  "Set up a game for the player."
   (core/set-up-game player-name team-name sally-duration))

(defn create-team [game-id name]
  (data/insert-team<! {:game-id game-id :name name}))

(defn remove-team [team-id]
  )

(defn create-player [team-id name]
  (data/insert-player<! (core/new-player team-id name)))

(defn remove-player [player-id]
  )

(defn attack [attacking-player-id target-team-id]
  )
