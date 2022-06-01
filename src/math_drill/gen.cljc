(ns math-drill.gen)

(defn rand-int-with-negs [n]
  (* (rand-int n)
     (rand-nth [1 -1])))

(defn simple-linear-equation
  "Creates equations of the type ax + b = c"
  []
  (let [a (rand-int-with-negs 20)
        b (rand-int-with-negs 20)
        x (rand-int-with-negs 20)
        c (+ (* a x) b)]
    {:question (str "$" a "x + " b " = " c "$")
     :answer x}))
