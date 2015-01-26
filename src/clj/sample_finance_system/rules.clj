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
   :operations {:log-in (constantly nil)
                :show-transactions (constantly transactions)
                :log-out (constantly nil)}
   :conditions {:initial [:log-in]
                :logged-in [:show-transactions :log-out]}})

