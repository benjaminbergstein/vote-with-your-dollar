(ns places-annotations.settings
  (:require [ring.middleware.defaults :refer :all]))

(defmacro defenv [env-def env-key default-value]
  `(def ~env-def (or (System/getenv ~env-key) ~default-value)))

(defenv app-env "APP_ENV"           "development")
(defenv db-host "DATABASE_HOST"     "localhost"  )
(defenv db-port "DATABASE_PORT"     "65432"      )
(defenv db-user "DATABASE_USER"     "ben"        )
(defenv db-pass "DATABASE_PASSWORD" ""           )

(def ring-config
  (let [test-config (-> site-defaults
                      (assoc-in [:security :anti-forgery] false)
                      (assoc-in [:responses :absolute-redirects] false))

        production-config (-> secure-site-defaults
                              (assoc :proxy true))]

    (case app-env
      "development" site-defaults
      "test"        test-config
      production-config)))
