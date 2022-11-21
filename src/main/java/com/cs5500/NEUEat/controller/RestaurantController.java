package com.cs5500.NEUEat.controller;

import com.cs5500.NEUEat.exception.DishNotExistException;
import com.cs5500.NEUEat.exception.OrderNotFinishedException;
import com.cs5500.NEUEat.exception.PasswordNotMatchException;
import com.cs5500.NEUEat.exception.UserAlreadyExistException;
import com.cs5500.NEUEat.exception.UserNotExistException;
import com.cs5500.NEUEat.model.Comment;
import com.cs5500.NEUEat.model.Dish;
import com.cs5500.NEUEat.model.Order;
import com.cs5500.NEUEat.model.Restaurant;
import com.cs5500.NEUEat.model.RestaurantInfo;
import com.cs5500.NEUEat.service.OrderServiceImpl;
import com.cs5500.NEUEat.service.RestaurantServiceImpl;
import com.cs5500.NEUEat.service.SearchEngineServiceImpl;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

  private final RestaurantServiceImpl restaurantService;
  private final OrderServiceImpl orderService;
  private final SearchEngineServiceImpl searchEngineService;

  @Autowired
  public RestaurantController(RestaurantServiceImpl restaurantService,
      OrderServiceImpl orderService, SearchEngineServiceImpl searchEngineService) {
    this.restaurantService = restaurantService;
    this.orderService = orderService;
    this.searchEngineService = searchEngineService;
  }

  @GetMapping(path = "/all")
  public List<Restaurant> getAllRestaurants() {
    return restaurantService.getUsers();
  }

  @GetMapping(path = "/search/" + "{query}")
  public List<Restaurant> SearchRestaurants(@PathVariable("query") String query) {
    List<Restaurant> res = new ArrayList<>();
    List<String> ids = searchEngineService.searchRestaurant(query);
    if (ids != null) {
      for (String id : ids) {
        if (restaurantService.getUser(id).isPresent()) {
          res.add(restaurantService.getUser(id).get());
        }
      }
    }
    return res;
  }

  @GetMapping(path = "{id}")
  public Restaurant getRestaurantById(@PathVariable("id") String id)
      throws UserNotExistException {
    return restaurantService.getUser(id)
        .orElseThrow(() -> new UserNotExistException("O usuario não existe"));
  }

  @PostMapping(path = "/login")
  public Restaurant loginRestaurant(@RequestBody String jsonUser)
      throws UserNotExistException, PasswordNotMatchException {

    JSONObject user = new JSONObject(jsonUser);
    String userName = user.getString("userName");
    String password = user.getString("password");
    Optional<Restaurant> restaurant = restaurantService.getUserByName(userName);
    if (restaurant.isEmpty()) {
      throw new UserNotExistException("O usuario não existe");
    }
    if (!restaurantService.passwordMatch(restaurant.get().getId(), password)) {
      throw new PasswordNotMatchException("A senha não confere");
    }
    return restaurant.get();
  }

  @PostMapping(path = "/register")
  public Restaurant registerRestaurant(@RequestBody String jsonUser)
      throws UserAlreadyExistException {

    JSONObject user = new JSONObject(jsonUser);
    String userName = user.getString("userName");
    String password = user.getString("password");
    String phoneNumber = user.getString("phoneNumber");
    String address = user.getString("address");
    String city = user.getString("city");
    String state = user.getString("state");
    String zip = user.getString("zip");
    Restaurant restaurant = restaurantService
        .addUser(userName, password, phoneNumber, address, city, state, zip);
    if (restaurant == null) {
      throw new UserAlreadyExistException("O usuário já existe, faça o login");
    }
    return restaurant;
  }

  @PostMapping(path = "/logout")
  public int logoutRestaurant() {
    System.out.println("sair do usuário");
    return 1;
  }

  @GetMapping(path = "/myActiveOrders/" + "{id}")
  public List<Order> getActiveOrders(@PathVariable("id") String id)
      throws UserNotExistException {
    if (restaurantService.getUser(id).isEmpty()) {
      throw new UserNotExistException("O estabelecimento indicado não existe");
    }
    return orderService.restaurantGetActiveOrders(id);
  }

  @GetMapping(path = "/myOrderHistory/" + "{id}")
  public List<Order> getOrderHistory(@PathVariable("id") String id)
      throws UserNotExistException {
    if (restaurantService.getUser(id).isEmpty()) {
      throw new UserNotExistException("O estabelecimento indicado não existe");
    }
    return orderService.restaurantFindPastOrders(id);
  }

  @GetMapping(path = "/menu/" + "{id}")
  public List<Dish> getMenu(@PathVariable("id") String id)
      throws UserNotExistException {
    if (restaurantService.getUser(id).isEmpty()) {
      throw new UserNotExistException("O estabelecimento indicado não existe");
    }
    return restaurantService.getAllDishes(id);
  }

  @PostMapping(path = "/addToMenu")
  public int addDishToMenu(@RequestBody String jsonDish)
      throws UserNotExistException {
    JSONObject dish = new JSONObject(jsonDish);
    String restaurantId = dish.getString("restaurantId");
    String dishName = dish.getString("dishName");
    String imageUrl = dish.getString("imageUrl");
    double price = dish.getDouble("price");
    Dish newDish = new Dish(dishName, price, imageUrl);
    int res = restaurantService.addDish(restaurantId, newDish);
    if (res == -1) {
      throw new UserNotExistException("O estabelecimento indicado não existe");
    }
    // handle search engine
    searchEngineService.addRestaurant(dishName, restaurantId);
    return res;
  }

  @PostMapping(path = "/removeDish")
  public int removeDishFromMenu(@RequestBody String jsonDish)
      throws UserNotExistException, DishNotExistException {
    JSONObject dish = new JSONObject(jsonDish);
    String restaurantId = dish.getString("restaurantId");
    Object dishObject = dish.getJSONObject("dish");
    Gson gson = new Gson();
    Dish newDish = gson.fromJson(dishObject.toString(), Dish.class);
    int res = restaurantService.removeDish(restaurantId, newDish);
    if (res == -1) {
      throw new UserNotExistException("O estabelecimento indicado não existe");
    }
    if (res == 0) {
      throw new DishNotExistException("O cardápio não existe");
    }
    // handle search engine
    searchEngineService.removeRestaurant(newDish.getDishName(), restaurantId);
    return res;
  }

  @GetMapping(path = "/information/" + "{id}")
  public RestaurantInfo getRestaurantInformation(@PathVariable("id") String id)
      throws UserNotExistException {
    if (restaurantService.getInformation(id) != null) {
      return restaurantService.getInformation(id);
    }
    throw new UserNotExistException("O estabelecimento indicado não existe");
  }

  @PostMapping(path = "/information")
  public int updateRestaurantInformation(@RequestBody String jsonInfo)
      throws UserNotExistException {
    JSONObject object = new JSONObject(jsonInfo);
    String restaurantId = object.getString("restaurantId");
    boolean open = object.getBoolean("status");
    String name = object.getString("name");
    String description = object.getString("description");
    String imageUrl = object.getString("imageUrl");
    String tag1 = object.getString("tag1");
    String tag2 = object.getString("tag2");
    String tag3 = object.getString("tag3");
    RestaurantInfo newInfo = new RestaurantInfo(open, name, description, imageUrl, tag1, tag2,
        tag3);
    // handle search engine
    RestaurantInfo oldInfo = restaurantService.getInformation(restaurantId);
    if (oldInfo != null) {
      searchEngineService.eraseInfo(oldInfo, restaurantId);
    }
    searchEngineService.updateInfo(newInfo, restaurantId);
    int res = restaurantService.updateInfo(restaurantId, newInfo);
    if (res == -1) {
      throw new UserNotExistException("O estabelecimento indicado não existe");
    }
    return res;
  }

  @DeleteMapping(path = "{id}")
  public int deleterRestaurant(@PathVariable("id") String id)
      throws UserNotExistException, OrderNotFinishedException {
    if (orderService.restaurantGetActiveOrders(id).size() != 0) {
      throw new OrderNotFinishedException("Você ainda tem pedidos ativos, finalize-os primeiro");
    }
    // handle search engine
    RestaurantInfo oldInfo = restaurantService.getInformation(id);
    if (oldInfo != null) {
      searchEngineService.eraseInfo(oldInfo, id);
    }
    List<Dish> dishes = restaurantService.getAllDishes(id);
    if (dishes != null) {
      searchEngineService.eraseDishes(dishes, id);
    }
    int res = restaurantService.deleteUser(id);
    if (res == -1) {
      throw new UserNotExistException("O usuario não existe");
    }
    return res;
  }

  @PostMapping(path = "/resetPassword")
  public int resetPassword(@RequestBody String jsonPassword)
      throws UserNotExistException, PasswordNotMatchException {
    JSONObject object = new JSONObject(jsonPassword);
    String id = object.getString("id");
    String oldPassword = object.getString("oldPassword");
    String newPassword = object.getString("newPassword");
    int res = restaurantService.updatePassword(id, oldPassword, newPassword);
    if (res == -1) {
      throw new UserNotExistException("O usuario não existe");
    }
    if (res == 0) {
      throw new PasswordNotMatchException("A senha não confere");
    }
    return res;
  }

  @PostMapping(path = "/resetPhone")
  public int resetPhoneNumber(@RequestBody String jsonPhone)
      throws UserNotExistException {
    JSONObject object = new JSONObject(jsonPhone);
    String id = object.getString("id");
    String phoneNumber = object.getString("phoneNumber");
    int res = restaurantService.updatePhoneNumber(id, phoneNumber);
    if (res == -1) {
      throw new UserNotExistException("O usuario não existe");
    }
    return res;
  }

  @PostMapping(path = "/resetAddress")
  public int resetAddress(@RequestBody String jsonAddress)
      throws UserNotExistException {
    JSONObject object = new JSONObject(jsonAddress);
    String id = object.getString("id");
    String address = object.getString("address");
    String city = object.getString("city");
    String state = object.getString("state");
    String zip = object.getString("zip");
    int res = restaurantService.updateAddress(id, address, city, state, zip);
    if (res == -1) {
      throw new UserNotExistException("O usuario não existe");
    }
    return res;
  }

  @GetMapping(path = "/getComments/" + "{id}")
  public List<Comment> findCommentsByRestaurant(@PathVariable("id") String id)
      throws UserNotExistException {
    Optional<Restaurant> restaurantOptional = restaurantService.getUser(id);
    if (restaurantOptional.isEmpty()) throw new UserNotExistException("O usuario não existe");
    return orderService.restaurantGetComments(id);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UserNotExistException.class, PasswordNotMatchException.class,
      UserAlreadyExistException.class, DishNotExistException.class,
      OrderNotFinishedException.class})
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
