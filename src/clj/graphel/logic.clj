(ns graphel.logic
  (:require [graphel.utils :refer :all]))


(defn state [system db]
  (or (->> system :states (filter-vals #(% db)) ffirst)
      :initial))

(defn operations [system state]
  (get-in system [:conditions state]))

(defn with-base-ops [system]
  (-> system
      (assoc-in  [:operations :op?] (fn [system db v] (operations system (state system db))))
      (update-in [:conditions]      (fn [conditions] (map-vals (p< conj :op?) conditions)))))

(defn get-identity [system]
  (:identity system))

(defn get-operation [system operation]
  (get-in system [:operations operation]))

(defn respond [system db [e a v t]]
  [(get-identity system) a ((get-operation system a) system db v) t])

(defn should-run [db action]
  (let [last-timestamp (some-> db last (nth 3))]
    (or (not last-timestamp) (< last-timestamp (nth action 3)))))

(defn converse [system db action]
    (if (should-run db action)
      (concat db [action (respond system db action)])
      db))
