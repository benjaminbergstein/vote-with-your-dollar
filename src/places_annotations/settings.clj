(ns places-annotations.settings
  (:require [ring.middleware.defaults :refer :all]))

(defmacro defenv

  ([env-def env-key default-value]
    `(def ~env-def (or (System/getenv ~env-key) ~default-value)))

  ([env-def env-key]
    `(def ~env-def (System/getenv ~env-key))))

(defenv app-env        "APP_ENV"           "development")
(defenv db-host        "DATABASE_HOST"     "localhost"  )
(defenv db-port        "DATABASE_PORT"     "65432"      )
(defenv db-user        "DATABASE_USER"     "ben"        )
(defenv db-pass        "DATABASE_PASSWORD" ""           )
(defenv google-api-key "GOOGLE_API_KEY"                 )

(def ring-config
  (let [test-config (-> site-defaults
                      (assoc-in [:security :anti-forgery] false)
                      (assoc-in [:responses :absolute-redirects] false))

        dev-config site-defaults

        production-config (-> secure-site-defaults
                              (assoc :proxy true))]

    (case app-env
      "development" dev-config
      "test"        test-config
      production-config)))
