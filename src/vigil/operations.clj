(ns vigil.operations
  (:require [vigil.core :as core]
            [vigil.data :as data]
            [vigil.util :as util]
            [vigil.sally :as sally]))


(defn player-view [game player]
  "Use the sally information in game to mark player alive or dead."
  (assoc player :alive (nil? (core/killed-by-sally game player))))

(defn game-view [game]
  "Transform game to a structure easily parsed by templates. Right now this
  means use the sally information in game to mark each player alive or dead."
  (assoc game
         :teams (map #(assoc % :players (map (partial player-view game)
                                             (:players %)))
                     (:teams game))))

(defn get-game-view-for-player [player]
  (let [game (data/get-full-game-by-player-id player)]
    {:game (game-view game)
     :current-player (player-view game (data/get-player player))}))


(defn get-full-game [game]
  (game-view (data/get-full-game game)))

(defn check [stale-player]
  "1. Mark any non-overdue sallies against stale-player intercepted.
   2. Gather all the data a player needs for a view of their game."
  (let [player (data/get-player stale-player)
        game (data/get-full-game-by-player-id player)]
    (if (core/alive? game player)
      (dorun (map #(core/intercept
                    game
                    (data/get-player {:id (:attacking-player-id %)})
                    player)
                  (filter
                   #(and
                     (not (sally/overdue? (:sally-duration game) %))
                     (sally/against-team? {:id (:team-id player)} %))
                   (:sallies game)))))))

(defn new-game [name sally-duration]
  "Set up a game for the player."
  ;; For now, there's no frontend for teams, so just name the team after the
  ;; player.
  (game-view (core/set-up-game name name sally-duration)))

(defn create-team [game-id name]
  (data/insert-team<! {:game-id game-id :name name}))

(defn create-player [team-id name]
  (data/insert-player<! (core/new-player team-id name)))

(defn sally-forth [target-team attacking-player]
  (core/sally-forth (data/get-full-game-by-player-id attacking-player)
                    target-team
                    attacking-player))
