(ns places-annotations.view
  (:require [hiccup.core :as h]
            [hiccup.page :as ph]
            [hiccup.element :as elem]
            [cheshire.core :refer :all]
            [places-annotations.place :as place]
            [places-annotations.score :as score])
  (:use hiccup.def))

(defn title [text] [:h1.title text])

(defn common [page]
  (ph/html5
    [:head [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
     [:title "Vote with your dollar"]
     (ph/include-css "/css/my.css")
     (ph/include-css "/css/bulma.css")]
    [:body [:div.container { :style "margin-top: 3em;" }(page :page/content )]]
    (ph/include-js "/js/my.js")))

(defn places-near=>table [lat-lng]
  (common {:page/content
    [:table.table.is-fullwidth
     [:thead
      [:tr [:th {:colspan 3} "Places Nearby"]]]
      (for [result (place/near-by lat-lng)]
        [:tr
          [:td
            [:a {:href (place/=>url result) :target "_BLANK"} (get result "name")]]

          [:td
            [:a {:href (str "/places/" (get result "id") "/scores/new?name=" (get result "name"))} "add score"]]

          [:td
            [:a {:href (str "/places/" (get result "id") "/scores")} "see scores"]]]

        )]}))

(defn places [lat-lng]
  (common {:page/content
            [:div
              (title "Vote with your dollar")
              (places-near=>table lat-lng)]}))

(defn new-score [id name]
  (common {:page/content
    [:div
      (title (str "Score \"" name "\""))

      [:div.content
        [:p "Does this place ask if you want cutlery when ordering to go?"]]

      [:form { :action (str "/scores") :method :POST }
        [:input {:name "score[id]" :type "hidden" :value id}]

        (for [i [1 2 3 4 5]]
          [:span
            [:button.button {:name "score[value]" :value i} i]
            [:span " "]])

      ]]}))

(defn scores-for-place [id]
  (common {:page/content
    [:div
      (for [score (score/for-place id)]
        [:div score])

    ]}))

(defn redirect []
  (common {:page/content
            [:div
              (title "One moment... determining your location....")
              [:form#resultsForm { :action "/places" :method :GET }
                [:input#latitude {:type "hidden" :name "coord[lat]"}]
                [:input#longitude {:type "hidden" :name "coord[lng]"}]

              ]]}))

