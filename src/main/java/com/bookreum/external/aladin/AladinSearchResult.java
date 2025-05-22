// 3. AladinSearchResult.java (DTO)
package com.bookreum.external.aladin;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AladinSearchResult {
    private int totalResults;
    private List<AladinItem> item;
}