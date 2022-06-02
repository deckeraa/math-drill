(ns math-drill.gen-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [math-drill.gen :as g]))

(deftest coeff-var-test
  (is (= (g/coeff-var 5 "x") "5x"))
  (is (= (g/coeff-var 0 "x") ""))
  (is (= (g/coeff-var 1 "x") "x"))
  (is (= (g/coeff-var -1 "x") "-x"))
  (is (= (g/coeff-var -10 "x") "-10x")))

(deftest render-simple-linear-equation-test
  (is (= (g/render-simple-linear-equation 2 3 4)
         "$2x + 3 = 4$"))
  (is (= (g/render-simple-linear-equation 0 4 4)
         "$4 = 4$"))
  (is (= (g/render-simple-linear-equation 4 0 4)
         "$4x = 4$"))
  (is (= (g/render-simple-linear-equation 1 1 4)
         "$x + 1 = 4$")))
