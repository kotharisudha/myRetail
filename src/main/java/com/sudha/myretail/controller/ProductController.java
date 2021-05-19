package com.sudha.myretail.controller;

import com.sudha.myretail.model.Product;
import com.sudha.myretail.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "products/")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Optional<Product> getProduct(@PathVariable Long id) {
          return productService.getProduct(id);
    }

    @PostMapping
    public void addProduct(@Valid @RequestBody Product product) {
         productService.addProduct(product);
    }
}
