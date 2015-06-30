(ns vigil.web.pages.join-game
  (:require [net.cgrand.enlive-html :as enlive]))

(def snip
  (enlive/snippet
   "join-game.html"
   [:#join-game]
   [game]
   [:#join-game-id]
   (enlive/set-attr :value (:id game))))
