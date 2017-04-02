(ns server.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [rum.core :as rum]
            [example.ui :as ui]))

(defonce state
  (atom
   {:number 42}))

(defn layout [state partial]
  (str
   "<!doctype html>
    <html>
      <head>
        <title>boot + figwheel example</title>
        <link rel='stylesheet' href='css/app.css'>
      </head>
      <script>window.state = '" (pr-str state) "';</script>
      <body>
        <div id='app'>" partial "</div>
        <script src='js/example.js'></script>
      </body>
    </html>"))

(defn index [req]
  {:status  200
   :headers {"content-type" "text/html"}
   :body (layout @state (rum/render-html (ui/app state)))})

(defroutes app
  (route/resources "/" {:root "public"})
  (GET "/" [] #'index))
