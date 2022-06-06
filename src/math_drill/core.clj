(ns math-drill.core
  (:require [math-drill.print :as print])
  (:gen-class))

(defn hello [req]
  (println "hello: " req)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World!"})

(defn simple-router [req]
  (case (:uri req)
    "/hello" (hello req)
    "/download-exercise-sheet" (print/download-exercise-sheet-handler req)
    {:status 404 :headers {"Content-Type" "text/html"} :body "Not found."}))

(def app simple-router)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
