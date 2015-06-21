(ns vigil.web.pages.index
  (:require [net.cgrand.enlive-html :as enlive]))

(def page
  (enlive/template
   "templates/index.html" []))
