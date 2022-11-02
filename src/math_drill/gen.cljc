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
  ([] (simple-linear-equation {}))
  ([opts]
   (let [a (rand-int-with-negs 20)
         b (rand-int-with-negs 20)
         x (rand-int-with-negs 20)
         c (+ (* a x) b)]
     {:type :simple-linear-equation
      :question (render-simple-linear-equation a b c)
      :answer x})))

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
   (let [sol1 (rand-int-with-negs 14)
         sol2 (rand-int-with-negs 14)
         scale-factor (if isolate-x? 1 (rand-nonzero-int-with-negs 9))
         ;; (x - sol1)(x - sol2) = x^2 - sol1*x - sol2*x + sol1*sol2 = x^2 - (sol1 + sol2)x + sol1*sol2
         a scale-factor
         b (* scale-factor -1 (+ sol1 sol2))
         c (* scale-factor sol1 sol2)]
     {:type :quadratic-equation
      :question (render-quadratic-equation-in-standard-form a b c)
      :answer [sol1, sol2]})))

(defn render-simple-linear-inequality [a b c op]
  (str "$"
       (coeff-var a "x")
       (when (and (not (zero? a))
                (not (zero? b)))
         " + ")
       (when-not (zero? b) b)
       " " op " " c "$"))

(defn linear-inequality
  ([] (linear-inequality {}))
  (
   [opts]
   (let [a (rand-int-with-negs 20)
         b (rand-int-with-negs 20)
         x (rand-int-with-negs 20)
         c (+ (* a x) b)
         op (rand-nth ['< '<= '> '>=])]
     {:type :linear-inequality
      :question (render-simple-linear-inequality a b c op)
      })))

(defn render-complete-the-square-blanks [a]
  (str "$x^2 + " (* 2 a) "x + \\uline{\\hspace{2em}} = (x + \\uline{\\hspace{2em}})^2$")
  )

(defn complete-the-square-blanks
  ([] (complete-the-square-blanks {}))
  ([opts]
   (let [a (rand-int-with-negs 20)]
     {:type :complete-the-square-blanks
      :question (render-complete-the-square-blanks a)})))

(def type->fn
  {:simple-linear-equation simple-linear-equation
   :quadratic-equation quadratic-equation
   :linear-inequality linear-inequality
   :complete-the-square-blanks complete-the-square-blanks})

(def type->name
  {:simple-linear-equation "simple linear equation"
   :quadratic-equation "quadratic equation"
   :linear-inequality "linear inequality"
   :complete-the-square-blanks "complete-the-square fill-in-the-blanks"})

(def exercise-types (keys type->fn))

(defn exercise->vspace [exercise]
  (case (:type exercise)
    :simple-linear-equation 0.75
    :quadratic-equation 1.00
    :linear-inequality 0.75
    :complete-the-square-blanks 0.5
    1))

(defn exercise->num-on-page [exercise]
  (let [t (if (map? exercise) (:type exercise) exercise)]
    (case t
      :simple-linear-equation 18
      :quadratic-equation 12
      :linear-inequality 18
      :complete-the-square-blanks 12)))

(defn exercise->approx-percent-of-page [exercise]
  (/ 1 (exercise->num-on-page exercise)))

(defn approx-page-length [exercises]
  (->> exercises
       (map :type)
       (map exercise->approx-percent-of-page)
       (apply +)))

(defn random-exercise-type-from-list [exr-types]
  (rand-nth (keys (filter (fn [[k v]] v) exr-types))))

(defn random-exercise-from-list [exr-types opts]
  (println "random-exercise-from-list" exr-types opts)
  ((type->fn (random-exercise-type-from-list exr-types)) opts))

(defn exercises-lazy-seq [exr-types]
  (map (fn [exercise-type]
         ((type->fn exercise-type) {}))
       (repeatedly #(random-exercise-type-from-list exr-types))))

(defn exercises-for-page [exr-types page-length]
  ;; yikes, this code is very imperative. Would be nice to clean this up using seq operations.
  (let [cur-length (atom 0)
        new-exercise-atom (atom (random-exercise-from-list exr-types {}))
        exercises (transient [])]
    (while (< (+ @cur-length (exercise->approx-percent-of-page @new-exercise-atom))
              page-length)
      (swap! cur-length + (exercise->approx-percent-of-page @new-exercise-atom))
      (conj! exercises @new-exercise-atom)
      (reset! new-exercise-atom (random-exercise-from-list exr-types {})))
    (persistent! exercises)))
