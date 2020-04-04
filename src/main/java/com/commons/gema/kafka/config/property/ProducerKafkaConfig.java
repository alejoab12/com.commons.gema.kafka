package com.commons.gema.kafka.config.property;

import lombok.Data;

@Data
public class ProducerKafkaConfig {
	private boolean enabled;
	private String bootstrapServer;
}
