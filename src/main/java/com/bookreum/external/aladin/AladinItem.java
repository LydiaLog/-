// 4. AladinItem.java (DTO)
package com.bookreum.external.aladin;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AladinItem {
    private Long itemId;
    private String title;
    private String author;
    private String cover;
    private String isbn13;
    private String categoryName;
    private String description;
}