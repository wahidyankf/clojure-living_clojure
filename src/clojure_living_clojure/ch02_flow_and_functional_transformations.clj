(ns clojure-living-clojure.ch02-flow-and-functional-transformations)

(class true)
;; => java.lang.Boolean

(= '(1 2) [1 2])
;; => true

(some #(> % 3) [1 2 3 4 5])

(defn grow [name direction]
  (if (= direction :small)
    (str name " is growing smaller")
    (str name " is growing bigger")))
(grow "Alice" :small)
;; => "Alice is growing smaller"
((partial grow "Alice") :small)
;; => "Alice is growing smaller"
((partial (partial grow)) "Alice" :small)
;; => "Alice is growing smaller"

(let [[color [size] :as original] ["blue" ["small"]]]
  {:color color :size size :original original})
;; => {:color "blue", :size "small", :original ["blue" ["small"]]}
(let [{flower1 :flower1 :as all-flowers}
      {:flower1 "red"}]
  [flower1 all-flowers])
;; => ["red" {:flower1 "red"}]
(let [{:keys [flower1 flower2]}
      {:flower1 "red" :flower2 "blue"}]
  (str "The flowers are " flower1 " and " flower2))
;; => "The flowers are red and blue"
(defn flower-colors [{:keys [flower1 flower2]}]
  (str "The flowers are " flower1 " and " flower2))
(flower-colors {:flower1 "red" :flower2 "blue"})
;; => "The flowers are red and blue"

(take 10 (range))
;; => (0 1 2 3 4 5 6 7 8 9)

(class (take 10 (range)))
;; => clojure.lang.LazySeq

(repeat 5 (rand-int 10))
;; => (6 6 6 6 6)
(repeatedly 5 #(rand-int 10))
;; => (0 8 0 5 6)
(take 5 (repeatedly #(rand-int 10)))
;; => (9 9 4 3 3)
(take 6 (cycle ["big" "small"]))
;; => ("big" "small" "big" "small" "big" "small")
(take 3 (rest (cycle ["big" "small"])))
;; => ("small" "big" "small")

;; Recursion

(def adjs ["normal"
           "too small"
           "too big"
           "swimming"])
(defn alice-is [in out]
  (if (empty? in)
    out
    (alice-is
     (rest in)
     (conj out
           (str "Alice is " (first in))))))
(alice-is adjs [])
;; => ["Alice is normal" "Alice is too small" "Alice is too big" "Alice is swimming"]

(defn alice-is-recur [input]
  (loop [in input
         out []]
    (if (empty? in)
      out
      (recur (rest in)
             (conj out
                   (str "Alice is " (first in)))))))
(alice-is-recur adjs)
;; => ["Alice is normal" "Alice is too small" "Alice is too big" "Alice is swimming"]

(defn countdown [n]
  (if (= n 0)
    n
    (countdown (- n 1))))
(countdown 3)
;; -> 0
;; (countdown 100000)
;; -> StackOverflowError

(defn countdown-recur [n]
  (if (= n 0)
    n
    (recur (- n 1))))
(countdown-recur 100000)
;; -> 0

(def animals [:mouse :duck :dodo :lory :eaglet])
(map #(str %) animals)
;; => (":mouse" ":duck" ":dodo" ":lory" ":eaglet")
(class
 (map #(str %) animals))
;; => clojure.lang.LazySeq

(take 3 (map #(str %) (range)))
;; => ("0" "1" "2")

(reduce (fn [r x] (+ r (* x x))) [1 2 3])
;; => 14
(reduce (fn [r x] (if (nil? x) r (conj r x)))
        []
        [:mouse nil :duck nil nil :lory])
;; => [:mouse :duck :lory]

(for [animal [:mouse :duck :lory]]
  (str (name animal)))
;; => ("mouse" "duck" "lory")
(for [animal [:mouse :duck :lory]
      color  [:red :blue]]
  (str (name color) (name animal)))
;; => ("redmouse" "bluemouse" "redduck" "blueduck" "redlory" "bluelory")
(for [animal [:mouse :duck :lory]
      color  [:red :blue]
      :let  [animal-str (str "animal-" (name animal))
             color-str (str "color-" (name color))
             display-str (str animal-str "-" color-str)]]
  display-str)
;; => ("animal-mouse-color-red"
;;     "animal-mouse-color-blue"
;;     "animal-duck-color-red"
;;     "animal-duck-color-blue"
;;     "animal-lory-color-red"
;;     "animal-lory-color-blue")
(for [animal [:mouse :duck :lory]
      color  [:red :blue]
      :let  [animal-str (str "animal-" (name animal))
             color-str (str "color-" (name color))
             display-str (str animal-str "-" color-str)]
      :when (= color :blue)]
  display-str)
;; => ("animal-mouse-color-blue" "animal-duck-color-blue" "animal-lory-color-blue")

(flatten [[:duck [:mouse] [[:lory]]]])
;; => (:duck :mouse :lory)

(into {} [[:a 1] [:b 2] [:c 3]])
;; => {:a 1, :b 2, :c 3}
(into [] {:a 1, :b 2, :c 3})
;; => [[:a 1] [:b 2] [:c 3]]

(partition 3 [1 2 3 4 5 6 7 8 9])
;; => ((1 2 3) (4 5 6) (7 8 9))
(partition 3 [1 2 3 4 5 6 7 8 9 10])
;; => ((1 2 3) (4 5 6) (7 8 9))
(partition-all 3 [1 2 3 4 5 6 7 8 9 10])
;; => ((1 2 3) (4 5 6) (7 8 9) (10))
(partition-by #(= 7 %) [1 2 3 4 5 6 7 8 9 10])
;; => ((1 2 3 4 5 6) (7) (8 9 10))
