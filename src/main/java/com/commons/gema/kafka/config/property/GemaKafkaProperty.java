package com.commons.gema.kafka.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import lombok.Data;

@Configuration
@EnableKafka

@ConfigurationProperties(prefix = "gema.kafka", ignoreInvalidFields = true)
@Data
public class GemaKafkaProperty {
	private ProducerKafkaConfig producer;
	private ConsumerKafkaConfig consumer;
}
