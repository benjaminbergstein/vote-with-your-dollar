(ns places-annotations.score-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [places-annotations.score :as score]))

;;(def score [id value] { :id "some-id" :value "5" })
(defn score [id value question_id] { :id id :value value :question_id question_id })

(def db (score/setup "test"))

(deftest test-submit-score
  (with-redefs [places-annotations.score/conn db]
    (jdbc/execute! db ["TRUNCATE scores"])
    (score/submit (score "some-id" "5" "1"))
    (is (= 1 (score/total)))

  ))

(deftest test-for-place
  (with-redefs [places-annotations.score/conn db]
    (jdbc/execute! db ["TRUNCATE scores"])
    (score/submit (score "some-id" "5" "1"))
    (score/submit (score "some-id" "4" "2"))
    (score/submit (score "other-id" "3" "3"))

    (let [scores (score/for-place "some-id")]
      (is (= 2 (count scores)))

      (is (= 1 (:question_id (nth scores 0))))
      (is (= 5 (:value (nth scores 0))))

      (is (= 2 (:question_id (nth scores 1))))
      (is (= 4 (:value (nth scores 1))))

    )))
