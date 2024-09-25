(ns database.user
  (:require
   [database.core :as db]
   [utils.encryption :as enc]))

(defn by-id [id]
  (db/execute-one! ["select * from users where id = ?" id]))

(defn by-email [email]
  (db/execute-one! ["select * from users where email = ?" email]))

(defn all []
  (db/execute! ["select * from users"]))

(defn delete [id]
  (db/execute! ["delete from users where id = ?" id]))

(defn insert [{:keys [email password]}]
  (db/execute-one!
   ["insert into users(email, password) values (?,?)"
    email (enc/hash-password password)]))

(defn correct-password? [email password]
  (let [user (by-email email)]
    (if user 
      (enc/password= (:users/password user) password)
      false)))

(comment (correct-password? "hans@wursde" "test"))
(comment (insert {:email "hans@wurst.de" :password "test"}))
(comment (all))
