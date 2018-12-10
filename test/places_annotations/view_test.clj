(ns places-annotations.view-test
  (:require [clojure.test :refer :all]
            [places-annotations.view :as view]))

(def places [{ "id"   "place-id"
               "name" "some place" }])

(def lat-lng { :lat 70 :lng -125 })

(deftest test-results
  (with-redefs [places-annotations.place/near-by
    (fn [ll query]
      (is (= ll lat-lng))
      (is (= "Restaurants" query))
      places)]

    (let [html (view/places lat-lng "Restaurants")
          matches [#"some place" #"/places/place-id/scores/new"]]
      (doseq [match matches] (is (re-find match html))))))

