(ns sample-finance-system.api
  (:require [tailrecursion.castra :refer [defrpc]]
            [graphel.logic :as graphel]
            [sample-finance-system.rules :as rules]))


(def system (graphel/with-base-ops rules/webbank-system))

(def db (atom []))

(defrpc step [datum]
  {:action datum
   :db (if (= datum :reset)
         (reset! db [])
         (swap! db #(graphel/converse system % datum)))})
