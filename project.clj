(defproject places-annotations "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring "1.7.1"]
                 [ring-json-params "0.1.3"]
                 [compojure "1.1.0"]
                 [hiccup "1.0.5"]
                 [cheshire "5.8.1"]
                 [io.forward/yaml "1.0.9"]
                 [http-kit "2.3.0"]
                 [org.postgresql/postgresql "42.2.5"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [ring/ring-mock "0.3.2"]]
  :profiles {:dev {:plugins [[lein-ring "0.12.4"]]
                    :ring {:handler places-annotations.core/app}}}
  :ring {:handler places-annotations.core/app})

