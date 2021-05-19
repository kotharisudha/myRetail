package com.sudha.controller;

import com.sudha.myretail.controller.ProductController;
import com.sudha.myretail.exception.AlreadyExistsException;
import com.sudha.myretail.exception.ProductNotFoundException;
import com.sudha.myretail.model.Price;
import com.sudha.myretail.model.Product;
import com.sudha.myretail.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    Product product = new Product(1L,"Pulse Oximeter", new Price("13.49", "USD"));

    @Test
    public void getProducts_WithSuccessResponse() throws Exception {

    Mockito.when(productService.getProduct(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(product));
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/1").accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"Pulse Oximeter\",\n" +
            "  \"currentPrice\": {\n" +
            "    \"value\": \"13.49\",\n" +
            "    \"currencyCode\": \"USD\"\n" +
            "  }\n" +
            "}";

    JSONAssert.assertEquals(expected, result.getResponse()
            .getContentAsString(), false);
    }

    @Test
    public void getProducts_WithBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/abcd").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    public void getProducts_WithNonExistProduct() throws Exception {
        Mockito.when(productService.getProduct(Mockito.anyLong())).thenThrow(ProductNotFoundException.class);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/1234").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void createProduct() throws Exception {
    String requestBody = "{\"name\":\"Thermometer\",\"price\":10}";
    Product product = new Product("Thermometer", new Price("10", "USD"));
    doNothing().when(productService).addProduct(product);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/products/")
            .accept(MediaType.APPLICATION_JSON).content(requestBody)
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals("", response.getContentAsString());
   }

    @Test
    public void createProduct_AlreadyExists() throws Exception {
        String requestBody = "{\"name1\":\"Thermometer\",\"price\":10}";
        Product product = new Product("Thermometer", new Price("10", "USD"));

        doThrow(AlreadyExistsException.class).when(productService).addProduct(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products/")
                .accept(MediaType.APPLICATION_JSON).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void createProduct_InvalidContentType() throws Exception {
        String requestBody = "{\"name\":\"Thermometer\",\"price\":10}";
        Product product = new Product("Thermometer", new Price("10", "USD"));

        doNothing().when(productService).addProduct(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products/")
                .accept(MediaType.APPLICATION_JSON).content(requestBody);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), response.getStatus());
    }

    @Test
    public void createProduct_InvalidRequest() throws Exception {
        String requestBody = "{\"productname\":\"Thermometer\",\"price\":10}";
        Product product = new Product("Thermometer", new Price("10", "USD"));

        doThrow(AlreadyExistsException.class).when(productService).addProduct(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products/")
                .accept(MediaType.APPLICATION_JSON).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
