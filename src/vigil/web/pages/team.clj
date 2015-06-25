(ns vigil.web.pages.team
  (:require [net.cgrand.enlive-html :as enlive]
            [vigil.web.pages.player :as player]))

(def snip
  (enlive/snippet
   "team.html"
   [:.team]
   [{:keys [id name players joinable]}]
   [:.team-name] (enlive/content name)
   [:.players] (enlive/content (map player/snip players))))
