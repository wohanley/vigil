(ns vigil.web.start
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :as ring-response]
            [ring.middleware.json :as json]
            [vigil.operations :as ops]
            [vigil.core :as core]
            [environ.core :refer [env]]
            [ring.adapter.jetty :as jetty]
            [vigil.web.pages.index :as index]
            [vigil.web.pages.game :as game]))


(defn parse-id [id]
  "Turn an ID from a parameter string into a map with an :id key to an integer,
  suitable for passing on to business logic."
  {:id (Integer/parseInt id)})


(defn redirect-to-player-game [player-id]
  (ring-response/redirect (format "/my-game/%s" player-id)))

(defn new-game [player-name team-name sally-duration]
  (redirect-to-player-game
   (:id (ops/new-game player-name
                      team-name
                      (Integer/parseInt sally-duration)))))

(defn get-game [id]
  "You don't have to be in a game to view its state."
  (game/page {:game (ops/get-full-game (parse-id id))}))

(defn get-player-game [player-id]
  "Show a player the state of their game, crucially including a check for
  attackers. This handler is where the game is played."
  (let [player (parse-id player-id)]
    (ops/check player)
    (game/page (ops/get-game-view-for-player player))))

(defn join-game [game-id name]
  (redirect-to-player-game
   (:id (core/add-player-to-game (parse-id game-id) name))))

(defn sally-forth [target-team-id attacking-player-id]
  (redirect-to-player-game
   (:attacking-player-id (ops/sally-forth (parse-id target-team-id)
                                          (parse-id attacking-player-id)))))


(defroutes app-routes
  (GET "/" [] (index/page))
  (GET "/game/:id" [id] (get-game id))
  (GET "/my-game/:player-id" [player-id]
       (get-player-game player-id))
  (POST "/game/new" [player-name team-name sally-duration]
        (new-game player-name team-name sally-duration))
  (POST "/join-game" [game-id name]
        (join-game game-id name))
  (POST "/sally-forth" [target-team-id attacking-player-id]
        (sally-forth target-team-id attacking-player-id))
  (route/not-found "Oops"))

(defn -main [& [port]]
  "This is some Heroku magic I don't really understand."
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app-routes) {:port port :join? false})))

(def app (wrap-defaults app-routes api-defaults))
