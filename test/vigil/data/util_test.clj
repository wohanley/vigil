(ns vigil.data.util-test
  (require [midje.sweet :refer :all]
           [vigil.data.util :as dutil]))

(facts "about filter-keys"
  (fact "doesn't filter if keys not supplied"
    (dutil/filter-keys :item nil) => :item)
  (fact "select-keys keys from item"
    (dutil/filter-keys ..item.. ..keys..) => :filtered
    (provided
      (select-keys ..item.. ..keys..) => :filtered)))

(facts "about replace-rename-keys"
  (fact "replaces all instances of search term in key names"
    (dutil/replace-rename-keys "s" "r" {:test 1
                                        :other-key 2}) => {:tert 1
                                                           :other-key 2}))
