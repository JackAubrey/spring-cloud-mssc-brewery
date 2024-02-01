package guru.springframework.msscbrewery.web.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.v2.BeerServiceV2;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import guru.springframework.msscbrewery.web.model.v2.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BeerControllerV2.class)
class BeerControllerV2Test {
    @MockBean
    BeerServiceV2 beerServiceV2;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerDtoV2 validBeer;

    @BeforeEach
    void setUp() {
        validBeer = BeerDtoV2.builder()
                .id(UUID.randomUUID())
                .beerName("Beer1")
                .beerStyle(BeerStyleEnum.LAGER)
                .upc(123456789L)
                .build();
    }

    @Test
    void getBeer() throws Exception {
        // given
        given(beerServiceV2.getBeerById(any(UUID.class))).willReturn(validBeer);

        // when
        mockMvc.perform(get("/api/v2/beer/"+validBeer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(validBeer.getId().toString()))
                .andExpect(jsonPath("$.beerName").value(validBeer.getBeerName()))
                .andExpect(jsonPath("$.beerStyle").value(validBeer.getBeerStyle().name()));

        // then
        verify(beerServiceV2, times(1)).getBeerById(any(UUID.class));
        verifyNoMoreInteractions(beerServiceV2);
    }

    @Test
    void handlePost() throws Exception {
        // given
        BeerDtoV2 beerDto = validBeer;
        beerDto.setId(null);
        BeerDtoV2 savedDto = BeerDtoV2.builder().id(UUID.randomUUID())
                .beerName("New Beer")
                .beerStyle(BeerStyleEnum.GOSE)
                .build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerServiceV2.saveNewBeer(any(BeerDtoV2.class))).willReturn(savedDto);

        // when
        mockMvc.perform(post("/api/v2/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isCreated());

        // then
        verify(beerServiceV2, times(1)).saveNewBeer(any(BeerDtoV2.class));
        verifyNoMoreInteractions(beerServiceV2);
    }

    @Test
    void handleUpdate() throws Exception {
        // given
        BeerDtoV2 beerDto = validBeer;
        beerDto.setId(null);
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        doNothing().when(beerServiceV2).updateBeer(any(UUID.class), any(BeerDtoV2.class));

        // when
        mockMvc.perform(put("/api/v2/beer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isNoContent());

        // then
        verify(beerServiceV2, times(1)).updateBeer(any(UUID.class), any(BeerDtoV2.class));
        verifyNoMoreInteractions(beerServiceV2);
    }

    @Test
    void deleteBeer() throws Exception {
        // given
        doNothing().when(beerServiceV2).deleteById(any(UUID.class));

        // when
        mockMvc.perform(delete("/api/v2/beer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        verify(beerServiceV2, times(1)).deleteById(any(UUID.class));
        verifyNoMoreInteractions(beerServiceV2);
    }
}