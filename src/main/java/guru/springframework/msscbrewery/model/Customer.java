package guru.springframework.msscbrewery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Null
    private UUID id;
    @NotBlank(message = "The Customer Name is mandatory")
    @Size(min = 3, max = 100, message = "The Customer Name must to be at least 3 and max 100 characters")
    private String name;
}
