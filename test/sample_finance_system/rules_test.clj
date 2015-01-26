(ns sample-finance-system.rules-test
  (:require [midje.sweet :refer :all]
            [sample-finance-system.rules :refer :all]
            [graphel.utils :refer :all]
            [graphel.logic :as logic]))

(def conversation
  [[:user :op? nil 1]
   [:machine :op? [:log-in :op?] 1]
   [:user :log-in nil 2]
   [:machine :log-in nil 2]
   [:user :op? nil 3]
   [:machine :op? [:show-transactions :log-out :op?] 3]
   [:user :show-transactions nil 4]
   [:machine :show-transactions transactions 4]
   [:user :op? nil 5]
   [:machine :op? [:show-transactions :log-out :op?] 5]
   [:user    :log-out nil 6]
   [:machine :log-out nil 6]
   [:user :op? nil 7]
   [:machine :op? [:log-in :op?] 7]])

(fact
 (let [system (logic/with-base-ops webbank-system)]
   (->> conversation
        (filter (comp (p> = :user) first))
        (reduce #(logic/converse system %1 %2) []))
   => conversation))
