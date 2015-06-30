(ns vigil.web.pages.game
  (:require [net.cgrand.enlive-html :as enlive]
            [vigil.web.pages.current-player :as current-player]
            [vigil.web.pages.player :as player]
            [vigil.web.pages.team :as team]
            [vigil.web.pages.attack-team :as attack-team]
            [vigil.web.pages.join-game :as join-game]
            [clojure.pprint :refer [pprint]]))

(def page
  (enlive/template
   "game.html"
   [{:keys [game current-player]}]
   [:#current-player]
   (if (not (empty? current-player))
     (enlive/substitute (current-player/snip current-player)))
   [:#sally-duration]
   (enlive/content (str (:sally-duration game)))
   [:#teams]
   (enlive/content (map #(concat (team/snip %)
                                 (if (not= (:id %) (:team-id current-player))
                                   (attack-team/snip
                                    {:team %
                                     :current-player current-player})
                                   []))
                        (:teams game)))
   [:#join-game]
   (if (empty? current-player)
     (enlive/substitute (join-game/snip game))
     (enlive/substitute ""))))
