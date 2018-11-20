(ns places-annotations.view
  (:require [hiccup.core :as h]
            [hiccup.page :as ph]
            [hiccup.element :as elem]
            [cheshire.core :refer :all]
            [places-annotations.place :as place])
  (:use hiccup.def))

(defn common [page]
  (ph/html5
    [:head [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
     [:title "Vote with your dollar"]
     (ph/include-css "/css/my.css")]
    [:body  (page :page/content )]
    (ph/include-js "/js/my.js")))

(defn results [lat-lng]
  (for [result (place/near-by lat-lng)]
    [:p (get result "name")]))

(defn index [lat-lng]
  (common {:page/content
            [:div
              [:h1 "Results"]
              (results lat-lng)]}))

(defn redirect []
  (common {:page/content
           [:form#resultsForm { :action "/results" :method :GET }
             [:input#latitude {:type "hidden" :name "coord[lat]"}]
             [:input#longitude {:type "hidden" :name "coord[lng]"}]]}))
