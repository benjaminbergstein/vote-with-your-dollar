(ns places-annotations.settings)

(def app-env (or (System/getenv "APP_ENV") "development"))

(def db-host (or (System/getenv "DATABASE_HOST") "localhost"))
(def db-port (or (System/getenv "DATABASE_PORT") "65432"))
(def db-user (or (System/getenv "DATABASE_USER") "ben"))
(def db-pass (or (System/getenv "DATABASE_PASSWORD") ""))

