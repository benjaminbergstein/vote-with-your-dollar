(ns places-annotations.view.shared
  (:require [hiccup.core :as h]
            [hiccup.page :as ph]
            [hiccup.element :as elem])
  (:use [hiccup.def]
        [ring.util.anti-forgery]))

(defn title [text] [:h1.title text])

(defn icon
  ([type] (icon type {}))
  ([type options]
    [:span { :class (str "icon " (:class options)) }
      [:i { :class (str "fas fa-" type) }]]))

(defn common
  ([page] (common {} page))
  ([session page]
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
          [:a.navbar-item.has-text-primary { :href (or (session :query-url) "/") }
           (icon 'vote-yea) [:span " &nbsp;WYR"] (icon 'dollar-sign)]]]]

        [:div.section
         [:div.container
          (page :page/content)]]

        (ph/include-js "/js/my.js")])))

