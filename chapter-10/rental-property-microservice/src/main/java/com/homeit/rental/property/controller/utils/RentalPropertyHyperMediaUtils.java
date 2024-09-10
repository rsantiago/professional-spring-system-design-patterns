package com.homeit.rental.property.controller.utils;

import com.homeit.rental.property.controller.RentalPropertyControllerV2;
import com.homeit.rental.property.dto.RentalPropertyDTO;
import com.homeit.rental.property.dto.descriptors.PropertiesCollectionDescriptor;
import com.homeit.rental.property.dto.descriptors.RentalPropertyDescriptor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class RentalPropertyHyperMediaUtils {

    public RentalPropertyDescriptor describeRentalProperty(
            RentalPropertyDTO rentalPropertyDTO) {

        return Stream.of(new RentalPropertyDescriptor())
            .peek( desc -> desc.setRentalProperty(rentalPropertyDTO))
            .peek( desc -> desc.add(
                WebMvcLinkBuilder.linkTo(RentalPropertyControllerV2.class)
                    .slash(rentalPropertyDTO.id()).withSelfRel())
                // , add as many links as you want for this entity
                // WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    // .methodOn(RentalPropertyControllerV2.class)
                //    .getPropertyById(rentalPropertyDTO.id()))
                    //    .withRel("anotherFunction");
                // WebMvcLinkBuilder.linkTo(RentalPropertyControllerV2.class)
                //    .slash(rentalPropertyDTO.id()).withRel("anotherFunction")
            )
            .findFirst().get();
    }

    public PropertiesCollectionDescriptor
        describeRentalPropertyCollection(
            List<RentalPropertyDTO> allProperties) {

        if(allProperties.isEmpty())
            return null;

        List<RentalPropertyDescriptor> parsedProperties =
            parseProperties(allProperties);

        return Stream.of(new PropertiesCollectionDescriptor())
            .peek( cDes ->
                cDes.setDescribedRentalProperties(parsedProperties))
            .peek(this::addAllPropertiesLink)
            .findFirst().get();
    }

    private List<RentalPropertyDescriptor>
        parseProperties(List<RentalPropertyDTO> allProperties) {
        return allProperties.stream()
            .map(this::describeRentalProperty).toList();
    }

    public void addAllPropertiesLink(
            PropertiesCollectionDescriptor collectionDescriptorDTO) {
        collectionDescriptorDTO.add(
            WebMvcLinkBuilder.linkTo(
                RentalPropertyControllerV2.class)
                    .withRel("allProperties")
        );
    }

}
