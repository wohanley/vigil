(ns vigil.operations.check-test
  (:require [vigil.operations.check :as check]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]
            [midje.sweet :refer :all]))


(facts "about overdue?"

  (fact "returns false if sally is not overdue"
    (check/overdue? 0 nil) => false
    (check/overdue? 10 {:started (time/minus
                                  (time/now)
                                  (time/seconds 5))}) => false)
  
  (fact "returns true if sally is overdue"
    (let [overdue-sally {:started (time/minus
                                   (time/now)
                                   (time/seconds 15))}]
      (check/overdue? 10 overdue-sally) => true)))
