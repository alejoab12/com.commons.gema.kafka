package com.commons.gema.kafka.config.property;

import lombok.Data;

@Data
public class ConsumerKafkaConfig {
	private boolean enabled;
	private String bootstrapServer;
	private String groupId;

}
