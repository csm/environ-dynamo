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
* `:environ-dynamo-table-name` The name of the table to scan. The
  default is "environProperties".
* `:environ-dynamo-poll-interval` The interval between polls, in
  milliseconds. The default is 60000.
* `:environ-dynamo-poll-limit` The number of entries to scan in each
  poll. Default is 100. If this is zero or negative, then polling is
  not done (but the table is read at least once).
  
## Table Format

The DynamoDB table must contain at least two fields: `key` and
`value`. The `key` field is taken as the key, and is massaged into a
keyword the same way `environ` does: lowercased, with `_` or `.`
replaced by `-`; the `key` field should also be the partition key of
the table. The `value` field should contain strings.
  
## Usage

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

## Thanks

Inspired by [archaius](https://github.com/Netflix/archaius).
