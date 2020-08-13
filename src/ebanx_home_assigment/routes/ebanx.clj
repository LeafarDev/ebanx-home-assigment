(ns ebanx-home-assigment.routes.ebanx
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [ebanx-home-assigment.middleware.cors :refer [cors-mw]]
            [ebanx-home-assigment.route-functions.ebanx :as ebanxrf]))

(def ebanx
  (context "/" []
    :tags ["api"]
    (POST "/reset" request
      :summary "Reset state before starting tests
                POST /reset
                200 OK"
      :middleware [cors-mw]
      (ebanxrf/reset))))