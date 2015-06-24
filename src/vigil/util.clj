(ns vigil.util)


(defn map-or-apply [f x]
  "If x is sequential, map f over it. If not, simply pass x to f."
  (if (sequential? x)
    (map f x)
    (f x)))
