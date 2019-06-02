package my;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.reactive.messaging.amqp.AmqpMessage;

@ApplicationScoped
public class AmqpReceiver {

    Connection conn;

    private static final Logger logger = LoggerFactory.getLogger(AmqpReceiver.class);

    // @Incoming("my-amqp-input")
    // @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    // public CompletableFuture<Object> receive(Message<String> message) {
    //     String input = message.getPayload();
    //     CompletableFuture<Object> cf = CompletableFuture.runAsync(new DummyWorker(input))
    //         .handle((s, t) -> { 
    //             if (t == null) {
    //                 // This logger message prints the hashcode usefull to compare 2 messages
    //                 logger.info("sending ack to message with hashcode:" + ((AmqpMessage<String>) message).getAmqpMessage().hashCode());
    //                 message.ack();
    //             } else {
    //                 // usefull if the workers throw an error
    //                 logger.warn("Error, no ack will be sent");
    //             }
    //             return CompletableFuture.runAsync(() -> {});
    //         });
    //     return cf;
    // }

    // @Incoming("my-amqp-input")
    // @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    // public CompletionStage<Message<Void>> receive(Message<String> message) {

    //     String input = message.getPayload();
    //     System.out.println(input + " - " + new Date().getTime());
    //     System.out.println("  processed: " + Thread.currentThread().getName());

    //     CompletableFuture<Message<Void>> cf = new CompletableFuture<>();
    //     return cf;

    // }

    @Incoming("my-amqp-input")
    public CompletionStage<Void> receive(String input) {
        return CompletableFuture.runAsync(new DummyWorker(input));
    }

}