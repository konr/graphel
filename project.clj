(defproject graphel "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj"]
  :test-paths   ["test/"]
  :plugins      [[lein-midje "3.1.3"]]
  :dependencies [[org.clojure/clojure "1.7.0-alpha1"]
                 [tailrecursion/boot.task   "2.2.4"]
                 [tailrecursion/hoplon      "5.10.24"  :exclusions [org.clojure/clojure]]
                 [midje "1.6.3" :exclusions [org.clojure/core]]])
