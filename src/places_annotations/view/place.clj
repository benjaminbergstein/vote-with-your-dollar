(ns places-annotations.view.place
  (:require [hiccup.core :as h]
            [hiccup.page :as ph]
            [hiccup.element :as elem]
            [cheshire.core :refer :all]
            [places-annotations.place :as place]
            [places-annotations.question :as question]
            [places-annotations.view.location :as view.location]
            [places-annotations.score :as score])
  (:use [hiccup.def]
        [places-annotations.view.shared]
        [ring.util.anti-forgery]))

(defn split-at' [idx v]
      [(subvec v 0 idx) (subvec v idx)])

(defn link [result text]
  [:a {:href (str "/places/" (place/id result) "/scores?name=" (place/name result)) :class "card-footer-item"} text])

(defn- near=>table [lat-lng query]
  (for [row (partition 3 3 nil (place/near-by lat-lng query))]
   [:div.columns
    (for [result row]
     [:div.column.is-full-mobile.is-one-third-desktop.is-full-mobile.is-half-tablet
      [:div.card
       [:header.card-header
        [:div.card-header-title
         [:p (place/name result)]]]
       [:div.card-content.content { :style "min-height: 6em;" }
         [:p
          [:span.tag (get result "category")]
          [:span "&nbsp"]
          [:span.tag (get result "type")]]
         [:p (place/address result)]]
       [:footer.card-footer
         (let [c (count (score/for-place (place/id result)))]
          (cond
           (= c 1) (link result "1 score")
           (> c 1) (link result (str c " scores"))
           (= c 0) [:span {:class "card-footer-item"} "no scores"]))
         [:a {:href (str "/places/" (place/id result) "/scores/new?name=" (place/name result)) :class "card-footer-item"} "add score"]
         [:a {:href (place/=>url result) :target "_BLANK" :class "card-footer-item"} [:span "info &nbsp;"] (icon "external-link-alt" {:class "is-small"})]

         ]]])]))

(defn index [lat-lng query]
  (common {:page/content
            [:div
              (title "Places Nearby")

              (view.location/form { :query query
                                    :input-type "text"
                                    :lat-lng lat-lng })

              (near=>table lat-lng query)]}))

