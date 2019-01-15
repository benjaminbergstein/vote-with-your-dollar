(ns places-annotations.place
  (:require [org.httpkit.client :as http]
            [places-annotations.settings :as settings]
            [haversine.core :as h]
            [clj-postgresql.core :as pg]
            [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]
            [places-annotations.question :as question]
            [places-annotations.db :as db]
            [cheshire.core :refer :all])
  (:use [clojure.set :only (rename-keys)]))

(defn name [place] (get-in place ["namedetails" "name"]))
(defn address [place]
   (str
     (if (not (nil? (get-in place ["address" "house_number"]))) (str (get-in place ["address" "house_number"]) " "))
     (if (not (nil? (get-in place ["address" "road"]))) (str (get-in place ["address" "road"]) ", "))
     (get-in place ["address" "city"]) ", "
     (get-in place ["address" "state"]) " "
     (get-in place ["address" "postcode"])))

(defn id [place] (get place "place_id"))

(defn viewbox=>str [box]
  (str (box :maxlongitude) "," (box :minlatitude) "," (box :minlongitude) "," (box :maxlatitude)))

(defn miles=>meters [miles] (* miles 1609.344))

(defn ensure-numeric [map key]
  (assoc map key (bigdec (map key))))

(defn lat-lng=>viewbox [lat-lng miles]
  (-> lat-lng
      (ensure-numeric :lat)
      (ensure-numeric :lng)
      (rename-keys { :lat :latitude :lng :longitude })
      (assoc :distance-from (/ (miles=>meters miles) 1000))
      (h/neighborhood)))

(def fields (str "formatted_address,geometry,name,types"))

(defn base-url []
  "https://nominatim.openstreetmap.org/search.php")

(defn lat-lng=>str [lat-lng]
  (str (:lat lat-lng) "," (:lng lat-lng)))

(defn params [lat-lng query] { :q              query
                               :format         "jsonv2"
                               :addressdetails "1"
                               :namedetails    "1"
                               :bounded        "1"
                               :limit          "25"
                               :viewbox        (-> lat-lng
                                                   (lat-lng=>viewbox 7)
                                                   (viewbox=>str)) })

(defn =>url [place]
  (str "https://www.google.com/maps/search/" (name place)))

(defn near-by [lat-lng query]
  (as-> { :query-params (params lat-lng query) } options
    (-> @(http/get (base-url) options) (:body)
      (doto (->> (spit "./tmp/last_response.json")))
      (parse-string))))

(defn attrs=>insert [attrs] { :osm_id   (attrs "place_id")
                              :name     (or (attrs "name") (name attrs)) })

(defn create [attrs]
  (doto (db/uuid) (as-> uuid
    (-> attrs (as-> attrs
      (attrs=>insert attrs)
      (assoc attrs :uuid uuid)
      (jdbc/insert! db/conn "places" attrs))))))

(defn place [attrs] attrs)

(defn find-by [col value]
  (as-> (case col :id "id" :osm_id "osm_id" :uuid "uuid") col
    (as-> (str "SELECT * FROM places WHERE " col "= ?") sql
      (-> db/conn (jdbc/query [sql value] { :row-fn place }) (first)))))
