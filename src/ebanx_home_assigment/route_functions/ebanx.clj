(ns ebanx-home-assigment.route-functions.ebanx
  (:require [ring.util.http-response :refer :all]
            [ebanx-home-assigment.defs :refer [accounts]]))

(defn reset []
  (reset! accounts [{:id 100 :balance 10}])
  (ok "OK"))

(defn balance [account-id]
  (let [account (first (filter #(= (bigint account-id) (:id %)) @accounts))]
    (if (nil? account)
      (not-found! "0")
      (ok (str (:balance account))))))

