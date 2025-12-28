package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Branches.*;
import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateBranchRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateNameRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ApiResponse;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.BranchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/branches")
@Tag(name = "Sucursales", description = "API para gestión de sucursales")
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;
    private final GetAllBranchesUseCase getAllBranchesUseCase;
    private final GetByIdBranchUseCase getByIdBranchUseCase;
    private final GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase;
    private final DeleteBranchUseCase deleteBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    public BranchController(CreateBranchUseCase createBranchUseCase,
                            GetAllBranchesUseCase getAllBranchesUseCase,
                            GetByIdBranchUseCase getByIdBranchUseCase,
                            GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase,
                            DeleteBranchUseCase deleteBranchUseCase,
                            UpdateBranchNameUseCase updateBranchNameUseCase) {
        this.createBranchUseCase = createBranchUseCase;
        this.getAllBranchesUseCase = getAllBranchesUseCase;
        this.getByIdBranchUseCase = getByIdBranchUseCase;
        this.getBranchesByFranchiseUseCase = getBranchesByFranchiseUseCase;
        this.deleteBranchUseCase = deleteBranchUseCase;
        this.updateBranchNameUseCase = updateBranchNameUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear sucursal", description = "Crea una nueva sucursal asociada a una franquicia")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(@RequestBody CreateBranchRequest request) {
        try {
            Branch branch = createBranchUseCase.execute(request.getName(), request.getFranchiseId());
            BranchResponse response = toResponse(branch);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Sucursal creada exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creando la sucursal: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", description = "Retorna una lista de todas las sucursales registradas")
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getAllBranches() {
        List<Branch> branches = getAllBranchesUseCase.execute();
        List<BranchResponse> response = branches.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID", description = "Busca y retorna una sucursal por su identificador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<ApiResponse<BranchResponse>> getBranchById(
            @Parameter(description = "ID de la sucursal") @PathVariable Long id) {
        return getByIdBranchUseCase.execute(id)
                .map(branch -> ResponseEntity.ok(ApiResponse.success(toResponse(branch))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No existe una sucursal con el id: " + id)));
    }

    @GetMapping("/franchise/{franchiseId}")
    @Operation(summary = "Obtener sucursales por franquicia", description = "Retorna todas las sucursales de una franquicia específica")
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getBranchesByFranchise(
            @Parameter(description = "ID de la franquicia") @PathVariable Long franchiseId) {
        List<Branch> branches = getBranchesByFranchiseUseCase.execute(franchiseId);
        List<BranchResponse> response = branches.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal del sistema por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sucursal eliminada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<ApiResponse<Void>> deleteBranch(
            @Parameter(description = "ID de la sucursal a eliminar") @PathVariable Long id) {
        boolean deleted = deleteBranchUseCase.execute(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Sucursal eliminada exitosamente."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("No existe una sucursal con el id: " + id));
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Actualizar nombre de sucursal", description = "Actualiza el nombre de una sucursal existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<ApiResponse<BranchResponse>> updateBranchName(
            @Parameter(description = "ID de la sucursal") @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return updateBranchNameUseCase.execute(id, request.getName())
                .map(branch -> ResponseEntity.ok(ApiResponse.success(toResponse(branch), "Branch name updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No existe una sucursal con el id: " + id)));
    }

    private BranchResponse toResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }
}
