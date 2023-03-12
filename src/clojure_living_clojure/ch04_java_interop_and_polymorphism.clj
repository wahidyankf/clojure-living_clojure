(ns clojure-living-clojure.ch04-java-interop-and-polymorphism
  (:import (java.net InetAddress)))

(.indexOf "caterpillar" "pillar")
;; => 5

(InetAddress/getByName "localhost")
;; => #object[java.net.Inet4Address 0x316a960 "localhost/127.0.0.1"]
(.getHostName
 (InetAddress/getByName "localhost"))
;; => "localhost"

(def sb (doto (StringBuffer. "Who ")
          (.append "are ")
          (.append "you?")))
(.toString sb)
;; => "Who are you?"

(import 'java.util.UUID)
(UUID/randomUUID)
;; => #uuid "54e3f7c3-0e0a-446b-96a2-e90fbb91b66e"
(str (UUID/randomUUID))
;; => "b86cde91-4faa-41f4-a49f-56f08ff73ce2"

(defn who-are-you [input]
  (cond
    (= java.lang.String (class input)) "String - Who are you?"
    (= clojure.lang.Keyword (class input)) "Keyword - Who are you?"
    (= java.lang.Long (class input)) "Number - Who are you?"))
(who-are-you :alice)
;; -> "Keyword - Who are you?"
(who-are-you "alice")
;; -> "String - Who are you?"
(who-are-you 123)
;; -> "Number - Who are you?"
(who-are-you true)
;; -> nil

(defmulti who-are-you-multi class)
(defmethod who-are-you-multi java.lang.String [input]
  (str "String - who are you? " input))
(defmethod who-are-you-multi clojure.lang.Keyword [input]
  (str "Keyword - who are you? " input))
(defmethod who-are-you-multi java.lang.Long [input]
  (str "Number - who are you? " input))
(who-are-you-multi :alice)
;; => "Keyword - who are you? :alice"
(who-are-you-multi "alice")
;; => "String - who are you? alice"
(who-are-you-multi 123)
;; => "Number - who are you? 123"
;; (who-are-you-multi true)
;; => Execution error (IllegalArgumentException) at clojure-living-clojure.ch04-java-interop-and-polymorphism/eval19744 (form-init5133794598832690764.clj:52).
;;    No method in multimethod 'who-are-you-multi' for dispatch value: class java.lang.Boolean

(defmethod who-are-you-multi :default [input]
  (str "I don't know - who are you? " input))
(who-are-you-multi true)
;; => "I don't know - who are you? true"

(defmulti eat-mushroom-multi #(if (< % 3)
                                :grow
                                :shrink))
(defmethod eat-mushroom-multi :grow [_]
  "Eat the right side to grow.")
(defmethod eat-mushroom-multi :shrink [_]
  "Eat the left side to shrink.")
(eat-mushroom-multi 10)

(defprotocol BigMushroom
  (eat-mushroom [this]))

(extend-protocol BigMushroom
  java.lang.String
  (eat-mushroom [this]
    (str (.toUpperCase this) " mmmm tasty!"))

  clojure.lang.Keyword
  (eat-mushroom [this]
    (case this
      :grow "Eat the right side!"
      :shrink "Eat the left side!"))

  java.lang.Long
  (eat-mushroom [this]
    (if (< this 3)
      "Eat the right side to grow"
      "Eat the left side to shrink")))
(eat-mushroom "Big Mushroom")
;; => "BIG MUSHROOM mmmm tasty!"
(eat-mushroom :grow)
;; => "Eat the right side!"
(eat-mushroom 1)
;; => "Eat the right side to grow"

(defrecord Mushroom [color height])
;; => clojure_living_clojure.ch04_java_interop_and_polymorphism.Mushroom
(def regular-mushroom (Mushroom. "white and blue polka dots" "2 inches"))
(class regular-mushroom)
;; => clojure_living_clojure.ch04_java_interop_and_polymorphism.Mushroom

(.-color regular-mushroom)
;; => "white and blue polka dots"
(.-height regular-mushroom)
;; => "2 inches"
(:color regular-mushroom)
;; => "white and blue polka dots"
(:height regular-mushroom)
;; => "2 inches"

(defprotocol Edible
  (bite-right-side [this])
  (bite-left-side [this]))

(defrecord WonderlandMushroom [color height]
  Edible
  (bite-right-side [_]
    (str "The " color " bite makes you grow bigger"))
  (bite-left-side [_]
    (str "The " color " bite makes you grow smaller")))
(defrecord RegularMushroom [color height]
  Edible
  (bite-right-side [_]
    (str "The " color " bite tastes bad"))
  (bite-left-side [_]
    (str "The " color " bite tastes bad too")))
(def alice-mushroom (WonderlandMushroom. "blue dots" "3 inches"))
(def reg-mushroom (RegularMushroom. "brown" "1 inches"))
(bite-right-side alice-mushroom)
;; => "The blue dots bite makes you grow bigger"
(bite-left-side alice-mushroom)
;; => "The blue dots bite makes you grow smaller"
(bite-right-side reg-mushroom)
;; => "The brown bite tastes bad"
(bite-left-side reg-mushroom)
;; => "The brown bite tastes bad too"

(deftype WonderlandMushroomV2 []
  Edible
  (bite-right-side [_]
    (str "The bite makes you grow bigger"))
  (bite-left-side [_]
    (str "The bite makes you grow smaller")))
(deftype RegularMushroomV2 []
  Edible
  (bite-right-side [_]
    (str "The bite tastes bad"))
  (bite-left-side [_]
    (str "The bite tastes bad too")))
(def alice-mushroom-v2 (WonderlandMushroom.))
(def reg-mushroom-v2 (RegularMushroom.))
(bite-right-side alice-mushroom-v2)
;; -> "The bite makes you grow bigger"
(bite-left-side alice-mushroom-v2)
;; -> "The bite makes you grow smaller"
(bite-right-side reg-mushroom-v2)
;; -> "The bite tastes bad"
(bite-left-side reg-mushroom-v2)
;; -> "The bite tastes bad too"