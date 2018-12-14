(ns places-annotations.view.score
  (:require [hiccup.core :as h]
            [hiccup.page :as ph]
            [hiccup.element :as elem]
            [cheshire.core :refer :all]
            [places-annotations.question :as question]
            [places-annotations.score :as score])
  (:use [hiccup.def]
        [places-annotations.view.shared]
        [ring.util.anti-forgery]))

(def button-class-map { 1 ""
                        2 "is-light"
                        3 "has-text-weight-semibold"
                        4 "is-light"
                        5 ""  })

(defn new [id name]
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

(defn list-for-place [id name]
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

