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
             [:section.hero
              [:div.hero-body
               [:div.columns
                [:div.column.is-half-desktop.is-three-quarters-mobile.is-three-quarters-tablet
                 [:h1.title.has-text-grey-dark { :style "margin-bottom: 2em;" } "Vote with Your Dollar"]
                 [:h2.subtitle.has-text-grey
                  (icon 'hand-point-right { :class "is-small has-text-primary" })
                  [:span { :style "margin-left: 0.75em" } "You want to make a "]
                  [:span.has-text-black.is-italic "difference in the world"]
                  [:span "."]]
                [:h2.subtitle.has-text-grey-dark
                  (icon 'hand-point-right { :class "is-small has-text-primary" })
                  [:span { :style "margin-left: 0.75em" } "Start with where you "]
                  [:span.has-text-black.is-italic "open your wallet."]]
                 [:h2.subtitle.has-text-grey-darker
                  [:span.has-text-primary.is-italic.has-text-weight-semibold "Vote with Your Dollar"]
                  [:span { :style "margin-left: 2px" } " is a community-backed info bank to help you find and track businesses consistent with your values"]]]]

               [:form#resultsForm { :action "/places" :method :GET }
                [:input {:type "hidden" :name "query" :value "Restaurants"}]
                [:input#latitude {:type "hidden" :name "coord[lat]"}]
                [:input#longitude {:type "hidden" :name "coord[lng]"}]

                [:div { :style "margin-top: 2em;" }
                  [:div#beforeLocationDetermined
                   [:button.button.is-loading.is-large "Determining Location"]
                   [:div.content { :style "margin-top: 0.5em;" }
                    [:p
                     (icon 'globe)
                      " One moment. Determining your location."]]]

                  [:div#afterLocationDetermined.is-hidden
                   [:button.button.is-primary.is-large "Get Started &rarr;"]]]]

              ]]]}))
