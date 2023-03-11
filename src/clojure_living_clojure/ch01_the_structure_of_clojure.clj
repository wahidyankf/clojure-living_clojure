(ns clojure-living-clojure.ch01-the-structure-of-clojure)

(cons 3 '(1))
;; => (3 1)

(cons 3 [1])
;; => (3 1)

(conj '(1) 3)
;; => (3 1)

(conj [1] 3)
;; => [1 3]

(nth '(1 2 3 4 5) 2)
;; => 3

(nth [1 2 3 4 5] 2)
;; => 3

(keys {:jam1 "strawberry" :jam2 "blackberry" :jam3 "marmalade"})
;; => (:jam1 :jam2 :jam3)
(vals {:jam1 "strawberry" :jam2 "blackberry" :jam3 "marmalade"})
;; => ("strawberry" "blackberry" "marmalade")
(assoc {:jam1 "red" :jam2 "black"} :jam1 "orange")
;; => {:jam1 "orange", :jam2 "black"}
(dissoc {:jam1 "strawberry" :jam2 "blackberry"} :jam1)
;; => {:jam2 "blackberry"}
(merge {:jam1 "red" :jam2 "black"}
       {:jam1 "orange" :jam3 "red"}
       {:jam4 "blue"})
;; => {:jam1 "orange", :jam2 "black", :jam3 "red", :jam4 "blue"}

(let [developer "Alice in Wonderland"
      rabbit "White Rabbit"]
  [developer rabbit])
;; => ["Alice in Wonderland" "White Rabbit"]

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def a-var 123)