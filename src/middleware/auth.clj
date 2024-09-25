(ns middleware.auth
  (:require
   [ring.util.response :as r]
   [clojure.string :as str]
   [database.user :as user]))

(def restricted-pages
  ["/profile"
   "/wurst"])

(defn path-restricted? [path]
  (some? (first (filter #(str/includes? path %) restricted-pages))))

(comment (path-restricted? "/prof"))

(defn wrap-auth [handler]
  (fn [req]
    (if (path-restricted? (:uri req))
      (r/redirect (str "/login?url=" (:uri req)))
      (handler req))))
