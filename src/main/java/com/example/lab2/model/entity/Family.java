package com.example.lab2.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "family")
@Data
@Getter
@Setter
public class Family {

    public Family(){}
    public Family(Money money){
        this.money = money;
        userList = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    private Money money;
    @OneToMany(mappedBy = "family",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> userList = new ArrayList<>();

    public void addUser(User user) {
        userList.add(user);
        user.setFamily(this);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
// public void addInFamily(User user){
   //     userList.add(user);
   // }

  // public float getTotalExpenses() {
  //     float total = 0;
  //     for (User user : userList) {
  //         for (Buy buy : user.getBuyList()) {
  //             total += buy.getCost();
  //         }
  //     }
  //     return total;
  // }

  // public float getUserExpenses(String userName) {
  //     for (User user : userList) {
  //         if (user.getName().equals(userName)) {
  //             float total = 0;
  //             for (Buy buy : user.getBuyList()) {
  //                 total += buy.getCost();
  //             }
  //             return total;
  //         }
  //     }
  //     return 0;
  // }

}