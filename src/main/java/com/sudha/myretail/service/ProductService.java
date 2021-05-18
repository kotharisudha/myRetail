package com.sudha.myretail.service;

import com.sudha.myretail.dao.ProductRepository;
import com.sudha.myretail.model.Price;
import com.sudha.myretail.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductService {

    public static final String ID = "id";
    public static final String DATA = "data";
    public static final String NAME = "name";


    private static final String GET_NAME_BY_ID = "https://gorest.co.in/public-api/products/{id}";
    private static final String GET_PRICE_BY_ID = "https://run.mocky.io/v3/5d6573fc-1051-41e6-8b4d-11f46eef06b8";

    private final ProductRepository productRepository;
       
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProduct(Long id)
    {
        Optional<Product> product = Optional.ofNullable(productRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Unsupported value: " + id)));

        product.get().setName(getNameById(id));
        product.get().setCurrentPrice(getPriceById(id));

        return product;
    }

    private String getNameById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String,Long> requestParams =new HashMap<>();
        requestParams.put(ID, id);

        Map productMap =
                restTemplate.getForObject(GET_NAME_BY_ID, Map.class,requestParams);
        Map data= (Map) productMap.get(DATA);
        return (String) data.get(NAME);
    }

    private Price getPriceById (Long id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<Price> response =
                restTemplate.exchange(GET_PRICE_BY_ID, HttpMethod.GET, request, Price.class );

        return response.getBody();
    }

    public void addProduct(Product product) {
        Optional<Product> productOptional =
                productRepository.findProductByName(product.getName());

        if(productOptional.isPresent()) throw new IllegalArgumentException("Product already exists");
        productRepository.save(product);
    }
}
