(ns vigil.util-test
  (:use midje.sweet)
  (:require [vigil.util :as util]
            [vigil.core :as core]))

(facts "about opposing-teams"

  (let [player {:team-id 1 :alive true}]

    (fact "returns a vector of all teams in player's game that player is not a member of"
      (util/opposing-teams {:team ..team-1..}
                           {:teams [..team-1..
                                    ..team-2..
                                    ..team-3..]}) => [..team-2..
                                                      ..team-3..]
                                    (provided
                                      ..team-1.. =contains=> {:id 1}
                                      ..team-2.. =contains=> {:id 2}
                                      ..team-3.. =contains=> {:id 3}))
    
    (fact "returns an empty vector rather than failing"
      (util/opposing-teams nil nil) => []
      (util/opposing-teams player nil) => []
      (util/opposing-teams player {}) => []
      (util/opposing-teams player {:teams nil}) => [])))


(facts "about opposing-players"
  (fact "flattens opposing-teams"
    (util/opposing-players ..player.. ..game..) => :players
    (provided
      (util/opposing-teams ..player.. ..game..) => :teams
      (flatten :teams) => :players)))


(facts "about attacking"
  (let [attacked {:id 1}]

    (fact "returns team currently being attacked"
      (util/attacking ..player..) => attacked
      (provided
        ..player.. =contains=> {:sallies [{:active true :against attacked}
                                          {:active false}]})))
  (fact "ignores inactive sallies"
    (util/attacking ..player..) => nil
    (provided
      ..player.. =contains=> {:sallies [{:active false}
                                        {:active false}]}))

  (fact "handles missing sallies"
    (util/attacking ..player..) => nil
    (provided
      ..player.. =contains=> {:sallies nil}))
  
  (fact "handles empty sallies"
    (util/attacking ..player..) => nil))


(facts "about attacking-players"
  (fact "collects attackers of player's team"
    (util/attacking-players ..player.. ..game..) => [:team-2-attacker
                                                     :team-3-attacker
                                                     :team-3-attacker-2]
    (provided
      ..player.. =contains=> {:team :home-team}
      (util/opposing-players ..player.. ..game..) => [:team-2-attacker
                                                      :civilian
                                                      :team-3-attacker
                                                      :team-3-attacker-2
                                                      :civilian
                                                      :civilian]
      (util/attacking :team-2-attacker) => :home-team
      (util/attacking :team-3-attacker) => :home-team
      (util/attacking :team-3-attacker-2) => :home-team
      (util/attacking :civilian) => nil)))


(facts "about map-or-apply"
  (fact "applies function to non-seq argument"
    (util/map-or-apply #(.toUpperCase %) "hi") => "HI")
  (fact "maps function over seq"
    (util/map-or-apply #(.toUpperCase %) ["hi" "there"]) => ["HI" "THERE"]))
