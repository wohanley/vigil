(ns vigil.util-test
  (:use midje.sweet)
  (:require [vigil.util :as util]
            [vigil.core :as core]))


(facts "about map-or-apply"
  (fact "applies function to non-seq argument"
    (util/map-or-apply #(.toUpperCase %) "hi") => "HI")
  (fact "maps function over seq"
    (util/map-or-apply #(.toUpperCase %) ["hi" "there"]) => ["HI" "THERE"]))
