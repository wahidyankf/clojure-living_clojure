(ns clojure-living-clojure.ch03-state-and-concurrency)

(def who-atom (atom :caterpillar))

(reset! who-atom :chrysalis)

(defn change [state]
  (case state
    :caterpillar :chrysalis
    :chrysalis :butterfly
    :butterfly))
(swap! who-atom change)
@who-atom
;; => :butterfly

(def counter (atom 0))

@counter
;; -> 0

(let [n 5]
  (reset! counter 0)
  (future (dotimes [_ n] (swap! counter inc)))
  (future (dotimes [_ n] (swap! counter inc)))
  (future (dotimes [_ n] (swap! counter inc))))
@counter
;; => 15

(defn inc-print [val]
  (println val)
  (inc val)) @who-atom

(let [n 10]
  (reset! counter 0)
  (future (dotimes [_ n] (swap! counter inc-print)))
  (future (dotimes [_ n] (swap! counter inc-print)))
  (future (dotimes [_ n] (swap! counter inc-print))))
@counter
;; => 30

(def alice-height (ref 3))
(def right-hand-bites (ref 10))
@alice-height
;; -> 3
@right-hand-bites
;; -> 10
(defn eat-from-right-hand []
  (dosync (when (pos? @right-hand-bites)
            (alter right-hand-bites dec)
            (alter alice-height #(+ % 24)))))
;; (eat-from-right-hand)
;; => Execution error (IllegalStateException) at clojure-living-clojure.ch03-state-and-concurrency/eat-from-right-hand (ch03_state_and_concurrency.clj:49).
;;    No transaction running

(ref-set alice-height 3)
(ref-set right-hand-bites 3)
(dosync (eat-from-right-hand))
@alice-height
;; => 27
@right-hand-bites
;; => 9

(let [n 2]
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand))))
@alice-height
;; => 147
@right-hand-bites
;; => 4

;; Agents
(def who-agent (agent :caterpillar))
@who-agent
;; => :caterpillar
(defn change-agent [state]
  (case state
    :caterpillar :chrysalis
    :chrysalis :butterfly
    :butterfly))
(send who-agent change)
;; => #<Agent@34e90594: :butterfly>
@who-agent
;; => :chrysalis
