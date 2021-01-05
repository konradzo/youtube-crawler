package pl.kzochowski.youtubecrawler.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "channel")
public class Channel {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "enabled_to_crawl")
    private boolean enabledToCrawl;

    @Column(name = "added_at")
    private ZonedDateTime addedAt;

    @Column(name = "last_execution")
    private ZonedDateTime lastExecution;
}
