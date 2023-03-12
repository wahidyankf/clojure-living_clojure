(ns clojure-living-clojure.ch06-communication-with-core-async
  #_{:clj-kondo/ignore [:unused-referred-var]}
  (:require [clojure.core.async :refer [<! <!! >! >!! alts! chan close! go
                                        go-loop]]))

(def tea-channel-v0 (chan 10))
(>!! tea-channel-v0 :cup-of-tea)
;; => true
(<!! tea-channel-v0)
;; => :cup-of-tea
(close! tea-channel-v0)
;; => nil
(<!! tea-channel-v0)
;; => nil

(let [tea-channel (chan)]
  (go (>! tea-channel :cup-of-tea-1))
  (go (println "Thanks for the " (<! tea-channel))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def tea-channel (chan 10))
;; (go-loop []
;;   (println "Thanks for the " (<! tea-channel))
;;   (recur))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def milk-channel (chan 10))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def sugar-channel (chan 10))

;; (go-loop []
;;   (let [[v ch] (alts! [tea-channel
;;                        milk-channel
;;                        sugar-channel])]
;;     (println "Got " v " from " ch)
;;     (recur)))

(def google-tea-service-chan (chan 10))
(def yahoo-tea-service-chan (chan 10))
(defn random-add []
  (reduce + (conj [] (repeat (rand-int 100000) 1))))
(defn request-google-tea-service []
  (go
    (random-add)
    (>! google-tea-service-chan
        "tea compliments of google")))
(defn request-yahoo-tea-service []
  (go
    (random-add)
    (>! yahoo-tea-service-chan
        "tea compliments of yahoo")))
(def result-chan (chan 10))
(defn request-tea []
  (request-google-tea-service)
  (request-yahoo-tea-service)
  (go (let [[v] (alts!
                 [google-tea-service-chan
                  yahoo-tea-service-chan])]
        (>! result-chan v))))
;; (request-tea)
;; (println (<!! result-chan))

(defn -main [& _]
  (println "Requesting tea!")
  (request-tea)
  (println (<!! result-chan)))
