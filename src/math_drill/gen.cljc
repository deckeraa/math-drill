(ns math-drill.gen)

(defn rand-int-with-negs [n]
  (* (rand-int n)
     (rand-nth [1 -1])))

(defn coeff-var [coeff var-name]
  (case coeff
    1 var-name
    -1 (str "-" var-name)
    0 ""
    (str coeff var-name)))

(defn render-simple-linear-equation [a b c]
  (str "$"
       (coeff-var a "x")
       (when (and (not (zero? a))
                (not (zero? b)))
         " + ")
       (when-not (zero? b) b)
       " = " c "$"))

(defn simple-linear-equation
  "Creates equations of the type ax + b = c"
  []
  (let [a (rand-int-with-negs 20)
        b (rand-int-with-negs 20)
        x (rand-int-with-negs 20)
        c (+ (* a x) b)]
    {:type :simple-linear-equation
     :question (render-simple-linear-equation a b c)
     :answer x}))
