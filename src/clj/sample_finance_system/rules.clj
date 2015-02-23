(ns sample-finance-system.rules
  (:require [graphel.utils :refer :all]
            [graphel.logic :refer :all]))

(def transactions
  [["books" 13]
   ["beer"  37]
   ["gas"   42]])

(def webbank-system
  {:identity :machine
   :states {:logged-in (fn [db] (and (find-first (comp (p> = :log-in) second) db)
                                     (not (find-first (comp (p> = :log-out) second) db))))}
   :operations {:log-in {:description "Logs in"
                         :request-schema {:password {:schema :string, :name "Password"},
                                          :username {:schema :string, :name "Username"}}
                         :response-schema {:schema :discoverable :name "state"}
                         :response-fn (fn [_ _ {:keys [username password]}]
                                           (and (= username "foo") (= password "bar") :logged-in))}
                :show-transactions {:response-fn (constantly transactions)
                                    :description "Show user's transactions"
                                    :request-schema nil
                                    ;; TODO link these with the function's schem
                                    :response-schema [[{:schema :string, :name "transaction name"}
                                                       {:schema :int, :name "index"}]]}
                :log-out {:response-fn (constantly nil)
                          :description "Logs out"
                          :request-schema nil}}
   :conditions {:initial [:log-in]
                :logged-in [:show-transactions :log-out]}})

