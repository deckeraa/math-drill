(ns math-drill.core-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [math-drill.core :as c]))

(deftest mathify-value-test
  (is (= (c/mathify-value 5) "$5$"))
  (testing "We assume, rightly or wrongly, that passed string values shouldn't be converted. Maybe change this behavior in the future to always convert while still retaining idempotency?"
    (is (= (c/mathify-value "$5$") "$5$")))
  (testing "I guess this is acceptable nil behavior?"
    (is (= (c/mathify-value nil) "$$"))))

(deftest mathify-values-test
  (is (= (c/mathify-values [1 4]) "$1$,$4$"))
  (is (= (c/mathify-values [1 "$4$"]) "$1$,$4$")))
