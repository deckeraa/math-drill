(ns math-drill.print
  (:require [math-drill.gen :as gen]
            [clojure.java.shell :as shell]))

(defn exercise->vspace [exercise]
  (case (:type exercise)
    :simple-linear-equation 0.75
    1))

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
                     (str "    \\item{" question "}\n\\vspace{" (exercise->vspace exercise) "in}\n"))
                   exercises))
   "  \\end{enumerate}
\\end{multicols}
"))

(defn postamble []
  "\\end{document}")

(defn write-page! []
  (let [filename "data/sample.tex"
        num-exercises 10
        exercises (take num-exercises (repeatedly gen/simple-linear-equation))]
    (spit filename
          (str
           (preamble)
           (custom-header)
           (exercises-section exercises)
           "\\vspace{0.5in}"
           (custom-header)
           (exercises-section exercises)
           (postamble)))
    (shell/sh "pdflatex" "sample.tex" :dir "data")))
