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
2019-06-02 11:19:16,434 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-5) received message with payload: hello 7
2019-06-02 11:19:16,435 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) starting long task hello 7
2019-06-02 11:19:17,435 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) long task finished hello 7
2019-06-02 11:19:17,437 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-3) received message with payload: hello 3
2019-06-02 11:19:17,437 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) starting long task hello 3
2019-06-02 11:19:18,438 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) long task finished hello 3
2019-06-02 11:19:18,439 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-5) received message with payload: hello 4
2019-06-02 11:19:18,440 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) starting long task hello 4
2019-06-02 11:19:19,440 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) long task finished hello 4
2019-06-02 11:19:19,441 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-3) received message with payload: hello 5
2019-06-02 11:19:19,442 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) starting long task hello 5
2019-06-02 11:19:20,442 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) long task finished hello 5
2019-06-02 11:19:20,443 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-5) received message with payload: hello 6
2019-06-02 11:19:20,444 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) starting long task hello 6
2019-06-02 11:19:21,444 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) long task finished hello 6
2019-06-02 11:19:21,445 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-3) received message with payload: hello 7
2019-06-02 11:19:21,446 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) starting long task hello 7
2019-06-02 11:19:22,446 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) long task finished hello 7
```
Here the message with payload `hello 7` was received twice.
