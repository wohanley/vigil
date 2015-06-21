(ns vigil.web.pages.current-player
  (:require [net.cgrand.enlive-html :as enlive]))

(def snip
  (enlive/snippet
   "templates/current-player.html"
   [:#current-player]
   [{:keys [name team alive]}]
   [:.name] (enlive/content name)
   [:.team] (enlive/content team)
   [:.status] (enlive/content (if alive "alive" "dead"))))
