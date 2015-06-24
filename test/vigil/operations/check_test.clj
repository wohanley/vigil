(ns vigil.operations.check-test
  (:require [vigil.operations.check :as check]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]
            [midje.sweet :refer :all]))


(facts "about due-to-kill"

  (fact "returns an empty vector if no sallies are overdue"
    (check/due-to-kill 0 []) => []
    (check/due-to-kill 10 [{:started (tcoerce/to-sql-time
                                      (time/minus
                                       (time/now)
                                       (time/seconds 5)))}]) => [])
  
  (fact "collects sallies that are overdue"
    (let [overdue-sally {:started (tcoerce/to-sql-time (time/minus
                                                        (time/now)
                                                        (time/seconds 15)))}]
      (check/due-to-kill 10 [overdue-sally]) => [overdue-sally])))
