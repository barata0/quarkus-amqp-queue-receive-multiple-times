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

    @Incoming("my-amqp-input")
    public CompletionStage<Void> receive(String input) {
        logger.info("received message with payload: " + input);
        return CompletableFuture.runAsync(new DummyWorker(input));
    }

}