(ns example.core
  (:require-macros [example.log :refer [log]])
  (:require [clojure.string :as string]))

(defn $ [sel]
  (.querySelector js/document sel))

(defn inner-html! [el str]
  (aset el "innerHTML" str))

(let [el ($ "code")]
  (inner-html! el (+ 42 (Math/random))))

(log "you're on!")

(enable-console-print!)
