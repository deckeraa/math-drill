(ns math-drill.print-ui
  "UI code for generating printed exercise sheets"
  (:require
   [math-drill.util :as util]
   [math-drill.gen :as gen]
   [reagent.core :as r]))

(defn exercise-selector-line [exercise-type exercise-cursor]
  [:div
   [:input {:type :checkbox
            :value @exercise-cursor
            :on-change #(swap! exercise-cursor (fn [v] (not v)))}]
   (gen/type->name exercise-type)])

(defn print-ui-core []
  (r/with-let [opts-atom (r/atom {:half-page? false :exr-types {}})]
    [:div
     (str @opts-atom)
     [:ul
      [:li
       [:input {:type :checkbox
                :value (:half-page? @opts-atom)
                :on-change #(swap! opts-atom (fn [m] (update m :half-page? not)))}]
       "Use half-page format"]
      (map (fn [exercise-type]
             ^{:key exercise-type}
             [:li [exercise-selector-line exercise-type (r/cursor opts-atom [:exr-types exercise-type])]])
           gen/exercise-types)]
     [:a {:href (str util/data-url "/download-exercise-sheet?opts=" (pr-str @opts-atom))
          :target "_blank"}
      "Generate exercise sheet"]]))
