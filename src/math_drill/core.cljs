(ns math-drill.core
  (:require
   [math-drill.gen :as gen]
   [reagent.core :as r]
   [reagent.dom :as rdom]
   ["math-renderer" :as math-renderer]))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (r/atom {:text "Hello world!"}))

(defn math
  "Reagent component that renders the string of math.
   If a number is passed in, it will be changed to an in-line expression string."
  [s]
  (let [s (if (string? s)
            s
            (str "$" s "$"))]
    [:> math-renderer {:value (str s)}]))

(defn hello-world []
  (r/with-let [exercise-atom (r/atom (gen/simple-linear-equation))]
    (let [{:keys [question answer]} @exercise-atom]
      [:div
       [:h1 (:text @app-state)]
       [:span "Q: " [math question]]
       [:br]
       [:span "A: " [math answer]]
       [:button {:on-click #(reset! exercise-atom (gen/simple-linear-equation))}
        "New problem"]])))

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
