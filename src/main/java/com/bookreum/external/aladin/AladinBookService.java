package com.bookreum.external.aladin;
//5. AladinBookService.java (Service Layer)

import com.bookreum.external.aladin.AladinBookClient;
import com.bookreum.external.aladin.AladinSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AladinBookService {

 private final AladinBookClient aladinBookClient;

 public AladinSearchResult searchBooks(String keyword) {
     return aladinBookClient.searchBooks(keyword);
 }
}