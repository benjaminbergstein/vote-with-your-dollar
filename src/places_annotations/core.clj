(ns places-annotations.core
  (:use compojure.core
        ring.middleware.params
        ring.middleware.json-params)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [places-annotations.score :as score]
            [places-annotations.view :as view]))

(defroutes main-routes
  (GET "/" [] (view/redirect))
  (GET "/places" [coord] (view/places coord))
  (GET "/places/:id/scores/new" [id name] (view/new-score id name))
  (POST "/scores" [score]
    (score/submit score)
    {:status 302, :headers {"Location" "/"}, :body ""})
  (GET "/places/:id/scores" [id] (view/scores-for-place id))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app (-> main-routes handler/site wrap-params))
