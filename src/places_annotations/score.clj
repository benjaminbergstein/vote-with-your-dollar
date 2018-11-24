(ns places-annotations.score
  (:require [clojure.java.jdbc :as jdbc]
            [places-annotations.settings :as settings]))

(defn setup [app-env] { :dbtype   "postgres"
                        :dbname   (str "places_annotations_" app-env)
                        :host     "localhost"
                        :user     "ben"
                        :port     65432
                        :password "" })

(def conn (setup settings/app-env))

(defn total []
  (:count (jdbc/query conn ["SELECT COUNT(id) FROM scores"] {:result-set-fn first})))


(defn- params=>insert [params]
  { :id    (:id params)
    :value (Integer/parseInt (:value params)) })

(defn submit [score]
  (jdbc/insert! conn :scores (params=>insert score)))


(defn for-place [id]
  (jdbc/query conn ["SELECT value FROM scores WHERE id = ?" id] { :row-fn (partial :value) }))
