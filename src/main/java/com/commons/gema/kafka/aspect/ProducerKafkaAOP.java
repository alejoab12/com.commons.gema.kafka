package com.commons.gema.kafka.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.commons.gema.kafka.aspect.annotation.GenerateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class ProducerKafkaAOP {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Value("${spring.application.name}")
	private String applicationName;

	@AfterReturning(pointcut = "@annotation(com.commons.gema.kafka.aspect.annotation.GenerateEvent)", returning = "retVal")
	public Object sendEvent(JoinPoint jp, Object retVal) throws JsonProcessingException {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		GenerateEvent annotation = signature.getMethod().getAnnotation(GenerateEvent.class);
		Object message = null;
		if (annotation.params().length == 1) {
			message = jp.getArgs()[annotation.params()[0]];
		} else {
			List<Object> listMessage = new ArrayList<>(annotation.params().length);
			for (Integer index : annotation.params()) {
				listMessage.add(jp.getArgs()[index]);
			}
			message = listMessage;
		}
		if (Objects.nonNull(jp.getArgs()) && jp.getArgs().length > 0) {

			if (annotation.value().isBlank()) {
				kafkaTemplate.send(applicationName, signature.getName(),
						new ObjectMapper().writeValueAsString(message));
			} else if (!annotation.key().isBlank()) {
				kafkaTemplate.send(annotation.value(), signature.getName(),
						new ObjectMapper().writeValueAsString(message));
			} else {
				kafkaTemplate.send(annotation.value(), annotation.key(),
						new ObjectMapper().writeValueAsString(message));
			}

		}
		return retVal;

	}
}
