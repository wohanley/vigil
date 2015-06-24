(ns vigil.operations
  (:require [vigil.core :as core]
            [vigil.data :as data]
            [vigil.util :as util]
            [vigil.sally :as sally]))


(defn get-full-game [game]
  (data/get-full-game game))

(defn check [stale-player]
  "1. Mark any non-overdue sallies against stale-player intercepted.
   2. Gather all the data a player needs for a view of their game."
  (let [player (data/get-player stale-player)
        game (data/get-game-by-player-id {:player-id (:id player)})
        sallies (data/get-sallies-by-game-id game)]
    (do
      (map (comp #(assoc % :intercepter-id (:id player)) data/intercept-sally!)
           (filter
            (comp
             (partial (comp not sally/overdue?) (:sally-duration game))
             (partial sally/against-team? (:team-id player)))
            sallies))
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
