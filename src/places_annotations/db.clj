(ns places-annotations.db
  (:require [clj-postgresql.core :as pg]
            [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]
            [places-annotations.settings :as settings]))

(defn setup [app-env] (pg/pool :dbname   (str "places_annotations_" app-env)
                               :host     settings/db-host
                               :user     settings/db-user
                               :port     settings/db-port
                               :password settings/db-pass ))

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(def conn (setup settings/app-env))
