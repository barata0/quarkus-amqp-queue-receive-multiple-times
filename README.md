# Quarkus AMQP Message error for long task runners

## Description
When consuming messages for an AMQP queue the message must be processed only once no matter how many clients are connected.
For long duration task runeers, this message is delivered once per thread on the ForkJoinPool.commonPool-worker-*

## How to reproduce
Start an amqp server on localhost

Currently using _apache-activemq-5.15.0_

Run the quarkus project

```
./mvnw compile quarkus:dev 
```

Create some messages

```
curl localhost:8080/app 
```

Check the log messages. There will be more than ONE long task started for the same message

```
2019-06-02 12:17:41,025 INFO  [my.App] (ForkJoinPool.commonPool-worker-5) received message with payload: Hello 7!
2019-06-02 12:17:41,025 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) starting long task Hello 7!
2019-06-02 12:17:42,026 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) long task finished Hello 7!
2019-06-02 12:17:42,027 INFO  [my.App] (ForkJoinPool.commonPool-worker-3) received message with payload: Hello 5!
2019-06-02 12:17:42,027 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) starting long task Hello 5!
2019-06-02 12:17:43,028 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) long task finished Hello 5!
2019-06-02 12:17:43,029 INFO  [my.App] (ForkJoinPool.commonPool-worker-5) received message with payload: Hello 6!
2019-06-02 12:17:43,029 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) starting long task Hello 6!
2019-06-02 12:17:44,030 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) long task finished Hello 6!
2019-06-02 12:17:44,031 INFO  [my.App] (ForkJoinPool.commonPool-worker-3) received message with payload: Hello 7!
2019-06-02 12:17:44,032 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) starting long task Hello 7!
2019-06-02 12:17:45,032 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) long task finished Hello 7!
```

Here the message with payload `hello 7` was received twice.
