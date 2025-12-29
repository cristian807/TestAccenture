package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Franchises.*;
import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateFranchiseRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateNameRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ApiResponse;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.FranchiseResponse;
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
@RequestMapping("/api/franchises")
@Tag(name = "Franquicias", description = "API para gestión de franquicias")
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final GetAllFranchiseUseCase getAllFranchiseUseCase;
    private final GetByIdFranchiseUseCase getByIdFranchiseUseCase;
    private final DeleteByIdFranchiseUseCase deleteByIdFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    public FranchiseController(CreateFranchiseUseCase createFranchiseUseCase,
                               GetAllFranchiseUseCase getAllFranchiseUseCase,
                               GetByIdFranchiseUseCase getByIdFranchiseUseCase,
                               DeleteByIdFranchiseUseCase deleteByIdFranchiseUseCase,
                               UpdateFranchiseNameUseCase updateFranchiseNameUseCase) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.getAllFranchiseUseCase = getAllFranchiseUseCase;
        this.getByIdFranchiseUseCase = getByIdFranchiseUseCase;
        this.deleteByIdFranchiseUseCase = deleteByIdFranchiseUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear franquicia", description = "Crea una nueva franquicia en el sistema")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Franquicia creada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> createFranchise(@RequestBody CreateFranchiseRequest request) {
        return createFranchiseUseCase.execute(request.getName())
                .map(franchise -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(toResponse(franchise), "Franquicia creada exitosamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(ApiResponse.error("Se presento un error creando la franquicia: " + e.getMessage()))));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las franquicias", description = "Retorna todas las franquicias registradas")
    public Mono<ResponseEntity<ApiResponse<List<FranchiseResponse>>>> getAllFranchises() {
        return getAllFranchiseUseCase.execute()
                .map(this::toResponse)
                .collectList()
                .map(franchises -> ResponseEntity.ok(ApiResponse.success(franchises)));
    }

    @GetMapping("/stream")
    @Operation(summary = "Stream de franquicias", description = "Retorna un stream SSE de todas las franquicias")
    public Flux<FranchiseResponse> getAllFranchisesStream() {
        return getAllFranchiseUseCase.execute()
                .map(this::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener franquicia por ID", description = "Busca y retorna una franquicia por su identificador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Franquicia encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> getFranchiseById(
            @Parameter(description = "ID de la franquicia") @PathVariable Long id) {
        return getByIdFranchiseUseCase.execute(id)
                .map(franchise -> ResponseEntity.ok(ApiResponse.success(toResponse(franchise))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontro franquicia con id: " + id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar franquicia", description = "Elimina una franquicia del sistema por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Franquicia eliminada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteFranchise(
            @Parameter(description = "ID de la franquicia a eliminar") @PathVariable Long id) {
        return deleteByIdFranchiseUseCase.execute(id)
                .map(deleted -> {
                    if (deleted) {
                        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Franquicia eliminada exitosamente."));
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.<Void>error("No se encontro franquicia con id: " + id));
                });
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Actualizar nombre de franquicia", description = "Actualiza el nombre de una franquicia existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> updateFranchiseName(
            @Parameter(description = "ID de la franquicia") @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return updateFranchiseNameUseCase.execute(id, request.getName())
                .map(franchise -> ResponseEntity.ok(ApiResponse.success(toResponse(franchise), "Nombre de la franquicia actualizado con exito.")))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontro franquicia con id: " + id)));
    }

    private FranchiseResponse toResponse(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}
