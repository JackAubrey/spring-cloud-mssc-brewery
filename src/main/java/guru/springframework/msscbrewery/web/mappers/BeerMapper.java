package guru.springframework.msscbrewery.web.mappers;

import guru.springframework.msscbrewery.model.Beer;
import guru.springframework.msscbrewery.web.converters.DateConverter;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import org.mapstruct.Mapper;

@Mapper(uses = {DateConverter.class})
public interface BeerMapper {
    BeerDtoV2 beerToBeerDtoV2(Beer beer);
    Beer beerDtoV2ToBeer(BeerDtoV2 beerDtoV2);
}
