(ns vigil.operations-test
  (require [vigil.operations :as ops]
           [vigil.core :as core]
           [vigil.data :as data]
           [midje.sweet :refer :all]))

(facts "about new-game"
  (fact "creates a new game and loads it"
    (ops/new-game :player-name :team-name) => :player
    (provided
      (core/set-up-game :player-name :team-name) => :player)))

(facts "about create-team"
  (fact "data/insert-team an empty team"
    (ops/create-team :game-id-in :name-in) => :created-team-id
    (provided
      (data/insert-team {:game-id :game-id-in
                         :name :name-in}) => :created-team-id)))

(facts "about create-player"
  (fact "data/insert-player a core/empty-player"
    (ops/create-player :team-id :name) => :created-player-id
    (provided
      (core/new-player :team-id :name) => :created-player
      (data/insert-player :created-player) => :created-player-id)))
