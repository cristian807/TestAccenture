package com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBranchRequest {
    private String name;
    private Long franchiseId;
}
