(ns places-annotations.lat-lng-test
  (:require [haversine.core :as h]
            [clojure.test :refer :all]))


(defn dbug [box]
  (str (box :maxlongitude) "," (box :minlatitude) "," (box :minlongitude) "," (box :maxlatitude)))

(deftest root
  (-> { :latitude 37.765264 :longitude -122.446521 }
      (assoc :distance-from 5)
      (h/neighborhood)
      (dbug)
      (println)))

