(ns sample-finance-system.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [tailrecursion.javelin :refer [cell]]
   [tailrecursion.castra :refer [mkremote]]))

(def command-list
  [[:user :op? nil 1]
   [:user :log-in nil 2]
   [:user :op? nil 3]
   [:user :show-transactions nil 4]
   [:user :op? nil 5]
   [:user :log-out nil 6]
   :reset])

(def i (atom 0))

(defc state {:action nil :db nil})
(defc error nil)
(defc loading [])

(defc= action (get state :action))
(defc= db     (get state :db))

(def get-state
  (mkremote 'sample-finance-system.api/step state error loading))

(def reset-system
  (mkremote 'sample-finance-system.api/reset-system (cell nil) (cell nil) (cell nil)))

(defn step []
  (get-state (nth command-list (rem @i (count command-list))))
  (swap! i inc))

(defn init []
  (js/setInterval step 500))
