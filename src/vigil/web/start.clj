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

(defn new-game [player-name team-name sally-duration]
  (ring-response/redirect
   (format "/my-game/%s"
           (:id (ops/new-game player-name
                              team-name
                              (Integer/parseInt sally-duration))))))

(defn get-game [id]
  "You don't have to be in a game to view its state."
  (game/page {:game (ops/get-full-game (Integer/parseInt id))}))

(defn get-player-game [player-id]
  "Show a player the state of their game, crucially including a check for
  attackers. This handler is where the game is played."
  (game/page (ops/load-player-game (Integer/parseInt player-id))))


(defroutes app-routes
  (GET "/" [] (index/page))
  (GET "/game/:id" [id] (get-game id))
  (GET "/my-game/:player-id" [player-id]
       (get-player-game player-id))
  (POST "/game/new" [player-name team-name sally-duration]
        (new-game player-name team-name sally-duration))
  (route/not-found "Oops"))

(defn -main [& [port]]
  "This is some Heroku magic I don't really understand."
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app-routes) {:port port :join? false})))

(def app (wrap-defaults app-routes api-defaults))
