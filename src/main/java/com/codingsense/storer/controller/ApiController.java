package com.codingsense.storer.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingsense.storer.dto.RequestPayload;
import com.codingsense.storer.dto.ResponsePayload;
import com.codingsense.storer.model.A;
import com.codingsense.storer.repository.ARepository;
import com.codingsense.storer.dto.DeliveryReport;
import com.codingsense.storer.dto.DeliveryReportMessage;

@RestController
@RequestMapping(path = "/api/v2/")
public class ApiController {

	@Autowired
	private ARepository aRepository;
	
	@PostMapping("MessageStatus")
	public HashMap<String, Object> callProcess(@RequestBody DeliveryReport deliveryReport) {
		
		HashMap<String, Object> responseMap = new HashMap<>();
		HashMap<String, Object> statusInfo = new HashMap<>();
		try {
			UUID uuid = UUID.randomUUID();
			
			LinkedHashMap<String, Object> main = new LinkedHashMap<>();
			DeliveryReportMessage message = deliveryReport.getMessage();
			if(message!=null) {
		        
		        System.out.println("IATLID: " + uuid + " " +"REQUEST : " + message.toString());
		
				A a = A.builder().messageId(message.getId()).messageStatus(message.getStatus()).mobile(message.getMobile())
						.pduCount(message.getPducount()).price(message.getPducount()).sentDate(message.getSentdate())
						.shortMessage(message.getShortmessage()).doneDate(message.getDonedate()).gsmError(message.getGsmerror())
						.build();
				
				aRepository.save(a);
				
				statusInfo.put("statusCode", "1000");
				statusInfo.put("errordescription", "Sucess");
				responseMap.put("statusInfo", statusInfo);
			}
			else {
				statusInfo.put("statusCode", "1020");
				statusInfo.put("errordescription", "Internal Server Error");
				responseMap.put("statusInfo", statusInfo);
			}
		} catch (Exception e) {
			statusInfo.put("statusCode", "1020");
			statusInfo.put("errordescription", "Internal Server Error");
			responseMap.put("statusInfo", statusInfo);
			e.printStackTrace();
		}
		
		return responseMap;
	}

}
