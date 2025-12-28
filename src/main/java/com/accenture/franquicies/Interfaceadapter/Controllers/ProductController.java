package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Products.*;
import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateProductRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateNameRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateStockRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ApiResponse;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ProductResponse;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ProductWithBranchResponse;
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
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final GetByIdProductUseCase getByIdProductUseCase;
    private final GetProductsByBranchUseCase getProductsByBranchUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final GetTopStockProductsByFranchiseUseCase getTopStockProductsByFranchiseUseCase;

    public ProductController(CreateProductUseCase createProductUseCase,
                             GetAllProductsUseCase getAllProductsUseCase,
                             GetByIdProductUseCase getByIdProductUseCase,
                             GetProductsByBranchUseCase getProductsByBranchUseCase,
                             DeleteProductUseCase deleteProductUseCase,
                             UpdateProductStockUseCase updateProductStockUseCase,
                             UpdateProductNameUseCase updateProductNameUseCase,
                             GetTopStockProductsByFranchiseUseCase getTopStockProductsByFranchiseUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.getByIdProductUseCase = getByIdProductUseCase;
        this.getProductsByBranchUseCase = getProductsByBranchUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.updateProductStockUseCase = updateProductStockUseCase;
        this.updateProductNameUseCase = updateProductNameUseCase;
        this.getTopStockProductsByFranchiseUseCase = getTopStockProductsByFranchiseUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto asociado a una sucursal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody CreateProductRequest request) {
        try {
            Product product = createProductUseCase.execute(request.getName(), request.getStock(), request.getBranchId());
            ProductResponse response = toResponse(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Producto creado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Se presento un error creando el producto: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos registrados")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<Product> products = getAllProductsUseCase.execute();
        List<ProductResponse> response = products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Busca y retorna un producto por su identificador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        return getByIdProductUseCase.execute(id)
                .map(product -> ResponseEntity.ok(ApiResponse.success(toResponse(product))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontro el producto con id: " + id)));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Obtener productos por sucursal", description = "Retorna todos los productos de una sucursal específica")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByBranch(
            @Parameter(description = "ID de la sucursal") @PathVariable Long branchId) {
        List<Product> products = getProductsByBranchUseCase.execute(branchId);
        List<ProductResponse> response = products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "ID del producto a eliminar") @PathVariable Long id) {
        boolean deleted = deleteProductUseCase.execute(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("No se encontro el producto con id: " + id));
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock de producto", description = "Actualiza el stock de un producto existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductStock(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @RequestBody UpdateStockRequest request) {
        return updateProductStockUseCase.execute(id, request.getStock())
                .map(product -> ResponseEntity.ok(ApiResponse.success(toResponse(product), "Se ha actualizado el stock del producto")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontro el producto con id: " + id)));
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Actualizar nombre de producto", description = "Actualiza el nombre de un producto existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductName(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @RequestBody UpdateNameRequest request) {
        return updateProductNameUseCase.execute(id, request.getName())
                .map(product -> ResponseEntity.ok(ApiResponse.success(toResponse(product), "Nombre del producto actualizado exitosamente.")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontro el producto con id: " + id)));
    }

    @GetMapping("/top-stock/franchise/{franchiseId}")
    @Operation(summary = "Obtener productos con mayor stock por franquicia", 
               description = "Retorna el producto con mayor stock de cada sucursal de una franquicia")
    public ResponseEntity<ApiResponse<List<ProductWithBranchResponse>>> getTopStockProductsByFranchise(
            @Parameter(description = "ID de la franquicia") @PathVariable Long franchiseId) {
        List<ProductWithBranchResponse> products = getTopStockProductsByFranchiseUseCase.execute(franchiseId);
        return ResponseEntity.ok(ApiResponse.success(products, 
                "Producto con mas stock por franquicia " + franchiseId));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }
}
