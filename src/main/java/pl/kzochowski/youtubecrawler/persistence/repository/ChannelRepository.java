package pl.kzochowski.youtubecrawler.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

public interface ChannelRepository extends JpaRepository<Channel, String> {

    //todo implement fetching channel to crawl
}
