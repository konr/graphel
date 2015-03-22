(ns tic-tac-toe.rules
  (:require [graphel.utils :refer :all]))

(defn ea->v [datum e a]
  (when (and (= (nth datum 0) e)
             (= (nth datum 1) a))
   (nth datum 2)))

(defn dea->v [data e a]
  (keep-first (p< ea->v e a) data))

(defn user-credentials [db id]
  (keep-first
   (fn [data]
     (let [name (dea->v data id :name)
           key  (dea->v data id :key)]
       (when (and name key)
         {:name name :key key})))
   db))

(defn users-identified [world db]
  (let [user1 #nu/tapd (user-credentials db :user1)
        user2 #nu/tapd (user-credentials db :user2)]
    (when (and user1 user2)
      (assoc world
             :user1 user1
             :user2 user2))))

(defn update-world [world db]
  (let [triggers #{users-identified}]
    (reduce (fn [world trigger] (trigger world db))
            world triggers)))

(def state
  {::initial          {}
   ::users-identified {}
   ::game-started     {}
   ::game-ended       {}})

(def system
  {:identity :game-manager
   :states {:waiting-for-players (fn [world db] (when-not (users-identified db) world))
            :users-identified    users-identified}})
