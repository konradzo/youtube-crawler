package pl.kzochowski.youtubecrawler.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, String> {

    Optional<Channel> findFirstByEnabledToCrawlOrderByLastExecutionAsc(boolean enabled);

}
