(ns vigil.operations
  (:require [vigil.core :as core]
            [vigil.data :as data]
            [vigil.util :as util]
            [vigil.operations.check :as check]))

(defn get-full-game [game]
  (data/get-full-game game))

(defn check [player]
  "1. Kill player if there are any sallies due to kill them.
   2. If player is alive, check for attackers and kill them if we catch any.
   3. If player is alive, kill anyone they have overdue sallies against.
   4. Gather all the data a player needs for a view of their game."
  (let [game (data/get-full-game-by-player-id player)
        killer (check/due-to-kill player game)]
    (do
      (if (not (nil? killer))
        (data/kill-player! {:id (:id player) :killer-id (:id killer)})
        (do
          (map data/kill-player! (util/attackers player game))
          (map data/kill-player! (check/kill-attacked player game))))
      ;; We need to grab the game again after possibly changing it above.
      {:game (data/get-full-game-by-player-id player)
       :current-player (data/get-player player)})))

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
