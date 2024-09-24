package com.homeit.rental.property.dto.descriptors;

import com.homeit.rental.property.dto.RentalPropertyDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class RentalPropertyDescriptor
        extends RepresentationModel<RentalPropertyDescriptor> {
    private RentalPropertyDTO rentalProperty;
}


