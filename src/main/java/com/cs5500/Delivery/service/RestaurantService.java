package com.cs5500.Delivery.service;

import com.cs5500.Delivery.model.Comment;
import com.cs5500.Delivery.model.Dish;
import com.cs5500.Delivery.model.Restaurant;
import com.cs5500.Delivery.model.RestaurantInfo;
import java.util.List;

public interface RestaurantService {

  int addDish(String id, Dish dish);

  int removeDish(String id, Dish dish);

  List<Dish> getAllDishes(String id);

  RestaurantInfo getInformation(String id);

  int updateInfo(String id, RestaurantInfo info);
}
