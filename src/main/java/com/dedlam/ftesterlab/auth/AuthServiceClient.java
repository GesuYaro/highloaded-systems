package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.dto.CreateUserRequest;
import com.dedlam.ftesterlab.auth.dto.InfoFromTokenRequest;
import com.dedlam.ftesterlab.auth.dto.InfoFromTokenResponse;
import com.dedlam.ftesterlab.auth.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient("auth-service")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/exists")
    Boolean userExists(@RequestParam UUID id);

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    User user(@PathVariable UUID id);

    @RequestMapping(method = RequestMethod.GET, value = "/users/exists")
    Boolean existsByUsername(@RequestParam String username);

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    User findUserByUsername(@RequestParam String username);

    @RequestMapping(method = RequestMethod.GET, value = "/users/bulk/by-usernames")
    List<User> findUsersByUsernames(@RequestParam Set<String> usernames);

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    UUID createUser(CreateUserRequest createUserRequest);

    @RequestMapping(method = RequestMethod.POST, value = "/auth/tokens/access-info")
    InfoFromTokenResponse infoFromAccessToken(InfoFromTokenRequest infoFromTokenRequest);
}
