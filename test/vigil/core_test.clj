(ns vigil.core-test
  (:require [midje.sweet :refer :all]
            [vigil.core :refer :all :as core]
            [clj-time.core :as time]))


(facts "about new-game"
  (fact "creates game with defaults"
    (core/new-game ..duration..) => {:started nil
                                     :sally-duration ..duration..}))

(facts "about killed-by-sally"

  (fact "returns nil if no sally killed player"
    (core/killed-by-sally {:sallies []} ..player..) => nil
    (core/killed-by-sally {:teams [{:id 1
                                    :players [{:id 1 :team-id 1}]}
                                   {:id 2
                                    :players [{:id 2 :team-id 2}]}]
                           :sally-duration 10
                           :sallies [{;; a successful sally against team 2
                                      :target-team-id 2
                                      :started (time/minus
                                                (time/now)
                                                (time/seconds 15))}
                                     {;; a non-overdue sally against team 1
                                      :target-team-id 1
                                      :started (time/minus
                                                (time/now)
                                                (time/seconds 5))}]}
                          {:id 1 :team-id 1}) => nil)

  (fact "returns the oldest successful sally against player, if there is one"
    (let [successful-sally {:target-team-id 1
                            :started (time/minus
                                      (time/now)
                                      (time/seconds 15))}]
      (core/killed-by-sally
       {:sally-duration 10
        :sallies [successful-sally
                  { ;; this sally would also be successful, but
                   ;; it's younger than successful-sally.
                   :target-team-id 1
                   :started (time/minus (time/now) (time/seconds 12))}]}
       {:team-id 1}) => successful-sally))
  
  (fact "returns a sally by player that was intercepted"
    (let [intercepted-sally {:attacking-player-id 1
                             :intercepted-by-player-id 2}]
      (core/killed-by-sally
       {:sally-duration 10
        :teams [{:id 1 :players [{:id 1}]}]
        :sallies [intercepted-sally]}
       {:id 1}) => intercepted-sally)))


(facts "about sally-forth"
  (fact "checks that attacking-player is alive"
    (core/sally-forth {:sally-duration 10
                       :teams [{:id 1
                                :players [{:id 1 :team-id 1}]}
                               {:id 2
                                :players [{:id 2 :team-id 2}]}]
                       :sallies [{;; this sally has killed team 1
                                  :target-team-id 1
                                  :started (time/minus
                                            (time/now)
                                            (time/seconds 15))}]}
                      {:id 2}
                      {:id 1 :team-id 1}) => (partial instance?
                                                      vigil.core.error))
  
  (fact "checks that attacking-player is in game"
    (core/sally-forth {:sally-duration 10
                       :teams [{:id 1
                                :players []} ;; player is missing here
                               {:id 2
                                :players [{:team-id 2}]}]
                       :sallies []}
                      {:id 2}
                      {:id 1 :team-id 1}) => (partial instance?
                                                      vigil.core.error))

  (fact "checks that target-team is in game"
    (core/sally-forth {:sally-duration 10
                       :teams [;; no team with ID 2
                               {:id 1
                                :players [{:id 1
                                           :team-id 1}]}]
                       :sallies []}
                      {:id 2}
                      {:id 1 :team-id 1}) => (partial instance?
                                                      vigil.core.error)))
