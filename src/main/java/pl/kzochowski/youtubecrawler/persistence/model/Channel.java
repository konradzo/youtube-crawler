package pl.kzochowski.youtubecrawler.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "channel")
public class Channel {

    @Id
    @Column(name = "id")
    @NotBlank
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "enabled_to_crawl")
    private boolean enabledToCrawl;

    @Column(name = "added_at")
    @CreationTimestamp
    private ZonedDateTime addedAt;

    @Column(name = "last_execution")
    private ZonedDateTime lastExecution;
}
