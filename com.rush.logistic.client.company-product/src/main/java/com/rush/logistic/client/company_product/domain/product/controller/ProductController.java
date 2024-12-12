package com.rush.logistic.client.company_product.domain.product.controller;

import com.rush.logistic.client.company_product.domain.product.dto.ProductDto;
import com.rush.logistic.client.company_product.domain.product.dto.request.ProductCreateRequest;
import com.rush.logistic.client.company_product.domain.product.dto.request.ProductUpdateRequest;
import com.rush.logistic.client.company_product.domain.product.dto.response.ProductSearchResponse;
import com.rush.logistic.client.company_product.domain.product.service.ProductService;
import com.rush.logistic.client.company_product.global.exception.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {
    
    @Value("{server.port}")
    private String port;

    private final ProductService productService;

    @PostMapping
    public Response<?> createProduct(@RequestBody ProductCreateRequest request){
        ProductDto result = productService.createProduct(request);
        return Response.success(result, "상품 생성에 성공하였습니다.");
    }

    @GetMapping
    public Response<Page<ProductDto>> getAllProducts(Pageable pageable){
        Page<ProductDto> products = productService.getAllProduct(pageable);
        return Response.success(products, "상품 조회에 성공하였습니다");
    }

    @GetMapping("/{id}")
    public Response<ProductSearchResponse> getProduct(@PathVariable UUID id){
        return Response.success(productService.getProduct(id), "상품 단건 조회에 성공하였습니다.");
    }

    @PutMapping("/{id}")
    public Response<?> updateProduct(@PathVariable UUID id, @RequestBody ProductUpdateRequest request){
        return Response.success(productService.updateProduct(id,request), "상품 수정에 성공하였습니다.");
    }

    @DeleteMapping("/{id}")
    public Response<?> deleteProduct(@PathVariable UUID id){
        productService.deleteProduct(id);
        return Response.success(null, "상품 삭제에 성공하였습니다.");
    }

}
