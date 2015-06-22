(ns vigil.util)

(defn opposing-teams [player game]
  (filter #(not= (:id %) (:id (:team player))) (:teams game)))

(defn opposing-players [player game]
  (flatten (opposing-teams player game)))

(defn attacking [player]
  "Get the team that the player is currently attacking, if any."
  (:against (first (filter #(:active %) (:sallies player)))))

(defn attacking-players [player game]
  (filter (fn [opponent] (= (attacking opponent) (:team player))) (opposing-players player game)))

(defn map-or-apply [f x]
  "If x is sequential, map f over it. If not, simply pass x to f."
  (if (sequential? x)
    (map f x)
    (f x)))
