(ns places-annotations.view.place
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

(defn- places-near=>table [lat-lng query]
  [:table.table.is-fullwidth
    [:thead
      [:tr [:th {:colspan 4} "Places Nearby"]]]
      (for [result (place/near-by lat-lng query)]
        [:tr
          [:td
           [:a {:href (place/=>url result) :target "_BLANK"}
            [:p (str (get result "name"))]]
           [:p (get result "formatted_address")]]

          [:td
           [:a {:href (str "/places/" (get result "id") "/scores/new?name=" (get result "name"))} "add score"]]

          [:td
           [:a {:href (str "/places/" (get result "id") "/scores?name=" (get result "name"))} "see scores"]]

          [:td
           (count (score/for-place (get result "id")))]]

      )])

(defn- search-field [query]
    [:input.input { :type "text"
                    :name "query"
                    (case query "Restaurants" :placeholder :value) query
                    :autofocus "autofocus"}])

(defn index [lat-lng query]
  (common {:page/content
            [:div

              (title "Places Nearby")


              [:form { :action "/places" :method :GET }
                [:div.field.has-addons
                  [:div.control.is-expanded.has-icons-left
                    (search-field query)
                    (icon 'search { :class "is-left" })]
                  [:div.control
                    [:button.button.is-info "Search"]]]
                [:input#latitude {:type "hidden" :name "coord[lat]" :value (:lat lat-lng)}]
                [:input#longitude {:type "hidden" :name "coord[lng]" :value (:lng lat-lng)}]]

              (places-near=>table lat-lng query)]}))

