(ns graphel.logic-test
  (:require [midje.sweet :refer :all]
            [graphel.utils :refer :all]
            [graphel.logic :refer :all]))

(facts "on state"
       (let [system {:states {:foo (fn [db] (find-first (comp (p> = :bar) second) db))}}]
         (state system []) => :initial
         (state system [[irrelevant :bar]]) => :foo))

(facts "on operations"
       (let [system {:conditions {:foo [:bar :baz]}}]
         (operations system :foo) => [:bar :baz]
         (operations system :bar) => nil))

(facts "on with-base-ops"
       (with-base-ops {:conditions {:foo [:bar]}
                       :operations {}})
       => (contains {:conditions {:foo [:bar :op?]}
                     :operations (contains {:op? irrelevant})}))

(facts "on get-identity"
       (get-identity {:identity ..x..}) => ..x..
       (get-identity {}) => nil)

(facts "on get-operation"
       (let [system {:operations {:foo ..foo..}}]
         (get-operation system :foo) => ..foo..
         (get-operation system :bar) => nil))

(facts "on respond"
       (respond ..system.. ..db.. [..e.. ..a.. ..v.. ..t..])
       => [..identity.. ..a.. ..results.. ..t..]
       (provided
        (get-identity ..system..) => ..identity..
        (get-operation ..system.. ..a..) => #'..operation..
        (..operation.. ..system.. ..db.. ..v..) => ..results..))

(facts "on should-run"
       (should-run [] irrelevant) => true
       (tabular (facts (should-run [irrelevant [irrelevant irrelevant irrelevant ?last]] [irrelevant irrelevant irrelevant ?new]) => ?res)
                ?last ?new ?res
                42    43   truthy
                42    42   falsey
                42    41   falsey))

(facts "on converse"
       (converse ..system.. [..datum1.. ..datum2..] [..e.. ..a.. ..v.. ..t..])
       => [..datum1..  ..datum2.. [..e.. ..a.. ..v.. ..t..] ..response-datum..]
       (provided
        (should-run [..datum1.. ..datum2..] [..e.. ..a.. ..v.. ..t..]) => true
        (respond ..system.. [..datum1.. ..datum2..] [..e.. ..a.. ..v.. ..t..]) => ..response-datum..))
