(ns vigil.core-test
  (:require [midje.sweet :refer :all]
            [vigil.core :refer :all :as core]
            [clj-time.core :as time]))


(facts "about new-game"
  (fact "creates game with defaults"
    (core/new-game ..duration..) => {:started nil
                                     :sally-duration ..duration..}))

(facts "about killed-by"
  (fact "returns nil if player is still alive"
    (core/killed-by {:sallies []} ..player..) => nil
    (core/killed-by {:sally-duration 10
                     :sallies [{}
                               {:target-team-id 1
                                :started (time/minus
                                          (time/now)
                                          (time/seconds 5))}]}
                    {:team-id 1}) => nil)
  (fact "returns the oldest successful sally against player, if there is one"
    (let [successful-sally {:target-team-id 1
                            :started (time/minus
                                      (time/now)
                                      (time/seconds 15))}]
      (core/killed-by {:sally-duration 10
                       :sallies [successful-sally
                                 { ;; this sally would also be successful, but
                                   ;; it's younger than successful-sally.
                                  :target-team-id 1
                                  :started (time/minus
                                            (time/now)
                                            (time/seconds 12))}]}
                      {:team-id 1}) => successful-sally)))
