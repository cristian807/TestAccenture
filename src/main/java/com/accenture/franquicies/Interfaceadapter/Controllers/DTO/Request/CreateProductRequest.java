package com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String name;
    private Integer stock;
    private Long branchId;
}
