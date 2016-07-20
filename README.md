# environ-dynamo

A simple extension to
[environ](https://github.com/weavejester/environ) that polls a
DynamoDB table for configuration overrides.

## Configuration

You config `environ-dynamo` via environ itself; the following environ
keys are used:

* `:environ-dynamo-access-key` Your AWS access key.
* `:environ-dynamo-secret-key` Your AWS secret key.
* `:environ-dynamo-endpoint` The AWS endpoint to use (may also be used
  to specify the AWS region).
* `:environ-dynamo-profile` The credential profile to use, in lieu of
  access/secret keys.
* `:environ-dynamo-poll-interval` The interval between polls, in
  milliseconds. The default is 60000.
* `:environ-dynamo-poll-limit` The number of entries to scan in each
  poll. Default is 100.
  
# Usage

```clojure
(ns example
  (:require [environ.dynamo :as env]))
  
;;; Starts polling DynamoDB.
(env/start-polling!)

;;; Results from DynamoDB are merged with environ.core/env
(:some-config (env/env))

;;; You can stop polling too.
(env/stop-polling!)
```
