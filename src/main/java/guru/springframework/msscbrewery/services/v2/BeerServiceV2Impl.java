package guru.springframework.msscbrewery.services.v2;

import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import guru.springframework.msscbrewery.web.model.v2.BeerStyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class BeerServiceV2Impl implements BeerServiceV2 {
    /**
     * @param beerId 
     * @return
     */
    @Override
    public BeerDtoV2 getBeerById(UUID beerId) {
        log.debug("V2 | Getting Beer by ID {}", beerId);
        return BeerDtoV2.builder().id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyleEnum.ALE)
                .build();
    }

    /**
     * @param beerDto 
     * @return
     */
    @Override
    public BeerDtoV2 saveNewBeer(BeerDtoV2 beerDto) {
        log.debug("V2 | Creating a new Beer {}", beerDto);
        return BeerDtoV2.builder()
                .id(UUID.randomUUID())
                .beerName(beerDto.getBeerName())
                .beerStyle(beerDto.getBeerStyle())
                .upc(beerDto.getUpc())
                .build();
    }

    /**
     * @param beerId 
     * @param beerDto
     */
    @Override
    public void updateBeer(UUID beerId, BeerDtoV2 beerDto) {
        // TODO impl - could add a real impl to update beer
        log.debug("V2 | Updating Beer by Id {} with values {}", beerId, beerDto);
    }

    /**
     * @param beerId 
     */
    @Override
    public void deleteById(UUID beerId) {
        // TODO impl - could add a real impl to delete beer
        log.debug("V2 | Deleting Beer by Id {}", beerId);
    }
}
