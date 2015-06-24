(ns vigil.sally-test
  (:require [vigil.sally :as sally]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]
            [midje.sweet :refer :all]))


(facts "about overdue?"

  (fact "returns false if sally is not overdue"
    (sally/overdue? 0 nil) => false
    (sally/overdue? 10 {:started (time/minus
                                  (time/now)
                                  (time/seconds 5))}) => false)
  
  (fact "returns true if sally is overdue"
    (let [overdue-sally {:started (time/minus
                                   (time/now)
                                   (time/seconds 15))}]
      (sally/overdue? 10 overdue-sally) => true)))
