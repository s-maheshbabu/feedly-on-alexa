package com.feedlyonalexa.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {
	public static com.mashape.unirest.http.ObjectMapper streamContentsObjectMapper() {
		if (streamContentsObjectMapper == null) {
			streamContentsObjectMapper = new com.mashape.unirest.http.ObjectMapper() {
				private ObjectMapper jacksonObjectMapper = getJacksonItemObjectMapper();
				
				public <StreamContents> StreamContents readValue(String value, Class<StreamContents> valueType) {
			        try {
			            return jacksonObjectMapper.readValue(value, valueType);
			        } catch (IOException e) {
			            throw new RuntimeException("Error parsing Feedly response", e);
			        }
			    }

			    public String writeValue(Object value) {
			        try {
			            return jacksonObjectMapper.writeValueAsString(value);
			        } catch (JsonProcessingException e) {
			            throw new RuntimeException("Error parsing Feedly response", e);
			        }
			    }
			};
		}
		return streamContentsObjectMapper;
	}

	private static ObjectMapper getJacksonItemObjectMapper()
	{
		ObjectMapper jacksonObjectMapper = new ObjectMapper();
		jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		return jacksonObjectMapper;
	}
	private static com.mashape.unirest.http.ObjectMapper streamContentsObjectMapper = null;
	
	public static ObjectMapper getInstance() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return mapper;
	}
	private static ObjectMapper mapper = null;
}