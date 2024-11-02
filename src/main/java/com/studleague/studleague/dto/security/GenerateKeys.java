package com.studleague.studleague.dto.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class GenerateKeys {

    public static void main(String[] args) {
        SecretKey accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String accessKeyBase64 = Base64.getEncoder().encodeToString(accessKey.getEncoded());
        System.out.println("Access Token Key (Base64): " + accessKeyBase64);

        SecretKey refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String refreshKeyBase64 = Base64.getEncoder().encodeToString(refreshKey.getEncoded());
        System.out.println("Refresh Token Key (Base64): " + refreshKeyBase64);
    }
}

