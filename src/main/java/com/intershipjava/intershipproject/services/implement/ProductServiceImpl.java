package com.intershipjava.intershipproject.services.implement;

import com.intershipjava.intershipproject.constants.Constant;
import com.intershipjava.intershipproject.dto.ProductDto;
import com.intershipjava.intershipproject.exceptions.ProductExceptions;
import com.intershipjava.intershipproject.model.Product;
import com.intershipjava.intershipproject.repository.ProductRepository;
import com.intershipjava.intershipproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductDto saveProductDb(Product product) {
        if (product == null) {
            throw new ProductExceptions(Constant.errorMessages.INVALID_OBJECT);
        }

        if (product.getQuantity() < 1) {
            throw new ProductExceptions(Constant.errorMessages.INVALID_AMOUNT_OF_STOCK);
        }
        if (product.getPrice() < 1) {
            throw new ProductExceptions(Constant.errorMessages.INVALID_ENTERED_PRICE);
        }
        List<Product> productList = productRepository.findAll();
        IntStream.range(0, productList.size())
                .filter(i -> productList.get(i).getProductName().equals(product.getProductName()))
                .forEach(i -> {
            throw new ProductExceptions(Constant.errorMessages.PRODUCT_EXIST);
        });
        productRepository.save(product);
        return productToDto(product);
    }

    @Override
    public List<Product> getProductsDb() {

        if (productRepository.findAll().isEmpty()) {
            throw new ProductExceptions(Constant.errorMessages.EMPTY_PRODUCT_LIST);
        }
        return productRepository.findAll();
    }

    @Override
    public Product getProductDba(String uuid) {

        if (productRepository.findByUuid(uuid) == null) {
            throw new ProductExceptions(Constant.errorMessages.NOT_FOUND_PRODUCTS);
        }

        return productRepository.findByUuid(uuid);
    }

    @Override
    public List<ProductDto> getProductListByIdDB(List<String> idProducts) {
        if (idProducts == null) {
            throw new ProductExceptions(Constant.errorMessages.EMPTY_PRODUCT_LIST);
        }

        List<ProductDto> productDtoList = new ArrayList<>();

        for (String idProduct : idProducts) {
            if (productRepository.findByUuid(idProduct) == null) {
                throw new ProductExceptions(Constant.errorMessages.NOT_FOUND_PRODUCTS);
            }
            productDtoList.add(productToDto(productRepository.findByUuid(idProduct)));
        }
        return productDtoList;
    }

    @Override
    public ProductDto editProduct(Product product){
        if(product == null) {
            throw new ProductExceptions(Constant.errorMessages.NOT_FOUND_PRODUCTS);
        }

      Product product1 = productRepository.findByUuid(product.getUuid());
        if (!product.getProductName().isEmpty()){
            product1.setProductName(product.getProductName());
        }
        if(product.getQuantity() >= 0){
            product1.setQuantity(product.getQuantity());
        }
        if (!product.getCategory().isEmpty()){
            product1.setCategory(product.getCategory());
        }

        if (product.getPrice() > 0){
            product1.setPrice(product.getPrice());
        }

        if(!product.getIssues().isEmpty()){
            product1.setIssues(product.getIssues());
        }

        return productToDto(productRepository.save(product1));
    }

    @Override
    public ProductDto productToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setUuid(product.getUuid());
        productDto.setProductName(product.getProductName());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory());
        productDto.setIssues(product.getIssues());
        productDto.setQuantity(product.getQuantity());

        return productDto;
    }

    @Override
    public List<ProductDto> productsToDto(List<Product> productList) {
        List<ProductDto> productDto = new ArrayList<>();
        for (Product product : productList) {
            productDto.add(productToDto(product));
        }
        return productDto;
    }

    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setIssues(productDto.getIssues());
        product.setQuantity(productDto.getQuantity());
        product.setUuid(productDto.getUuid());

        return product;
    }

    @Override
    public List<Product> productDtoToProduct(List<ProductDto> productListDto) {
        List<Product> productList = new ArrayList<>();
        for (ProductDto productDto : productListDto) {
            productList.add(productDtoToProduct(productDto));
        }
        return productList;
    }

}
