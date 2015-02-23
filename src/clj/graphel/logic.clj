(ns graphel.logic
  (:require [graphel.utils :refer :all]))


(defn state [system db]
  (or (->> system :states (filter-vals #(% db)) ffirst)
      :initial))

(defn operations [system state]
  (get-in system [:conditions state]))

(defn sanitize-op [op]
  (-> op
      (dissoc :response-fn)))

(defn get-operation [system operation]
  (get-in system [:operations operation]))

(defn with-base-ops [system]
  (-> system
      (assoc-in  [:operations :op?]
                 {:description "Discovers about valid operations"
                  :request-schema {:schema :string :name "target"}
                  :response-fn (fn [system db v]
                                 (->>
                                  (state system db)
                                  (operations system)
                                  (map (fn [x] {x (sanitize-op (get-operation system x))}))
                                  (into {})))})
      (update-in [:conditions]      (fn [conditions] (map-vals (p< conj :op?) conditions)))))

(defn get-identity [system]
  (:identity system))

(def get-operation-fn (comp :response-fn get-operation))

(defn respond [system db [e a v t]]
  [(get-identity system) a ((get-operation-fn system a) system db v) t])

(defn should-run [db action]
  (let [last-timestamp (some-> db last (nth 3))]
    (or (not last-timestamp) (< last-timestamp (nth action 3)))))

(defn converse [system db action]
    (if (should-run db action)
      (concat db [action (respond system db action)])
      db))
