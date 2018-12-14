(ns places-annotations.core
  (:use compojure.core
        ring.middleware.params
        ring.middleware.json-params)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer :all]
            [places-annotations.score :as score]
            [places-annotations.settings :as settings]
            [places-annotations.view.place :as view.place]
            [places-annotations.view.score :as view.score]
            [places-annotations.view.location :as view.location]
            ))

(defroutes main-routes
  (GET "/" [] (view.location/determine))
  (GET "/places" [coord query]
      (view.place/index coord query))
  (GET "/places/:id/scores/new" [id name :as request]
      (view.score/new id name))
  (POST "/scores" [score]
    (score/submit score)
    {:status 302 :headers {"Location" "/"} :body ""})
  (GET "/places/:id/scores" [id name]
    (view.score/list-for-place id name))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app (-> main-routes
             handler/site
             (wrap-defaults settings/ring-config)
         ))
