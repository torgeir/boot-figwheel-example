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
                 [adzerk/boot-cljs "2.0.0"          :scope "test"] ; cljs
                 [pandeiro/boot-http "0.7.2"        :scope "test"] ; serve
                 [deraen/boot-less "0.6.1"          :scope "test"] ; less

                 [rum "0.10.8"]])

(load-file "file_utils.clj")

;; fix figwheel browser repl for boot+cider repls in emacs
(swap! boot.repl/*default-middleware*
       conj 'cemerick.piggieback/wrap-cljs-repl)

(require '[boot-figwheel :refer [figwheel]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-cljs :refer [cljs]]
         '[deraen.boot-less :refer [less]]
         '[server.app :as app])

(defn- frontend []
  "Frontend task to compile and hot reload cljs, as well as hot reload css.

   As figwheel compiles to a folder on disk (:target-path), this task copies the
   initially compiled js files into `public/js/` on the classpath, so that the
   compojure resources route for `public` can serve figwheel compiles js files,
   along with the rest of the static files."
  (comp
   (figwheel
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
                       :open-file-command "emacsclient"})
   (file-utils/copy-dir-to-classpath "./target/public/js" "public/js")))

(deftask dev
  "Build for development.
   - hot reloads cljs with figwheel
   - starts the app server serving routes and static files
   - starts a repl server so an editor can connect and reload server code
   - watches and compile less on change"
  []
  (comp
   (do (app/start-server) identity)
   (frontend)
   (repl :server true)
   (watch)
   (less)
   (target :no-clean true)))

(deftask dist
  "Create a runnable jar.
   Run it with `java -jar target/<jar-file>`."
  []
  (comp
   (cljs)
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
