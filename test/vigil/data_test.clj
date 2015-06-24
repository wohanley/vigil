(ns vigil.data-test
  (:require [vigil.data :as data]
            [midje.sweet :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :as tcoerce]))


(facts "about db-prepare"
  (fact "converts - to _"
    (data/db-prepare {:test-key 1}) => {:test_key 1})
  (fact "converts joda times to SQL timestamps"
    (data/db-prepare {:time (time/epoch)}) => {:time (tcoerce/to-sql-time
                                                      (time/epoch))}))

(facts "about db-transform"
  (fact "converts _ to -"
    (data/db-transform {:test_key 1}) => {:test-key 1})
  (fact "converts SQL timestamps to joda times"
    (data/db-transform {:time (tcoerce/to-sql-time
                               (time/epoch))}) => {:time (time/epoch)}))
