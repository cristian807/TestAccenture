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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> createBranch(@RequestBody CreateBranchRequest request) {
        return createBranchUseCase.execute(request.getName(), request.getFranchiseId())
                .map(branch -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(toResponse(branch), "Sucursal creada exitosamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(ApiResponse.error("Error creando la sucursal: " + e.getMessage()))));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", description = "Retorna todas las sucursales registradas")
    public Mono<ResponseEntity<ApiResponse<List<BranchResponse>>>> getAllBranches() {
        return getAllBranchesUseCase.execute()
                .map(this::toResponse)
                .collectList()
                .map(branches -> ResponseEntity.ok(ApiResponse.success(branches)));
    }

    @GetMapping("/stream")
    @Operation(summary = "Stream de sucursales", description = "Retorna un stream SSE de todas las sucursales")
    public Flux<BranchResponse> getAllBranchesStream() {
        return getAllBranchesUseCase.execute()
                .map(this::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID", description = "Busca y retorna una sucursal por su identificador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> getBranchById(
            @Parameter(description = "ID de la sucursal") @PathVariable Long id) {
        return getByIdBranchUseCase.execute(id)
                .map(branch -> ResponseEntity.ok(ApiResponse.success(toResponse(branch))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No existe una sucursal con el id: " + id)));
    }

    @GetMapping("/franchise/{franchiseId}")
    @Operation(summary = "Obtener sucursales por franquicia", description = "Retorna todas las sucursales de una franquicia específica")
    public Mono<ResponseEntity<ApiResponse<List<BranchResponse>>>> getBranchesByFranchise(
            @Parameter(description = "ID de la franquicia") @PathVariable Long franchiseId) {
        return getBranchesByFranchiseUseCase.execute(franchiseId)
                .map(this::toResponse)
                .collectList()
                .map(branches -> ResponseEntity.ok(ApiResponse.success(branches)));
    }

    @GetMapping("/franchise/{franchiseId}/stream")
    @Operation(summary = "Stream de sucursales por franquicia", description = "Retorna un stream SSE de sucursales por franquicia")
    public Flux<BranchResponse> getBranchesByFranchiseStream(
            @Parameter(description = "ID de la franquicia") @PathVariable Long franchiseId) {
        return getBranchesByFranchiseUseCase.execute(franchiseId)
                .map(this::toResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal del sistema por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sucursal eliminada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteBranch(
            @Parameter(description = "ID de la sucursal a eliminar") @PathVariable Long id) {
        return deleteBranchUseCase.execute(id)
                .map(deleted -> {
                    if (deleted) {
                        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Sucursal eliminada exitosamente."));
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.<Void>error("No existe una sucursal con el id: " + id));
                });
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Actualizar nombre de sucursal", description = "Actualiza el nombre de una sucursal existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> updateBranchName(
            @Parameter(description = "ID de la sucursal") @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return updateBranchNameUseCase.execute(id, request.getName())
                .map(branch -> ResponseEntity.ok(ApiResponse.success(toResponse(branch), "Nombre de sucursal actualizado exitosamente")))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
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
