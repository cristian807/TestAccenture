package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Franchises.*;
import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateFranchiseRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateNameRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ApiResponse;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.FranchiseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final GetAllFranchise getAllFranchise;
    private final GetByIdFranchise getByIdFranchise;
    private final DeleteByIdFranchise deleteByIdFranchise;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    public FranchiseController(CreateFranchiseUseCase createFranchiseUseCase,
                               GetAllFranchise getAllFranchise,
                               GetByIdFranchise getByIdFranchise,
                               DeleteByIdFranchise deleteByIdFranchise,
                               UpdateFranchiseNameUseCase updateFranchiseNameUseCase) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.getAllFranchise = getAllFranchise;
        this.getByIdFranchise = getByIdFranchise;
        this.deleteByIdFranchise = deleteByIdFranchise;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FranchiseResponse>> createFranchise(@RequestBody CreateFranchiseRequest request) {
        try {
            Franchise franchise = createFranchiseUseCase.execute(request.getName());
            FranchiseResponse response = toResponse(franchise);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Franquicia creada exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Se presento un error creando la franquicia: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FranchiseResponse>>> getAllFranchises() {
        List<Franchise> franchises = getAllFranchise.execute();
        List<FranchiseResponse> response = franchises.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FranchiseResponse>> getFranchiseById(@PathVariable Long id) {
        return getByIdFranchise.execute(id)
                .map(franchise -> ResponseEntity.ok(ApiResponse.success(toResponse(franchise))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontro franquicia con id: " + id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFranchise(@PathVariable Long id) {
        boolean deleted = deleteByIdFranchise.execute(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Franquicia eliminada exitosamente."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("No se encontro franquicia con id: " + id));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ApiResponse<FranchiseResponse>> updateFranchiseName(
            @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return updateFranchiseNameUseCase.execute(id, request.getName())
                .map(franchise -> ResponseEntity.ok(ApiResponse.success(toResponse(franchise), "Nombre de la franquicia actualizado con exito.")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontro franquicia con id: " + id)));
    }

    private FranchiseResponse toResponse(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}
