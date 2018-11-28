(ns places-annotations.core-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [places-annotations.core :refer :all]))

(deftest root
  (with-redefs [places-annotations.place/near-by
    (fn [lat-lng]
      (is (= { :lat 37.7709925 :lng -122.43769239999999 }))
      [{"id" "foo-bar" "name" "DragonEats"}])]

    (let [req (mock/request :get "/places?coord%5Blat%5D=37.7709925&coord%5B")
          res (app req)]

      (is (re-find #".*DragonEats.*" (:body res))))))

(deftest submit-score
  (with-redefs [places-annotations.score/submit
    (fn [score]
      (is (= score { :id "123" :value "1" })))]

    (let [req (-> :post
                (mock/request "/scores")
                (mock/body "score[id]=123&score[value]=1")
                (mock/content-type "application/x-www-form-urlencoded"))

          res (app req)]

      (is (= 302 (:status res))
      (is (= "/" (get-in res [:headers "Location"])))))))

(defn score [value]
  { :value value
    :question { :question "Foo" } })

(deftest index-place-scores
  (with-redefs [places-annotations.score/for-place
                (fn [id]
                  (is (= "123" id))
                  (map score [1 2 3]))]
    (let [req (mock/request :get "/places/123/scores?name=Foo%20Bar")
          res (app req)]

      (is (re-find #"1" (:body res)))
      (is (re-find #"2" (:body res)))
      (is (re-find #"3" (:body res)))
      (is (re-find #"Foo Bar" (:body res)))

      )))
