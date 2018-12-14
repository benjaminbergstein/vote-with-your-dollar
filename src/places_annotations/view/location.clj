(ns places-annotations.view.location
  (:require [hiccup.core :as h]
            [hiccup.page :as ph]
            [hiccup.element :as elem]
            [cheshire.core :refer :all]
            [places-annotations.place :as place]
            [places-annotations.question :as question]
            [places-annotations.score :as score])
  (:use [hiccup.def]
        [places-annotations.view.shared]
        [ring.util.anti-forgery]))

(defn determine []
  (common {:page/content
            [:div
              (title "Welcome to Vote with Your Dollar")
              [:p
                (icon 'globe)
                " One moment. Determining your location."]

              [:form#resultsForm { :action "/places" :method :GET }
                [:input {:type "hidden" :name "query" :value "Restaurants"}]
                [:input#latitude {:type "hidden" :name "coord[lat]"}]
                [:input#longitude {:type "hidden" :name "coord[lng]"}]

              ]]}))
