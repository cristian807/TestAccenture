package com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {
    private Long id;
    private String name;
    private Long franchiseId;
}
