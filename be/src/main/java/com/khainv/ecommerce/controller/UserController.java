package com.khainv.ecommerce.controller;

import com.khainv.ecommerce.dto.BaseReponse;
import com.khainv.ecommerce.dto.user.UserResponseDTO;
import com.khainv.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "user_controller")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
    public String addUser(@RequestBody UserResponseDTO createDTO) {
        userService.addUser(createDTO);
        return "success";
    }

    @Operation(summary = "Confirm user", description = "Send a request via this API to confirm user")
    @GetMapping("/confirm/{userId}")
    public BaseReponse<String> confirm(@Min(1) @PathVariable int userId, @RequestParam String verifyCode) {
        try {
            userService.confirmUser(userId, verifyCode);
            return new BaseReponse<>(HttpStatus.ACCEPTED.value(), "User has confirmed successfully", null);
        } catch (Exception e) {
            return new BaseReponse<>(HttpStatus.BAD_REQUEST.value(), "Confirm was failed", null);
        }
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("secretKey") String secretKey) {
        return "success";
    }
}
