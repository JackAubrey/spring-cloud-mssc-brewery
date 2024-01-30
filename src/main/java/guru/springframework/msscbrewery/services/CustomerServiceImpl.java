package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        log.debug("Getting Customer by ID {}", customerId);
        return CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("Joe Buck")
                .build();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDto) {
        log.debug("Creating a new Customer {}", customerDto);
        return CustomerDto.builder()
                .id(UUID.randomUUID())
                .name(customerDto.getName())
                .build();
    }

    @Override
    public void updateCustomer(UUID customerId, CustomerDto customerDto) {
        // TODO impl - could add a real impl to update beer
        log.debug("Updating Customer by Id {} with values {}", customerId, customerDto);
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        // TODO impl - could add a real impl to delete beer
        log.debug("Deleting Customer by Id {}", customerId);
    }
}
