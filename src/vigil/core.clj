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

(defn killed-by-sally [game player]
  "Return the sally that killed player in game, or nil if player is still
  alive."
  (first
   ;; It's probably not /quite/ right to simply sort this by :started, as it
   ;; might make more sense to rank a after b where a is a sally against player
   ;; that succeeded and b is a sally by player that started after a but was
   ;; intercepted before a succeeded. That makes sense, right? uh, TODO
   (sort-by :started
            (concat
             (filter #(or ;; there are two ways a player can die:
                       (and
                        ;; a sally by another player that got them
                        (sally/against-team? {:id (:team-id player)} %)
                        (sally/overdue? (:sally-duration game) %))
                       (and
                        ;; their own sally being intercepted
                        (sally/by-player? player %)
                        (not (nil? (:intercepted-by-player-id %)))))
                     (:sallies game))))))


(defn sally-forth [game target-team attacking-player]
  "Launch an attack against an opposing team. Return the inserted sally."
  (data/sally-forth<! {:attacking-player-id (:id attacking-player)
                       :target-team-id (:id target-team)}))

(dire/with-precondition! #'sally-forth
  :live-attacker
  (fn [game _ attacking-player]
    (nil? (killed-by-sally game attacking-player))))

(dire/with-handler! #'sally-forth
  {:precondition :live-attacker}
  (fn [e & args] (->error "attacking-player must be alive.")))

(dire/with-precondition! #'sally-forth
  :in-game
  (fn [game team player]
    (and (some #(= (:id %) (:id team)) (:teams game))
         (some #(= (:id %) (:id player)) (flatten (map :players
                                                       (:teams game)))))))

(dire/with-handler! #'sally-forth
  {:precondition :in-game}
  (fn [e & args]
    (->error "attacking-player and target-team must both be in game.")))


(defn add-player-to-game [game name]
  "Create a team with a single player named name in the game identified by
  game-id. The team gets the same name as player. Return the inserted player."
  (data/insert-player<! (->player (:id (data/insert-team<! (->team (:id game)
                                                                   name)))
                                  name)))
