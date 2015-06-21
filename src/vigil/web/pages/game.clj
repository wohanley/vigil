(ns vigil.web.pages.game
  (:require [net.cgrand.enlive-html :as enlive]
            [vigil.web.pages.current-player :as current-player]
            [vigil.web.pages.player :as player]
            [vigil.web.pages.team :as team]
            [clojure.pprint :refer [pprint]]))

(def page
  (enlive/template
   "game.html"
   [{:keys [game current-player]}]
   [:#current-player] (if (not (empty? current-player)) (enlive/substitute (current-player/snip current-player)))
   [:#sally-duration] (enlive/content (str (:sally-duration game)))
   [:#teams] (enlive/content (map #(team/snip (assoc % :joinable (empty? current-player))) (:teams game)))))
