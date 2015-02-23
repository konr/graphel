(ns sample-finance-system.rules-test
  (:require [midje.sweet :refer :all]
            [sample-finance-system.rules :refer :all]
            [graphel.utils :refer :all]
            [graphel.logic :as logic]))

(def conversation
  [[:user :op? nil 1]
   [:machine :op? {:log-in {:description "Logs in"
                            :response-schema {:name "state" :schema :discoverable}
                            :request-schema  {:username {:name "Username" :schema :string}
                                              :password {:name "Password" :schema :string}}}
                   :op? {:description "Discovers about valid operations"
                         :request-schema {:name "target" :schema :string}}} 1]
   [:user :log-in {:username "in" :password "valid"} 2]
   [:machine :log-in false 2]
   [:user :log-in {:username "foo" :password "bar"} 3]
   [:machine :log-in :logged-in 3]
   [:user :op? :logged-in 4] ;; TODO react on :logged-in
   [:machine :op? {:show-transactions {:description "Show user's transactions"
                                       :response-schema [[{:name "transaction name" :schema :string}
                                                          {:name "index" :schema :int}]]
                                       :request-schema nil}
                   ;; TODO request schema as enum with current affordances
                   :op? {:description "Discovers about valid operations"
                         :request-schema {:name "target" :schema :string}}
                   :log-out {:description "Logs out"
                             :request-schema nil}} 4]
   [:user :show-transactions nil 5]
   [:machine :show-transactions transactions 5]
   [:user :op? nil 6]
   [:machine :op? {:show-transactions {:description "Show user's transactions"
                                       :response-schema [[{:name "transaction name" :schema :string}
                                                          {:name "index" :schema :int}]]
                                       :request-schema nil}
                   :op? {:description "Discovers about valid operations"
                         :request-schema {:name "target" :schema :string}}
                   :log-out           {:description "Logs out"
                                       :request-schema nil}} 6]
   [:user    :log-out nil 7]
   [:machine :log-out nil 7]
   [:user :op? nil 8]
   [:machine :op? {:log-in {:description "Logs in"
                            :response-schema {:name "state" :schema :discoverable}
                            :request-schema  {:username {:name "Username" :schema :string}
                                              :password {:name "Password" :schema :string}}}

                   :op? {:description "Discovers about valid operations"
                         :request-schema {:name "target" :schema :string}}} 8]])

(fact
 (let [system (logic/with-base-ops webbank-system)]
   (->> conversation
        (filter (comp (p> = :user) first))
        (reduce #(logic/converse system %1 %2) []))
   => conversation))
