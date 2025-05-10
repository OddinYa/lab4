package com.example.lab2.service;

import com.example.lab2.model.dto.BuyDTO;
import com.example.lab2.model.dto.MoneyDTO;
import com.example.lab2.model.dto.UserDTO;

import java.util.List;

public interface FinanceService {


    void addUser(UserDTO userDTO, Integer familyId);


    UserDTO getUser(Integer userId);


    void deleteUser(Integer userId);


    void addBuy(BuyDTO buyDTO, Integer familyId);


    List<BuyDTO> getBuys(Integer familyId);


    void deleteBuy(Integer buyId, Integer familyId);


    Float getBalance(Integer familyId);


    void addBalance(MoneyDTO moneyDTO, Integer familyId);

    List<UserDTO> getUsers();

}
