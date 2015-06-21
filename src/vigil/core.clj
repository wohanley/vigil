(ns vigil.core
  (:require [vigil.util :refer :all]
            [vigil.data :as data]))

(defrecord sally [id player-id started])

(defrecord player [id team-id name alive])

(defn new-player [team-id name]
  (->player nil team-id name true))

(defrecord team [id game-id name])

(defrecord game [id sally-duration started])

(defn new-game [sally-duration] (->game nil sally-duration nil))

(defn set-up-game [player-name team-name sally-duration]
  "Create a game, add a team, and add a player to the team. This function makes
  the database calls you need for a minimal working game. What is ultimately
  returned is the last thing inserted into the database - the new player."
  (data/insert-player
   (->player nil
             (:id (data/insert-team
                   (->team nil
                           (:id (data/insert-game
                                 (new-game sally-duration)))
                           team-name)))
             player-name
             true)))

(defn kill [player]
  (assoc player :alive false))

(defn check [player game]
  "Look to see if anyone is attacking player's team, and kill them if they are.
  Returns a vector of killed players."
  (map kill (attacking-players player game)))
