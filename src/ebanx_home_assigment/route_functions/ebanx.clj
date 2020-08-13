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

(defn- decrease-account-balance
  [account-id amount]
  (reset! accounts
          (conj (filter #(not= account-id (:id %)) @accounts)
                {:id account-id :balance (- (:balance (find-account (bigint account-id))) amount)})))

(defn- increase-account-balance
  [account-id amount]
  (reset! accounts (conj (filter #(not= account-id (:id %)) @accounts)
                         {:id account-id :balance (+ amount (:balance (find-account (bigint account-id))))})))

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
      (do (increase-account-balance account-id amount)
          (created "" {:destination (find-account account-id)})))))

(defn- withdraw
  "Withdraw from non-existing accoun
  POST /event {\"type\":\"withdraw\", \"origin\":\"200\", \"amount\":10}
  404 0

  Withdraw from existing account
  POST /event {\" type \":\" withdraw \", \" origin \":\" 100 \", \" amount \":5}
  201 {\" origin \": {\" id \":\" 100 \", \" balance \":15}}"
  [data]
  (let [account-id (bigint (:origin data))
        account (find-account account-id)
        amount (bigint (:amount data))]
    (if (nil? account)
      (not-found! "0")
      (do (decrease-account-balance account-id amount)
          (created "" {:destination (find-account account-id)})))))

(defn- transfer
  "Transfer from existing account
  POST /event {\"type\":\"transfer\", \"origin\":\"100\", \"amount\":15, \"destination\":\"300\"}
  201 {\"origin\": {\"id\":\"100\", \"balance\":0}, \"destination\": {\"id\":\"300\", \"balance\":15}}

  Transfer from non-existing account
  POST /event {\"type\":\"transfer\", \"origin\":\"200\", \"amount\":15, \"destination\":\"300\"}
  404 0"
  [data]
  (let [origin-account-id (bigint (:origin data))
        origin-account (find-account origin-account-id)
        destination-account-id (bigint (:destination data))
        destination-account (find-account destination-account-id)
        amount (bigint (:amount data))]
    (if (nil? origin-account)
      (not-found! "0")
      (do (decrease-account-balance origin-account-id amount)
          (if (nil? destination-account)
            (reset! accounts (conj @accounts {:id destination-account-id :balance amount}))
            (increase-account-balance destination-account-id amount))
          (created "" {:origin      (find-account origin-account-id)
                       :destination (find-account destination-account-id)})))))

(defn event
  [request]
  (let [data (:body-params request)
        type (:type data)]
    (cond (= type "deposit")
          (deposit data)
          (= type "withdraw")
          (withdraw data)
          (= type "transfer")
          (transfer data)
          :else
          (bad-request "invalid type"))))

