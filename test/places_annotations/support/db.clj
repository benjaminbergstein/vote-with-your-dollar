(ns places-annotations.support.db
  (:require [clojure.test :refer :all]
            [places-annotations.db :as db]
            [clojure.java.jdbc :as jdbc]))

(def test-db (db/setup "test"))

(defmacro with-db [& body]
  `(with-redefs [places-annotations.db/conn test-db]
      (jdbc/execute! test-db ["TRUNCATE scores"])
      (jdbc/execute! test-db ["TRUNCATE places"])

      (do ~@body)))
