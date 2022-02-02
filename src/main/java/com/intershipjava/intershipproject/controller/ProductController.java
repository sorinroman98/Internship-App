package com.intershipjava.intershipproject.controller;

import com.intershipjava.intershipproject.dto.ProductDto;
import com.intershipjava.intershipproject.model.Product;
import com.intershipjava.intershipproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addProductDb")
    @ResponseBody
    public ResponseEntity<ProductDto> insertProductDb(@RequestBody ProductDto productDto) {
        UUID uuid2 = UUID.randomUUID();
        productDto.setUuid(uuid2.toString());
        ProductDto productResponse = productService.saveProductDb(productService.productDtoToProduct(productDto));
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/getProductsDb")
    public List<ProductDto> returnProductListDb() {

        return productService.productsToDto(productService.getProductsDb());

    }

    @GetMapping("/getProductDb")
    public ProductDto returnProductDb(@RequestParam String uuid) {

        return productService.productToDto(productService.getProductDba(uuid));

    }

    @PostMapping("/editProduct")
    @ResponseBody
    public ResponseEntity<ProductDto> editProductDb(@RequestBody Product product) {
        ProductDto productResponse = productService.editProduct(product);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

}
