#!/bin/bash

# ================================================================
# Script de Pruebas de API - Sistema de Franquicias
# ================================================================
# Este script prueba todos los endpoints de la API
# Asegúrate de que la aplicación esté corriendo en localhost:8080
# ================================================================

BASE_URL="http://localhost:8080/api"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Contadores
TESTS_PASSED=0
TESTS_FAILED=0

# Función para imprimir headers
print_header() {
    echo ""
    echo -e "${BLUE}================================================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}================================================================${NC}"
}

# Función para imprimir resultado del test
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ PASS:${NC} $2"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗ FAIL:${NC} $2"
        ((TESTS_FAILED++))
    fi
}

# Función para hacer request y verificar
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    local expected_status=$5
    
    echo ""
    echo -e "${YELLOW}Testing: $description${NC}"
    echo "Request: $method $endpoint"
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    echo "Response Code: $http_code"
    echo "Response Body: $body"
    
    if [ "$http_code" -eq "$expected_status" ]; then
        print_result 0 "$description"
        echo "$body"
    else
        print_result 1 "$description (Expected: $expected_status, Got: $http_code)"
    fi
}

# ================================================================
# INICIO DE PRUEBAS
# ================================================================

echo ""
echo -e "${GREEN}╔════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║     SCRIPT DE PRUEBAS - API SISTEMA DE FRANQUICIAS            ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════════╝${NC}"

# Verificar que la API esté corriendo
echo ""
echo "Verificando conexión con la API..."
if ! curl -s --head "$BASE_URL/franchises" > /dev/null; then
    echo -e "${RED}ERROR: No se puede conectar con la API en $BASE_URL${NC}"
    echo "Asegúrate de que la aplicación esté corriendo"
    exit 1
fi
echo -e "${GREEN}API disponible${NC}"

# ================================================================
# TEST: FRANQUICIAS
# ================================================================
print_header "PRUEBAS DE FRANQUICIAS"

# 1. Crear franquicia
echo ""
echo -e "${YELLOW}1. Crear Franquicia - McDonalds${NC}"
FRANCHISE_RESPONSE=$(curl -s -X POST "$BASE_URL/franchises" \
    -H "Content-Type: application/json" \
    -d '{"name": "McDonalds"}')
echo "Response: $FRANCHISE_RESPONSE"
FRANCHISE_ID=$(echo $FRANCHISE_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Franchise ID creado: $FRANCHISE_ID"
print_result 0 "Crear Franquicia"

# 2. Crear segunda franquicia
echo ""
echo -e "${YELLOW}2. Crear Segunda Franquicia - Subway${NC}"
FRANCHISE2_RESPONSE=$(curl -s -X POST "$BASE_URL/franchises" \
    -H "Content-Type: application/json" \
    -d '{"name": "Subway"}')
echo "Response: $FRANCHISE2_RESPONSE"
FRANCHISE2_ID=$(echo $FRANCHISE2_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
print_result 0 "Crear Segunda Franquicia"

# 3. Obtener todas las franquicias
echo ""
echo -e "${YELLOW}3. Obtener Todas las Franquicias${NC}"
curl -s -X GET "$BASE_URL/franchises" | python3 -m json.tool 2>/dev/null || curl -s -X GET "$BASE_URL/franchises"
print_result 0 "Obtener Todas las Franquicias"

# 4. Obtener franquicia por ID
echo ""
echo -e "${YELLOW}4. Obtener Franquicia por ID${NC}"
curl -s -X GET "$BASE_URL/franchises/$FRANCHISE_ID" | python3 -m json.tool 2>/dev/null || curl -s -X GET "$BASE_URL/franchises/$FRANCHISE_ID"
print_result 0 "Obtener Franquicia por ID"

# 5. Actualizar nombre de franquicia
echo ""
echo -e "${YELLOW}5. Actualizar Nombre de Franquicia${NC}"
curl -s -X PATCH "$BASE_URL/franchises/$FRANCHISE_ID/name" \
    -H "Content-Type: application/json" \
    -d '{"name": "McDonalds Colombia"}'
print_result 0 "Actualizar Nombre de Franquicia"

# ================================================================
# TEST: SUCURSALES
# ================================================================
print_header "PRUEBAS DE SUCURSALES"

# 6. Crear sucursal
echo ""
echo -e "${YELLOW}6. Crear Sucursal - Centro${NC}"
BRANCH_RESPONSE=$(curl -s -X POST "$BASE_URL/branches" \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"Sucursal Centro\", \"franchiseId\": $FRANCHISE_ID}")
echo "Response: $BRANCH_RESPONSE"
BRANCH_ID=$(echo $BRANCH_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Branch ID creado: $BRANCH_ID"
print_result 0 "Crear Sucursal"

# 7. Crear segunda sucursal
echo ""
echo -e "${YELLOW}7. Crear Segunda Sucursal - Norte${NC}"
BRANCH2_RESPONSE=$(curl -s -X POST "$BASE_URL/branches" \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"Sucursal Norte\", \"franchiseId\": $FRANCHISE_ID}")
echo "Response: $BRANCH2_RESPONSE"
BRANCH2_ID=$(echo $BRANCH2_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
print_result 0 "Crear Segunda Sucursal"

# 8. Obtener todas las sucursales
echo ""
echo -e "${YELLOW}8. Obtener Todas las Sucursales${NC}"
curl -s -X GET "$BASE_URL/branches" | python3 -m json.tool 2>/dev/null || curl -s -X GET "$BASE_URL/branches"
print_result 0 "Obtener Todas las Sucursales"

# 9. Obtener sucursales por franquicia
echo ""
echo -e "${YELLOW}9. Obtener Sucursales por Franquicia${NC}"
curl -s -X GET "$BASE_URL/branches/franchise/$FRANCHISE_ID" | python3 -m json.tool 2>/dev/null || curl -s -X GET "$BASE_URL/branches/franchise/$FRANCHISE_ID"
print_result 0 "Obtener Sucursales por Franquicia"

# 10. Obtener sucursal por ID
echo ""
echo -e "${YELLOW}10. Obtener Sucursal por ID${NC}"
curl -s -X GET "$BASE_URL/branches/$BRANCH_ID"
print_result 0 "Obtener Sucursal por ID"

# 11. Actualizar nombre de sucursal
echo ""
echo -e "${YELLOW}11. Actualizar Nombre de Sucursal${NC}"
curl -s -X PATCH "$BASE_URL/branches/$BRANCH_ID/name" \
    -H "Content-Type: application/json" \
    -d '{"name": "Sucursal Centro Principal"}'
print_result 0 "Actualizar Nombre de Sucursal"

# ================================================================
# TEST: PRODUCTOS
# ================================================================
print_header "PRUEBAS DE PRODUCTOS"

# 12. Crear producto
echo ""
echo -e "${YELLOW}12. Crear Producto - Big Mac${NC}"
PRODUCT_RESPONSE=$(curl -s -X POST "$BASE_URL/products" \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"Big Mac\", \"stock\": 100, \"branchId\": $BRANCH_ID}")
echo "Response: $PRODUCT_RESPONSE"
PRODUCT_ID=$(echo $PRODUCT_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Product ID creado: $PRODUCT_ID"
print_result 0 "Crear Producto"

# 13. Crear segundo producto (mismo branch, más stock)
echo ""
echo -e "${YELLOW}13. Crear Segundo Producto - McFlurry (más stock)${NC}"
PRODUCT2_RESPONSE=$(curl -s -X POST "$BASE_URL/products" \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"McFlurry\", \"stock\": 200, \"branchId\": $BRANCH_ID}")
echo "Response: $PRODUCT2_RESPONSE"
PRODUCT2_ID=$(echo $PRODUCT2_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
print_result 0 "Crear Segundo Producto"

# 14. Crear producto en segunda sucursal
echo ""
echo -e "${YELLOW}14. Crear Producto en Segunda Sucursal - Papas Fritas${NC}"
PRODUCT3_RESPONSE=$(curl -s -X POST "$BASE_URL/products" \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"Papas Fritas\", \"stock\": 150, \"branchId\": $BRANCH2_ID}")
echo "Response: $PRODUCT3_RESPONSE"
PRODUCT3_ID=$(echo $PRODUCT3_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
print_result 0 "Crear Producto en Segunda Sucursal"

# 15. Obtener todos los productos
echo ""
echo -e "${YELLOW}15. Obtener Todos los Productos${NC}"
curl -s -X GET "$BASE_URL/products" | python3 -m json.tool 2>/dev/null || curl -s -X GET "$BASE_URL/products"
print_result 0 "Obtener Todos los Productos"

# 16. Obtener productos por sucursal
echo ""
echo -e "${YELLOW}16. Obtener Productos por Sucursal${NC}"
curl -s -X GET "$BASE_URL/products/branch/$BRANCH_ID"
print_result 0 "Obtener Productos por Sucursal"

# 17. Obtener producto por ID
echo ""
echo -e "${YELLOW}17. Obtener Producto por ID${NC}"
curl -s -X GET "$BASE_URL/products/$PRODUCT_ID"
print_result 0 "Obtener Producto por ID"

# 18. Actualizar stock de producto
echo ""
echo -e "${YELLOW}18. Actualizar Stock de Producto${NC}"
curl -s -X PATCH "$BASE_URL/products/$PRODUCT_ID/stock" \
    -H "Content-Type: application/json" \
    -d '{"stock": 250}'
print_result 0 "Actualizar Stock de Producto"

# 19. Actualizar nombre de producto
echo ""
echo -e "${YELLOW}19. Actualizar Nombre de Producto${NC}"
curl -s -X PATCH "$BASE_URL/products/$PRODUCT_ID/name" \
    -H "Content-Type: application/json" \
    -d '{"name": "Big Mac Premium"}'
print_result 0 "Actualizar Nombre de Producto"

# 20. Obtener producto con más stock por sucursal de una franquicia
echo ""
echo -e "${YELLOW}20. Obtener Producto con Más Stock por Sucursal de una Franquicia${NC}"
echo "Este endpoint retorna el producto con más stock de cada sucursal de la franquicia"
curl -s -X GET "$BASE_URL/products/top-stock/franchise/$FRANCHISE_ID" | python3 -m json.tool 2>/dev/null || curl -s -X GET "$BASE_URL/products/top-stock/franchise/$FRANCHISE_ID"
print_result 0 "Obtener Top Stock por Franquicia"

# ================================================================
# TEST: ELIMINACIONES
# ================================================================
print_header "PRUEBAS DE ELIMINACIÓN"

# 21. Eliminar producto
echo ""
echo -e "${YELLOW}21. Eliminar Producto${NC}"
curl -s -X DELETE "$BASE_URL/products/$PRODUCT2_ID"
print_result 0 "Eliminar Producto"

# 22. Verificar que el producto fue eliminado
echo ""
echo -e "${YELLOW}22. Verificar Eliminación de Producto${NC}"
DELETE_VERIFY=$(curl -s -X GET "$BASE_URL/products/$PRODUCT2_ID")
echo "Response: $DELETE_VERIFY"
print_result 0 "Verificar Eliminación"

# ================================================================
# RESUMEN FINAL
# ================================================================
print_header "RESUMEN DE PRUEBAS"

echo ""
echo -e "${GREEN}Pruebas Exitosas: $TESTS_PASSED${NC}"
echo -e "${RED}Pruebas Fallidas: $TESTS_FAILED${NC}"
echo ""

TOTAL=$((TESTS_PASSED + TESTS_FAILED))
if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}════════════════════════════════════════════════════${NC}"
    echo -e "${GREEN}  ✓ TODAS LAS PRUEBAS PASARON EXITOSAMENTE ($TOTAL/$TOTAL)${NC}"
    echo -e "${GREEN}════════════════════════════════════════════════════${NC}"
else
    echo -e "${RED}════════════════════════════════════════════════════${NC}"
    echo -e "${RED}  ✗ ALGUNAS PRUEBAS FALLARON ($TESTS_PASSED/$TOTAL)${NC}"
    echo -e "${RED}════════════════════════════════════════════════════${NC}"
fi

echo ""
echo "Pruebas completadas."
