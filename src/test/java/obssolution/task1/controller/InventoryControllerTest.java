package obssolution.task1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import obssolution.task1.entity.Inventory;
import obssolution.task1.records.InventoryRequestDto;
import obssolution.task1.repository.InventoryRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static obssolution.task1.enums.InventoryType.T;
import static obssolution.task1.enums.InventoryType.W;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@EnableTransactionManagement
@EnableAutoConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryControllerTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(InventoryControllerTest.class);


    private final MockMvc mockMvc;
    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;

    public InventoryControllerTest(@Autowired MockMvc mockMvc, @Autowired InventoryRepository inventoryRepository, @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.inventoryRepository = inventoryRepository;
        this.objectMapper = objectMapper;
    }

    @Test
    @Order(0)
    void getInventoryList() throws Exception {
        // Perform GET request and validate response
        mockMvc.perform(get("/api/inventories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(-1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].type").value(T.toString()));
    }

    @Test
    @Order(1)
    void saveInventory() throws Exception {
        // Prepare the request body
        String requestBody = new ObjectMapper().writeValueAsString(new InventoryRequestDto(-1L, 15, T));

        // Perform POST request and validate response
        mockMvc.perform(post("/api/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(T.toString()));

        Inventory inventory = inventoryRepository.findById(1L).orElseThrow();
        assertEquals(15, inventory.getQuantity());
        assertEquals(T, inventory.getType());
    }

    @Test
    @Order(2)
    void getInventoryById() throws Exception {
        // Perform GET request using a valid inventory ID and validate response
        mockMvc.perform(get("/api/inventories/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(T.toString()));
    }

    @Test
    @Order(3)
    void editInventory() throws Exception {
        // Prepare the request body
        String updatedRequestBody = new ObjectMapper().writeValueAsString(new InventoryRequestDto(-1L, 25, W));

        // Perform PUT request and validate response
        mockMvc.perform(put("/api/inventories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(W.toString()));
    }

    @Test
    @Order(4)
    void deleteInventory() throws Exception {
        // Perform DELETE request and validate response
        mockMvc.perform(delete("/api/inventories/{id}", 1))
                .andExpect(status().isNoContent());
        assertTrue(inventoryRepository.findById(1L).isEmpty());
    }


    @Test
    @Order(5)
    void createInventoryWithNullAttributes() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new InventoryRequestDto(null, null, T));

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }

    void addInventoryAndCheckItemStock() {}
    
    
}