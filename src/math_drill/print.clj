(ns math-drill.print
  (:require [math-drill.gen :as gen]
            [clojure.java.shell :as shell]))

(defn exercise->vspace [exercise]
  (case (:type exercise)
    :simple-linear-equation 0.75
    :quadratic-equation 1.50
    1))

(defn exercise->num-on-page [exercise]
  (let [t (if (map? exercise) (:type exercise) exercise)]
    (case t
      :simple-linear-equation 20
      :quadratic-equation 12)))

(defn preamble []
  (str
   "\\documentclass[11pt]{article}
\\usepackage[letterpaper]{geometry}
\\usepackage{multicol}
\\usepackage{layout}

\\voffset=-0.5in
\\addtolength{\\textheight}{-2\\voffset}

\\begin{document}
\\pagestyle{empty}
"))

(defn custom-header []
  "Math Drill \\hspace{3in} Name: 
\\vspace{0.1in}
\\hrule")

(defn exercises-section [exercises]
  (str
   "\\begin{multicols}{3}
  \\begin{enumerate}
"
   (apply str (map (fn [{:keys [question] :as exercise}]
                     (str "    \\item{" question "}\n\\vspace*{" (exercise->vspace exercise) "in}\n"))
                   exercises))
   "  \\end{enumerate}
\\end{multicols}
"))

(defn postamble []
  "\\end{document}")

(defn write-page! []
  (let [filename "data/sample.tex"
        half-page? true
        exercise-type :quadratic-equation
        exercise-fn (gen/type->fn exercise-type)
        num-exercises (exercise->num-on-page exercise-type)
        num-exercises (if half-page?
                        (quot num-exercises 2)
                        num-exercises)
        exercises (take num-exercises (repeatedly exercise-fn))]
    (spit filename
          (str
           (preamble)
           (custom-header)
           (exercises-section exercises)
           (when half-page?
             (str
              "\\vspace{0.5in}"
              (custom-header)
              (exercises-section exercises)))
           (postamble)))
    (shell/sh "pdflatex" "sample.tex" :dir "data")))
