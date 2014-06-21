(ns turing-machine.core
  (:gen-class)
)

(defn rule-splitter [s]
  (clojure.string/split (str s " ")  #" +"))

(defn prep-rules [raw-rules]
  (def fl-rules (remove empty? raw-rules ))
  (defn rules-map-item [rule-vect rule-map]
    (if (empty? rule-vect)
      rule-map
      (recur (rest rule-vect) 
        (assoc rule-map 
            [(get rule-vect 0) (get rule-vect 1)] 
            {:write (get rule-vect 2) :move (get rule-vect 3) :nextstate (get rule-vect 4)})))))

(defn rule-from-string [rule-map str-rule]
  (def rule-vect (clojure.string/split (str str-rule " ")  #" +"))
  ;(println (str "rule vector=" rule-vect))
  (if (empty? str-rule)
    rule-map
    (assoc rule-map 
        [(get rule-vect 0) (get rule-vect 1)] 
        {:write (first (get rule-vect 2)) :move (get rule-vect 3) :nextstate (get rule-vect 4)})))

(defn print-tape [left-side right-side]
  (println (str (clojure.string/join "" (reverse left-side)) (clojure.string/join right-side))))
 
(defn get-lazy-machine [filename]
  (let [tape-atom (atom ['() '()])]
    (with-open [rdr (clojure.java.io/reader filename)]
      (do 
        (def head-pos (Integer. (first (line-seq rdr))))
        (def tape1
          (split-at head-pos (seq (first (line-seq rdr)))))
        (def tape
          [(reverse (first tape1)) (second tape1)])

        (def rules
          (reduce rule-from-string {} (doall (line-seq rdr))))

        (println head-pos)
        (println tape)
        (println rules)

        (defn- get-next-state [[left-side right-side state]]
          (def rule (val (find rules [state (str (first left-side))]))) 
            (if (= "R" (get rule :move)) 

              [ (conj (conj (rest left-side) (get rule :write)) (first right-side)) 
                (rest right-side) 
                (get rule :nextstate)]
              [ (rest left-side) 
                (conj right-side (get rule :write)) 
                (get rule :nextstate)]))

        (defn- lazy-state-seq [[ left-side right-side state]]
          (cons 
            [left-side right-side state] 
            (lazy-seq (lazy-state-seq (get-next-state [left-side right-side state])))))

        (lazy-state-seq [(first tape) (second tape) "0"])))))

(defn run-machine [filename]
  (def lazy-tm (get-lazy-machine filename))
  (defn- print-tape-w [mst]
    (do
      (print-tape (first mst) (second mst))
      (Thread/sleep 10)))

  (doseq [mst (get-lazy-machine filename)] (print-tape-w mst)))


(defn -main
  [& args]
  (if args
    (run-machine (first args))
    (println "Usage: java turing-machine-0.1.0-SNAPSHOT.jar <turing instructions file>"))) 
