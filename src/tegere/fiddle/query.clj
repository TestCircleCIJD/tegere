(ns tegere.fiddle.query
  (:require [tegere.loader :as l]
            [tegere.parser :as p]
            [tegere.query :as query]))

(comment

  ;; Experiment with pasing ``:tegere.query/query-tree`` expressions to
  ;; ``:tegere.query/query`` here. The output should be a list of all-tags
  ;; vectors. Each vector is the set of tags belonging to a scenario that maches
  ;; the supplied query.
  (let [features (l/load-feature-files "examples/apes/src/apes/features")
        extract-all-scenario-tags
        (fn [features]
          (mapcat (fn [f]
                    (->> f
                         :tegere.parser/scenarios
                         (map :tegere.query/tags)))
                  features))
        where
        '(and
          (or "chimpanzees" "bonobos")
          (or "fruit=banana" "fruit=pear"))]
    (->> (query/query features where)
         extract-all-scenario-tags))

  (query/user-query->datalog-query
   "a")

  (query/user-query->datalog-query
   '(and
     (or "chimpanzees" "bonobos")
     (or "fruit=banana" "fruit=pear")))

  (query/user-query->datalog-query
   '(not "a"))

  (query/user-query->datalog-query
   '(and "a" "b"))

  (query/user-query->datalog-query
   '(or "a" "b"))

  (query/user-query->datalog-query
   '(and
     (and "a" "b")
     (and "c" "d")))

  (query/user-query->datalog-query
   '(and
     (and
      (and "a" "b")
      (or "e" "f"))
     (or "c" "d")))

  (query/user-query->datalog-query
   '(and "a" "b"))

  (query/user-query->datalog-query
   '(or "a" "b"))

  ;; This should evaluate to true, showing the equivalence of the all
  ;; ``:tegere.query/query-tree`` and the old-style query expressions in each
  ;; ``where`` set under the ``where-sets`` binding.
  (let [features (l/load-feature-files "examples/apes/src/apes/features")
        extract-all-scenario-tags
        (fn [features]
          (mapcat (fn [f]
                    (->> f
                         :tegere.parser/scenarios
                         (map :tegere.query/tags)))
                  features))
        where-sets
        [["bonobos"
          (p/parse-old-style-tag-expression "@bonobos")
          (p/parse-old-style-tag-expression "bonobos")]
         ['(not "bonobos")
          (p/parse-old-style-tag-expression "~@bonobos")
          (p/parse-old-style-tag-expression "~bonobos")]
         ['(or "fruit=banana" (not "chimpanzees"))
          (p/parse-old-style-tag-expression "fruit=banana,~@chimpanzees")]]]
    (apply
     =
     (for [ws where-sets]
       (apply
        =
        (for [where ws]
          (->> (query/query features where) extract-all-scenario-tags))))))

)
