# ================================================================
# Script de Pruebas de API - Sistema de Franquicias (PowerShell)
# ================================================================
# Este script prueba todos los endpoints de la API
# Asegurate de que la aplicacion este corriendo en localhost:8080
# ================================================================

$BASE_URL = "http://localhost:8080/api"

# Contadores
$TESTS_PASSED = 0
$TESTS_FAILED = 0

# Funcion para imprimir headers
function Print-Header {
    param($Message)
    Write-Host ""
    Write-Host "================================================================" -ForegroundColor Blue
    Write-Host $Message -ForegroundColor Blue
    Write-Host "================================================================" -ForegroundColor Blue
}

# Funcion para imprimir resultado
function Print-Result {
    param($Success, $Description)
    if ($Success) {
        Write-Host "PASS: $Description" -ForegroundColor Green
        $script:TESTS_PASSED++
    } else {
        Write-Host "FAIL: $Description" -ForegroundColor Red
        $script:TESTS_FAILED++
    }
}

# ================================================================
# INICIO DE PRUEBAS
# ================================================================

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     SCRIPT DE PRUEBAS - API SISTEMA DE FRANQUICIAS            ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan

# Verificar conexion
Write-Host ""
Write-Host "Verificando conexion con la API..." -ForegroundColor Yellow
try {
    $null = Invoke-RestMethod -Uri "$BASE_URL/franchises" -Method GET -ErrorAction Stop
    Write-Host "API disponible" -ForegroundColor Green
} catch {
    Write-Host "ERROR: No se puede conectar con la API en $BASE_URL" -ForegroundColor Red
    Write-Host "Asegurate de que la aplicacion este corriendo" -ForegroundColor Red
    exit 1
}

# ================================================================
# TEST: FRANQUICIAS
# ================================================================
Print-Header "PRUEBAS DE FRANQUICIAS"

# 1. Crear franquicia
Write-Host ""
Write-Host "1. Crear Franquicia - McDonalds" -ForegroundColor Yellow
$body = @{ name = "McDonalds" } | ConvertTo-Json
try {
    $franchiseResponse = Invoke-RestMethod -Uri "$BASE_URL/franchises" -Method POST -Body $body -ContentType "application/json"
    $FRANCHISE_ID = $franchiseResponse.data.id
    Write-Host "Response: $($franchiseResponse | ConvertTo-Json -Compress)"
    Write-Host "Franchise ID creado: $FRANCHISE_ID"
    Print-Result $true "Crear Franquicia"
} catch {
    Print-Result $false "Crear Franquicia - $($_.Exception.Message)"
}

# 2. Crear segunda franquicia
Write-Host ""
Write-Host "2. Crear Segunda Franquicia - Subway" -ForegroundColor Yellow
$body = @{ name = "Subway" } | ConvertTo-Json
try {
    $franchise2Response = Invoke-RestMethod -Uri "$BASE_URL/franchises" -Method POST -Body $body -ContentType "application/json"
    $FRANCHISE2_ID = $franchise2Response.data.id
    Write-Host "Franchise 2 ID creado: $FRANCHISE2_ID"
    Print-Result $true "Crear Segunda Franquicia"
} catch {
    Print-Result $false "Crear Segunda Franquicia"
}

# 3. Obtener todas las franquicias
Write-Host ""
Write-Host "3. Obtener Todas las Franquicias" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/franchises" -Method GET
    Write-Host ($response | ConvertTo-Json -Depth 5)
    Print-Result $true "Obtener Todas las Franquicias"
} catch {
    Print-Result $false "Obtener Todas las Franquicias"
}

# 4. Obtener franquicia por ID
Write-Host ""
Write-Host "4. Obtener Franquicia por ID" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/franchises/$FRANCHISE_ID" -Method GET
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Obtener Franquicia por ID"
} catch {
    Print-Result $false "Obtener Franquicia por ID"
}

# 5. Actualizar nombre de franquicia
Write-Host ""
Write-Host "5. Actualizar Nombre de Franquicia" -ForegroundColor Yellow
$body = @{ name = "McDonalds Colombia" } | ConvertTo-Json
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/franchises/$FRANCHISE_ID/name" -Method PATCH -Body $body -ContentType "application/json"
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Actualizar Nombre de Franquicia"
} catch {
    Print-Result $false "Actualizar Nombre de Franquicia"
}

# ================================================================
# TEST: SUCURSALES
# ================================================================
Print-Header "PRUEBAS DE SUCURSALES"

# 6. Crear sucursal
Write-Host ""
Write-Host "6. Crear Sucursal - Centro" -ForegroundColor Yellow
$body = @{ name = "Sucursal Centro"; franchiseId = $FRANCHISE_ID } | ConvertTo-Json
try {
    $branchResponse = Invoke-RestMethod -Uri "$BASE_URL/branches" -Method POST -Body $body -ContentType "application/json"
    $BRANCH_ID = $branchResponse.data.id
    Write-Host "Response: $($branchResponse | ConvertTo-Json -Compress)"
    Write-Host "Branch ID creado: $BRANCH_ID"
    Print-Result $true "Crear Sucursal"
} catch {
    Print-Result $false "Crear Sucursal - $($_.Exception.Message)"
}

# 7. Crear segunda sucursal
Write-Host ""
Write-Host "7. Crear Segunda Sucursal - Norte" -ForegroundColor Yellow
$body = @{ name = "Sucursal Norte"; franchiseId = $FRANCHISE_ID } | ConvertTo-Json
try {
    $branch2Response = Invoke-RestMethod -Uri "$BASE_URL/branches" -Method POST -Body $body -ContentType "application/json"
    $BRANCH2_ID = $branch2Response.data.id
    Write-Host "Branch 2 ID creado: $BRANCH2_ID"
    Print-Result $true "Crear Segunda Sucursal"
} catch {
    Print-Result $false "Crear Segunda Sucursal"
}

# 8. Obtener todas las sucursales
Write-Host ""
Write-Host "8. Obtener Todas las Sucursales" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/branches" -Method GET
    Write-Host ($response | ConvertTo-Json -Depth 5)
    Print-Result $true "Obtener Todas las Sucursales"
} catch {
    Print-Result $false "Obtener Todas las Sucursales"
}

# 9. Obtener sucursales por franquicia
Write-Host ""
Write-Host "9. Obtener Sucursales por Franquicia" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/branches/franchise/$FRANCHISE_ID" -Method GET
    Write-Host ($response | ConvertTo-Json -Depth 5)
    Print-Result $true "Obtener Sucursales por Franquicia"
} catch {
    Print-Result $false "Obtener Sucursales por Franquicia"
}

# 10. Obtener sucursal por ID
Write-Host ""
Write-Host "10. Obtener Sucursal por ID" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/branches/$BRANCH_ID" -Method GET
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Obtener Sucursal por ID"
} catch {
    Print-Result $false "Obtener Sucursal por ID"
}

# 11. Actualizar nombre de sucursal
Write-Host ""
Write-Host "11. Actualizar Nombre de Sucursal" -ForegroundColor Yellow
$body = @{ name = "Sucursal Centro Principal" } | ConvertTo-Json
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/branches/$BRANCH_ID/name" -Method PATCH -Body $body -ContentType "application/json"
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Actualizar Nombre de Sucursal"
} catch {
    Print-Result $false "Actualizar Nombre de Sucursal"
}

# ================================================================
# TEST: PRODUCTOS
# ================================================================
Print-Header "PRUEBAS DE PRODUCTOS"

# 12. Crear producto
Write-Host ""
Write-Host "12. Crear Producto - Big Mac" -ForegroundColor Yellow
$body = @{ name = "Big Mac"; stock = 100; branchId = $BRANCH_ID } | ConvertTo-Json
try {
    $productResponse = Invoke-RestMethod -Uri "$BASE_URL/products" -Method POST -Body $body -ContentType "application/json"
    $PRODUCT_ID = $productResponse.data.id
    Write-Host "Response: $($productResponse | ConvertTo-Json -Compress)"
    Write-Host "Product ID creado: $PRODUCT_ID"
    Print-Result $true "Crear Producto"
} catch {
    Print-Result $false "Crear Producto - $($_.Exception.Message)"
}

# 13. Crear segundo producto (mismo branch, mas stock)
Write-Host ""
Write-Host "13. Crear Segundo Producto - McFlurry (mas stock)" -ForegroundColor Yellow
$body = @{ name = "McFlurry"; stock = 200; branchId = $BRANCH_ID } | ConvertTo-Json
try {
    $product2Response = Invoke-RestMethod -Uri "$BASE_URL/products" -Method POST -Body $body -ContentType "application/json"
    $PRODUCT2_ID = $product2Response.data.id
    Write-Host "Product 2 ID creado: $PRODUCT2_ID"
    Print-Result $true "Crear Segundo Producto"
} catch {
    Print-Result $false "Crear Segundo Producto"
}

# 14. Crear producto en segunda sucursal
Write-Host ""
Write-Host "14. Crear Producto en Segunda Sucursal - Papas Fritas" -ForegroundColor Yellow
$body = @{ name = "Papas Fritas"; stock = 150; branchId = $BRANCH2_ID } | ConvertTo-Json
try {
    $product3Response = Invoke-RestMethod -Uri "$BASE_URL/products" -Method POST -Body $body -ContentType "application/json"
    $PRODUCT3_ID = $product3Response.data.id
    Write-Host "Product 3 ID creado: $PRODUCT3_ID"
    Print-Result $true "Crear Producto en Segunda Sucursal"
} catch {
    Print-Result $false "Crear Producto en Segunda Sucursal"
}

# 15. Obtener todos los productos
Write-Host ""
Write-Host "15. Obtener Todos los Productos" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products" -Method GET
    Write-Host ($response | ConvertTo-Json -Depth 5)
    Print-Result $true "Obtener Todos los Productos"
} catch {
    Print-Result $false "Obtener Todos los Productos"
}

# 16. Obtener productos por sucursal
Write-Host ""
Write-Host "16. Obtener Productos por Sucursal" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products/branch/$BRANCH_ID" -Method GET
    Write-Host ($response | ConvertTo-Json -Depth 5)
    Print-Result $true "Obtener Productos por Sucursal"
} catch {
    Print-Result $false "Obtener Productos por Sucursal"
}

# 17. Obtener producto por ID
Write-Host ""
Write-Host "17. Obtener Producto por ID" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products/$PRODUCT_ID" -Method GET
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Obtener Producto por ID"
} catch {
    Print-Result $false "Obtener Producto por ID"
}

# 18. Actualizar stock de producto
Write-Host ""
Write-Host "18. Actualizar Stock de Producto" -ForegroundColor Yellow
$body = @{ stock = 250 } | ConvertTo-Json
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products/$PRODUCT_ID/stock" -Method PATCH -Body $body -ContentType "application/json"
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Actualizar Stock de Producto"
} catch {
    Print-Result $false "Actualizar Stock de Producto"
}

# 19. Actualizar nombre de producto
Write-Host ""
Write-Host "19. Actualizar Nombre de Producto" -ForegroundColor Yellow
$body = @{ name = "Big Mac Premium" } | ConvertTo-Json
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products/$PRODUCT_ID/name" -Method PATCH -Body $body -ContentType "application/json"
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Actualizar Nombre de Producto"
} catch {
    Print-Result $false "Actualizar Nombre de Producto"
}

# 20. Obtener producto con mas stock por sucursal de una franquicia
Write-Host ""
Write-Host "20. Obtener Producto con Mas Stock por Sucursal de una Franquicia" -ForegroundColor Yellow
Write-Host "Este endpoint retorna el producto con mas stock de cada sucursal de la franquicia" -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products/top-stock/franchise/$FRANCHISE_ID" -Method GET
    Write-Host ($response | ConvertTo-Json -Depth 5)
    Print-Result $true "Obtener Top Stock por Franquicia"
} catch {
    Print-Result $false "Obtener Top Stock por Franquicia"
}

# ================================================================
# TEST: ELIMINACIONES
# ================================================================
Print-Header "PRUEBAS DE ELIMINACION"

# 21. Eliminar producto
Write-Host ""
Write-Host "21. Eliminar Producto" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products/$PRODUCT2_ID" -Method DELETE
    Write-Host ($response | ConvertTo-Json)
    Print-Result $true "Eliminar Producto"
} catch {
    Print-Result $false "Eliminar Producto"
}

# 22. Verificar que el producto fue eliminado
Write-Host ""
Write-Host "22. Verificar Eliminacion de Producto (deberia dar error 404)" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/products/$PRODUCT2_ID" -Method GET -ErrorAction Stop
    Print-Result $false "Verificar Eliminacion - El producto aun existe"
} catch {
    Write-Host "Producto eliminado correctamente (404 esperado)" -ForegroundColor Green
    Print-Result $true "Verificar Eliminacion"
}

# ================================================================
# RESUMEN FINAL
# ================================================================
Print-Header "RESUMEN DE PRUEBAS"

Write-Host ""
Write-Host "Pruebas Exitosas: $TESTS_PASSED" -ForegroundColor Green
Write-Host "Pruebas Fallidas: $TESTS_FAILED" -ForegroundColor Red
Write-Host ""

$TOTAL = $TESTS_PASSED + $TESTS_FAILED
if ($TESTS_FAILED -eq 0) {
    Write-Host "════════════════════════════════════════════════════" -ForegroundColor Green
    Write-Host "  TODAS LAS PRUEBAS PASARON EXITOSAMENTE ($TESTS_PASSED/$TOTAL)" -ForegroundColor Green
    Write-Host "════════════════════════════════════════════════════" -ForegroundColor Green
} else {
    Write-Host "════════════════════════════════════════════════════" -ForegroundColor Red
    Write-Host "  ALGUNAS PRUEBAS FALLARON ($TESTS_PASSED/$TOTAL)" -ForegroundColor Red
    Write-Host "════════════════════════════════════════════════════" -ForegroundColor Red
}

Write-Host ""
Write-Host "Pruebas completadas."
