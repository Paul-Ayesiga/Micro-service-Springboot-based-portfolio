package com.portifolio.portfolio_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;

@Entity
@Table(name = "experiences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
    name = "Experience.collections",
    attributeNodes = {
        @NamedAttributeNode("responsibilities"),
        @NamedAttributeNode("technologies")
    }
)
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String position;

    @Column(length = 2000)
    private String description;

    private String location;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean current;

    @Column(length = 1000)
    private String companyLogoUrl;

    @ElementCollection
    @CollectionTable(name = "experience_responsibilities", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "responsibility", length = 1000)
    private Set<String> responsibilities = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "experience_technologies", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "technology")
    private Set<String> technologies = new HashSet<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
