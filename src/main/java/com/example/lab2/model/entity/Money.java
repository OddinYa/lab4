package com.example.lab2.model.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data

public class Money  {

    public Money(float cash){

        this.cash = cash;
    }
    public Money(){

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private float cash;

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
// @Override
  // public void makeMoney(float cash) {
  //     this.cash += cash;
  // }
}