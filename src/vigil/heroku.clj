(ns vigil.heroku)

(defn convert-database-url [db-url]
  (let [[_ user password host port db] (re-matches #"postgres://(?:(.+):(.*)@)?([^:]+)(?::(\d+))?/(.+)" db-url)]
    {
      :user user
      :password password
      :host host
      :port (or port 80)
      :db db
    }))
