// that's just an example from my project, we'll discuss it later

//package be.kdg.common.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
// @Configuration
//public class RabbitMQTopology {
//
//    public static final String DELIVERY_EXCHANGE = "kdg.events";
//
//    public static final String ORDER_ACCEPTED_KEY_TEMPLATE = "restaurant.%s.order.accepted.v1";
//    public static final String ORDER_READY_KEY_TEMPLATE = "restaurant.%s.order.ready.v1";
//
//    public static final String ORDER_PICKED_UP_KEY = "delivery.*.order.pickedup.v1";
//    public static final String ORDER_LOCATION_KEY = "delivery.*.order.location.v1";
//    public static final String ORDER_DELIVERED_KEY = "delivery.*.order.delivered.v1";
//
//    public static final String ORDER_PICKED_UP_QUEUE = "delivery.order.pickedup";
//    public static final String ORDER_LOCATION_QUEUE = "delivery.order.location";
//    public static final String ORDER_DELIVERED_QUEUE = "delivery.order.delivered";
//    public static final String ORDER_ACCEPTED_QUEUE = "delivery.order.accepted";
//    public static final String ORDER_READY_QUEUE = "delivery.order.ready";
//
//
//    @Bean
//    public TopicExchange deliveryExchange() {
//        return ExchangeBuilder.topicExchange(DELIVERY_EXCHANGE).durable(true).build();
//    }
//
//    @Bean
//    Queue pickedUpQueue() {
//        return QueueBuilder.durable(ORDER_PICKED_UP_QUEUE).build();
//    }
//
//    @Bean
//    Queue locationQueue() {
//        return QueueBuilder.durable(ORDER_LOCATION_QUEUE).build();
//    }
//
//    @Bean
//    Queue deliveredQueue() {
//        return QueueBuilder.durable(ORDER_DELIVERED_QUEUE).build();
//    }
//
//    @Bean
//    Binding bindPickedUp() {
//        return BindingBuilder.bind(pickedUpQueue())
//                .to(deliveryExchange())
//                .with(ORDER_PICKED_UP_KEY);
//    }
//
//    @Bean
//    Binding bindLocation() {
//        return BindingBuilder.bind(locationQueue())
//                .to(deliveryExchange())
//                .with(ORDER_LOCATION_KEY);
//    }
//
//    @Bean
//    Binding bindDelivered() {
//        return BindingBuilder.bind(deliveredQueue())
//                .to(deliveryExchange())
//                .with(ORDER_DELIVERED_KEY);
//    }
//    @Bean
//    Queue acceptedQueue() {
//        return QueueBuilder.durable(ORDER_ACCEPTED_QUEUE).build();
//    }
//
//    @Bean
//    Queue readyQueue() {
//        return QueueBuilder.durable(ORDER_READY_QUEUE).build();
//    }
//
//    @Bean
//    Binding bindAccepted() {
//        return BindingBuilder.bind(acceptedQueue())
//                .to(deliveryExchange())
//                .with("restaurant.*.order.accepted.v1");
//    }
//
//    @Bean
//    Binding bindReady() {
//        return BindingBuilder.bind(readyQueue())
//                .to(deliveryExchange())
//                .with("restaurant.*.order.ready.v1");
//    }
//}
