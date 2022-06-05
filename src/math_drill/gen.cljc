(ns math-drill.gen)

(defn rand-int-with-negs [n]
  (* (rand-int n)
     (rand-nth [1 -1])))

(defn rand-nonzero-int-with-negs [n]
  (* (inc (rand-int (dec n)))
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

(defn render-quadratic-equation-in-standard-form [a b c]
  (str "$"
       (coeff-var a "x^2")
       (when-not (zero? a)
         (if-not (neg? b) " + " " - "))
       (coeff-var (abs b) "x")
       (when-not (zero? b)
         (if-not (neg? c) " + " " - "))
       (abs c)
       " = 0$"))

(defn quadratic-equation
  "Creates quadratic equations."
  ([] (quadratic-equation {}))
  ([{:keys [isolate-x?]}]
   (let [sol1 (rand-int-with-negs 20)
         sol2 (rand-int-with-negs 20)
         scale-factor (if isolate-x? 1 (rand-nonzero-int-with-negs 9))
         ;; (x - sol1)(x - sol2) = x^2 - sol1*x - sol2*x + sol1*sol2 = x^2 - (sol1 + sol2)x + sol1*sol2
         a scale-factor
         b (* scale-factor -1 (+ sol1 sol2))
         c (* scale-factor sol1 sol2)]
     {:type :quadratic-equation
      :question (render-quadratic-equation-in-standard-form a b c)
      :answer [sol1, sol2]})))

(defn type->fn [t]
  (case t
    :simple-linear-equation simple-linear-equation
    :quadratic-equation quadratic-equation))
