(ns places-annotations.view
  (:require [hiccup.core :as h]
            [hiccup.page :as ph]
            [hiccup.element :as elem]
            [cheshire.core :refer :all]
            [places-annotations.place :as place]
            [places-annotations.question :as question]
            [places-annotations.score :as score])
  (:use [hiccup.def]
        [ring.util.anti-forgery]))

(defn title [text] [:h1.title text])

(defn icon
  ([type] (icon type {}))
  ([type options]
    [:span { :class (str "icon " (:class options)) }
      [:i { :class (str "fas fa-" type) }]]))

(defn common [page]
    (ph/html5
      [:head [:meta {:charset "utf-8"}]
       [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
       [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
       [:title "Vote with your dollar"]
       (ph/include-css "/css/my.css")
       (ph/include-css "/css/bulma.css")
       (ph/include-css "/css/fontawesome.css")]

      [:body
       [:nav.navbar
        [:div.container
         [:div.navbar-brand
          [:a.navbar-item.has-text-primary { :href "/" }
           (icon 'vote-yea) [:span " &nbsp;WYR"] (icon 'dollar-sign)]]]]

        [:div.section
         [:div.container
          (page :page/content)]]

        (ph/include-js "/js/my.js")]))

(defn places-near=>table [lat-lng query]
  [:table.table.is-fullwidth
    [:thead
      [:tr [:th {:colspan 3} "Places Nearby"]]]
      (for [result (place/near-by lat-lng query)]
        [:tr
          [:td
            [:a {:href (place/=>url result) :target "_BLANK"}
              [:p (str (get result "name"))]]
            [:p (get result "formatted_address")]]

          [:td
            [:a {:href (str "/places/" (get result "id") "/scores/new?name=" (get result "name"))} "add score"]]

          [:td
            [:a {:href (str "/places/" (get result "id") "/scores?name=" (get result "name"))} "see scores"]]]

      )])

(defn search-field [query]
    [:input.input { :type "text"
                    :name "query"
                    (case query "Restaurants" :placeholder :value) query
                    :autofocus "autofocus"}])

(defn places [lat-lng query]
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

(def button-class-map { 1 ""
                        2 "is-light"
                        3 "has-text-weight-semibold"
                        4 "is-light"
                        5 ""  })

(defn new-score [id name]
  (common {:page/content
    [:div
      (title (str "Score \"" name "\""))

      [:div.columns
        [:div.column.is-four-fifths
          (for [question question/all]
            [:div.card { :style "margin-bottom: 1em;" }
              [:div.card-content
                [:div.content
                  [:p (:question question)]]

                [:form { :action (str "/scores") :method :POST }
                  (anti-forgery-field)
                  [:input {:name "score[id]" :type "hidden" :value id}]
                  [:input {:name "score[question_id]" :type "hidden" :value (:id question)}]

                  [:div.columns
                    (for [i [1 2 3 4 5]]
                      [:div.column.is-one-fifth
                        [:button.button.is-fullwidth { :class (get button-class-map i) :name "score[value]" :value i} i]]

                  )]]]
            ])]]]}))

(defn scores-for-place [id name]
  (common {:page/content
    [:div
     (title (str "Scores for \"" name "\""))
     [:table.table.is-fullwidth
       (for [score (score/for-place id)]
         [:tr
           [:td (:value score)]
           [:td (get-in score [:question :question])]
         ])

    ]]}))

(defn redirect []
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

