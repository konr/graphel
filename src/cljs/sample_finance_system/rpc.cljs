(ns sample-finance-system.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [cljs.reader :as reader]
   [tailrecursion.javelin :refer [cell]]
   [tailrecursion.castra :refer [mkremote]]))

(def i (atom 0))

(defc state {:action nil :db nil})
(defc error nil)
(defc loading [])

(defc= action (get state :action))
(defc= db     (get state :db))

(def step
  (mkremote 'sample-finance-system.api/step state error loading))

(defn update [command]
  (step (reader/read-string command)))

(defn init []
  (submit ":reset"))

(defn submit [command]
  (let [edn (reader/read-string command)]
    (.log js/console edn)
    (step edn)))
