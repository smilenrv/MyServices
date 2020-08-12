package com.cts.customerservice.config;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "config.prop")
@Data
public class CustomerConfig {

	private HashMap<String, String> errMessage;
	private String customerNotvailable;

}
