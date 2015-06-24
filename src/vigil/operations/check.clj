(ns vigil.operations.check
  (:require [vigil.core :as core]
            [vigil.util :as util]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]))


(defn due-to-kill [team game]
  "Return a vector of all sallies due to kill team. team must provide a key :id
  that gives the ID of the team being attacked."
  (filter
   #(> (time/in-seconds
        (time/interval
         (tcoerce/from-sql-time (:started %))
         (time/now)))
       (:sally-duration game))
   (util/attackers team game)))

(defn kill-attackers [player game]
  "Look to see if anyone is attacking player's team, and kill them if they are.
  Returns a vector of killed players."
  (map core/kill (util/attackers player game)))

(defn kill-attacked [player game]
  "Kill anyone player has overdue sallies against.")
