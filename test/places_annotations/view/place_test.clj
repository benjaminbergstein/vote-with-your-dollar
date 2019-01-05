(ns places-annotations.view.place-test
  (:require [clojure.test :refer :all]
            [places-annotations.view.place :as view.place]))

(def places [{ "place_id"   "place-id"
               "namedetails" { "name" "some place" } }])

(def lat-lng { :lat 70 :lng -125 })

(deftest test-results
  (with-redefs [places-annotations.place/near-by
    (fn [ll query]
      (is (= ll lat-lng))
      (is (= "Restaurants" query))
      places)]

    (let [html (view.place/index lat-lng "Restaurants")
          matches [#"some place" #"/places/place-id/scores/new"]]
      (doseq [match matches] (is (re-find match html))))))

