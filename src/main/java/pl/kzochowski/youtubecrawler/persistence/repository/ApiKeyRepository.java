package pl.kzochowski.youtubecrawler.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findByKey(String key);

    @Query(nativeQuery = true, value = "SELECT * FROM api_keys ORDER BY last_execution NULLS FIRST LIMIT 1")
    Optional<ApiKey> findFirstByOrderByLastExecutionAsc();

}
