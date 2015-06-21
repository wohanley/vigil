(ns vigil.core-test
  (:use midje.sweet)
  (:require [vigil.core :refer :all :as core]
            [vigil.util :as util]))

(facts "about kill"
  (fact "sets alive to false"
    (core/kill {:alive nil}) => {:alive false})
  (fact "kills anything"
    (core/kill nil) => {:alive false}))

(facts "about check"
  (fact "kills attacking players"
    (core/check ..player.. ..game..) => [:dead-1
                                         :dead-2]
    (provided
      (util/attacking-players ..player.. ..game..) => [:alive-1
                                                       :alive-2]
      (core/kill :alive-1) => :dead-1
      (core/kill :alive-2) => :dead-2)))

(facts "about new-game"
  (fact "creates game with defaults"
    (core/new-game ..duration..) => {:id nil
                                     :started nil
                                     :sally-duration ..duration..}))
