(ns vigil.operations.check-test
  (:require [vigil.operations.check :as check]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]
            [midje.sweet :refer :all]))


(facts "about due-to-kill"

  (fact "returns an empty vector if player isn't due to be killed"
    (check/due-to-kill {:team-id 1} {:sallies []}) => []
    (check/due-to-kill {:team-id 1} {:sallies [{:target-team-id 2}]}) => []
    (check/due-to-kill {:team-id 1}
                       {:sally-duration 10
                        :sallies [{:target-team-id 1
                                   :started (time/minus
                                             (time/now)
                                             (time/seconds 5))}]}) => [])
  
  (fact "collects sallies due to kill player"
    (let [overdue-sally {:target-team-id 1
                         :started (tcoerce/to-sql-time (time/minus
                                                        (time/now)
                                                        (time/seconds 5)))}]
      (check/due-to-kill {:team-id 1}
                         {:sally-duration 10
                          :sallies [overdue-sally]}) => [overdue-sally])))
