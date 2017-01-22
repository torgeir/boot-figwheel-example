(set-env!
 :dependencies
 '[[org.clojure/clojure "1.8.0" :scope "provided"]
   [org.clojure/clojurescript "1.9.229" :scope "provided"]
   [org.clojure/core.async "0.2.391"]

   [ajchemist/boot-figwheel "0.5.4-6" :scope "test"] ;; latest release
   [org.clojure/tools.nrepl "0.2.12" :scope "test"]
   [com.cemerick/piggieback "0.2.1" :scope "test"]
   [figwheel-sidecar "0.5.8" :scope "test"]])

(require 'boot-figwheel)
(refer 'boot-figwheel :rename '{cljs-repl fw-cljs-repl})

(task-options!
 pom {:project 'torgeir/boot-figwheel
      :version "0.1.0-SNAPSHOT"
      :description "boot+figwheel example"})

(def all-builds
  [{:id "example"
    :source-paths ["src"]
    :compiler '{:main          boot-figwheel.core
                :output-to     "dist/js/boot-figwheel.js"
                :source-map    true
                :optimizations :none}
    :figwheel true}])

(def figwheel-options
  {:server-port 7000
   :server-logfile ".figwheel/server.log"
   :validate-config true
   :css-dirs ["resources/public/css/"]})

(deftask dev-figwheel []
  (merge-env! :source-paths #{"src"})
  (figwheel
   :build-ids ["example"]
   :all-builds all-builds
   :target-path "resources/public"
   :figwheel-options figwheel-options))

(deftask dev []
  (comp
   (dev-figwheel)
   (repl :server true)
   (wait)))
