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
2019-06-02 11:15:18,601 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-5) received message with payload: hello! hello 5
2019-06-02 11:15:18,601 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) starting long task hello! hello 5
2019-06-02 11:15:19,602 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) long task finished hello! hello 5
2019-06-02 11:15:19,603 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-3) received message with payload: hello! hello 7
2019-06-02 11:15:19,603 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) starting long task hello! hello 7
2019-06-02 11:15:20,604 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) long task finished hello! hello 7
2019-06-02 11:15:20,605 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-5) received message with payload: hello! hello 6
2019-06-02 11:15:20,605 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) starting long task hello! hello 6
2019-06-02 11:15:21,606 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-3) long task finished hello! hello 6
2019-06-02 11:15:21,607 INFO  [my.AmqpReceiver] (ForkJoinPool.commonPool-worker-3) received message with payload: hello! hello 7
2019-06-02 11:15:21,607 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) starting long task hello! hello 7
2019-06-02 11:15:22,608 INFO  [my.DummyWorker] (ForkJoinPool.commonPool-worker-5) long task finished hello! hello 7
```
Here the message with payload `hello 7` was received twice.
