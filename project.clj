(defproject environ-dynamo "0.1.0"
  :description "Extension to environ that polls DynamoDB"
  :url "https://github.com/csm/environ-dynamo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ "1.0.3"]
                 [amazonica "0.3.67"]
                 [overtone/at-at "1.2.0"]])
