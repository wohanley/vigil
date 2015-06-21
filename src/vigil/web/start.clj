(ns vigil.web.start
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :as ring-response]
            [ring.middleware.json :as json]
            [vigil.operations :as ops]
            [environ.core :refer [env]]
            [ring.adapter.jetty :as jetty]
            [vigil.web.pages.index :as index]
            [vigil.web.pages.game :as game]))

(defn get-player-game [player-id]
  (game/page (ops/load-player-game (Integer/parseInt player-id))))

(defn new-game [player-name team-name sally-duration]
  (ring-response/redirect
   (format "/my-game/%s"
           (:id (ops/new-game player-name team-name (Integer/parseInt sally-duration))))))

(defn get-game [id]
  (game/page {:game (ops/get-full-game (Integer/parseInt id))}))

(defroutes app-routes
  (GET "/" [] (index/page))
  (GET "/game/:id" [id] (get-game id))
  (GET "/my-game/:player-id" [player-id]
       (get-player-game player-id))
  (POST "/game/new" [player-name team-name sally-duration]
        (new-game player-name team-name sally-duration))
  (route/not-found "Oops"))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app-routes) {:port port :join? false})))

(def app (wrap-defaults app-routes api-defaults))
