(ns vigil.web.pages.team
  (:require [net.cgrand.enlive-html :as enlive]
            [vigil.web.pages.player :as player]
            [vigil.web.pages.join-team :as join-team]))

(def snip
  (enlive/snippet
   "templates/team.html"
   [:.team]
   [{:keys [id name players joinable]}]
   [:.team-name] (enlive/content name)
   [:.players] (enlive/content (map player/snip players))
   [:.join] (enlive/substitute (if joinable (join-team/snip id)))))
