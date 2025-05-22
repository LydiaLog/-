package com.bookreum.ai.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "RecommendedBook")
public class RecommendedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "isbn13")
    private String isbn13;

    @Column(name = "description")
    private String description;
}
