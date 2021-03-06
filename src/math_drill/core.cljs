(ns math-drill.core
  (:require
   [math-drill.gen :as gen]
   [math-drill.util :refer [mathify mathify-values]]
   [math-drill.print-ui :as print-ui]
   [reagent.core :as r]
   [reagent.dom :as rdom]
   ["math-renderer" :as math-renderer]))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (r/atom {:text "Hello world!"}))

(defn math
  "Reagent component that renders the string of math.
   If a number is passed in, it will be changed to an in-line expression string."
  [v]
  [:> math-renderer {:value (mathify v)}])

(defn hello-world []
  (r/with-let [exercise-fn gen/quadratic-equation
               exercise-opts {:isolate-x? true}
               exercise-atom (r/atom (exercise-fn exercise-opts))]
    (let [{:keys [question answer]} @exercise-atom]
      [:div
       [:h1 (:text @app-state)]
       [:span "Q: " [math question]]
       [:br]
       [:span "A: " [math (mathify-values answer)]]
       [:button {:on-click #(reset! exercise-atom (exercise-fn exercise-opts))}
        "New problem"]
       [print-ui/print-ui-core]])))

(defn start []
  (rdom/render [hello-world]
               (. js/document (getElementById "app"))))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

(defn stop []
  ;; stop is called before any code is reloaded
  ;; this is controlled by :before-load in the config
  (js/console.log "stop"))
