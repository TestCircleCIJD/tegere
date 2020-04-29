(ns tegere.cli2
  (:require [clojure.string :as str]
            [clojure.tools.cli :as cli]
            [clojure.set :as set]
            [me.raynes.fs :as fs]
            [tegere.runner :as r]))

(defn- update-with-merge [opts id val]
  (update opts id merge val))

(defn- parse-data [data]
  (let [[k v] (str/split data #"=" 2)]
    {(keyword k) v}))

(defn- setify-with-comma-split [s]
  (let [[x & y :as z] (str/split s #",\s*")]
    (if y (set z) (set [x]))))

(defn- process-tags-directive
  [opts _ val]
  (update-in
   opts
   [::r/tags (if (= 1 (count val)) ::r/and-tags ::r/or-tags)]
   set/union
   val))

(def cli-options
  [["-h" "--help" :id ::r/help]
   ["-s" "--stop" :default false :id ::r/stop]
   ["-v" "--verbose" :default false :id ::r/verbose]
   ["-t" "--tags TAGS" "Tags to control which features are executed"
    :assoc-fn process-tags-directive
    :parse-fn setify-with-comma-split
    :id ::r/tags]
   ["-D" "--data KEYVAL" "Data in key=val format to pass to Apes Gherkin"
    :assoc-fn update-with-merge
    :parse-fn parse-data
    :id ::r/data]])

(defn usage [options-summary]
  (->> ["TeGere Runner."
        ""
        "Usage: tegere-runner [options] features-path"
        ""
        "Options:"
        options-summary
        ""
        "features-path: path to directory with Gherkin feature files."]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn features-path-exists?
  [features-path]
  (and features-path (fs/directory? features-path)))

(def default-config
  {::r/stop false
   ::r/verbose false
   ::r/data {}
   ::r/tags {::r/and-tags #{} ::r/or-tags #{}}
   ::r/features-path "."})

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with a error message, and optional ok status), or a
  ``:tegere.runner/config``config map."
  [args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) ; help => exit OK with usage summary
      {:exit-message (usage summary) :ok? true}
      errors ; errors => exit with description of errors
      {:exit-message (error-msg errors)}
      (features-path-exists? (first arguments))
      (merge default-config
             {::r/features-path (first arguments)}
             options)
      :else ; failed custom validation => exit with usage summary
      {:exit-message (usage summary)})))

(comment

  (validate-args ["--help"])

  (validate-args ["examples/apes/src/apes/features" "--stop"])

  (validate-args
   ["examples/apes/src/apes/features" "-Durl=http://www.url.com"
    "-Dpassword=1234" "--tags=chimpanzees"])

  (validate-args
   ["-Durl=http://www.url.com" "-Dpassword=1234" "--tags=dogs" "--tags=chimpanzees"
    "examples/apes/src/apes/features"])

  (validate-args
   ["-Durl=http://www.url.com" "-Dpassword=1234" "--stop" "--tags=dogs"
    "--tags=chimpanzees" "examples/apes/src/apes/features"])

  (validate-args
   ["examples/apes/src/apes/features" "-Durl=http://www.url.com"
    "-Dpassword=1234" "--stop" "--tags=dogs" "--tags=chimpanzees"])

  (validate-args
   ["examples/apes/src/apes/features" "-Durl=http://www.url.com"
    "-Dpassword=1234" "--stop" "--tags=dogs" "--tags=chimpanzees"
    "--tags=apple,orange,papaya"])

  (validate-args
   ["examples/apes/src/apes/features" "-Durl=http://www.url.com"
    "-Dpassword=1234" "--stop" "--tags=dogs" "--tags=chimpanzees"
    "--tags=apple,orange,papaya" "--tags=apple,orange,banana"])

)
