(ns clojure-living-clojure.ch08-the-power-of-macros)

(macroexpand '(when (= 2 2) (println "It is four!")))
;; => (if (= 2 2) (do (println "It is four!")))

(defn hi-queen [phrase]
  (str phrase ", so please your Majesty."))

(defmacro def-hi-queen [name phrase]
  (list 'defn
        (symbol name)
        []
        (list 'hi-queen phrase)))
(macroexpand-1 '(def-hi-queen alice-hi-queen "My name is Alice"))
;; => (defn alice-hi-queen [] (hi-queen "My name is Alice"))

(defmacro def-hi-queen-v2 [name phrase]
  `(defn ~name []
     (hi-queen ~phrase)))
(macroexpand-1 '(def-hi-queen-v2 alice-hi-queen "My name is Alice"))
;; => (clojure.core/defn alice-hi-queen [] ('clojure-living-clojure.ch08-the-power-of-macros/hi-queen "My name is Alice"))

(def-hi-queen alice-hi-queen "My name is Alice")
(alice-hi-queen)
;; => "My name is Alice, so please your Majesty."

(def-hi-queen-v2 alice-hi-queen-v2 "My name is Alice")
(alice-hi-queen-v2)
;; => "My name is Alice, so please your Majesty."


