(set-env!
 :source-paths #{"src"}
 :resource-paths #{"resources"}
 :dependencies
 '[[org.clojure/clojure "1.9.0-alpha15" :scope "provided"]
   [org.clojure/clojurescript "1.9.494" :scope "provided"]
   [org.clojure/core.async "0.3.442"]

   [org.clojure/tools.nrepl "0.2.12"  :scope "test"]
   [com.cemerick/piggieback "0.2.1"   :scope "test"]
   [figwheel-sidecar "0.5.7"          :scope "test"]
   [pandeiro/boot-http "0.7.2"        :scope "test"]
   [ajchemist/boot-figwheel "0.5.4-6" :scope "test"]
   [deraen/boot-less "0.6.1"          :scope "test"]])

(swap! boot.repl/*default-middleware*
       conj 'cemerick.piggieback/wrap-cljs-repl)

(require
 '[boot-figwheel :refer [figwheel]]
 '[pandeiro.boot-http :refer [serve]]
 '[deraen.boot-less :refer [less]])

(task-options!
 pom {:project 'torgeir/boot-figwheel
      :version "0.1.0-SNAPSHOT"
      :description "boot+figwheel example"})

(def all-builds
  [{:id "example"
    :source-paths ["src"]
    :compiler '{:main          example.core
                :output-to     "dist/js/example.js"
                :source-map    true
                :optimizations :none}
    :figwheel true}])

(def figwheel-options
  {:server-port 7000
   :server-logfile ".figwheel/server.log"
   :css-dirs ["target/public/css/"]
   :repl true
   :validate-config true})

(deftask dev-figwheel []
  (merge-env! :source-paths #{"src"})
  (figwheel
   :build-ids ["example"]
   :all-builds all-builds
   :target-path "target/public"
   :figwheel-options figwheel-options))

(deftask styles []
  (comp
   (watch)
   (less)
   (target :no-clean true)))

(deftask dev []
  (comp
   (dev-figwheel)
   (repl :server true)
   (serve :dir "target/public")
   (styles)))
