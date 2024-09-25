(ns database.core
  (:require
   [next.jdbc :as jdbc]))

(defonce db (jdbc/get-connection {:dbtype "postgres"
                                  :dbname "<somedb>"
                                  :user "postgres"
                                  :password "test1234"
                                  :port 15432}))

(defn execute!
  ([sql]
   (jdbc/execute! db sql))
  ([tx sql]
   (jdbc/execute! tx sql)))

(defn execute-one!
  ([sql]
   (jdbc/execute-one! db sql))
  ([tx sql]
   (jdbc/execute-one! tx sql)))

(defn initialize-db []
  (execute-one! [(slurp "init.sql")]))

(comment (initialize-db))

(defn version []
  (execute-one! ["select version()"]))
