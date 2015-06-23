(ns vigil.operations.check
  (:require [vigil.core :as core]
            [vigil.util :as util]))


(defn sally-to-kill-player [player game]
  "If there is a sally due to kill player, return it, else return nil."
  (util/attacking-players player game))

(defn kill-attackers [player game]
  "Look to see if anyone is attacking player's team, and kill them if they are.
  Returns a vector of killed players."
  (map core/kill (util/attacking-players player game)))
