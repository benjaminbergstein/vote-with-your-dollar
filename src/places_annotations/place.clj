(ns places-annotations.place
  (:require [org.httpkit.client :as http]
            [cheshire.core :refer :all]))

(defn miles=>meters [miles] (* miles 1609.344))

(def api-key (System/getenv "GOOGLE_API_KEY"))
(def fields (str "formatted_address,geometry,name"))

(defn base-url [path format]
  (str "https://maps.googleapis.com/maps/api/place/" path "/" format))

(defn lat-lng=>str [lat-lng]
  (str (:lat lat-lng) "," (:lng lat-lng)))

(defn lat-lng=>point [lat-lng]
  (str "point:" (lat-lng=>str lat-lng)))

(defn lat-lng=>circle [lat-lng meters]
  (str "circle:" meters "@" (lat-lng=>str lat-lng)))

(defn params [lat-lng]
  { :query 'restaurants
    :fields fields
    :key api-key
    :location (lat-lng=>str lat-lng)
    :radius (miles=>meters 1) })

(defn =>url [place]
  (str "https://www.google.com/maps/search/?api=1&query=" (get place "name") "&query_place_id=" (get place "id")))

(defn near-by [lat-lng]
  (let [options { :query-params (params lat-lng) }
        res @(http/get (base-url "textsearch" "json") options)]

    (-> res
      (:body)
      (parse-string)
      (get "results"))))

