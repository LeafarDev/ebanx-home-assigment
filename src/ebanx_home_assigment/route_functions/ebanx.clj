(ns ebanx-home-assigment.route-functions.ebanx
  (:require [ring.util.http-response :refer :all]
            [ebanx-home-assigment.defs :refer [accounts]]))

(defn reset []
  (reset! accounts [])
  (ok "OK"))

(defn- find-account
  [account-id]
  (first (filter #(= account-id (:id %)) @accounts)))

(defn balance [account-id]
  (let [account (find-account (bigint account-id))]
    (if (nil? account)
      (not-found! "0")
      (ok (str (:balance account))))))

(defn- deposit
  "Create account with initial balance
  POST /event {\"type\":\"deposit\", \"destination\":\"100\", \"amount\":10}
  201 {\"destination\": {\"id\":\"100\", \"balance\":10}}

  Deposit into existing account
  POST /event {\"type\":\"deposit\", \"destination\":\"100\", \"amount\":10}
  201 {\"destination\": {\"id\":\"100\", \"balance\":20}}"
  [data]
  (let [account-id (bigint (:destination data))
        account (find-account account-id)
        amount (bigint (:amount data))]
    (if (nil? account)
      (do (reset! accounts (conj @accounts {:id account-id :balance amount}))
          (created "" {:destination (find-account account-id)}))

      (do (reset! accounts
                  (conj (filter #(not= account-id (:id %)) @accounts)
                        {:id account-id :balance (+ amount (:balance account))}))
          (created "" {:destination (find-account account-id)})))))

(defn- withdraw
  "Withdraw from existing account
  POST /event {\" type \":\" withdraw \", \" origin \":\" 100 \", \" amount \":5}
  201 {\" origin \": {\" id \":\" 100 \", \" balance \":15}}"
  [data]
  (let [account-id (bigint (:origin data))
        account (find-account account-id)
        amount (bigint (:amount data))]
    (do (reset! accounts
                (conj (filter #(not= account-id (:id %)) @accounts)
                      {:id account-id :balance (- (:balance account) amount)}))
        (created "" {:destination (find-account account-id)}))))

(defn event
  [request]
  (let [data (:body-params request)
        type (:type data)]
    (cond (= type "deposit")
          (deposit data)
          (= type "withdraw")
          (withdraw data)
          :else
          (bad-request "invalid type"))))

