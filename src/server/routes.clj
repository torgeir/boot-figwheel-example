(ns server.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defn index [req]
  {:status  200
   :headers {"content-type" "text/html"}
   :body (str
          "<!doctype html>
           <html>
             <head>
               <title>boot + figwheel example</title>
               <link rel='stylesheet' href='css/app.css'>
             </head>
             <body>
               <div id='app'></div>
               <script src='js/example.js'></script>
             </body>
           </html> ")})


(defroutes app
  (route/resources "/" {:root "public"})
  (GET "/" [] #'index))
