(ns math-drill.gen-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [math-drill.gen :as g]))

(deftest coeff-var-test
  (is (= (g/coeff-var 5 "x") "5x")))
;; (defn coeff-var [coeff var-name]
;;   (case coeff
;;     1 var-name
;;     -1 (str "-" var-name)
;;     0 ""
;;     (str coeff var-name)))
