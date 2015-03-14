(ns tic-tac-toe.logic)


(def state (atom {}))

(def system
  {:identity :game-manager
   :states {:game-not-started
            (fn [db] (and (find-first (comp (p> = :log-in) second) db)
                         (not (find-first (comp (p> = :log-out) second) db))))
            :game-started
            (fn [db] (-> state deref (get-in [players])))}
   :operations {:mark {:description "Marks a square"
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
   :conditions {:initial   [:log-in]
                :logged-in [:show-transactions :log-out]}})
