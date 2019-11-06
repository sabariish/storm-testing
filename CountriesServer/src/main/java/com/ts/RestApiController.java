package com.ts;

import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);
	
	@Autowired
	 RestTemplate restTemplate;
	 
	 
	 @Value("${operations.restURL.countryCode}")
	 public String countryCodeURL;
	 
	 @Value("${operations.restURL.countryName}")
	 String countryNameURL;
	 
	 //Consuming a service by GET method
	 @GetMapping("/countryCode/{code}")
	 @ResponseBody
	 public ResponseEntity<Object> getCapitalByCountryCode(@PathVariable("code") String countryCode) {
		 HashMap<String, String> capitals= new HashMap<String,String>();
		 String response = restTemplate.getForObject(countryCodeURL+"/"+countryCode, String.class);
		 
		 ObjectMapper mapper = new ObjectMapper();
		 JsonNode root = null;
		try {
			root = mapper.readTree(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 capitals.put(root.path("name").toString(), root.path("capital").toString());
		 return new ResponseEntity<>(capitals, HttpStatus.OK);
		 
		// return restTemplate.getForObject(countryCodeURL+"/"+countryCode, String.class);
	 
	 
	 
	 }
	 
	 @GetMapping("/countryName/{name}")
	 @ResponseBody
	 HashMap<String, String> getCapitalByCountryName(@PathVariable("name") String countryName) {
		 HashMap<String, String> capitals= new HashMap<String,String>();
		 JSONParser jsonParser = new JSONParser();
		 JSONArray jsonObject = null;
		 try {
			 jsonObject = (JSONArray) jsonParser.parse(restTemplate.getForObject(countryNameURL+"/"+countryName, String.class));
			
			 for(int n = 0; n < jsonObject.size(); n++)
			 {
			     JSONObject object = (JSONObject) jsonObject.get(n);
			     capitals.put(object.get("name").toString(), object.get("capital").toString());
			     ;
			 }
			 
		 } catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return capitals;
	 }
	 
	 
	 @Bean
	 public RestTemplate rest() {
	 return new RestTemplate();
	 }
	
}
