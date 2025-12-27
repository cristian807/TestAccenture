package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Branches.*;
import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateBranchRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateNameRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ApiResponse;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.BranchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/branches")
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
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(@RequestBody CreateBranchRequest request) {
        try {
            Branch branch = createBranchUseCase.execute(request.getName(), request.getFranchiseId());
            BranchResponse response = toResponse(branch);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Branch created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creating branch: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getAllBranches() {
        List<Branch> branches = getAllBranchesUseCase.execute();
        List<BranchResponse> response = branches.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranchById(@PathVariable Long id) {
        return getByIdBranchUseCase.execute(id)
                .map(branch -> ResponseEntity.ok(ApiResponse.success(toResponse(branch))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Branch not found with id: " + id)));
    }

    @GetMapping("/franchise/{franchiseId}")
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getBranchesByFranchise(@PathVariable Long franchiseId) {
        List<Branch> branches = getBranchesByFranchiseUseCase.execute(franchiseId);
        List<BranchResponse> response = branches.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBranch(@PathVariable Long id) {
        boolean deleted = deleteBranchUseCase.execute(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Branch deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Branch not found with id: " + id));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ApiResponse<BranchResponse>> updateBranchName(
            @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return updateBranchNameUseCase.execute(id, request.getName())
                .map(branch -> ResponseEntity.ok(ApiResponse.success(toResponse(branch), "Branch name updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Branch not found with id: " + id)));
    }

    private BranchResponse toResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }
}
