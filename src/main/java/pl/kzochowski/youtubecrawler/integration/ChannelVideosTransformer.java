package pl.kzochowski.youtubecrawler.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.Author;
import pl.kzochowski.youtubecrawler.integration.model.Document;
import pl.kzochowski.youtubecrawler.integration.model.Metadata;
import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;

import java.util.List;
import java.util.stream.Collectors;

import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_CHANNEL_URL_PREFIX;
import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_VIDEO_URL_PREFIX;

@Slf4j
@Component
public class ChannelVideosTransformer extends AbstractPayloadTransformer<ChannelDto, List<Document>> {

    @Override
    protected List<Document> transformPayload(ChannelDto channelDto) {
        log.info("Converting list with {} videos, channelId: {}", channelDto.getVideoDtos().size(), channelDto.getChannel().getId());
        List<Document> documents = channelDto.getVideoDtos().stream().map(videoDto -> convertVideoToDocument(videoDto)).collect(Collectors.toList());
        log.info("Documents converted");
        return documents;
    }

    private Document convertVideoToDocument(VideoDto videoDto) {
        return Document.builder()
                .author(Author.builder()
                        .name(videoDto.getVideo().getSnippet().getChannelTitle())
                        .url(channelUrl(videoDto))
                        .build())
                .url(videoUrl(videoDto))
                .body(videoDto.getVideo().getSnippet().getDescription())
                .header(videoDto.getVideo().getSnippet().getTitle())
                .publishDate(videoDto.getVideo().getSnippet().getPublishedAt())
                .metadata(Metadata.builder()
                        .viewCount(videoDto.getData().getStatistics().getViewCount())
                        .likeCount(videoDto.getData().getStatistics().getLikeCount())
                        .dislikeCount(videoDto.getData().getStatistics().getDislikeCount())
                        .favouriteCount(videoDto.getData().getStatistics().getFavouriteCount())
                        .commentCount(videoDto.getData().getStatistics().getCommentCount())
                        .build())
                .build();
    }

    private String channelUrl(VideoDto videoDto) {
        return YOUTUBE_CHANNEL_URL_PREFIX + videoDto.getVideo().getSnippet().getChannelId();
    }

    private String videoUrl(VideoDto videoDto) {
        return YOUTUBE_VIDEO_URL_PREFIX + videoDto.getVideo().getId().getVideoId();
    }

}
