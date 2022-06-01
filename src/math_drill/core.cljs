(ns math-drill.core
  (:require
   [math-drill.gen :as gen]
   [reagent.core :as reagent :refer [atom]]
   ["math-renderer" :as math-renderer]))

;; import 'katex/dist/katex.min.css'

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))

(defn math [s]
  [:> math-renderer {:value s}])

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [math (gen/simple-linear-equation)]])

(defn start []
  (reagent/render-component [hello-world]
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
