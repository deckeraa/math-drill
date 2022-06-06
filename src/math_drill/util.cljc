(ns math-drill.util)

(def data-url "http://localhost:3000")

(defn mathify-value
  "Takes a single value and turns it into a LaTeX math string."
  [v]
  (if (and (string? v) (= "$" (first v)) (= "$" (last v)))
    v
    (str "$" v "$")))

(defn mathify-values
  "Takes a seq of values and turns them into a comma-delimited list of LaTeX math string."
  [vs]
  (apply str (interpose "," (map mathify-value vs))))

(defn mathify
  "Turns something into a LaTeX math string."
  [v]
  (if (sequential? v)
    (mathify-values v)
    (mathify-value v)))
