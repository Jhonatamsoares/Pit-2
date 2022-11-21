package com.cs5500.NEUEat.service;

import com.cs5500.NEUEat.model.Customer;
import com.cs5500.NEUEat.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements UserService<Customer> {

  @Autowired
  CustomerRepository customerRepository;
  PasswordService passwordService = new PasswordService();

  @Override
  public Customer addUser(String userName, String password, String phoneNumber, String address,
      String city, String state, String zip) {
    if (this.getUserIdByName(userName) == null) {
      String newPassword = passwordService.generatePassword(password);
      Customer customer = new Customer(userName, newPassword, phoneNumber, address, city, state, zip);
      customerRepository.insert(customer);
      System.out.println("Cliente adicionado ao banco de dados");
      return customer;
    }
    System.out.println("O cliente não pode ser adicionado ao banco de dados");
    return null;
  }

  @Override
  public int deleteUser(String id) {
    if (this.getUser(id).isPresent()) {
      customerRepository.deleteById(id);
      System.out.println("Cliente excluído do banco de dados");
      return 1;
    }
    System.out.println("O cliente não pode ser excluído do banco de dados");
    return -1;
  }

  @Override
  public Optional<Customer> getUser(String id) {
    if (id != null) {
      return customerRepository.findById(id);
    }
    return Optional.empty();
  }

  @Override
  public String getUserIdByName(String userName) {
    List<Customer> customers = this.getUsers();
    for (Customer customer : customers) {
      if (customer.getUserName().equals(userName)) {
        return customer.getId();
      }
    }
    System.out.println("O nome de usuário fornecido não foi encontrado no banco de dados do cliente");
    return null;
  }

  @Override
  public Optional<Customer> getUserByName(String userName) {
    return this.getUser(getUserIdByName(userName));
  }

  @Override
  public List<Customer> getUsers() {
    return customerRepository.findAll();
  }

  @Override
  public boolean passwordMatch(String id, String password) {
    Optional<Customer> customer = this.getUser(id);
    return customer.isPresent() && passwordService.passwordMatch(password, customer.get().getPassword());
  }

  @Override
  public int updatePassword(String id, String oldPassword, String newPassword) {
    Optional<Customer> customer = this.getUser(id);
    if (customer.isPresent()) {
      if (this.passwordMatch(id, oldPassword)) {
        customer.get().setPassword(passwordService.generatePassword(newPassword));
        customerRepository.save(customer.get());
        System.out.println("Atualize a senha");
        return 1;
      } else {
        System.out.println("A senha não corresponde");
        return 0;
      }
    }
    System.out.println("Não é possível atualizar a senha");
    return -1;
  }

  @Override
  public int updatePhoneNumber(String id, String newNumber) {
    Optional<Customer> customer = this.getUser(id);
    if (customer.isPresent()) {
      customer.get().setPhoneNumber(newNumber);
      customerRepository.save(customer.get());
      System.out.println("Atualize o numero do telefone");
      return 1;
    }
    System.out.println("Não é possível atualizar o número de telefone");
    return -1;
  }

  @Override
  public int updateAddress(String id, String address, String city, String state,
      String zip) {
    Optional<Customer> customer = this.getUser(id);
    if (customer.isPresent()) {
      customer.get().setAddress(address);
      customer.get().setCity(city);
      customer.get().setState(state);
      customer.get().setZip(zip);
      customerRepository.save(customer.get());
      System.out.println("Atualize o endereço");
      return 1;
    }
    System.out.println("Não é possível atualizar o endereço");
    return -1;
  }
}
