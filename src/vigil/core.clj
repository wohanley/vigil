(ns vigil.core
  (:require [vigil.util :refer :all]
            [vigil.data :as data]
            [vigil.sally :as sally]
            [dire.core :as dire]))


;; I don't like this at all. I need to take a solid look at error handling in
;; Clojure, because I haven't seen anything that waters my flowers yet.
(defrecord error [message])

(defrecord sally [player-id started])

(defrecord player [team-id name])

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
             player-name)))

(defn killed-by [game player]
  "Return the player that killed player in game, or nil if player is still
  alive."
  (first
   (sort-by :started
            (filter (and (partial sally/against-team? (:team-id player))
                         (partial sally/overdue? (:sally-duration game)))
                    (:sallies game)))))


(defn sally-forth [game target-team attacking-player]
  "Launch an attack against an opposing team."
  (data/sally-forth! {:attacking-player-id (:id attacking-player)
                      :target-team-id (:id target-team)}))

(dire/with-precondition! #'sally-forth
  :live-attacker
  (fn [_ _ attacking-player]
    (not (nil? (killed-by attacking-player)))))

(dire/with-handler! #'sally-forth
  {:precondition :live-attacker}
  (fn [e & args] (->error "attacking-player must be alive.")))

(dire/with-precondition! #'sally-forth
  :in-game
  (fn [game team player]
    (and (some (comp :id (partial = (:id team))) (:teams game))
         (some (comp :id (partial = (:id player))) (flatten (:teams game))))))

(dire/with-handler! #'sally-forth
  {:precondition :in-game}
  (fn [e & args]
    (->error "attacking-player and target-team must both be in game.")))


(defn game-view [game]
  "Transform game to a structure easily parsed by templates. Right now this
  means use the sally information in game to mark each player alive or dead."
  (assoc game
         :teams (map
                 (fn [team]
                   (assoc
                    team
                    :players (map
                              (fn [player]
                                (assoc player
                                       :alive (nil?  (killed-by game player))))
                              (:players team))))
                 (:teams game))))
