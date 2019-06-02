package my;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.annotations.Stream;

@Path("/app")
public class App {

    // used to generate unique messages
    private static int helloCount = 0;
    private static synchronized String nextHello() {
        return "hello " + helloCount++;
    }

    @Inject
    @Stream("my-amqp-output")
    Emitter<String> emitter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws InterruptedException {
        String s = nextHello();
        emitter.send(s); // send the message to amq with unique payload for debugging
        return "hello sent: " + s;
    }
}