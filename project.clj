(defproject ebanx-home-assigment "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [metosin/compojure-api "2.0.0-alpha30"]
                 ]
  :plugins [[lein-cljfmt "0.6.5"]
            [lein-kibit "0.1.8"]
            [lein-ring "0.12.5"]]
  :ring {:handler ebanx-home-assigment.handler/app}
  :uberjar-name "ebanx.jar"
  :profiles {
             ;; Set these in ./profiles.clj
             :test-env-vars {}
             :dev-env-vars  {}
             :test          [:test-env-vars]
             :dev           [{:dependencies   [[javax.servlet/javax.servlet-api "3.1.0"]]
                              :resource-paths ["resources"]}
                             :dev-env-vars]
             })
