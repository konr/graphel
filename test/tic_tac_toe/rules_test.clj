(ns tic-tac-toe.rules-test
  (:require [midje.sweet :refer :all]
            [tic-tac-toe.rules :as ttt-r]
            [graphel.utils :refer :all]
            [graphel.logic :as logic]))

(facts "on users-identified"
       (let [db
             [[[:user1 :name "John"]
               [:user1 :key "1337"]]
              [[:user2 :name "Sudhana"]
               [:user2 :key "53"]]]]

         (ttt-r/users-identified {} db)
         => {:user1 {:name "John"    :key "1337"}
             :user2 {:name "Sudhana" :key "53"}}

         (ttt-r/users-identified {} [])
         => nil))



(def conversation
  [[[:user1 :name "John"]
    [:user1 :key "1337"]]
   [[:user2 :name "Sudhana"]
    [:user2 :key "53"]]
   ;; [[:manager :start :game42]
   ;;  [:manager :key "42"]
   ;;  [:game42 :type "tic-tac-toe"]
   ;;  [:game42 :roles {:x :user1
   ;;                   :o :user2}]
   ;;  [:game42 :cells [:cell0 :cell1, :cell2, :cell3, :cell4, :cell5, :cell6, :cell7, :cell8]]
   ;;  [:game42 :op? [[:mark ?cell]
   ;;                 {:description "Mark a cell"
   ;;                  :request-schema {:cell :cell} 
   ;;                  :conditions [['?cell :mark :blank]
   ;;                                        control?]}
   ;;                 :no-op {:description "Does nothing"
   ;;                         :request-schema {}
   ;;                         :conditions []}]]
   ;;  [:game42 :rules [[:control? ?user] [[:game42 :control ?user]]
   ;;                   [:line ?z]         [[:row      ?m ?z]]
   ;;                   [:line ?z]         [[:column   ?m ?z]]
   ;;                   [:line ?z]         [[:diagonal ?m ?z]]
   ;;                   [:row ?m ?z]       [[?cell0 :x 0] [?cell0 :y ?m] [?cell0 :mark ?z]
   ;;                                       [?cell1 :x 1] [?cell1 :y ?m] [?cell1 :mark ?z]
   ;;                                       [?cell2 :x 2] [?cell2 :y ?m] [?cell2 :mark ?z]]
                     
   ;;                   [:row ?m ?z]       [[?cell0 :x 0] [?cell0 :y ?m] [?cell0 :mark ?z]
   ;;                                       [?cell1 :x 1] [?cell1 :y ?m] [?cell1 :mark ?z]
   ;;                                       [?cell2 :x 2] [?cell2 :y ?m] [?cell2 :mark ?z]]
   ;;                   [:diagonal ?m ?z]  [[?cell0 :x 0] [?cell0 :y 2] [?cell0 :mark ?z]
   ;;                                       [?cell1 :x 1] [?cell1 :y 1] [?cell1 :mark ?z]
   ;;                                       [?cell2 :x 2] [?cell2 :y 0] [?cell2 :mark ?z]]
   ;;                   [:diagonal ?m ?z]  [[?cell0 :x 0] [?cell0 :y 0] [?cell0 :mark ?z]
   ;;                                       [?cell1 :x 1] [?cell1 :y 1] [?cell1 :mark ?z]
   ;;                                       [?cell2 :x 2] [?cell2 :y 2] [?cell2 :mark ?z]]
   ;;                   [:open]            [(not [?cell :mark :blank])]]]
   ;;  [:game42 :goals {:x {100 [[:game42 :line :x]       [(not :game42 :line :o)]]
   ;;                       50  [[(not :game42 :line :x)] [(not :game42 :line :o)]]
   ;;                       0   [[:game42 :line :o]       [(not :game42 :line :x)]]}
   ;;                   :o {100 [[:game42 :line :o]       [(not :game42 :line :x)]]
   ;;                       50  [[(not :game42 :line :x)] [(not :game42 :line :o)]]
   ;;                       0   [[:game42 :line :x]       [(not :game42 :line :o)]]}}]

   ;;  [:game42 :terminal [[:line :x]
   ;;                      [:line :o]
   ;;                      [:open]]]
   ;;  [:game42 :control :x 0]
   ;;  [:cell0 :x 0]
   ;;  [:cell0 :y 0]
   ;;  [:cell0 :type :cell]
   ;;  [:cell0 :mark :blank]
   ;;  [:cell1 :x 0]
   ;;  [:cell1 :y 1]
   ;;  [:cell1 :type :cell]
   ;;  [:cell1 :mark :blank]
   ;;  [:cell2 :x 0]
   ;;  [:cell2 :y 2]
   ;;  [:cell2 :type :cell]
   ;;  [:cell2 :mark :blank]
   ;;  [:cell3 :x 1]
   ;;  [:cell3 :y 0]
   ;;  [:cell3 :type :cell]
   ;;  [:cell3 :mark :blank]
   ;;  [:cell4 :x 1]
   ;;  [:cell4 :y 1]
   ;;  [:cell4 :type :cell]
   ;;  [:cell4 :mark :blank]
   ;;  [:cell5 :x 1]
   ;;  [:cell5 :y 2]
   ;;  [:cell5 :type :cell]
   ;;  [:cell5 :mark :blank]
   ;;  [:cell6 :x 2]
   ;;  [:cell6 :y 0]
   ;;  [:cell6 :type :cell]
   ;;  [:cell6 :mark :blank]
   ;;  [:cell7 :x 2]
   ;;  [:cell7 :y 1]
   ;;  [:cell7 :type :cell]
   ;;  [:cell7 :mark :blank]
   ;;  [:cell8 :x 2]
   ;;  [:cell8 :y 2]
   ;;  [:cell8 :type :cell]
   ;;  [:cell8 :mark :blank]]

   ;; [[:user1 :sig "1337"]
   ;;  [:game42 :turn 0]
   ;;  [:mark :cell0 :x]]
   ;; [[:user2 :sig "53"]
   ;;  [:game42 :turn 0]
   ;;  [:no-op]]
   ;; [[:manager :sig "42"]
   ;;  [:game42 :turn 0]
   ;;  [:cell0 :mark :x]
   ;;  [:control :o 2]]
   ;; [[:user2 :sig "53"]
   ;;  [:game42 :turn 1]
   ;;  [:mark :cell3 :o]]
   ;; [[:user1 :sig "1337"]
   ;;  [:game42 :turn 1]
   ;;  [:no-op]]
   ;; [[:manager :sig "42"]
   ;;  [:game42 :turn 1]
   ;;  [:cell3 :mark :o]
   ;;  [:control :x 2]]

   ;; [[:user1 :sig "1337"]
   ;;  [:game42 :turn 2]
   ;;  [:mark :cell4 :x]]
   ;; [[:user2 :sig "53"]
   ;;  [:game42 :turn 2]
   ;;  [:no-op]]
   ;; [[:manager :sig "42"]
   ;;  [:game42 :turn 2]
   ;;  [:cell4 :mark :x]
   ;;  [:control :o 3]]
   ;; [[:user2 :sig "53"]
   ;;  [:game42 :turn 3]
   ;;  [:mark :cell8 :o]]
   ;; [[:user1 :sig "1337"]
   ;;  [:game42 :turn 3]
   ;;  [:no-op]]
   ;; [[:manager :sig "42"]
   ;;  [:game42 :turn 3]
   ;;  [:cell8 :mark :o]
   ;;  [:control :x 4]]

   ;; [[:user1 :sig "1337"]
   ;;  [:game42 :turn 4]
   ;;  [:mark :cell2 :x]]
   ;; [[:user2 :sig "53"]
   ;;  [:game42 :turn 4]
   ;;  [:no-op]]
   ;; [[:manager :sig "42"]
   ;;  [:game42 :turn 4]
   ;;  [:cell2 :mark :x]
   ;;  [:control :o 5]]
   ;; [[:user2 :sig "53"]
   ;;  [:game42 :turn 5]
   ;;  [:mark :cell1 :o]]
   ;; [[:user1 :sig "1337"]
   ;;  [:game42 :turn 5]
   ;;  [:no-op]]
   ;; [[:manager :sig "42"]
   ;;  [:game42 :turn 5]
   ;;  [:cell1 :mark :o]
   ;;  [:control :x 6]]

   ;; [[:user1 :sig "1337"]
   ;;  [:game42 :turn 6]
   ;;  [:mark :cell6 :x]]
   ;; [[:user2 :sig "53"]
   ;;  [:game42 :turn 6]
   ;;  [:no-op]]
   ;; [[:manager :sig "42"]
   ;;  [:game42 :turn 6]
   ;;  [:cell6 :mark :x]
   ;;  [:game42 :winner :x]]
   ])
                         

;; A priori knowledge is minimal
;; Quando estado provoca novas acções, como saber?

;; (fact
;;  (let [system (logic/with-base-ops webbank-system)]
;;    (->> conversation
;;         (filter (comp (p> = :user) first))
;;         (reduce #(logic/converse system %1 %2) []))
;;    => conversation))
