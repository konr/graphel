(ns graphel.core
  (:require [graphel.utils :refer :all]))


(defn state [system db]
  (or (->> system :states (filter-vals #(% db)) ffirst)
      :initial))

(defn operations [system state]
  (get-in system [:conditions state]))

(defn with-base-ops [system]
  (-> system
      (assoc-in  [:operations :op?] (fn [system db] (operations system (state system db))))
      (update-in [:conditions]      (fn [conditions] (map-vals (p< conj :op?) conditions)))))

(defn get-identity [system]
  (:identity system))

(defn get-operation [system operation]
  (get-in system [:operations operation]))

(defn respond [system [e a v t]]
  [(get-identity system) a ((get-operation system a) v) t])

(defn converse [system db [e a v t :as action]]
  (concat db
          [(respond system [e a    v   t])
           (respond system [e :op? nil t])]))
