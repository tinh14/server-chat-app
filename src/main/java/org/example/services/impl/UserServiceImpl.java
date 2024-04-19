package org.example.services.impl;

import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.entities.UserEntity;
import org.example.mappers.UserMapper;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.example.utils.FileUtils;
import org.example.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${default.user.avatar.code}")
    private String defaultUserAvatarCode;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseDTO<UserDTO> findById(String userId) {
        Optional<UserEntity> optionalUser = this.userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return ResponseDTO.<UserDTO>builder()
                .data(this.userMapper.toDTO(optionalUser.get()))
                .build();
    }

    @Override
    public ResponseDTO<String> update(UserDTO userDTO, MultipartFile avatar) throws Exception {

        this.userValidator.validate(userDTO);

        Optional<UserEntity> optionalUser = this.userRepository.findById(userDTO.getId());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity foundUser = optionalUser.get();
        foundUser.setName(userDTO.getName());

        try {

            String newFileCode = foundUser.getAvatarCode();

            if (avatar != null) {
                newFileCode = FileUtils.getFileCode(avatar);
                String oldFileCode = foundUser.getAvatarCode();

                foundUser.setAvatarCode(newFileCode);

                FileUtils.storeFile(oldFileCode, newFileCode, avatar);
            }

            this.userRepository.save(foundUser);

            return ResponseDTO.<String>builder()
                    .data(newFileCode)
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Cannot update user");
        }
    }


}
