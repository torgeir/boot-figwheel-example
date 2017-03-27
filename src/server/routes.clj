(ns server.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(def index-html (slurp (clojure.java.io/resource "public/index.html")))

(defn index [req]
  {:status  200
   :headers {"content-type" "text/html"}
   :body index-html})

(defroutes app
  (GET "/" [] #'index)
  (route/resources "/" {:root "public"}))
