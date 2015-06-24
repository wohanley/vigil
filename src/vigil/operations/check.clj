(ns vigil.operations.check
  (:require [vigil.core :as core]
            [vigil.util :as util]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]))


(defn due-to-kill [sally-duration sallies]
  "Return a vector of all sallies that are due to kill their target."
  (filter
   #(> (time/in-seconds
        (time/interval
         ;; TODO: I should handle this coercion at the data layer.
         (tcoerce/from-sql-time (:started %))
         (time/now)))
       sally-duration)
   sallies))

(defn kill-attackers [player game]
  "Look to see if anyone is attacking player's team, and kill them if they are.
  Returns a vector of killed players."
  )

(defn kill-attacked [player game]
  "Kill anyone player has overdue sallies against.")
