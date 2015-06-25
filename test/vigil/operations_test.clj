(ns vigil.operations-test
  (:require [midje.sweet :refer :all]
            [vigil.operations :as ops]))


(facts "about game-view"
  (fact "marks players alive if there are no sallies due to kill them"
    (ops/game-view {:sally-duration 10
                     :teams [{:players [{}]}]
                     :sallies []}) => {:sally-duration 10
                                       :teams [{:players [{:alive true}]}]
                                       :sallies []}))
