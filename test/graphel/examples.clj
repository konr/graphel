(ns graphel.examples
  (:require [midje.sweet :refer :all]
            [graphel.utils :refer :all]
            [graphel.core :refer :all]))

(def transactions
  [["books" 13]
   ["beer"  37]
   ["gas"   42]])

(def webbank-system
  {:identity :machine
   :states {:logged-in (fn [db] (and (find-first (comp (p> = :log-in) second) db)
                                     (not (find-first (comp (p> = :log-out) second) db))))}
   :operations {:log-in (constantly nil)
                :show-transactions (constantly transactions)
                :log-out (constantly nil)}
   :conditions {:initial [:log-in]
                :logged-in [:show-transactions :log-out]}})

(def conversation
  [[:user :op? nil 1]
   [:machine :op? [:log-in :op?] 1]
   [:user :log-in nil 2]
   [:machine :log-in nil 2]
   [:user :op? nil 3]
   [:machine :op? [:show-transactions :log-out :op?] 3]
   [:user :show-transactions nil 3]
   [:machine :show-transactions transactions 3]
   [:user :op? nil 4]
   [:machine :op? [:show-transactions :log-out :op?] 4]
   [:user    :log-out nil 5]
   [:machine :log-out nil 5]
   [:user :op? nil 4]
   [:machine :op? [:log-in :op?] 4]])

(fact
 (let [system (with-base-ops webbank-system)]
   (->> conversation
        (filter (comp (p> = :user) first))
        (reduce #(converse system %1 %2) []))
   => conversation))
