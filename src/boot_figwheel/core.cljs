(ns boot-figwheel.core
  (:require-macros [boot-figwheel.log :refer [log]])
  (:require [clojure.string :as string]))

(defn $ [sel]
  (.querySelector js/document sel))

(defn inner-html! [el str]
  (aset el "innerHTML" str))

(let [el ($ "code")]
  (inner-html! el (+ (Math/random))))

(enable-console-print!)
