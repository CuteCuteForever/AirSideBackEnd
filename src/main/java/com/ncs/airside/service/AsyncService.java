package com.ncs.airside.service;

import com.ncs.airside.Exception.SoundAlertException;
import com.ncs.airside.helper.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AsyncService {

	private static Logger logger = LoggerFactory.getLogger(AsyncService.class);


	@Value("${hostnameUrl}")
	private String hostnameUrl;

	@Async("asyncExecutor")
	public void AntennaPassiveStartScan() throws InterruptedException
	{
		new RestTemplate().getForEntity("http://"+hostnameUrl+":8080/antennaPassiveStartContinuousScan", null);
	}

	@Async("asyncExecutor")
	public void AntennaActiveStartScan() throws InterruptedException
	{
		new RestTemplate().getForEntity("http://"+hostnameUrl+":8080/antennaActiveStartContinuousScan", null);
	}

	@Async("asyncExecutor")
	public void MockAntennaStartScan() throws InterruptedException
	{
		logger.info("Start AsyncService Scan");
		new RestTemplate().getForEntity("http://"+hostnameUrl+":8080/MockAntennaStartScan", null);
	}

	@Async("asyncExecutor")
	public void RfidScanMultiple(int numberOfTimes) throws InterruptedException
	{
		new RestTemplate().getForEntity("http://"+hostnameUrl+":8080/rfidscantagMuliple/"+numberOfTimes, null);
	}


}

