package com.cts.cardservice.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
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

import com.cts.cardservice.exception.CardServiceException;

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
		String baseUrl = serviceInstance.getUri().toString();
		log.info("baseUrl - "+baseUrl);
		return baseUrl;
	}
	
	public void parseExceptionAndThrowIfExists(Throwable e) {
		if (e instanceof HttpClientErrorException) {
			HttpClientErrorException ex = (HttpClientErrorException) e;
			if (HttpStatus.EXPECTATION_FAILED == ex.getStatusCode()) {
				String errorMsg = ex.getResponseBodyAsString();
				try {
					DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
					df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
					df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // c7ompliant
					DocumentBuilder builder = df.newDocumentBuilder();
					InputSource src = new InputSource();
					src.setCharacterStream(new StringReader(errorMsg));

					Document doc = builder.parse(src);
					String msg = doc.getElementsByTagName("errorMessage").item(0).getTextContent();
					if (StringUtils.isNotBlank(msg)) {
						errorMsg = msg;
					}
				} catch (ParserConfigurationException | SAXException | IOException e2) {
					log.error("Error occured ", e2);
				}
				throw new CardServiceException(errorMsg);
			}
		}
	}

}
