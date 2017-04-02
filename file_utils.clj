(ns file-utils
  (:require [boot.core :refer :all]
            [clojure.java.io :as io]))

(set-env! :dependencies
          #(conj % '[me.raynes/fs "1.4.5" :scope "test"]))

(require '[me.raynes.fs :refer [copy-dir]])

(defn copy-dir-to-classpath [from to]
  (let [tmp (tmp-dir!)]
    (fn middleware [next-handler]
      (fn handler [fileset]
        (empty-dir! tmp)
        (copy-dir (io/file from) (io/file tmp to))
        (-> fileset (add-resource tmp) commit! next-handler)))))
