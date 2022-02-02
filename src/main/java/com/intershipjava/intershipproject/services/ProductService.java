package com.intershipjava.intershipproject.services;

import com.intershipjava.intershipproject.dto.ProductDto;
import com.intershipjava.intershipproject.model.Product;

import java.util.List;

public interface ProductService {

    ProductDto saveProductDb(Product product);

    List<Product> getProductsDb();

    ProductDto productToDto(Product product);

    List<ProductDto> productsToDto(List<Product> productList);

    Product productDtoToProduct(ProductDto productDto);

    List<Product> productDtoToProduct(List<ProductDto> productListDto);

    List<ProductDto> getProductListByIdDB(List<String> idProducts);

    Product getProductDba(String uuid);

    public ProductDto editProduct(Product product);
}
