(ns environ.dynamo
  (:require [environ.core :as env]
            [amazonica.aws.dynamodbv2 :as dynamo]
            [overtone.at-at :as at-at]
            [clojure.string :as str]))

(def
  ;^:private
  dynamic-env (atom {}))
(def ^:private poll-id (atom 0))
(def ^:private pool (at-at/mk-pool))

(defn- keywordize [s]
  (-> (str/lower-case s)
      (str/replace "_" "-")
      (str/replace "." "-")
      (keyword)))

(defn- str->int
  [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException _ nil)))

(defn- poll-dynamo
  [id]
  (println "polling with poll ID" id)
  (when (= @poll-id id)
   (let [creds (into {} (keep #(if (val %) %)
                              {:access-key (:environ-dynamo-access-key env/env)
                               :secret-key (:environ-dynamo-secret-key env/env)
                               :endpoint (:environ-dynamo-endpoint env/env)
                               :profile (:environ-dynamo-profile env/env)}))
         table-name (or (:environ-dynamo-table-name env/env) "environProperties")]
     (println "polling with creds:" creds "in table:" table-name)
     (reset! dynamic-env
      (into {}
            (map
             (fn [e] [(keywordize (:key e)) (:value e)])
             (loop [results []]
               (let [limit (or (str->int (:environ-dynamo-poll-limit env/env)) 100)
                     last-result (last results)
                     scan-key (when (not (nil? last-result))
                                {:exclusive-start-key (select-keys last-result [:key])})
                     scan-result (dynamo/scan creds (into {:table-name table-name} scan-key))]
                 (if (= limit (:count scan-result))
                   (recur (into results (:items scan-result)))
                   (into results (:items scan-result))))))))
     (at-at/after (or (str->int (:environ-dynamo-poll-frequency env/env)) 60000)
                  #(poll-dynamo id) pool))))

(defn start-polling!
  []
  (let [pool (at-at/mk-pool)
        id @poll-id]
    (at-at/after 100 #(poll-dynamo id) pool)))

(defn stop-polling!
  []
  (swap! poll-id inc))

(defn env
  []
  (merge env/env @dynamic-env))
