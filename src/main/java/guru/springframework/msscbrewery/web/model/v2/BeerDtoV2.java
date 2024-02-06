package guru.springframework.msscbrewery.web.model.v2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDtoV2 {
    @Null
    private UUID id;
    @NotBlank(message = "The Beer Name is mandatory")
    private String beerName;
    @NotNull(message = "The Beer Style is mandatory")
    private BeerStyleEnum beerStyle;
    @Positive
    private Long upc;

    private Timestamp createdDate;
    private Timestamp lastUpdatedDate;
}
