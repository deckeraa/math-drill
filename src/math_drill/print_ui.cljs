(ns math-drill.print-ui
  "UI code for generating printed exercise sheets"
  (:require
   [math-drill.util :as util]
   [reagent.core :as r]))

(defn print-ui-core []
  [:div
   [:a {:href (str util/data-url "/download-exercise-sheet")
        :target "_blank"}
    "Generate exercise sheet"]])
