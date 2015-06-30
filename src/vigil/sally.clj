(ns vigil.sally
  (:require [clj-time.core :as time]))

(defn overdue? [sally-duration sally]
  "Predicate checking that sally is due to kill its target."
  (if (nil? (:started sally))
    false
    (> (time/in-seconds (time/interval (:started sally) (time/now)))
       sally-duration)))

(defn against-team? [team sally]
  "Predicate for sallies against team."
  (= (:id team) (:target-team-id sally)))

(defn by-player? [player sally]
  "Predicate for sallies launched by player."
  (= (:id player) (:attacking-player-id sally)))

(defn intercepted? [sally]
  ((comp nil?) (:intercepted-by sally)))
