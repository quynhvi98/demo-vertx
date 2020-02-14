package rest;

import io.vertx.blog.first.MyFirstVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoThreadingModel extends AbstractVerticle {

//    final Map<String, AtomicInteger> threadCounts = new ConcurrentHashMap<>();

    private final Map<String, AtomicInteger> threadCounts = new ConcurrentHashMap<>();

    MyVerticle(Map<String, AtomicInteger> threadCounts) {
        this.threadCounts = threadCounts;
    }

    @Override
    public void start() {
        threadCounts.computeIfAbsent(Thread.currentThread().getName(),
                t -> new AtomicInteger(0)).incrementAndGet();
    }
}
