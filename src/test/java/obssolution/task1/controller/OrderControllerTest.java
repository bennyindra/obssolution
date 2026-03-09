package obssolution.task1.controller;

import obssolution.task1.records.OrderRequestDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@EnableTransactionManagement
@EnableAutoConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderControllerTest.class);
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    OrderControllerTest(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @Order(0)
    void createOrder() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new OrderRequestDto(-2000L, 2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNo").value("O1"))
                .andExpect(jsonPath("$.price").value(1.00));

    }

    @Test
    @Order(1)
    void getAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .param("sortBy", "orderNo")
                        .param("direction", "desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty())
                .andExpect(jsonPath("$.content[0].orderNo").value("O1"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @Order(2)
    void getOrder() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", "O1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value("O1"))
                .andExpect(jsonPath("$.price").value(1))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.item.name").value("Pencil"));

        mockMvc.perform(get("/api/items/{id}", -2000)
                        .param("includeStock", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-2000))
                .andExpect(jsonPath("$.stock").value(38));
    }

    @Test
    @Order(4)
    void updateOrderAdd1Quantity() throws Exception {
        String updatedRequestBody = objectMapper.writeValueAsString(new OrderRequestDto(-2000L, 3));

        mockMvc.perform(put("/api/orders/{id}", "O1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo").value("O1"))
                .andExpect(jsonPath("$.price").value(1.00));

        mockMvc.perform(get("/api/items/{id}", -2000)
                        .param("includeStock", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-2000))
                .andExpect(jsonPath("$.stock").value(37));
    }

    @Test
    @Order(5)
    void updateOrderAdd41Quantity() throws Exception {
        String updatedRequestBody = objectMapper.writeValueAsString(new OrderRequestDto(-2000L, 41));

            mockMvc.perform(put("/api/orders/{id}", "O1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedRequestBody))
                    .andDo(result -> log.info(result.getResponse().getContentAsString()))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}", "O1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/items/{id}", -2000)
                        .param("includeStock", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-2000))
                .andExpect(jsonPath("$.stock").value(40));
    }
}