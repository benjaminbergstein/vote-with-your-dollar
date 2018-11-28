(ns places-annotations.question
  (:require [clojure.java.jdbc :as jdbc]
            [cheshire.core :refer :all]
            [clojure.set :refer :all]
            [places-annotations.settings :as settings]))

(defn question [attrs]
  (rename-keys attrs { "id" :id "question" :question }))

(def all
  (->> "config/questions.yml"
      (slurp)
      (parse-string)
      (map question)))

(defn by-id [id]
  (->> all
    (filter #(= (:id %) id))
    first))
