(ns tegere.grammar-fiddle
  "Fiddle file for playing around with grammar.clj."
  (:require [clojure.string :as string]
            [tegere.grammar :refer :all]
            [instaparse.core :as insta]))


(def monkey-feature
  (str
   "# This is a comment about this feature\n"
   "\n"
   "@monkeys\n"
   "Feature: Monkeys behave as expected\n"
   "  Experimenters want to ensure that their monkey simulations are behaving\n"
   "  correctly.\n"
   "\n"
   "\n"
   "  # This is a comment about this scenario ...\n"
   "  @fruit-reactions\n"
   "  Scenario Outline: Monkeys behave as expected when offered various foods.\n"
   "    Given a monkey\n"
   "    When I give him a <fruit>\n"
   "    Then he is <response>\n"
   "    But he doesn't eat it\n"
   "    And he looks at me <manner_of_looking>\n"
   "   \n"
   "   \n"
   "  Examples: monkey characteristics:\n"
   "  | fruit  | response  | manner_of_looking  |\n"
   "  | banana | happy     | quizzically        |\n"
   "  | pear   | sad       | loathingly         |\n"
   "   \n"
   "  # This is a comment about this scenario outline...\n"
   "\n"
   "  @orangutan\n"
   "  Scenario: Monkeys adore orangutans.\n"
   "    Given a monkey\n"
   "    When I present him with an orangutan\n"
   "    Then he is happy\n"
   "\n"
   "\n"))

(comment

  (step-label-prsr "Given")

  (step-label-prsr "When")

  (step-label-prsr "Then")

  (step-label-prsr "But")

  (step-prsr " Given a monkey\n")

  (step-prsr " When I give him a banana\n")

  (step-prsr " Then he is happy\n")

  (step-prsr " But he doesn't eat it\n")

  (step-prsr " And he looks at me quizzically\n")

  (step-block-prsr
   (str
    " Given a monkey\n"
    " When I give him a banana\n"
    " Then he is happy\n"
    " But he doesn't eat it\n"
    " And he looks at me quizzically\n"
    ))

  ;; comments work inside step blocks and scenario outline step blocks:
  (step-block-prsr
   (str
    " Given a monkey\n"
    " #When I give him a banana\n"
    " # Then he is happy\n"
    "    #But he doesn't eat it\n"
    "    #    And he looks at me quizzically\n"))

  ;; comments work inside step blocks and scenario outline step blocks:
  (so-step-block-prsr
   (str
    " Given a monkey\n"
    " #When I give him a banana\n"
    " Then he is happy\n"
    " But he doesn't eat it\n"
    " And he looks at me quizzically\n"))

  (scenario-line-prsr
   " Scenario: Monkeys are cautious when offered food.\n")

  (scenario-outline-line-prsr
   " Scenario Outline: Monkeys are cautious when offered food.\n")

  (tag-prsr "@ab-c_d")

  (tag-set-prsr "@ab-c_d")

  (tag-set-prsr "@ab-c_d @dog @cat")

  (tag-line-prsr "  @ab-c_d @dog @cat\n")

  (scenario-prsr
   (str
    "  Scenario: Monkeys are cautious when offered food.\n"
    "    Given a monkey\n"
    "    When I give him a banana\n"
    "    Then he is happy\n"
    "    But he doesn't eat it\n"
    "    And he looks at me quizzically\n"
    ))

  (scenario-prsr
   (str
    "  @monkeys @caution-tests\n"
    "  Scenario: Monkeys are cautious when offered food.\n"
    "    Given a monkey\n"
    "    When I give him a banana\n"
    "    Then he is happy\n"
    "    But he doesn't eat it\n"
    "    And he looks at me quizzically\n"
    ))

  (scenario-outline-prsr
   (str
    "  @monkeys @caution-tests\n"
    "  Scenario Outline: Monkeys are cautious when offered food.\n"
    "    Given a monkey\n"
    "    When I give him a banana\n"
    "    Then he is happy\n"
    "    But he doesn't eat it\n"
    "    And he looks at me quizzically\n"
    "   \n"
    "  Examples: monkey characteristics:\n"
    "  | h1  | h2  | h3  |\n"
    "  | d10 | d20 | d30 |\n"
    "  | d11 | d21 | d31 |\n"
    ))

  (examples-line-prsr " Examples: monkey characteristics:\n")

  (table-row-prsr " | a | b | c |\n")

  (table-prsr
   (str
    " | h1  | h2  | h3  |\n"
    " | d10 | d20 | d30 |\n"
    " | d11 | d21 | d31 |\n"
    ))

  (examples-prsr
   (str
    " Examples: monkey characteristics:\n"
    " | h1  | h2  | h3  |\n"
    " | d10 | d20 | d30 |\n"
    " | d11 | d21 | d31 |\n"
    ))

  (feature-line-prsr "Feature: Monkeys behave as expected\n")

  (feature-description-block-prsr
   (str
    " And my feature is so cool\n"
    " because blah blah blah\n"))

  (feature-block-prsr
   (str
    "@monkeys\n"
    "Feature: Monkeys behave as expected\n"
    " And my feature is so cool\n"
    " because blah blah blah\n"
    )
   )

  (feature-block-prsr
   (str
    "Feature: Monkeys behave as expected\n"
    " And my feature is so cool\n"
    " because blah blah blah\n"
    )
   )

  (feature-block-prsr
   (str
    "Feature: Monkeys behave as expected\n"
    " And my feature is so cool\n"
    )
   )

  (feature-block-prsr
   (str
    "Feature: Monkeys behave as expected\n"
    )
   )

  (feature-prsr
   (str
    "@monkeys\n"
    "Feature: Monkeys behave as expected\n"
    "  And my feature is so cool\n"
    "  because blah blah blah\n"
    "\n"
    "  @monkeys @caution-tests\n"
    "  Scenario Outline: Monkeys are cautious when offered food.\n"
    "    Given a monkey\n"
    "    When I give him a banana\n"
    "    Then he is happy\n"
    "    But he doesn't eat it\n"
    "    And he looks at me quizzically\n"
    "   \n"
    "  Examples: monkey characteristics:\n"
    "  | h1  | h2  | h3  |\n"
    "  | d10 | d20 | d30 |\n"
    "  | d11 | d21 | d31 |\n"
    "   \n"
    "  @monkeys @caution-tests @a.b.<c>\n"
    "  Scenario: Monkeys are cautious when offered food.\n"
    "    Given a monkey\n"
    "    When I give him a banana\n"
    "    Then he is happy\n"
    "    But he doesn't eat it\n"
    "    And he looks at me quizzically\n"
    ))

  (feature-prsr monkey-feature)

  ;; Show that we can parse feature files that do not end with a newline.
  (feature-prsr (string/trim monkey-feature))

  (let [real-feature
        (slurp (.getPath (clojure.java.io/resource "sample.feature")))]
    (feature-prsr real-feature))

  (let [real-feature
        (slurp (.getPath (clojure.java.io/resource "sample.feature")))
        parse (feature-prsr real-feature)
        [root-label & nodes] parse]
    (-> parse
        count
        )
    (map #(if (keyword? %) % (first %)) parse)
    (map type parse)
    nodes
    (->> nodes
         (map first)
         (filter (fn [x] (not= x :IGNORED_LINE))))
    parse
    )

  (so-step-prsr "Given a <modifier> monkey\n")

  (so-step-prsr "Given a <modifier> monkey of <qualifier> character\n")

  (so-step-block-prsr
   (str
    "Given a <modifier> monkey of <qualifier> character\n"
    "When experimenter approaches <adverb>\n"))

  feature-grmr

)
