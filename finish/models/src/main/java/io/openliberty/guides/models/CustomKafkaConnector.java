// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.models;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.spi.*;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.eclipse.microprofile.reactive.streams.operators.SubscriberBuilder;

import org.reactivestreams.*;

@ApplicationScoped
@Connector("custom-kafka")
public class CustomKafkaConnector implements IncomingConnectorFactory, OutgoingConnectorFactory {

    /* @Override
    public SubscriberBuilder<? extends Message<?>, Void> getSubscriberBuilder(Config arg0) {
      return ReactiveStreams.<Message<Object>> builder().to(
              new Subscriber<Message<Object>>() {
                @Override
                public void onSubscribe(Subscription s) {
                  s.request(Long.MAX_VALUE);
                }
                @Override
                public void onNext(Message<Object> integer) {
                      System.out.println("hello");
                      System.out.println(arg0.toString());
                      System.out.println(arg0.getValue("topic", String.class));
                }
                @Override
                public void onError(Throwable t) {
                }
                @Override
                public void onComplete() {
                }
              });
    } */

    private List<String> elements = new CopyOnWriteArrayList<>();

    /**
     * Stores the received configs.
     */
    private List<Config> configs = new CopyOnWriteArrayList<>();

    List<String> elements() {
        return elements;
    }

    List<Config> getReceivedConfigurations() {
        return configs;
    }

    @Override
    public SubscriberBuilder<? extends Message<?>, Void> getSubscriberBuilder(Config config) {
        System.out.println("OutgoingConnector - getSubscriberBuilder");
        // Check mandatory attributes
        System.out.println(config.getValue(CHANNEL_NAME_ATTRIBUTE, String.class));
        System.out.println(config.getValue(CONNECTOR_ATTRIBUTE, String.class));

        configs.add(config);
        System.out.println(configs);

        // Would throw a NoSuchElementException if not set.
        config.getValue("bootstrap.servers", String.class);
        config.getValue("topic", String.class);

        return ReactiveStreams.<Message<String>>builder().map(Message::getPayload).forEach(s -> elements.add(s));
    }

    @Override
    public PublisherBuilder<? extends Message<?>> getPublisherBuilder(Config config) {
        System.out.println("IncomingConnector - getPublisherBuilder");
        configs.add(config);
        System.out.println(configs);
        String[] values = config.getValue("items", String.class).split(",");
        System.out.println(values);

        // Check mandatory attributes
        System.out.println(config.getValue(CHANNEL_NAME_ATTRIBUTE, String.class));
        System.out.println(config.getValue(CONNECTOR_ATTRIBUTE, String.class));

        // Would throw a NoSuchElementException if not set.
        config.getValue("bootstrap.servers", String.class);
        config.getValue("topic", String.class);
        config.getValue("group.id", String.class);

        return ReactiveStreams.fromIterable(Arrays.asList(values)).map(Message::of);
    }
}