(ns vigil.web.pages.attack-team
  (:require [net.cgrand.enlive-html :as enlive]))

(def snip
  (enlive/snippet
   "attack-team.html"
   [:.attack-team]
   [{:keys [team current-player]}]
   [:.target-team-id] (enlive/set-attr :value (:id team))
   [:.attacking-player-id] (enlive/set-attr :value (:id current-player))))
