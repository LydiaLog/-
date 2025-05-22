package com.bookreum.external.aladin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class AladinBookClientImpl implements AladinBookClient {

    @Value("${aladin.ttb-key}")
    private String ttbKey; // properties 파일에서 API 키 로드

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://www.aladin.co.kr/ttb/api")
            .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
            .build();

    @Override
    public AladinSearchResult searchBooks(String keyword) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> buildItemSearchUri(uriBuilder, keyword))
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(errorBody -> new RuntimeException("API Error: " + errorBody)))
                    .bodyToMono(AladinSearchResult.class)
                    .block();
        } catch (Exception e) {
            log.error("Aladin API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("책 정보를 가져오는 데 실패했습니다. 나중에 다시 시도해주세요.");
        }
    }

    private java.net.URI buildItemSearchUri(UriBuilder uriBuilder, String keyword) {
        return uriBuilder
                .path("/ItemSearch.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("Query", keyword)
                .queryParam("QueryType", "Keyword")
                .queryParam("SearchTarget", "Book")
                .queryParam("Output", "js")
                .queryParam("Version", "20131101")
                .queryParam("Cover", "Mid")
                .queryParam("MaxResults", 5)
                .build();
    }
}
