(ns math-drill.util-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [math-drill.util :as u]))

(deftest mathify-value-test
  (is (= (u/mathify-value 5) "$5$"))
  (testing "string handling"
    (is (= (u/mathify-value "5") "$5$"))
    (is (= (u/mathify-value "$5") "$$5$"))) 
  (testing "Idempotency"
    (is (= (u/mathify-value "$5$") "$5$"))
    (is (= (u/mathify-value (u/mathify-value (u/mathify-value 5))) "$5$")))
  (testing "I guess this is acceptable nil behavior?"
    (is (= (u/mathify-value nil) "$$"))))

(deftest mathify-values-test
  (is (= (u/mathify-values [1 4]) "$1$,$4$"))
  (is (= (u/mathify-values [1 "$4$"]) "$1$,$4$")))

(deftest mathify
  (is (= (u/mathify [1 4]) "$1$,$4$"))
  (is (= (u/mathify 5) "$5$")))
