(ns vigil.web.pages.team
  (:require [net.cgrand.enlive-html :as enlive]
            [vigil.web.pages.player :as player]))

(def snip
  (enlive/snippet
   "team.html"
   [:.team]
   [{:keys [id name players]}]
   [:.team-name] (enlive/content name)
   [:.players] (enlive/content (map player/snip players))
   [:form.attack :input.target-team-id] (enlive/set-attr :value id)
   [:form.attack :input.attacking-player-id] (enlive/set-attr :value id)))
