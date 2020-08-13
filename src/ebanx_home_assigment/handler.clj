(ns ebanx-home-assigment.handler
  (:require [compojure.api.sweet :refer :all]
            [ebanx-home-assigment.routes.ebanx :refer :all]))

(def app
  (->
   (api
    {:swagger
     {:ui   "/"
      :spec "/swagger.json"
      :data {:info {:title       "ebanx-home-assigment"
                    :description " ebanx home assigment"}
             :tags [{:name "api", :description ""}]}}}
    ebanx)))

