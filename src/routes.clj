(ns routes
  (:require
   [compojure.core :as c]
   [compojure.route :as r]
   [view.index :as index]
   [view.login :as login]
   [view.profile :as profile]
   [view.register :as register]))

(c/defroutes routes
  (r/resources "/static" {:root ""})
  (c/GET "/" [] index/page)
  (c/GET "/register" [] register/index)
  (c/POST "/register" [] register/save)
  (c/GET "/login" [] login/index)
  (c/POST "/login" [] login/login)
  (c/GET "/logout" [] login/logout)
  (c/GET "/profile" [] profile/index))

