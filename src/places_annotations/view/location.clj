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

(defn form [options]
  (let [lat-lng (options :lat-lng)
        input-type (or (options :input-type) "hidden")
        query (options :query)]
    [:form#resultsForm { :action "/places" :method :GET }

     (case input-type

       "text" [:div.field.has-addons
               [:div.control.is-expanded.has-icons-left
                [:input.input {:type "text"
                         :name "query"
                         :autofocus "autofocus"
                         (case query "Restaurants" :placeholder :value) query}]
                 (icon 'search { :class "is-left" })]
                 [:div.control
                  [:button.button.is-info "Search"]]]

       "hidden"   [:input {:type "hidden"
                         :name "query"
                         :value query}])

     [:input#latitude {:type "hidden" :name "coord[lat]" :value (:lat lat-lng)}]
     [:input#longitude {:type "hidden" :name "coord[lng]" :value (:lng lat-lng)}]

     [:div (options :wrapper)
      [:div#beforeLocationDetermined (options :before)]
      [:div#afterLocationDetermined.is-hidden (options :after)]
      [:div#locationError.is-hidden (options :error)]
      [:div (options :content)]]]))

(defn determine []
  (common {:page/content
            [:div
             [:div.columns
              [:div.column.is-half-desktop.is-full-mobile.is-three-quarters-tablet
               [:h1.title.has-text-grey-dark { :style "margin-bottom: 2em; letter-spacing: 0.06em; font-variant: small-caps" } "Dollar Voting"]
                [:h2.subtitle.has-text-grey-dark
                 [:table
                  [:tr
                   [:td (icon 'hand-point-right { :class "is-small has-text-primary" })]
                   [:td { :style "padding: 0 2em 1em 1em;" }
                    [:span "You want to make a "]
                    [:span.has-text-black.is-italic "difference in the world"]
                    [:span "."]]]

                  [:tr
                   [:td (icon 'hand-point-right { :class "is-small has-text-primary" })]
                   [:td { :style "padding: 0 2em 1em 1em;" }
                    [:span "Start with where you "]
                    [:span.has-text-black.is-italic "open your wallet"]
                    [:span "."]]]]]

                 [:h2.subtitle.has-text-grey-darker
                  [:span.has-text-primary.is-italic.has-text-weight-semibold "Vote with Your Dollar"]
                  [:span { :style "margin-left: 2px" } " is a community-backed info bank to help you find and track businesses consistent with your values"]]

                  (form
                    { :before [:div
                               [:button.button.is-loading.is-large "Determining Location"]
                               [:div.content { :style "margin-top: 0.5em;" }
                                [:p (icon 'globe) " One moment. Determining your location."]]]

                      :after  [:button.button.is-primary.is-large "Enter &rarr;"]
                      :error [:button.button.is-danger.is-large {:disabled 'disabled } (icon 'exclamation) "&nbsp; Something went wrong"]
                      :wrapper {:style "margin-top: 2em;"}})

              ]]]}))
