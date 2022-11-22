package com.cs5500.Delivery.service;

import com.cs5500.Delivery.model.Driver;
import com.cs5500.Delivery.repository.DriverRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements UserService<Driver> {

  @Autowired
  DriverRepository driverRepository;
  PasswordService passwordService = new PasswordService();

  @Override
  public Driver addUser(String userName, String password, String phoneNumber, String address,
      String city, String state, String zip) {
    if (this.getUserIdByName(userName) == null) {
      String newPassword = passwordService.generatePassword(password);
      Driver driver = new Driver(userName, newPassword, phoneNumber, address, city, state, zip);
      driverRepository.insert(driver);
      System.out.println("Entregador adicionado ao banco de dados");
      return driver;
    }
    System.out.println("Entregador não pode ser adicionado ao banco de dados");
    return null;
  }

  @Override
  public int deleteUser(String id) {
    if (this.getUser(id).isPresent()) {
      driverRepository.deleteById(id);
      System.out.println("Entregador deleted from the database");
      return 1;
    }
    System.out.println("Entregador excluído do banco de dados");
    return -1;
  }

  @Override
  public Optional<Driver> getUser(String id) {
    if (id != null) {
      return driverRepository.findById(id);
    }
    return Optional.empty();
  }

  @Override
  public String getUserIdByName(String userName) {
    List<Driver> drivers = this.getUsers();
    for (Driver driver : drivers) {
      if (driver.getUserName().equals(userName)) {
        return driver.getId();
      }
    }
    System.out.println("O nome de usuário fornecido não foi encontrado no banco de dados do entregador");
    return null;
  }

  @Override
  public Optional<Driver> getUserByName(String userName) {
    return this.getUser(getUserIdByName(userName));
  }

  @Override
  public List<Driver> getUsers() {
    return driverRepository.findAll();
  }

  @Override
  public boolean passwordMatch(String id, String password) {
    Optional<Driver> driver = this.getUser(id);
    return driver.isPresent() && passwordService.passwordMatch(password, driver.get().getPassword());
  }

  @Override
  public int updatePassword(String id, String oldPassword, String newPassword) {
    Optional<Driver> driver = this.getUser(id);
    if (driver.isPresent()) {
      if (this.passwordMatch(id, oldPassword)) {
        driver.get().setPassword(passwordService.generatePassword(newPassword));
        driverRepository.save(driver.get());
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
    Optional<Driver> driver = this.getUser(id);
    if (driver.isPresent()) {
      driver.get().setPhoneNumber(newNumber);
      driverRepository.save(driver.get());
      System.out.println("Atualizar o número de telefone");
      return 1;
    }
    System.out.println("Não é possível atualizar o número de telefone");
    return -1;
  }

  @Override
  public int updateAddress(String id, String address, String city, String state,
      String zip) {
    Optional<Driver> driver = this.getUser(id);
    if (driver.isPresent()) {
      driver.get().setAddress(address);
      driver.get().setCity(city);
      driver.get().setState(state);
      driver.get().setZip(zip);
      driverRepository.save(driver.get());
      System.out.println("Atualize o endereço");
      return 1;
    }
    System.out.println("Não é possível atualizar o endereço");
    return -1;
  }
}
