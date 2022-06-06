(ns math-drill.print
  (:require [math-drill.gen :as gen]
            [math-drill.util :refer [mathify]]
            [clojure.java.shell :as shell]
            [clojure.walk :refer [keywordize-keys]]
            [ring.util.response :refer [file-response]]
            [ring.util.codec :as codec]))

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
                          (when show-answers? (str "\\newline " (mathify answer)))
                          "}\n"
                          "\\vspace*{" (gen/exercise->vspace exercise) "in}\n"))
                   exercises))
   "  \\end{enumerate}
\\end{multicols}
"))

(defn postamble []
  "\\end{document}")

(defn write-page!
  "You can run this like (write-page!) to get defaults or do something like:
  (write-page! {:half-page? true :exr-types {:quadratic-equation true}}) to customize."
  ([]
   (write-page! {}))
  ([{:keys [half-page? filename exercise-type exr-types show-answers?] :as opts}]
   (let [filename (or filename "sample.tex")
         exr-types (or exr-types {:simple-linear-equation true})
         exercises (gen/exercises-for-page exr-types (if half-page? 0.5 1))]
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

(defn download-exercise-sheet-handler
  [req]
  (let [query-map (as-> (:query-string req) $
                    (if $
                      (keywordize-keys (codec/form-decode $))))
        opts (clojure.edn/read-string (:opts query-map))
        uuid (.toString (java.util.UUID/randomUUID))
        filename (str uuid ".pdf")]
    (println "download-exercise-sheet-handler: " opts)
    (write-page! (merge opts {:filename filename}))
    (println "Just wrote: " filename)
    (file-response (str "data/" filename))))
