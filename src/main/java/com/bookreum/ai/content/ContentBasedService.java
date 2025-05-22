package com.bookreum.ai.content;

import com.bookreum.external.aladin.AladinBookClient;
import com.bookreum.external.aladin.AladinItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentBasedService {

    private static final Logger logger = LoggerFactory.getLogger(ContentBasedService.class);
    private final AladinBookClient aladinBookClient;

    @Autowired
    private RecommendedBookRepository recommendedBookRepository;

    // 📌 제목 기반 추천 (기존 로직)
    public List<ContentBookDto> recommendByTitle(String title) {
        return aladinBookClient.searchBooks(title).getItem()
                .stream()
                .limit(5)
                .map(this::mapToDto)
                .toList();
    }

    // 📌 콘텐츠 기반 추천 (Python 연동)
    public List<ContentBookDto> recommendContentBased(String title) {
        List<String> recommendedTitles = runPythonRecommendation(title);
        List<ContentBookDto> result = new ArrayList<>();

        for (String recommendedTitle : recommendedTitles) {
            List<AladinItem> items = aladinBookClient.searchBooks(recommendedTitle).getItem();
            if (!items.isEmpty()) {
                ContentBookDto bookDto = mapToDto(items.get(0));
                result.add(bookDto);
                saveRecommendedBook(bookDto); // ✅ DB 저장
            }
        }

        return result;
    }

    // 📌 Python 실행 (추천 수행)
    private List<String> runPythonRecommendation(String title) {
        List<String> result = new ArrayList<>();
        StringBuilder jsonOutput = new StringBuilder();

        try {
            // ✅ Python 실행 - 가상환경 Python 명시적으로 지정
            ProcessBuilder pb = new ProcessBuilder("D:\\capstone\\venv\\Scripts\\python.exe", "D:\\capstone\\recommender\\run_model.py", title);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("📌 [Python Output] {}", line);
                    jsonOutput.append(line.trim());
                }
            }

            process.waitFor();

            // ✅ JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonOutput.toString());
            JsonNode titlesNode = rootNode.get("recommended_titles");

            if (titlesNode != null && titlesNode.isArray()) {
                for (JsonNode titleNode : titlesNode) {
                    result.add(titleNode.asText());
                }
            } else {
                logger.error("❌ [Python Execution Error] JSON 형식이 올바르지 않습니다: {}", jsonOutput);
            }

        } catch (Exception e) {
            logger.error("❌ [Python Execution Error] {}", e.getMessage(), e);
            logger.error("❌ [Python Raw Output] {}", jsonOutput.toString());
        }

        return result;
    }

 // ✅ DB에 추천된 책 저장 (중복 확인 개선)
    private void saveRecommendedBook(ContentBookDto bookDto) {
        try {
            // 중복 확인 없이 직접 저장 시도 (중복이면 무시)
            recommendedBookRepository.findByTitleAndAuthor(bookDto.getTitle(), bookDto.getAuthor())
                    .orElseGet(() -> {
                        RecommendedBook recommendedBook = new RecommendedBook();
                        recommendedBook.setTitle(bookDto.getTitle());
                        recommendedBook.setAuthor(bookDto.getAuthor());
                        recommendedBook.setCoverImageUrl(bookDto.getCoverImageUrl());
                        recommendedBookRepository.save(recommendedBook);
                        logger.info("✅ [DB 저장] 추천된 책 저장: {}", bookDto.getTitle());
                        return recommendedBook;
                    });
        } catch (Exception e) {
            logger.error("❌ [DB 저장 오류] {}", e.getMessage(), e);
        }
    }


    // 📌 ContentBookDto 매핑
    private ContentBookDto mapToDto(AladinItem item) {
        return ContentBookDto.builder()
                .isbn13(item.getIsbn13())
                .title(item.getTitle())
                .author(item.getAuthor())
                .coverImageUrl(item.getCover())
                .description(item.getDescription())
                .build();
    }
}