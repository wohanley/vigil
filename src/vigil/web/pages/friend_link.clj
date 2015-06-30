(ns vigil.web.pages.friend-link
  (:require [net.cgrand.enlive-html :as enlive]))


(def snip
  (enlive/snippet
   "friend-link.html"
   [:#friend-link-area]
   [game]
   [:#friend-link]
   (enlive/set-attr :href (format "/game/%s" (:id game)))))
