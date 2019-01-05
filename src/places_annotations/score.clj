(ns places-annotations.score
  (:require [clj-postgresql.core :as pg]
            [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]
            [places-annotations.question :as question]
            [places-annotations.settings :as settings]))

(defn setup [app-env] (pg/pool :dbname   (str "places_annotations_" app-env)
                               :host     settings/db-host
                               :user     settings/db-user
                               :port     settings/db-port
                               :password settings/db-pass ))

(def conn (setup settings/app-env))

(defn total []
  (:count (jdbc/query conn ["SELECT COUNT(id) FROM scores"] { :result-set-fn first })))


(defn str=>int [str]
  (Integer/parseInt str))

(defn- params=>insert [params]
  { :id           (:id params)
    :question_id  (str=>int (:question_id params))
    :value        (str=>int (:value params)) })

(defn submit [score]
  (jdbc/insert! conn :scores (params=>insert score)))

(defn score [attrs]
  (-> attrs
      (assoc :question (question/by-id (:question_id attrs)))))

(defn for-place [id]
  (let [active-ids (map #(% :id) question/active)
        sql (str "SELECT value, question_id FROM scores WHERE id = '" id "' AND question_id IN (" (string/join ", " active-ids) ")")]
    (jdbc/query conn sql { :row-fn score })))
