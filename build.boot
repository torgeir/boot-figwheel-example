(set-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.9.0-alpha15"]
                 [org.clojure/clojurescript "1.9.494"]
                 [org.clojure/core.async "0.3.442"]

                 ;; backend
                 [http-kit "2.2.0"]
                 [compojure "1.5.2"]

                 ;; frontend
                 [org.clojure/tools.nrepl "0.2.12"  :scope "test"] ; figwheel
                 [com.cemerick/piggieback "0.2.1"   :scope "test"]
                 [ajchemist/boot-figwheel "0.5.4-6" :scope "test"]
                 [figwheel-sidecar "0.5.7"          :scope "test"]
                 [deraen/boot-less "0.6.1"          :scope "test"] ; less

                 [rum "0.10.8"]])

(load-file "figwheel_utils.clj")

;; fix figwheel browser repl for boot+cider repls in emacs
(swap! boot.repl/*default-middleware*
       conj 'cemerick.piggieback/wrap-cljs-repl)

(require '[boot-figwheel :refer [figwheel]]
         '[deraen.boot-less :refer [less]]
         '[server.app :as app])

(def target-path
  "Folder to build cljs and less to."
  "target/public")

(def builds
  "List of prod cljs-to-js builds."
  [{:id "example"
    :source-paths ["src"]
    :compiler {:main 'example.core
               :output-to "js/example.js"
               :optimizations :advanced}}])

(defn dev-builds
  "Transform list of builds to add figwheel-js for hot reloading, source maps
   and no google closure compiler optimizations."
  [builds]
  (doall (map #(-> %
                   (assoc-in [:figwheel] true)
                   (assoc-in [:compiler :source-map] true)
                   (assoc-in [:compiler :optimizations] :none))
              builds)))

(deftask dev
  "Build for development.
   - hot reloads cljs with figwheel
   - starts the app server serving routes and static files
   - starts a repl server so an editor can connect and reload server code
   - watches and compile less on change"
  []
  (comp
   (do (app/start-server) identity)
   (figwheel-utils/copying-built-js-to-class-path
    (figwheel :target-path target-path
              :all-builds (dev-builds builds)
              :figwheel-options {:server-port 7000
                                 :validate-config true
                                 :css-dirs [(format "%s/css/" target-path)]
                                 :open-file-command "emacsclient"}))
   (repl :server true)
   (watch)
   (less :source-map true)
   (target :no-clean true)))

(deftask dist
  "Create a runnable jar. Run it with `java -jar target/<jar-file>`."
  []
  (comp
   (figwheel-utils/copying-built-js-to-class-path
    (figwheel :target-path target-path
              :all-builds builds
              :once-ids (map :id builds)
              :figwheel-options {:server-port 7001}))
   (less :compression true)
   (pom :project 'torgeir/boot-figwheel-example
        :version "1.0.0"
        :description "A boot+figwheel example"
        :url "https://github.com/torgeir/boot-figwheel-example"
        :scm {:url "https://github.com/torgeir/boot-figwheel-example"}
        :license {"MIT License" "https://opensource.org/licenses/MIT"})
   (aot :all true)
   (uber)
   (jar :main 'server.main)
   (sift :include #{ #"^boot.*jar$" })
   (target)))
