package com.accenture.franquicies.Infraestructure.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "franchises")
@Getter
@Setter
@NoArgsConstructor
public class FranchiseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "franchise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BranchEntity> branches = new ArrayList<>();

    public FranchiseEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
