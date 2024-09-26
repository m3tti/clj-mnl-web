(ns database.core
  (:require
   [next.jdbc :as jdbc]
   [next.jdbc :as sql]))

(defonce db (jdbc/get-connection {:dbtype "postgres"
                                  :dbname "<somedb>"
                                  :user "postgres"
                                  :password "test1234"
                                  :port 15432}))

(defn execute!
  ([sql]
   (execute! db sql))
  ([tx sql]
   (jdbc/execute! tx sql)))

(defn execute-one!
  ([sql]
   (execute-one! db sql))
  ([tx sql]
   (jdbc/execute-one! tx sql)))

(defn insert!
  ([table key-map]
   (insert! db table key-map))
  ([tx table key-map]
   (sql/insert! tx table key-map)))

(defn delete!
  ([table where-params]
   (delete! db table where-params))
  ([tx table where-params]
   (sql/delete! tx table where-params)))

(defn update!
  ([table key-map where-params]
   (update! db table key-map where-params))
  ([tx table key-map where-params]
   (sql/update! tx table key-map where-params)))

(defn find-by-keys
  ([table key-map]
   (find-by-keys db table key-map))
  ([tx table key-map]
   (sql/find-by-keys tx table key-map)))

(defn initialize-db []
  (execute-one! [(slurp "init.sql")]))

(comment (initialize-db))

(defn version []
  (execute-one! ["select version()"]))
