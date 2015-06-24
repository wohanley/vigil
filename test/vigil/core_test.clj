(ns vigil.core-test
  (:require [midje.sweet :refer :all]
            [vigil.core :refer :all :as core]
            [vigil.util :as util]))

(facts "about kill"
  (fact "sets alive to false"
    (core/kill {:alive nil}) => {:alive false})
  (fact "kills anything"
    (core/kill nil) => {:alive false}))

(facts "about new-game"
  (fact "creates game with defaults"
    (core/new-game ..duration..) => {:started nil
                                     :sally-duration ..duration..}))
