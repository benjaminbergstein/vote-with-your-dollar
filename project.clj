(defproject places-annotations "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.1.0"]
                 [ring-json-params "0.1.3"]
                 [compojure "1.1.0"]
                 [hiccup "1.0.0"]
                 [cheshire "2.0.4"]
                 [http-kit "2.3.0"] 
                 [ring/ring-mock "0.3.2"] ]
  :profiles {:dev {:plugins [[lein-ring "0.7.1"]]
                    :ring {:handler places-annotations.core/app}}})
