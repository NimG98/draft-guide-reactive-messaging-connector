package io.openliberty.guides.models;

import io.reactivex.processors.BehaviorProcessor;

import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

@ApplicationScoped
public class BeanSource {

    CompletableFuture<Message<String>> future = new CompletableFuture<>();

    BehaviorProcessor<Message<String>> processor = BehaviorProcessor.create();

    public void publishMessage(String payload) {
        System.out.println("payload" + payload);
        System.out.printf("publishMessage, payload=%s\n", payload);
        Message<String> msg = Message.of(payload);
        processor.onNext(msg);
    }

    public void done() {
        future.complete(Message.of("End of transmission"));
    }

    public PublisherBuilder<? extends Message<?>> source() {
        PublisherBuilder<? extends Message<?>> publisherBuilder = ReactiveStreams.fromPublisher(processor);
        return publisherBuilder;
    }
}