package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(BeerController.class)
class BeerControllerTest {
    @MockBean
    BeerService beerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerDto validBeer;

    @BeforeEach
    void setUp() {
        validBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Beer1")
                .beerStyle("PALE_ALE")
                .upc(123456789L)
                .build();
    }

    @Test
    void getBeer() throws Exception {
        // given
        given(beerService.getBeerById(any(UUID.class))).willReturn(validBeer);

        // when
        mockMvc.perform(get("/api/v1/beer/"+validBeer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(validBeer.getId().toString()))
                .andExpect(jsonPath("$.beerName").value(validBeer.getBeerName()));

        // then
        verify(beerService, times(1)).getBeerById(any(UUID.class));
        verifyNoMoreInteractions(beerService);
    }

    @Test
    void handlePost() throws Exception {
        // given
        BeerDto beerDto = validBeer;
        beerDto.setId(null);
        BeerDto savedDto = BeerDto.builder().id(UUID.randomUUID()).beerName("New Beer").build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(savedDto);

        // when
        mockMvc.perform(post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated());

        // then
        verify(beerService, times(1)).saveNewBeer(any(BeerDto.class));
        verifyNoMoreInteractions(beerService);
    }

    @Test
    void handlePost_when_beer_name_is_blank() throws Exception {
        // given
        BeerDto beerDto = validBeer;
        beerDto.setId(null);
        beerDto.setBeerName(null);
        BeerDto savedDto = BeerDto.builder().id(UUID.randomUUID()).beerName("New Beer").build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(savedDto);

        // when
        mockMvc.perform(post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.beerName").value("The Beer Name is mandatory"));

        // then
        verifyNoInteractions(beerService);
    }

    @Test
    void handleUpdate() throws Exception {
        // given
        BeerDto beerDto = validBeer;
        beerDto.setId(null);
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        doNothing().when(beerService).updateBeer(any(UUID.class), any(BeerDto.class));

        // when
        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isNoContent());

        // then
        verify(beerService, times(1)).updateBeer(any(UUID.class), any(BeerDto.class));
        verifyNoMoreInteractions(beerService);
    }

    @Test
    void deleteBeer() throws Exception {
        // given
        doNothing().when(beerService).deleteById(any(UUID.class));

        // when
        mockMvc.perform(delete("/api/v1/beer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        verify(beerService, times(1)).deleteById(any(UUID.class));
        verifyNoMoreInteractions(beerService);
    }
}