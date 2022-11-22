package com.cs5500.Delivery.repository;

import com.cs5500.Delivery.model.SearchEngine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchEngineRepository extends MongoRepository<SearchEngine, String> {

}
