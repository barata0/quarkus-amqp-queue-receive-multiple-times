package my;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.annotations.Stream;

@Path("/app")
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    @Inject
    @Stream("my-amqp-output")
    private Emitter<String> emitter;

    /**
     * Emit a new Hello Message to the stream (AMQP queue)!
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        String s = "Hello " + ATOMIC_INTEGER.incrementAndGet() + "!";
        emitter.send(s);
        return s;
    }
    
    /**
     * Process all the messages on the queue
     * @param input
     * @return
     */
    @Incoming("my-amqp-input")
    public CompletionStage<Void> receive(String input) {
        logger.info("received message with payload: " + input);
        return CompletableFuture.runAsync(new DummyWorker(input));
    }

}