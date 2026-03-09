package obssolution.task1.controller;

import obssolution.task1.entity.Item;
import obssolution.task1.records.ItemDto;
import obssolution.task1.records.ItemRequestDto;
import obssolution.task1.repository.ItemRepository;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
class ItemControllerTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ItemControllerTest.class);

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ItemRepository itemRepository;

    public ItemControllerTest(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper, @Autowired ItemRepository itemRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.itemRepository = itemRepository;
    }

    @Test
    @Order(-1)
    void getAllItemsStock() throws Exception {
        mockMvc.perform(get("/api/items")
                        .param("includeStock", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10)); // Assuming there are 10 items
    }

    @Test
    @Order(0)
    void getAllItems() throws Exception {
        mockMvc.perform(get("/api/items")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10)); // Assuming there are 10 items

    }

    @Test
    @Order(1)
    void createItem() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new ItemDto(null, "New Item", BigDecimal.valueOf(100), null));

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Item"));

        Item item = itemRepository.findById(1L).orElseThrow();
        assertEquals("New Item", item.getName());
    }

    @Test
    @Order(2)
    void getItemById() throws Exception {
        mockMvc.perform(get("/api/items/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").isNotEmpty());
    }

    @Test
    @Order(3)
    void updateItem() throws Exception {
        String updatedRequestBody = objectMapper.writeValueAsString(new ItemDto(1L, "Updated Item", BigDecimal.valueOf(120), null));

        mockMvc.perform(put("/api/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Item"));

        Item item = itemRepository.findById(1L).orElseThrow();
        assertEquals("Updated Item", item.getName());
        assertEquals(0, BigDecimal.valueOf(120).compareTo(item.getPrice()));
    }

    @Test
    @Order(4)
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/api/items/{id}", 1))
                .andExpect(status().isNoContent());
        assertTrue(itemRepository.findById(1L).isEmpty());
    }


    @Test
    @Order(5)
    void createItemWithEmptyAttributes() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new ItemRequestDto("", null));

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }
}