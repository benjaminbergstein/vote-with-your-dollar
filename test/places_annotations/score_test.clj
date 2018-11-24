(ns places-annotations.score-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [places-annotations.score :as score]))

;;(def score [id value] { :id "some-id" :value "5" })
(defn score [id value] { :id id :value value })

(def db (score/setup "test"))

(deftest test-submit-score
  (with-redefs [places-annotations.score/conn db]
    (jdbc/execute! db ["TRUNCATE scores"])
    (score/submit (score "some-id" "5"))
    (is (= 1 (score/total)))))

(deftest test-for-place
  (with-redefs [places-annotations.score/conn db]
    (jdbc/execute! db ["TRUNCATE scores"])
    (score/submit (score "some-id" "5"))
    (score/submit (score "some-id" "4"))
    (score/submit (score "other-id" "3"))

    (is (= [5 4] (score/for-place "some-id")))))
