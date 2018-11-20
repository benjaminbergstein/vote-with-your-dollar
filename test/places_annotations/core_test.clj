(ns places-annotations.core-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [places-annotations.core :refer :all]))

 (defn request [resource web-app & params]
      (web-app {:request-method :get :uri resource :params (first params)}))

(deftest root
  (let [req (mock/request :get "/results?coord%5Blat%5D=37.7709925&coord%5Blng%5D=-122.43769239999999")
        res (app req)]

    (is (re-find #".*Rich Table.*" (:body res)))))

