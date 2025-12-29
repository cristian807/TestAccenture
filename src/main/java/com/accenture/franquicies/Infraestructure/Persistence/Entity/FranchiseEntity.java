package com.accenture.franquicies.Infraestructure.Persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("franchises")
public class FranchiseEntity {
    
    @Id
    private Long id;
    
    @Column("name")
    private String name;
}
