(ns vigil.web.pages.join-team
  (:require [net.cgrand.enlive-html :as enlive]
            [vigil.web.pages.player :as player]))

(def snip
  (enlive/snippet
   "join-team.html"
   [:.join]
   [team-id]
   [:input.team-id] (enlive/set-attr :value team-id)))
