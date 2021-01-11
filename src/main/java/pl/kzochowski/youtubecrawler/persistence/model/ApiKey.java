package pl.kzochowski.youtubecrawler.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "api_keys")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String projectName;

    @NotBlank
    private String key;

    @CreationTimestamp
    private ZonedDateTime addedAt;

    private ZonedDateTime lastExecution;

    @NotBlank
    private String email;

}
