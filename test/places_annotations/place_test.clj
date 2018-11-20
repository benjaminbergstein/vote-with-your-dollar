(ns places-annotations.place-test
  (:require [clojure.test :refer :all]
            [cheshire.core :refer :all]
            [places-annotations.place :as place]))

(def lat-lng { :lat 37.770594 :lng -122.435470 })

(def ten-miles (place/miles=>meters 10))

(def lat-lng-str (place/lat-lng=>str lat-lng))
(def point (str "point:" lat-lng-str))
(def circle (str "circle:" ten-miles "@" lat-lng-str))

(def expected-params
  { :query 'restaurants
    :fields place/fields
    :key place/api-key
    :location lat-lng-str
    :radius ten-miles })

(def expected-base-url "https://maps.googleapis.com/maps/api/place/textsearch/json")

(deftest lat-lng=>point
  (is (= point (place/lat-lng=>point lat-lng))))

(deftest lat-lng=>circle
  (is (= circle (place/lat-lng=>circle lat-lng ten-miles))))

(deftest params
    (is (= expected-params (place/params lat-lng))))

(deftest base-url
  (is (= expected-base-url (place/base-url "textsearch" "json"))))

(def sample-response
  { :body (generate-string { "results" [ "foo" "bar" "baz" ] }) })

(deftest near
  (with-redefs
    [org.httpkit.client/get (fn [url options]
      (let [p (promise)]
        (is (= expected-base-url url))
        (is (= expected-params (:query-params options)))

        (future
          (Thread/sleep 10)
          (deliver p sample-response))

        p))]

    (is (= [ "foo" "bar" "baz" ] (place/near-by lat-lng)))))

