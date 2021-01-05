package pl.kzochowski.youtubecrawler.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        //todo configure
        return new RestTemplate();
    }
}
