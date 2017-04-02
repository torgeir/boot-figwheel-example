(ns example.core
  (:require-macros [example.log :refer [log]])
  (:require [clojure.string :as string]
            [rum.core :as rum]
            [example.ui :as ui]
            [cljs.reader :as reader]))

(log "you're on!")

(defn server-state []
  (let [state (.-state js/window)]
    (reader/read-string state)))

(defonce state (atom (server-state)))

(defn $ [sel]
  (.querySelector js/document sel))

(defn render []
  (rum/mount (ui/app state) ($ "#app")))

(render)
