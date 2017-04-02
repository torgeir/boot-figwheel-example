(ns server.app
  (:require [org.httpkit.server :as httpkit]
            [server.routes :as routes]))

(defn- env [var]
  (when-let [v (System/getenv var)]
    (read-string v)))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (println "Stopping..")
    (@server :timeout 100)
    (reset! server nil)))

(defn start-server []
  (stop-server)
  (let [port (or (env "PORT") 8080)]
    (println (format "Starting.. http://localhost:%s" port))
    (reset! server
            (httpkit/run-server #'routes/app {:port port}))))
