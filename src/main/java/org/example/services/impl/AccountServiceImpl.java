package org.example.services.impl;

import org.example.dtos.AccountDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.entities.*;
import org.example.mappers.UserMapper;
import org.example.repositories.*;
import org.example.requests.ChangePasswordRequest;
import org.example.requests.DeleteAccountRequest;
import org.example.requests.SignupRequest;
import org.example.services.AccountService;
import org.example.utils.FileUtils;
import org.example.validators.AccountValidator;
import org.example.validators.ChangePasswordRequestValidator;
import org.example.validators.SignupRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Value("${default.user.avatar.code}")
    private String defaultUserAvatarCode;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private ContactRepository contactRepo;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private AccountValidator accountValidator;
    @Autowired
    private SignupRequestValidator signupRequestValidator;
    @Autowired
    private ChangePasswordRequestValidator changePasswordRequestValidator;
    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseDTO<UserDTO> signIn(AccountDTO accountDTO) throws Exception {
        Errors errors = new BeanPropertyBindingResult(accountDTO, "accountDTO");
        accountValidator.validate(accountDTO, errors);

        if (errors.hasErrors()) {
            throw new RuntimeException(errors.getAllErrors().get(0).getDefaultMessage());
        }

        Optional<AccountEntity> optionalAccount = this.accountRepo.findByUsernameAndPassword(
                accountDTO.getUsername(), accountDTO.getPassword());

        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Username or password is incorrect");
        }

        return ResponseDTO.<UserDTO>builder()
                .data(userMapper.toDTO(optionalAccount.get().getUser()))
                .build();
    }

    @Override
    public ResponseDTO<UserDTO> signUp(SignupRequest request) throws Exception {

        Errors errors = new BeanPropertyBindingResult(request, "signupRequest");
        signupRequestValidator.validate(request, errors);

        if (errors.hasErrors()) {
            throw new RuntimeException(errors.getAllErrors().get(0).getDefaultMessage());
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Password and confirm password do not match");
        }

        Optional<AccountEntity> optionalAccount = this.accountRepo.findById(request.getUsername());
        if (optionalAccount.isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        String randomId = UUID.randomUUID().toString();
        String randomAvatarCode = UUID.randomUUID().toString() + "." + ".png";

        UserEntity userEntity = UserEntity.builder()
                .id(randomId)
                .name(request.getUsername())
                .avatarCode(randomAvatarCode)
                .build();

        try {

            FileUtils.copyFile(this.defaultUserAvatarCode, randomAvatarCode);

            this.userRepo.save(userEntity);
            this.accountRepo.save(AccountEntity.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .user(userEntity)
                    .build());

            return ResponseDTO.<UserDTO>builder()
                    .data(userMapper.toDTO(userEntity))
                    .build();

        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot create account");
        }
    }

    @Override
    public ResponseDTO<Void> changePassword(ChangePasswordRequest changePasswordRequest) {

        Errors errors = new BeanPropertyBindingResult(changePasswordRequest, "changePasswordRequest");
        changePasswordRequestValidator.validate(changePasswordRequest, errors);

        if (errors.hasErrors()) {
            throw new RuntimeException(errors.getAllErrors().get(0).getDefaultMessage());
        }

        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmNewPassword = changePasswordRequest.getConfirmNewPassword();

        if (oldPassword.equals(newPassword)) {
            throw new RuntimeException("New password must be different from old password");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new RuntimeException("New password and confirm new password do not match");
        }

        Optional<AccountEntity> optionalAccount = accountRepo.findByUserIdAndPassword(
                changePasswordRequest.getUserId(), changePasswordRequest.getOldPassword());

        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Old password is incorrect");
        }

        AccountEntity foundAccount = optionalAccount.get();
        foundAccount.setPassword(newPassword);

        try {
            accountRepo.save(foundAccount);
        }catch (Exception e){
            throw new RuntimeException("Cannot change password");
        }

        return ResponseDTO.<Void>builder()
                .build();
    }

    @Override
    public ResponseDTO<Void> deleteAccount(DeleteAccountRequest deleteAccountRequest) {

        Optional<AccountEntity> optionalAccount = accountRepo.findByUserIdAndPassword(
                deleteAccountRequest.getUserId(), deleteAccountRequest.getPassword());

        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Password is incorrect");
        }

        AccountEntity foundAccount = optionalAccount.get();

        UserEntity foundUser = foundAccount.getUser();
        String avatarCode = foundUser.getAvatarCode();
        List<ConversationEntity> conversations = conversationRepo.findByParticipantId(foundUser.getId());
        try {

            conversations.forEach(conversation -> {
                if (conversation instanceof GroupConversationEntity group) {
                    if (group.getOwnedBy().equals(foundUser)){
                        try {
                            FileUtils.deleteFile(group.getAvatarCode());
                            conversationRepo.delete(group);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        conversation.getParticipants().remove(foundUser);
                        conversation.getMessages().removeIf(m -> m.getSender().equals(foundUser));

                        if (conversation.getLastestMessage().getSender().equals(foundUser)){
                            conversation.setLastestMessage(null);
                        }

                        messageRepo.deleteBySender(foundUser);
                        conversationRepo.save(conversation);

                    }
                }else {
                    conversationRepo.delete(conversation);
                    contactRepo.deleteBySenderOrReceiver(foundUser, foundUser);
                    conversationRepo.save(conversation);
                }
            });

            accountRepo.delete(foundAccount);

            FileUtils.deleteFile(avatarCode);

        }catch (Exception e){
            throw new RuntimeException("Cannot delete account");
        }

        return ResponseDTO.<Void>builder()
                .build();
    }
}
