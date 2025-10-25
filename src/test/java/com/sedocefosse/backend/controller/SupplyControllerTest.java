package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.controller.admin.SupplyController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.sedocefosse.backend.service.products.SupplyService;

@WebMvcTest(SupplyController.class)
class SupplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplyService supplyService;

    // @Test
    // void ShouldFindAllSuppliesReturnOK() throws Exception {

    // SupplyResponseDTO supply1 = new SupplyResponseDTO(1L, "Farinha de trigo", 5L, "kg", 5.0, new BigDecimal("10.0"), 1.0);
    // SupplyResponseDTO supply2 = new SupplyResponseDTO(2L, "Farinha de trigo sem glúten", 5L, "kg", 5.0, new BigDecimal("12.0"), 1.0);
    // List<SupplyResponseDTO> supplies = Arrays.asList(supply1, supply2);
    // when(supplyService.getAllSupplies()).thenReturn(supplies);

    //     mockMvc.perform(get("/admin/supplies")
    //         .contentType(MediaType.APPLICATION_JSON))
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$", hasSize(2)))
    //         .andExpect(jsonPath("$[0].id", is(1)))
    //         .andExpect(jsonPath("$[0].nome", is("Farinha de trigo")))
    //         .andExpect(jsonPath("$[0].quantidade", is(5)))
    //         .andExpect(jsonPath("$[0].unidade_medida", is("kg")))
    //         .andExpect(jsonPath("$[0].ponto_reposicao", is(1)))
    //         .andExpect(jsonPath("$[0].id", is(2)))
    //         .andExpect(jsonPath("$[0].nome", is("Farinha de trigo sem glúten")))
    //         .andExpect(jsonPath("$[0].quantidade", is(5)))
    //         .andExpect(jsonPath("$[0].unidade_medida", is("kg")))
    //         .andExpect(jsonPath("$[0].ponto_reposicao", is(1)));

    // }
}