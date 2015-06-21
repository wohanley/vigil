(defproject vigil "0.1.0-SNAPSHOT"
  :description "A game about patience."
  :url "http://bitbucket.org/wohanley/vigil"
  :license {:name "EPL"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [postgresql "9.3-1102.jdbc41"]
                 [korma "0.4.0"]
                 [environ "1.0.0"]
                 [enlive "1.1.5"]]
  :plugins [[lein-environ "1.0.0"]
            [environ/environ.lein "0.2.1"]]
  :hooks [environ.leiningen.hooks]
  :ring {:handler vigil.web.start/app}
  :min-lein-version "2.0.0"
  :uberjar-name "vigil.jar")
