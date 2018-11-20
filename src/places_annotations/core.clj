(ns places-annotations.core
  (:use compojure.core
        ring.middleware.params
        ring.middleware.json-params)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [places-annotations.view :as view]))

(defroutes main-routes
  (GET "/" [] (view/redirect))
  (GET "/results" [coord] (view/index coord))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app (-> main-routes handler/site wrap-params))

