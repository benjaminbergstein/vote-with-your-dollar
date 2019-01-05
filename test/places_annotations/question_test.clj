(ns places-annotations.question-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [cheshire.core :refer :all]
            [places-annotations.question :as question]))

(def first-question
  { :id 1
    :question "How easily recycled/disposed are the takeaway containers?"
    :type "scale"
    :active false
    :answers { 1 "1" 2 "2" 3 "3" 4 "4" 5 "5" } })

(def last-question
  { :id 7
    :question "Are there good vegan options?"
    :type "yn"
    :active true
    :answers { 1 "Yes" 0 "No" } })

(deftest test-scale-question
  (is (= first-question (question/by-id 1))))

(deftest test-yn-question
  (is (= last-question (question/by-id 7))))

(deftest test-active
  (is (= [4 5 6 7] (map #(% :id) question/active))))
