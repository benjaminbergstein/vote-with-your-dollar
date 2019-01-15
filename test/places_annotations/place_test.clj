(ns places-annotations.place-test
  (:require [clojure.test :refer :all]
            [cheshire.core :refer :all]
            [places-annotations.support.db :refer :all]
            [places-annotations.settings :as settings]
            [places-annotations.place :as place]))

(def lat-lng { :lat "37.765264" :lng "-122.446521" })

(def one-miles (place/miles=>meters 1))

(def lat-lng-str "-122.31831113896015,37.66377383783784,-122.57473086103985,37.86675416216217")
(def point (str "point:" lat-lng-str))
(def circle (str "circle:" one-miles "@" lat-lng-str))

(def expected-params { :q "lollipops"
                       :format "jsonv2"
                       :addressdetails "1"
                       :namedetails    "1"
                       :bounded        "1"
                       :limit          "25"
                       :viewbox lat-lng-str })

(def expected-base-url "https://nominatim.openstreetmap.org/search.php")

(deftest params
    (is (= expected-params (place/params lat-lng "lollipops"))))

(deftest base-url
  (is (= expected-base-url (place/base-url))))

(def sample-response
  { :body (generate-string [ "foo" "bar" "baz" ]) })

(defn fake-get []
  (fn [url options]
    (let [p (promise)]
      (is (= expected-base-url url))
      (is (= expected-params (:query-params options)))

      (future
        (Thread/sleep 10)
        (deliver p sample-response))

      p)))

(defmacro with-stubbed [& body]
  `(with-redefs
    [org.httpkit.client/get (fake-get)]

    (do ~@body)))

(deftest near
  (with-stubbed
    (is (= [ "foo" "bar" "baz" ] (place/near-by lat-lng "lollipops")))))

(def fixture
  (-> "test/places_annotations/fixtures/search.json"
      (slurp)
      (parse-string)))

(deftest create-scratch
  (with-db
    (as-> (place/create { "name" "Thailand Restaurant" }) pl
      (is (= "Thailand Restaurant"
             (->> pl (place/find-by :uuid) (:name)))))))

(deftest create-from-osm
  (with-db
    (place/create (first fixture))
    (is (= "Thailand Restaurant"
           (->> "5192196" (place/find-by :osm_id) (:name))))))
