package com.example.lab2.service;

import com.example.lab2.model.dto.*;
import com.example.lab2.model.entity.Buy;
import com.example.lab2.model.entity.Family;
import com.example.lab2.model.entity.Money;
import com.example.lab2.model.entity.User;
import com.example.lab2.repository.BuyRepository;
import com.example.lab2.repository.FamilyRepository;
import com.example.lab2.repository.MoneyRepository;
import com.example.lab2.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class FinanceServiceImpl implements FinanceService {

    private final UserRepository userRepository;
    private final BuyRepository buyRepository;
    private final MoneyRepository moneyRepository;
    private  final FamilyRepository familyRepository;

    @Autowired
    public FinanceServiceImpl(UserRepository userRepository,
                              BuyRepository buyRepository,MoneyRepository moneyRepository,
                              FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
        this.moneyRepository = moneyRepository;
        this.buyRepository = buyRepository;
    }



    @Override
    @Transactional
    public void addUser(UserDTO userDTO, Integer familyId) {
        // 1. Находим семью
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Семья не найдена"));

        // 2. Работа с деньгами семьи
        Money familyMoney = family.getMoney();
        familyMoney.setCash(familyMoney.getCash() + userDTO.getMoney().getCash());
        moneyRepository.save(familyMoney);  // Обновляем общий бюджет

        // 3. Создаем пользователя
        User user = new User();
        user.setName(userDTO.getName());
        user.setFamily(family);
        user.setMoney(familyMoney);  // Используем общий Money семьи
        user.setSpending(0);

        // 4. Сохраняем каскадно
        user.getBuyList().clear();  // Очищаем временные данные
        userRepository.save(user);

        // 5. Обновляем связь в семье (опционально)
        family.getUserList().add(user);
        familyRepository.save(family);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDTO getUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return convertToUserDTO(user.get());
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }


    @Override
    @Transactional
    public void addBuy(BuyDTO buyDTO, Integer familyId) {

       var buy = convertToBuy(buyDTO);
       var user = userRepository.findById(buyDTO.getIdUser());
       buy.setName(user.get().getName());

       buyRepository.save(buy);


    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyDTO> getBuys(Integer familyId) {

        var family = familyRepository.findById(familyId).orElseThrow();

        List<User> users = family.getUserList();

        List<BuyDTO> buyDTOs = new ArrayList<>();
        for (User user : users) {
            List<Buy> userBuys = user.getBuyList();
            for (Buy buy : userBuys) {
                buyDTOs.add(convertToBuyDTO(buy));
            }
        }

        return buyDTOs;
    }



    @Override
    @Transactional
    public void deleteBuy(Integer buyId, Integer familyId) {
      buyRepository.deleteById(buyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Float getBalance(Integer familyId) {
        Optional<Family> family = familyRepository.findById(familyId);

        return (Float) family.get().getMoney().getCash();
    }

    @Override
    @Transactional
    public void addBalance(MoneyDTO moneyDTO, Integer familyId) {

        Family family = familyRepository.findById(familyId)
                .orElseThrow();

        family.getMoney().setCash(
                family.getMoney().getCash() + moneyDTO.getCash()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers() {

        List<User> users = userRepository.findAll();

        return users.
                stream().
                map(this::convertToUserDTO).
                collect(Collectors.toList());
    }





    // Конвертеры DTO
    private User convertToUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setSpending(dto.getSpending());
        user.setBuyList(new ArrayList<>());
        return user;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setMoney(new MoneyDTO().cash(user.getMoney().getCash()));
        dto.setName(user.getName());
        dto.setSpending(user.getSpending());
        dto.setBuyList(user.getBuyList().stream()
                .map(this::convertToBuyDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private Buy convertToBuy(BuyDTO dto) {

        User user = userRepository.findById(dto.getIdUser()).orElseThrow();

        return new Buy(user,dto.getCost(),dto.getName(),dto.getDate(),dto.getUserName());
    }

    private BuyDTO convertToBuyDTO(Buy buy) {
        BuyDTO dto = new BuyDTO();
        dto.setId(buy.getId());
        dto.setCost(buy.getCost());
        dto.setName(buy.getName());
        dto.setDate(buy.getDate());
        dto.setUserName(buy.getUserName());
        return dto;
    }

    private FamilyDTO convertToFamilyDTO(Family family) {
        FamilyDTO dto = new FamilyDTO();
        dto.setMoney(new MoneyDTO().cash(family.getMoney().getCash()));
        dto.setUserList(family.getUserList().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList()));
        return dto;
    }


}