(ns clojure-living-clojure.core-test
  #_{:clj-kondo/ignore [:refer-all]}
  (:require [clojure.test :refer :all]
            [clojure-living-clojure.core :refer :all]))

(deftest a-test
  (testing "placeholder test"
    (is (not (= 0 1)))))
