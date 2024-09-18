package com.homeit.rental.property.dto.descriptors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class PropertiesCollectionDescriptor
    extends RepresentationModel<PropertiesCollectionDescriptor> {
    private Collection<RentalPropertyDescriptor> describedRentalProperties;
}


