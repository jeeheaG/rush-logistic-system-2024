package com.rush.logistic.client.company_product.domain.controller;

import com.rush.logistic.client.company_product.domain.dto.ProductDto;
import com.rush.logistic.client.company_product.domain.dto.request.ProductCreateRequest;
import com.rush.logistic.client.company_product.domain.service.ProductService;
import com.rush.logistic.client.company_product.global.exception.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
