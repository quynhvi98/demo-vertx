import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class HelloVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloVerticle());
    }

    @Override
    public void start(Future<Void> future) {
        System.out.println("Welcome to Vertx");
    }

    @Override
    public void stop() {
        System.out.println("Shutting down application");
    }

}
