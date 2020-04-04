package com.commons.gema.kafka.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.commons.gema.kafka.config.property.GemaKafkaProperty;

@Configuration
@ConditionalOnProperty(prefix = "gema.kafka.consumer", name = "enabled", matchIfMissing = false)
public class ConsumerConfigKafka {
	@Autowired
	private GemaKafkaProperty gemaKafkaProperty;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, gemaKafkaProperty.getConsumer().getBootstrapServer());
		if (Objects.nonNull(gemaKafkaProperty.getConsumer().getGroupId())
				&& !gemaKafkaProperty.getConsumer().getGroupId().isBlank()) {
			props.put(ConsumerConfig.GROUP_ID_CONFIG, gemaKafkaProperty.getConsumer().getGroupId());
		}
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
}
