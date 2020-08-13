(ns ebanx-home-assigment.route-functions.ebanx
  (:require [ring.util.http-response :refer :all]))

(def accounts (atom {:id 100 :balance 10}))
