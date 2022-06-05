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
      :simple-linear-equation 18
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

(defn exercises-section [exercises {:keys [show-answers?]}]
  (str
   "\\begin{multicols}{3}
  \\begin{enumerate}
"
   (apply str (map (fn [{:keys [question answer] :as exercise}]
                     (str "    \\item{"
                          question
                          (when show-answers? (str "\\newline " answer))
                          "}\n"
                          "\\vspace*{" (exercise->vspace exercise) "in}\n"))
                   exercises))
   "  \\end{enumerate}
\\end{multicols}
"))

(defn postamble []
  "\\end{document}")

(defn write-page!
  "You can run this like (write-page!) to get defaults or do something like:
  (write-page! {:half-page? true :exercise-type :quadratic-equation}) to customize."
  ([]
   (write-page! {}))
  ([{:keys [half-page? filename exercise-type show-answers?] :as opts}]
   (let [filename (or filename "sample.tex")
         exercise-type (or exercise-type :simple-linear-equation)
         exercise-fn (gen/type->fn exercise-type)
         num-exercises (exercise->num-on-page exercise-type)
         num-exercises (if half-page?
                         (quot num-exercises 2)
                         num-exercises)
         exercises (take num-exercises (repeatedly exercise-fn))]
     (spit (str "data/" filename)
           (str
            (preamble)
            (custom-header)
            (exercises-section exercises (select-keys opts [:show-answers?]))
            (when half-page?
              (str
               (custom-header)
               (exercises-section exercises (select-keys opts [:show-answers?]))))
            (postamble)))
     (shell/sh "pdflatex" filename :dir "data"))))
