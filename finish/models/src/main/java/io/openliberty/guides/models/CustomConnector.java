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
import javax.inject.Inject;
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
@Connector("custom-connector")
public class CustomConnector implements IncomingConnectorFactory, OutgoingConnectorFactory {

    @Inject
    BeanSource beanSource;

    @Override
    public SubscriberBuilder<? extends Message<?>, Void> getSubscriberBuilder(Config arg0) {
      return ReactiveStreams.<Message<Object>> builder().to(
              new Subscriber<Message<Object>>() {
                @Override
                public void onSubscribe(Subscription s) {
                  s.request(Long.MAX_VALUE);
                }
                @Override
                public void onNext(Message<Object> data) {
                      System.out.println("hello");
                      System.out.println(data);
                      System.out.println(arg0.getValue("topic", String.class));
                      // do something?
                }
                @Override
                public void onError(Throwable t) {
                  t.printStackTrace();
                }
                @Override
                public void onComplete() {
                  System.out.println("Done");
                }
              });
    }

    @Override
    public PublisherBuilder<? extends Message<?>> getPublisherBuilder(Config config) {
      return beanSource.source();
    }


}