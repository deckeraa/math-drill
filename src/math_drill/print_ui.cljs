(ns math-drill.print-ui
  "UI code for generating printed exercise sheets"
  (:require
   [math-drill.util :as util]
   [reagent.core :as r]))

(defn print-ui-core []
  (r/with-let [opts-atom (r/atom {:half-page? false})]
    [:div
     [:ul
      [:li
       [:input {:type :checkbox
                :value (:half-page? @opts-atom)
                :on-change #(swap! opts-atom (fn [m] (update m :half-page? not)))}]
       "Use half-page format"]]
     [:a {:href (str util/data-url "/download-exercise-sheet?opts=" (pr-str @opts-atom))
          :target "_blank"}
      "Generate exercise sheet"]]))
