package com.intershipjava.intershipproject.services.implement;

import com.intershipjava.intershipproject.dto.ProductDto;
import com.intershipjava.intershipproject.exceptions.ProductExceptions;
import com.intershipjava.intershipproject.model.Product;
import com.intershipjava.intershipproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void saveProductDb_throwNullException() {
        Product product = new Product();
        product.setQuantity(0);
        assertThrows(ProductExceptions.class, () -> productService.saveProductDb(null));

    }

    @Test
    void saveProductDb_throwQuantityException() {
        Product product = new Product();
        product.setQuantity(0);
        assertThrows(ProductExceptions.class, () -> productService.saveProductDb(product));

    }

    @Test
    void saveProductDb_throwPriceException() {
        Product product = new Product();
        product.setQuantity(5);
        product.setPrice(0);
        assertThrows(ProductExceptions.class, () -> productService.saveProductDb(product));
    }

    @Test
    void saveProductDb_throwAlreadyExistException() {
        Product product = new Product();
        product.setProductName("HP54");
        product.setQuantity(5);
        product.setPrice(100);
        Product product2 = new Product();
        product2.setProductName("HP54");
        Product product3 = new Product();
        product3.setProductName("HP5453");
        List<Product> productList = new ArrayList<>();
        productList.add(product2);
        productList.add(product3);
        when(productRepository.findAll()).thenReturn(productList);
        assertThrows(ProductExceptions.class, () -> productService.saveProductDb(product));
    }

    @Test
    void saveProductDb_True() {
        Product product = new Product();
        product.setProductName("HP54");
        product.setQuantity(5);
        product.setPrice(100);
        ProductDto productDto = new ProductDto();
        productDto.setProductName(product.getProductName());
        Product product2 = new Product();
        product2.setProductName("HP5432");
        Product product3 = new Product();
        product3.setProductName("HP5453");
        List<Product> productList = new ArrayList<>();
        productList.add(product2);
        productList.add(product3);
        when(productRepository.findAll()).thenReturn(productList);

        assertEquals(productService.saveProductDb(product).getProductName(), productDto.getProductName());
    }


    @Test
    void getProductsDb_throwException() {
        List<Product> productList = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(productList);
        assertThrows(ProductExceptions.class, () -> productService.getProductsDb());
    }

    @Test
    void getProductsDb_true() {
        Product product2 = new Product();
        product2.setProductName("HP5432");
        Product product3 = new Product();
        product3.setProductName("HP5453");
        List<Product> productList = new ArrayList<>();
        productList.add(product2);
        productList.add(product3);
        when(productRepository.findAll()).thenReturn(productList);
        assertEquals(productService.getProductsDb(), productList);
    }

    @Test
    void getProductListByIdDB_true() {
        List<String> idProductList = new ArrayList<>();
        idProductList.add("dsfsdf");
        List<ProductDto> productList = new ArrayList<>();
        Product product = new Product();
        product.setUuid("dsfsdf");
        product.setCategory("Calculator");
        ProductDto productDto = new ProductDto();
        productDto.setUuid("dsfsdf");
        productDto.setCategory("Calculator");
        productList.add(productDto);

        when(productRepository.findByUuid(anyString())).thenReturn(product);
        assertEquals(productService.getProductListByIdDB(idProductList).get(0).getCategory(), productList.get(0).getCategory());
    }

    @Test
    void getProductListByIdDB_throwException() {
        assertThrows(ProductExceptions.class, () -> productService.getProductListByIdDB(null));
    }

    @Test
    void getProductListByIdDB_throwInvalidProductIdException() {
        List<String> idProductList = new ArrayList<>();
        idProductList.add("dsfsdf");
        when(productRepository.findByUuid(anyString())).thenReturn(null);

        assertThrows(ProductExceptions.class, () -> productService.getProductListByIdDB(idProductList));
    }


    @Test
    void productToDto() {
        Product product = new Product();
        product.setId(1L);
        product.setUuid("5433sa21");
        product.setProductName("HP Lenovo");
        product.setPrice(432);
        product.setCategory("Laptop");
        product.setIssues("Nu sunt");
        product.setQuantity(10);
        ProductDto productDto = productService.productToDto(product);
        assertTrue(productDto.getProductName().equals(product.getProductName())
                && productDto.getUuid().equals(product.getUuid())
                && productDto.getId().equals(product.getId())
                && productDto.getPrice() == product.getPrice()
                && productDto.getCategory().equals(product.getCategory())
                && productDto.getIssues().equals(product.getIssues())
                && productDto.getQuantity() == (product.getQuantity()));
    }

    @Test
    void productsToDto() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setUuid("5433sa21");
        product.setProductName("HP Lenovo");
        product.setPrice(432);
        product.setCategory("Laptop");
        product.setIssues("Nu sunt");
        product.setQuantity(10);

        Product product2 = new Product();
        product2.setId(1L);
        product2.setUuid("5433sa21");
        product2.setProductName("HP Lenovo");
        product2.setPrice(432);
        product2.setCategory("Laptop");
        product2.setIssues("Nu sunt");
        product2.setQuantity(10);

        productList.add(product);
        productList.add(product2);

        List<ProductDto> productDtoList = productService.productsToDto(productList);

        assertTrue(productDtoList.get(0).getProductName().equals(productList.get(0).getProductName())
                && productDtoList.get(0).getUuid().equals(productList.get(0).getUuid())
                && productDtoList.get(0).getId().equals(productList.get(0).getId())
                && productDtoList.get(0).getPrice() == productList.get(0).getPrice()
                && productDtoList.get(0).getCategory().equals(productList.get(0).getCategory())
                && productDtoList.get(0).getIssues().equals(productList.get(0).getIssues())
                && productDtoList.get(0).getQuantity() == (productList.get(0).getQuantity()));
    }

    @Test
    void productDtoToProduct() {
        List<ProductDto> productDtoList = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setUuid("5433sa21");
        productDto.setProductName("HP Lenovo");
        productDto.setPrice(432);
        productDto.setCategory("Laptop");
        productDto.setIssues("Nu sunt");
        productDto.setQuantity(10);

        ProductDto productDto2 = new ProductDto();
        productDto2.setId(1L);
        productDto2.setUuid("5433sa21");
        productDto2.setProductName("HP Lenovo");
        productDto2.setPrice(432);
        productDto2.setCategory("Laptop");
        productDto2.setIssues("Nu sunt");
        productDto2.setQuantity(10);

        productDtoList.add(productDto);
        productDtoList.add(productDto2);

        List<Product> productList = productService.productDtoToProduct(productDtoList);

        assertTrue(productDtoList.get(0).getProductName().equals(productList.get(0).getProductName())
                && productDtoList.get(0).getUuid().equals(productList.get(0).getUuid())
                && productDtoList.get(0).getId().equals(productList.get(0).getId())
                && productDtoList.get(0).getPrice() == productList.get(0).getPrice()
                && productDtoList.get(0).getCategory().equals(productList.get(0).getCategory())
                && productDtoList.get(0).getIssues().equals(productList.get(0).getIssues())
                && productDtoList.get(0).getQuantity() == (productList.get(0).getQuantity()));
    }

    @Test
    void testProductDtoToProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setUuid("5433sa21");
        productDto.setProductName("HP Lenovo");
        productDto.setPrice(432);
        productDto.setCategory("Laptop");
        productDto.setIssues("Nu sunt");
        productDto.setQuantity(10);
        Product product = productService.productDtoToProduct(productDto);
        assertTrue(productDto.getProductName().equals(product.getProductName())
                && productDto.getUuid().equals(product.getUuid())
                && productDto.getId().equals(product.getId())
                && productDto.getPrice() == product.getPrice()
                && productDto.getCategory().equals(product.getCategory())
                && productDto.getIssues().equals(product.getIssues())
                && productDto.getQuantity() == (product.getQuantity()));
    }
}