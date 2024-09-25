(ns view.components
  (:require
   [hiccup2.core :as h]
   [utils.session :as s]
   [clojure.java.io :as io]
   [ring.middleware.anti-forgery :as af]
   [squint.compiler :as squint]))

(defn csrf-token []
  [:input {:type "hidden"
           :name "__anti-forgery-token"
           :value af/*anti-forgery-token*}])

(defn ->js [form]
  (->>
   (squint/compile-string* (str form))
   :body))

(defn cljs-resource [filename]
  [:script
   (->
    (str "cljs/" filename ".cljs")
    io/resource
    slurp
    ->js
    h/raw)])

(defn navbar [req]
  (let [user (s/current-user req)]
    [:nav.navbar.sticky-top.navbar-expand-lg.navbar-bg-body-tertiary
     [:div.container-fluid
      [:a.navbar-brand {:href "/"} "JobStop"]
      [:button.navbar-toggler {:type "button" :data-bs-toggle "collapse" :data-bs-target "#navbar"}
       [:span.navbar-toggler-icon]]
      [:div#navbar.collapse.navbar-collapse
       (when (not user)
         [:ul.navbar-nav
          [:li.nav-item
           [:a.nav-link {:href "/login"} "Login"]]
          [:li.nav-item
           [:a.nav-link {:href "/register"} "Register"]]])
       (when user
         [:ul.navbar-nav
          [:li.nav-item
           [:a.nav-link {:href "/profile"} "Profile"]]
          [:li.nav-item
           [:a.nav-link {:href "/logout"} "Logout"]]])]]]))

(defn alert [req]
  (let* [msg (get-in req [:flash :message])
         severity (:severity msg)
         msg (:message msg)]
    [:div.alert {:class (str "alert-" severity) :role "alert"}
     msg]))

(defn flash-msg [res severity msg]
  (assoc res :flash
         {:message {:severity severity
                    :message msg}}))

(defn layout [req & body]
  (str
   (h/html
       [:html
        [:head
         [:link {:href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                 :rel "stylesheet"
                 :integrity "sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                 :crossorigin "anonymous"}]
         (cljs-resource "helloworld")]
        [:body
         (navbar req)
         (alert req)
         body
         [:script {:src "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                   :integrity "sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                   :crossorigin "anonymous"}]]])))
