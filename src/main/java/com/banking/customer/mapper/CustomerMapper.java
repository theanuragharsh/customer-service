package com.banking.customer.mapper;

import com.banking.customer.dto.CustomerDetailsResponseDto;
import com.banking.customer.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

//    @Mapping(source = "Customer", target = "CustomerDetailsResponseDto" )
    CustomerDetailsResponseDto toDto(Customer customer);

/*    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "mobileNumber", target = "mobileNumber")
    @Mapping(source = "createdDate", target = "createdDate")
    CustomerDetailsResponseDto toDto(Customer customer);*/

}
