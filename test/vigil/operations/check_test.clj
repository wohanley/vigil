(ns vigil.operations.check-test
  (:require [vigil.operations.check :as check]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]
            [midje.sweet :refer :all]))


(facts "about due-to-kill"

  (fact "returns false if sally is not overdue"
    (check/due-to-kill 0 nil) => false
    (check/due-to-kill 10 {:started (tcoerce/to-sql-time
                                     (time/minus
                                      (time/now)
                                      (time/seconds 5)))}) => false)
  
  (fact "returns true if sally is overdue"
    (let [overdue-sally {:started (tcoerce/to-sql-time (time/minus
                                                        (time/now)
                                                        (time/seconds 15)))}]
      (check/due-to-kill 10 overdue-sally) => true)))
