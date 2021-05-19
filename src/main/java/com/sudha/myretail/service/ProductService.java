package com.sudha.myretail.service;

import com.sudha.myretail.common.Constants;
import com.sudha.myretail.dao.ProductRepository;
import com.sudha.myretail.exception.AlreadyExistsException;
import com.sudha.myretail.exception.ProductNotFoundException;
import com.sudha.myretail.model.Price;
import com.sudha.myretail.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
       
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProduct(Long id)
    {
        Optional<Product> product = Optional.ofNullable(productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product doesn't exist")));

        product.get().setName(getNameById(id));
        product.get().setCurrentPrice(getPriceById(id));

        return product;
    }

    private String getNameById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String,Long> requestParams =new HashMap<>();
        requestParams.put(Constants.ID, id);

        Map productMap =
                restTemplate.getForObject(Constants.GET_NAME_BY_ID_URL, Map.class,requestParams);
        Map data= (Map) productMap.get(Constants.DATA);
        return (String) data.get(Constants.NAME);
    }

    private Price getPriceById (Long id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<Price> response =
                restTemplate.exchange(Constants.GET_PRICE_BY_ID_URL, HttpMethod.GET, request, Price.class );

        return response.getBody();
    }

    public void addProduct(Product product) {
        Optional<Product> productOptional =
                productRepository.findProductByName(product.getName());

        if(productOptional.isPresent()) throw new AlreadyExistsException("Product already exists");
        productRepository.save(product);
    }
}
