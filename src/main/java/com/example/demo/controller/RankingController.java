package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.example.demo.domain.model.Ranking;

@Controller
public class RankingController {

	@Autowired
 private DiscoveryClient dc;


/**
	* ランキング情報を返す
 * @return
 */
@GetMapping("/ranking")
	@ResponseBody
	public Ranking getRanking() {
		RestTemplate restTemplate = new RestTemplate();
		List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
  ServiceInstance userApInstance = userApServiceList.get(0);
		String getRankingUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/ranking";
		return restTemplate.getForEntity(getRankingUrl, Ranking.class).getBody();
	}


}
