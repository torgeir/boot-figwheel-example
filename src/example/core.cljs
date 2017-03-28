(ns example.core
  (:require-macros [example.log :refer [log]])
  (:require [clojure.string :as string]))

(defn $ [sel]
  (.querySelector js/document sel))

(defn text-node [str]
  (.createTextNode js/document str))

(defonce random-number
  (+ 42 (Math/random)))

(defn format-number [number]
  (str "Number: " number))

(log "you're on!")

(let [el ($ "#app")]
  (set! (-> el .-innerHTML) "")
  (.appendChild el (text-node (format-number random-number))))

(enable-console-print!)
