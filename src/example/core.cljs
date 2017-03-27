(ns example.core
  (:require-macros [example.log :refer [log]])
  (:require [clojure.string :as string]))

(defn $ [sel]
  (.querySelector js/document sel))

(defn text-node [str]
  (.createTextNode js/document str))

(defn random-number []
  (+ 42 (Math/random)))

(log "you're on!")

(let [el ($ "#app")]
  (.appendChild el (text-node (random-number))))

(enable-console-print!)
