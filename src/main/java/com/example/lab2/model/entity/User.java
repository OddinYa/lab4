package com.example.lab2.model.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Setter
@Getter
public class User  {

    public User(){

    }
    public User(Money money,String name){

        this.money = money;
        this.spending = 0;
        this.name = name;
        this.buyList = new ArrayList<>();

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "money_id",referencedColumnName = "id")
    private Money money;
    private float spending;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    public void setFamily(Family family){
        this.family = family;
    }
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Buy> buyList = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void updateSpending() {
        if (buyList != null) {
            this.spending = (float) buyList.stream()
                    .mapToDouble(Buy::getCost)
                    .sum();
        }
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

    public float getSpending() {
        return spending;
    }

    public void setSpending(float spending) {
        this.spending = spending;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Buy> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<Buy> buyList) {
        this.buyList = buyList;
    }
//  @Override
 //  public void spendMoney(Buy buy) {
 //      buyList.add(buy);
 //      spending+=buy.getCost();
 //      money.setCash(money.getCash()- buy.getCost());
 //  }

 //  @Override
 //  public void makeMoney(float cash) {
 //       money.setCash(money.getCash()+cash);
 //   }
}