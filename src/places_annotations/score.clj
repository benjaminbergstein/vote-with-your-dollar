(ns places-annotations.score
  (:require [clj-postgresql.core :as pg]
            [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]
            [places-annotations.question :as question]
            [places-annotations.db :as db]))

(defn total []
  (:count (jdbc/query db/conn ["SELECT COUNT(id) FROM scores"] { :result-set-fn first })))

(defn str=>int [str]
  (Integer/parseInt str))

(defn- params=>insert [params]
  { :id           (:id params)
    :question_id  (str=>int (:question_id params))
    :value        (str=>int (:value params)) })

(defn submit [score]
  (jdbc/insert! db/conn :scores (params=>insert score)))

(defn score [attrs]
  (-> attrs
      (assoc :question (question/by-id (:question_id attrs)))))

(defn for-place [id]
  (let [active-ids (map #(% :id) question/active)
        sql (str "SELECT value, question_id FROM scores WHERE id = '" id "' AND question_id IN (" (string/join ", " active-ids) ")")]
    (jdbc/query db/conn sql { :row-fn score })))
