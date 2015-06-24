(ns vigil.operations.check
  (:require [vigil.core :as core]
            [vigil.util :as util]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]))


(defn due-to-kill [sally-duration sally]
  "Predicate checking that sally is due to kill its target."
  (if (nil? (:started sally))
    false
    (> (time/in-seconds
        (time/interval
         ;; TODO: I should handle this coercion at the data layer.
         (tcoerce/from-sql-time (:started sally))
         (time/now)))
       sally-duration)))

(defn against-team [team sally]
  "Predicate for sallies against team."
  (= (:id team) (:target-team-id sally)))

(defn by-player [player sally]
  "Predicate for sallies launched by player."
  (= (:id player) (:attacking-player-id sally)))

(defn kill-attackers [player game]
  "Look to see if anyone is attacking player's team, and kill them if they are.
  Returns a vector of killed players."
  )

(defn kill-attacked [player game]
  "Kill anyone player has overdue sallies against.")
