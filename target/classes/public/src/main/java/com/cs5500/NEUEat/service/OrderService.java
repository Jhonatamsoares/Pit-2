package com.cs5500.NEUEat.service;

import com.cs5500.NEUEat.model.Comment;
import com.cs5500.NEUEat.model.Dish;
import com.cs5500.NEUEat.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {

  int addOrderToCart(String customerId, String restaurantId, List<Dish> content);

  // O pedido só pode ser cancelado quando estiver no carrinho ou não na entrega
  int checkoutOrder(String id);

  // finalize todos os pedidos no carrinho de compras
  int checkoutAll(List<Order> orders);

  int cancelOrder(String id);

  Optional<Order> getOrder(String id);

  // Motorista aceita a encomenda, a encomenda só pode ser aceite se já iniciada e não em entrega
  int acceptOrder(String id, String driverId);

  // Motorista finaliza o pedido, o pedido só pode ser finalizado se estiver em entrega e não finalizado
  int finishOrder(String id);

  // Cliente verifica pedidos no carrinho
  List<Order> customerCart(String customerId);

  // O cliente verifica todos os pedidos ativos, não importa se há ou não um motorista e o pedido não foi finalizado
  List<Order> customerGetActiveOrders(String customerId);

  // Histórico de pedidos de verificação do cliente
  List<Order> customerFindPastOrders(String customerId);

  // Motorista verifique todos os pedidos que estão esperando por um motorista
  List<Order> getAllPendingOrders();

  //Motorista obtém pedido atual
  Order driverGetActiveOrder(String driverId);

  // Histórico de pedidos de verificação de motorista
  List<Order> driverFindPastOrders(String driverId);

  // O restaurante verifica todos os pedidos ativos, independentemente de haver ou não um motorista
  List<Order> restaurantGetActiveOrders(String restaurantId);

  // Histórico de pedidos de verificação de restaurante
  List<Order> restaurantFindPastOrders(String restaurantId);

  int addComment(String id, int rating, String content);

  int deleteComment(String id);

  List<Comment> restaurantGetComments(String restaurantId);
}
