package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
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
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerDto validCustomer;

    @BeforeEach
    void setUp() {
        validCustomer = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("John Buck")
                .build();
    }

    @Test
    void getCustomerById() throws Exception {
        // given
        given(customerService.getCustomerById(any(UUID.class))).willReturn(validCustomer);

        // when
        mockMvc.perform(get("/api/v1/customer/"+ validCustomer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(validCustomer.getId().toString()))
                .andExpect(jsonPath("$.name").value(validCustomer.getName()));

        // then
        verify(customerService, times(1)).getCustomerById(any(UUID.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void handlePost() throws Exception {
        // given
        CustomerDto customerDto = validCustomer;
        customerDto.setId(null);
        CustomerDto savedDto = CustomerDto.builder().id(UUID.randomUUID()).name("John Smith").build();
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        given(customerService.saveNewCustomer(any(CustomerDto.class))).willReturn(savedDto);

        // when
        mockMvc.perform(post("/api/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().isCreated());

        // then
        verify(customerService, times(1)).saveNewCustomer(any(CustomerDto.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void handlePost_when_name_is_null() throws Exception {
        // given
        CustomerDto customerDto = validCustomer;
        customerDto.setId(null);
        customerDto.setName(null);
        CustomerDto savedDto = CustomerDto.builder().id(UUID.randomUUID()).build();
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        given(customerService.saveNewCustomer(any(CustomerDto.class))).willReturn(savedDto);

        // when
        mockMvc.perform(post("/api/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("The Customer Name is mandatory"));

        // then
        verifyNoInteractions(customerService);
    }

    @Test
    void handlePost_when_name_is_less_than_3() throws Exception {
        // given
        CustomerDto customerDto = validCustomer;
        customerDto.setId(null);
        customerDto.setName("a");
        CustomerDto savedDto = CustomerDto.builder().id(UUID.randomUUID()).build();
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        given(customerService.saveNewCustomer(any(CustomerDto.class))).willReturn(savedDto);

        // when
        mockMvc.perform(post("/api/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("The Customer Name must to be at least 3 and max 100 characters"));

        // then
        verifyNoInteractions(customerService);
    }

    @Test
    void handlePut() throws Exception {
        // given
        CustomerDto customerDto = validCustomer;
        customerDto.setId(null);
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        doNothing().when(customerService).updateCustomer(any(UUID.class), any(CustomerDto.class));

        // when
        mockMvc.perform(put("/api/v1/customer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().isNoContent());

        // then
        verify(customerService, times(1)).updateCustomer(any(UUID.class), any(CustomerDto.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void deleteCustomer() throws Exception {
        // given
        doNothing().when(customerService).deleteCustomerById(any(UUID.class));

        // when
        mockMvc.perform(delete("/api/v1/customer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        verify(customerService, times(1)).deleteCustomerById(any(UUID.class));
        verifyNoMoreInteractions(customerService);
    }
}