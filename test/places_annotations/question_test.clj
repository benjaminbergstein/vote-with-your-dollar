(ns places-annotations.question-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [cheshire.core :refer :all]
            [places-annotations.question :as question]))

(def first-question
  { :id 1
    :question "How easily recycled/disposed are the takeaway containers?" })

(deftest test-question
  (is (= first-question (question/by-id 1))))

