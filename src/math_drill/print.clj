(ns math-drill.print
  (:require [math-drill.gen :as gen]
            [clojure.java.shell :as shell]))

(defn preamble []
  (str
   "\\documentclass[11pt]{article}
\\usepackage{multicol}
\\usepackage{fancyhdr}
\\author{aaron}
\\date{\\today}
\\title{}

\\begin{document}

\\pagestyle{fancy}
\\fancyhead{}
\\lhead{\\textit{Math Drill}}
\\rhead{\\textit{Simple Linear Equations}}

"))

(defn exercises-section [exercises]
  (str
   "\\begin{multicols}{3}
  \\begin{enumerate}
"
   (apply str (map (fn [{:keys [question] :as exercise}]
                     (str "    \\item{" question "}\n"))
                   exercises))
   "  \\end{enumerate}
\\end{multicols}
"))

(defn postamble []
  "\\end{document}")

(defn write-page! []
  (let [filename "data/sample.tex"
        num-exercises 30
        exercises (take num-exercises (repeatedly gen/simple-linear-equation))]
    (spit filename
          (str
           (preamble)
           (exercises-section exercises)
           (postamble)))
    (shell/sh "pdflatex" "sample.tex" :dir "data")))
