package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.dto.InfoFromTokenRequest;
import com.dedlam.ftesterlab.auth.dto.InfoFromTokenResponse;
import com.dedlam.ftesterlab.feign.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final AuthServiceClient authServiceClient;

    public InfoFromTokenResponse infoFromAccessToken(InfoFromTokenRequest infoFromTokenRequest) {
        return authServiceClient.infoFromAccessToken(infoFromTokenRequest);
    }

}
