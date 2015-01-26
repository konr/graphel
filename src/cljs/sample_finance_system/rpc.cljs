(ns sample-finance-system.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [tailrecursion.javelin :refer [cell]]
   [tailrecursion.castra :refer [mkremote]]))

(def command-list
  [:reset
   [:user :op? nil 0]
   [:user :log-in nil 1]
   [:user :op? nil 2]
   [:user :show-transactions nil 3]
   [:user :op? nil 4]
   [:user :log-out nil 5]])

(def i (atom 0))

(defc state {:action nil :db nil})
(defc error nil)
(defc loading [])

(defc= action (get state :action))
(defc= db     (get state :db))

(def step
  (mkremote 'sample-finance-system.api/step state error loading))

(defn update []
  (step (nth command-list (rem (swap! i inc) (count command-list)))))

(defn init []
  (js/setInterval update 500))
