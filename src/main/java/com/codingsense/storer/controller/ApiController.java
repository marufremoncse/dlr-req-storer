package com.codingsense.storer.controller;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingsense.storer.dto.DeliveryReport;
import com.codingsense.storer.dto.DeliveryReportMessage;
import com.codingsense.storer.model.A;
import com.codingsense.storer.model.B;
import com.codingsense.storer.repository.ARepository;
import com.codingsense.storer.repository.BRepository;

@RestController
@RequestMapping(path = "/api/v2/")
public class ApiController {

	@Autowired
	private ARepository aRepository;
	@Autowired
	private BRepository bRepository;

	private static char flag = 'A';

	@PostMapping("flag/{flag}")
	public HashMap<String, String> flagChanger(@PathVariable("flag") char f) {
		HashMap<String, String> hMap = new HashMap<>();
		try {
			synchronized (ApiController.class) {
				flag = f;
				hMap.put("message", "Success");
			}
		} catch (Exception e) {
			e.getStackTrace();
			hMap.put("message", "Failed, Exception occured");
		}
		return hMap;
	}

	@PostMapping(value = "MessageStatus", consumes = { "application/xml" })
	public HashMap<String, Object> callProcess(@RequestBody DeliveryReport deliveryReport) {

		HashMap<String, Object> responseMap = new HashMap<>();
		HashMap<String, Object> statusInfo = new HashMap<>();
		try {
			UUID uuid = UUID.randomUUID();

			DeliveryReportMessage message = deliveryReport.getMessage();
			if (message != null) {

				System.out.println("IATLID: " + uuid + " " + "REQUEST : " + message.toString());

				if (flag == 'A') {
					A a = A.builder().messageId(message.getId()).messageStatus(message.getStatus())
							.mobile(message.getMobile()).pduCount(message.getPducount()).price(message.getPrice())
							.sentDate(message.getSentdate()).status('N').shortMessage(message.getShortmessage())
							.doneDate(message.getDonedate()).gsmError(message.getGsmerror()).build();
					aRepository.save(a);
				} else if (flag == 'B') {
					B b = B.builder().messageId(message.getId()).messageStatus(message.getStatus())
							.mobile(message.getMobile()).pduCount(message.getPducount()).price(message.getPrice())
							.sentDate(message.getSentdate()).status('N').shortMessage(message.getShortmessage())
							.doneDate(message.getDonedate()).gsmError(message.getGsmerror()).build();
					bRepository.save(b);
				}

				statusInfo.put("statusCode", "1000");
				statusInfo.put("errordescription", "Sucess");
				responseMap.put("statusInfo", statusInfo);
			} else {
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
