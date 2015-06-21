(ns vigil.web.pages.player
  (:require [net.cgrand.enlive-html :as enlive]))

(def snip (enlive/snippet "templates/player.html"
  [:.player]
  [{name :name alive :alive}]
  [:span.name] (enlive/content name)
  [:span.status] (enlive/do->
                  (enlive/content (if alive "alive" "dead"))
                  (enlive/add-class (if alive "alive" "dead")))))
