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

(defn redirect-url [request] (get-in request [:session :query-url]))

(defroutes main-routes
  (GET "/" [] (view.location/determine))

  (GET "/places" [coord query :as request]
      (let [query-url (str (request :uri) "?" (request :query-string))]
        { :body (view.place/index coord query)
          :status 200
          :headers { "Content-Type" "text/html" }
          :session (assoc (request :session) :query-url query-url)}))

  (GET "/places/:id/scores/new" [id name :as request]
      { :body (view.score/new id name (request :session))
        :status 200
        :headers { "Content-Type" "text/html" } })

  (POST "/scores" [score :as request]
    (score/submit score)
    { :body ""
      :status 302
      :headers { "Location" (redirect-url request) } })

  (GET "/places/:id/scores" [id name :as request]
    (view.score/list-for-place id name (request :session)))

  (route/not-found "Page not found"))

(def app (-> main-routes
             (wrap-defaults settings/ring-config)
         ))
