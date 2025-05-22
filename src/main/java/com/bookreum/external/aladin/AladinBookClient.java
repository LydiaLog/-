// 1. AladinBookClient.java (Interface)
package com.bookreum.external.aladin;

/**
 * 알라딘 Open API와 통신하는 포트(Port) 역할.
 * - 서비스/도메인 레이어는 이 인터페이스에만 의존하게 만든다.
 */
public interface AladinBookClient {

    /**
     * 키워드로 책을 검색하여 최대 5권까지 반환한다.
     * @param keyword 사용자 입력 키워드 (제목·저자·ISBN 등)
     * @return 알라딘 검색 결과 DTO(총 개수, 책 리스트)
     */
    AladinSearchResult searchBooks(String keyword);
}
