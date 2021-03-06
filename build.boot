#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.5.1"

(set-env!
  :project      'graphel
  :version      "0.1.0-SNAPSHOT"
  :dependencies '[[tailrecursion/boot.task   "2.2.4"]
                  [tailrecursion/hoplon      "5.10.24"]]
  :out-path     "resources/public"
  :src-paths    #{"src/hl" "src/cljs" "src/clj"})

;; Static resources (css, images, etc.):
(add-sync! (get-env :out-path) #{"assets"})

(require '[tailrecursion.hoplon.boot :refer :all]
         '[tailrecursion.castra.task :as c])



(deftask development
  "Build graphel for development."
  []
  (comp (watch) (hoplon {:prerender false}) (c/castra-dev-server 'sample-finance-system.api)))

(deftask dev-debug
  "Build graphel for development with source maps."
  []
  (comp (watch) 
  (hoplon {:pretty-print true
                         :prerender false
                         :source-map true}) (c/castra-dev-server 'sample-finance-system.api)))

(deftask production
  "Build graphel for production."
  []
  (hoplon {:optimizations :advanced}))
