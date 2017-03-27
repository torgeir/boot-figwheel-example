(set-env!
 :source-paths #{"src"}
 :resource-paths #{"resources"}
 :dependencies
 '[[org.clojure/clojure "1.9.0-alpha15"]
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
   [adzerk/boot-cljs "2.0.0"          :scope "test"] ; cljs
   [pandeiro/boot-http "0.7.2"        :scope "test"] ; serve
   [deraen/boot-less "0.6.1"          :scope "test"] ; less

   ])

(swap! boot.repl/*default-middleware*
       conj 'cemerick.piggieback/wrap-cljs-repl)

(require
 '[boot-figwheel :refer [figwheel]]
 '[pandeiro.boot-http :refer [serve]]
 '[adzerk.boot-cljs :refer [cljs]]
 '[deraen.boot-less :refer [less]])

(deftask dev-figwheel []
  (figwheel
   :build-ids ["example"]
   :target-path "target/public"
   :all-builds [{:id "example"
                 :figwheel true
                 :source-paths ["src"]
                 :compiler {:main          'example.core
                            :output-to     "js/example.js"
                            :source-map    true
                            :optimizations :none}}]
   :figwheel-options {:validate-config   true
                      :server-port       7000
                      :server-logfile    ".figwheel/server.log"
                      :css-dirs          ["target/public/css/"]
                      :open-file-command "emacsclient"}))

(deftask dev
  "Build for development: hot reloads cljs, serves static files, watches styles."
  []
  (comp
   (dev-figwheel)
   (serve :dir "target/public" :port 8080)
   (watch)
   (less)
   (target :no-clean true)))

(deftask dist
  "Create a runnable jar.
  Run it with `java -jar target/boot-figwheel-example-1.0.0.jar`."
  []
  (comp
   (cljs)
   (less)
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
