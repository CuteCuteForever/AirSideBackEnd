package com.ncs.airside.service;

import com.ncs.airside.Exception.SoundAlertException;
import com.ncs.airside.helper.SoundUtils;
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
	public void AntennaStartScan() throws InterruptedException
	{
		new RestTemplate().getForEntity("http://"+hostnameUrl+":8080/antennastartcontinouosscan", null);
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

	public static AtomicBoolean isAlarmTriggerAtomic = new AtomicBoolean(false);

	@Async("asyncExecutor")
	public void toggleSoundAlert()
	{
		while(isAlarmTriggerAtomic.get()) {
			try {
				SoundUtils.tone(5000, 600);
				Thread.sleep(500);
			} catch (Exception ex){
				throw new SoundAlertException("Error loading sound alert for unscanned EPC");
			}
		}

		isAlarmTriggerAtomic.set(false);
	}

}

