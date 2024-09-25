(ns core
  (:require
   [routes :as ro]
   [middleware.auth :as auth]
   [ring.middleware.multipart-params :as mp]
   [ring.middleware.anti-forgery :as af]
   [ring.middleware.session :as s]
   [ring.middleware.params :as p]
   [ring.middleware.flash :as f]
   [ring.adapter.jetty :as ring])
  (:gen-class))

(def server (atom nil))

(defn -main [& args]
  (reset! server
          (ring/run-jetty
           (->
            #'ro/routes
            auth/wrap-auth
            (af/wrap-anti-forgery {:anti-forgery true
                                   :token-expiry (* 60 60 24)})
            f/wrap-flash
            s/wrap-session
            p/wrap-params
            mp/wrap-multipart-params)
           {:port 8080
            :join? false})))

(comment (-main))
(comment (.stop @server))
