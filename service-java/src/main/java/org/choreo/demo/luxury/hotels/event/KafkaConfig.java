package org.choreo.demo.luxury.hotels.event;

import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    private Map<String, Object> commonConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put("bootstrap.servers", System.getenv("BOOTSTRAP_SERVERS"));
        configProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configProps.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        return configProps;
    }

    @Bean
    @ConditionalOnProperty(name = "PREFERRED_BROKER", havingValue = "confluent")
    public ProducerFactory<?, ?> confluentProducerFactory() {
        Map<String, Object> configProps = commonConfig();
        configProps.put("sasl.mechanism", "PLAIN");
        configProps.put("security.protocol", "SASL_SSL");
        configProps.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username='"
                        + System.getenv("KAFKA_USERNAME") + "' password='" + System.getenv("KAFKA_PASSWORD") + "';");
        logger.info("CONFLUENT ################: " + configProps);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    @ConditionalOnProperty(name = "PREFERRED_BROKER", havingValue = "local-broker")
    public ProducerFactory<?, ?> defaultProducerFactory() {
        Map<String, Object> configProps = commonConfig();
        logger.info("LOCAL ################: " + configProps);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
}