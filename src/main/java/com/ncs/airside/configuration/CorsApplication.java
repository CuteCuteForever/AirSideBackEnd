package com.ncs.airside.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsApplication {

    @Value("${airside.antenna.comport}")
    private int comPort;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/test/all").allowedOrigins("http://localhost:4200");
                registry.addMapping("/api/auth/signup").allowedOrigins("http://localhost:4200");
                registry.addMapping("/api/auth/signin").allowedOrigins("http://localhost:4200");
                registry.addMapping("/mockRfidOpen").allowedOrigins("http://localhost:4200");
                registry.addMapping("/rfidopen").allowedOrigins("http://localhost:4200");
                registry.addMapping("/MockAntennaInit/"+comPort).allowedOrigins("http://localhost:4200");
                registry.addMapping("/mockRFIDScannedTest").allowedOrigins("http://localhost:4200");
                registry.addMapping("/MockAsyncAntennaStartScan").allowedOrigins("http://localhost:4200");
                registry.addMapping("/offAlertSound").allowedOrigins("http://localhost:4200");
                registry.addMapping("/rfidscan").allowedOrigins("http://localhost:4200");
                registry.addMapping("/AntennaStartScan").allowedOrigins("http://localhost:4200");
                registry.addMapping("/MockAntennaStartScan").allowedOrigins("http://localhost:4200");
                registry.addMapping("/AntennaScanOnce").allowedOrigins("http://localhost:4200");
                registry.addMapping("/vehicleCompanyInfos").allowedOrigins("http://localhost:4200");
                registry.addMapping("/companyInfos").allowedOrigins("http://localhost:4200");
                registry.addMapping("/insertCompany").allowedOrigins("http://localhost:4200");
                registry.addMapping("/rfidclose").allowedOrigins("http://localhost:4200");
                registry.addMapping("/insertTransponder").allowedOrigins("http://localhost:4200");
                registry.addMapping("/insertVehicle").allowedOrigins("http://localhost:4200");
                registry.addMapping("/vehicleInfos").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getTransponderByEPC/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getTransponderByEPC/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/insertBorrowTransponder").allowedOrigins("http://localhost:4200");
                registry.addMapping("/insertReturnTransponder").allowedOrigins("http://localhost:4200");
                registry.addMapping("/rfidsetting/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/rfidreaddeviceoneparam/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/rfidupdatedeviceoneparam/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getTransponderByEPC/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/transpondersInfos").allowedOrigins("http://localhost:4200");
                registry.addMapping("/borrowreturntransponderstatusInfos").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getSerialNumber").allowedOrigins("http://localhost:4200");
                registry.addMapping("/openantenna/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getrfpower").allowedOrigins("http://localhost:4200");
                registry.addMapping("/antennascanonce").allowedOrigins("http://localhost:4200");
                registry.addMapping("/setrfpower/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getreaderinformation").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getserialnumber").allowedOrigins("http://localhost:4200");
                registry.addMapping("/closeantenna").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getversioninfo").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getdrm").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getbeepstatus").allowedOrigins("http://localhost:4200");
                registry.addMapping("/getreadertemperature").allowedOrigins("http://localhost:4200");
                registry.addMapping("/setbeepstatus/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/setdrm/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/setrfpower/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/asyncantennastartscan").allowedOrigins("http://localhost:4200");
                registry.addMapping("/antennastopcontinuousscan").allowedOrigins("http://localhost:4200");
                registry.addMapping("/antennastartcontinouosscan").allowedOrigins("http://localhost:4200");
                registry.addMapping("/gettransponderbycallsign/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/gettransponderbyepc/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/updatetransponderstatus/**").allowedOrigins("http://localhost:4200");
            }
        };
    }
}
