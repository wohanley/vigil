(ns vigil.util-test
  (:require [vigil.web.start :as start]
            [midje.sweet :refer :all]))


(facts "about parse-id"
  (fact "pulls ID from string"
    (start/parse-id "52") => {:id 52}))
