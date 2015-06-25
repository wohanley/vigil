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


(defn parse-id [id]
  "Turn an ID from a parameter string into a map with an :id key to an integer,
  suitable for passing on to business logic."
  {:id (Integer/parseInt id)})


(defn new-game [player-name team-name sally-duration]
  (ring-response/redirect
   (format "/my-game/%s"
           (:id (ops/new-game player-name
                              team-name
                              (Integer/parseInt sally-duration))))))

(defn get-game [id]
  "You don't have to be in a game to view its state."
  (game/page {:game (ops/get-full-game (parse-id id))}))

(defn get-player-game [player-id]
  "Show a player the state of their game, crucially including a check for
  attackers. This handler is where the game is played."
  (game/page (ops/check (parse-id player-id))))

(defn join-game [game-id name]
  (game/page (ops/join-game (parse-id game-id) name)))

(defn sally-forth [target-team-id attacking-player-id]
  (game/page (ops/sally-forth (parse-id target-team-id)
                              (parse-id attacking-player-id))))


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
