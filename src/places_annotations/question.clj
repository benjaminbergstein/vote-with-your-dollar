(ns places-annotations.question
  (:require [clojure.java.jdbc :as jdbc]
            [cheshire.core :refer :all]
            [clojure.set :refer :all]
            [places-annotations.settings :as settings]))

(defn question [attrs]
  (-> attrs
      (rename-keys { "id" :id "question" :question "type" :type "active" :active })
      (assoc :answers
         (case (attrs "type")
           "scale" { 1 "1" 2 "2" 3 "3" 4 "4" 5 "5"}
           "yn" { 1 "Yes" 0 "No" }))))

(def all
  (->> "config/questions.yml"
      (slurp)
      (parse-string)
      (map question)))

(defn filter-by [by-fn] (filter by-fn all))

(def active (filter-by #(% :active)))

(defn by-id [id]
  (first (filter-by #(= (:id %) id))))
