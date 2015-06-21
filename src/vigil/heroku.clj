(ns vigil.heroku)

(defn convert-database-url [db-url]
  "Turn a database URL into a JDBC-style connection spec. Heroku likes its DB
  specs as URL strings stored in environment variables, so that's how we'll do
  it."
  (let [[_ user password host port db] (re-matches #"postgres://(?:(.+):(.*)@)?([^:]+)(?::(\d+))?/(.+)" db-url)]
    {:classname "org.postgresql.Driver"
     :subprotocol "postgresql"
     :user user
     :password password
     :host host
     :port (or port 80)
     :db db
     :subname (if (nil? port)
                (format "//%s/%s" host db)
                (format "//%s:%s/%s" host port db))}))
