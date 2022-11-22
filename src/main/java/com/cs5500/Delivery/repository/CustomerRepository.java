package com.cs5500.Delivery.repository;

import com.cs5500.Delivery.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface CustomerRepository extends MongoRepository<Customer, String> {

}
