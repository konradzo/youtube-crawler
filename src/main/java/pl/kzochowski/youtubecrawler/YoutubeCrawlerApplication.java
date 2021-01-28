package pl.kzochowski.youtubecrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.PollerSpec;
import pl.kzochowski.youtubecrawler.integration.*;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class YoutubeCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoutubeCrawlerApplication.class, args);
	}

	@Bean
	public IntegrationFlow integrationFlowVideos(ChannelProducer channelProducer,
												 VideoHandler videoHandler,
												 ChannelVideosTransformer videosTransformer,
												 ElasticChannel elasticChannel,
												 EmptyListFilter emptyListFilter){
		return IntegrationFlows
				.from(channelProducer, e -> e.poller(p -> {
					PollerSpec pollerSpec = p.fixedDelay(60, TimeUnit.SECONDS);
//					pollerSpec.errorHandler(errorHandler);
					return pollerSpec;
				}))
				.handle(videoHandler)
				.filter(emptyListFilter)
				.transform(videosTransformer)
				.channel(elasticChannel)
				.get();
	}

}
