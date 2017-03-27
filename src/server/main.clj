(ns server.main
  (:require [server.app :as app])
  (:gen-class))

(defn -main [& args]
  (app/start-server))

(comment
  (app/start-server)
  (app/stop-server))
