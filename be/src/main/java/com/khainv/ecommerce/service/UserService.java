package com.khainv.ecommerce.service;

import com.khainv.ecommerce.dto.user.UserResponseDTO;
import com.khainv.ecommerce.entity.UserEntity;
import com.khainv.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ModelMapper modelMapper = new ModelMapper();

    public void addUser(UserResponseDTO createDTO) {
        UserEntity userEntity = modelMapper.map(createDTO, UserEntity.class);
        UserEntity result = userRepository.save(userEntity);
        if(result != null) {
            kafkaTemplate.send("confirm-account-topic", String.format("email=%s,id=%s,code=%s", result.getEmail(), result.getId(), "code@123"));
        }
    }

    public String confirmUser(int userId, String verifyCode) {
        return String.format("Confirmed user %s with verification code %s!", userId, verifyCode);
    }


}