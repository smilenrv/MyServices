package com.cts.paymentservice.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cts.paymentservice.exception.PaymentServiceException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UtilityService {

	private LoadBalancerClient loadBalancer;

	public UtilityService(LoadBalancerClient loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	public String getServiceUrl(String svc) {
		ServiceInstance serviceInstance = loadBalancer.choose(svc);
		String baseUrl = serviceInstance.getUri().toString();;
		log.info("baseUrl - "+baseUrl);
		return baseUrl;
	}
	
	public void parseExceptionAndThrowIfExists(Throwable e) {
		if (e instanceof HttpClientErrorException) {
			HttpClientErrorException ex = (HttpClientErrorException) e;
			if (HttpStatus.EXPECTATION_FAILED == ex.getStatusCode()) {
				String errorMsg = ex.getResponseBodyAsString();
				DocumentBuilder builder;
				try {
					builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					InputSource src = new InputSource();
					src.setCharacterStream(new StringReader(errorMsg));

					Document doc = builder.parse(src);
					String msg = doc.getElementsByTagName("errorMessage").item(0).getTextContent();
					if(StringUtils.isNotBlank(msg)) {
						errorMsg = msg;
					}
				} catch (ParserConfigurationException | SAXException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				throw new PaymentServiceException(errorMsg);
			}
		}
	}

}
