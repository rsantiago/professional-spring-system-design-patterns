package com.homeit.authprovider.service;

import com.homeit.authprovider.dto.TokenRequest;
import com.homeit.authprovider.dto.TokenResponse;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService{

    @Override
    public TokenResponse getJWTToken(TokenRequest tokenRequest, String scope, String userId) {
        try {
            KeyPair keyPair = loadRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            Date issueTime = new Date();
            Date expiry = new Date(System.currentTimeMillis() + 3600000);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId)
                    .issueTime(issueTime)
                    .claim("scope", scope)
                    .expirationTime(expiry) // 1 hour expiry
                    .build();

            String keyId = "fab38aa6-d05b-4ab8-b045-8362b90acfdf";
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(keyId).build(),
                    claimsSet
            );

            signedJWT.sign(new RSASSASigner(privateKey));
            String jwtToken = signedJWT.serialize();
            return new TokenResponse(jwtToken, "Bearer", "3600", scope );
        } catch (Exception e) {
            throw new RuntimeException("Error creating token", e);
        }
    }

    private static KeyPair loadRsaKey() {
        // Decode the Base64 encoded strings
        byte[] privateKeyBytes = Base64.getDecoder().decode("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCu+HCxXu8elxzLLwkbXkdheZzFoTD5X8zkYoVT0U56NYVg9AZ5pHk1KW/c7OLt5BTa42TbWBaau5Y+NmPfVOSka5goobzhKNm9Zq3rywHz1G8Azi69TyshYaO3qFvw6Ui2pzbClUTuyfuEEybHCCTQpDm/9mres8/I9yhwmIjaCMX1IQssiM/IO05X5p48kqZXVEpRfZX2IIJzg99dhuZn3mtSJhSzn7+ePICikx6Ej6PWswAIRdsdi7+rT8Oqtr3TDWzHKH/xAYVPzVERfdt2l4CGXKX6/1vujYJe8vL31ACZ7hXC5DHgBm7CDbApWEjuo/dLIHqmkBIxjhQi2rvhAgMBAAECggEAT/EU1AVS7UAPmMnBuG5n4KJGvYj7LkTWAVb+IaCU4YNIkFaOCs/ZzS9Ee8UDnbtCyAtzDkB9+25Po9cH5IcN/A1otM/I03rtmPqr5fqXxsa9fLm3YykjxJmuyn+ymiyL22AkxdybZRqFvv9dDTysTWNrO0ij5WJ0QhaitGcMx8jN7aTSFhuzhSriBBgbMPmcI/zWgd6Y2+sYJRyWbladuwc7UNF8UK8+WGq/qkdBwZnaKYm4JTu/LIUqbpGeMDsYT41D/328mclL3HbuZI0HI5WfhQGy0CMzHecPQuiJvIpVDMFO3zgd3h8F7OwamX02Y+lrJ/YRqyrksFGt9+WeHQKBgQC+p7ACr1Bhfp+vc54bwlztCvCUQA27I6apYgZDIEdgjW62ONtWAXCeUjIcG5ZAm4lT2BKG8dQUsQxZTzu6h/rkGYEYqLqjWKwb0qDVu2eSoT7+8wIBe59M+Q3DOYYuqXWiUX6E4JH74XPIBcYah+H1oGGiiRlTvyzM+ytXSFnJ/wKBgQDq8JHStoEhjDJ1lJMRbyX92jOXBu/ppIob0liEgZoC0SJ+RRoJbSrJYEbSBYFMq9azsBvJSHo8Vlnxzjkmx0JqjJKMSHptxV16MmajIWR0uL/aQUa5PYAo8LWxbtVQaEpU4t9P5xvnO9KtvyBaxTWoTfYlLSBm68tnyy3srMi6HwKBgHox16mg24/hLE+zdp6sEAsNe+xIsYRD8UmWvhS/13+WpmK8Rjf1bEzMubkyJTb7eST308gRrrAkWZnUIqAHD1Kq1gcL/bM73fNnKBZUqqlcF5goAB0bMpqO9bcuY0X87pw+Ryy7ElWxxhdpO/M9LCHVzT3zSPxYB0rJoJmskX/ZAoGAVy4D/pvomQlMJCOI21xSTbutjyps1ldPRHmujSUGgGsYkKCVw8+1o9pNDVwrmcbeOflspil16nPklnz+6ivgsmcNbm7qt1cYk+KmAXy/qSxxTM0SMGTo7Mg9s+S71UWN56f0U9MfoJOrXvvgRq6NdbHQWAVB089FwHM9zGhlZ+MCgYBgLrqV66zaxxEdo4zWAwBnTTHu5tHJFDsjWd+g3SaJh0j5U8Oaj2x1ac872qqkxHfOqsj7RIhMbaNuwcLfMbycM1JsDV09g9IbFSGANPDHKk8lyyPh7R4/BQDQILAx6GzEEzZc4ZWhOAUFuVXRHnMNY3+UxCNqGP6tqEV+6nAHIA==");
        byte[] publicKeyBytes = Base64.getDecoder().decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArvhwsV7vHpccyy8JG15HYXmcxaEw+V/M5GKFU9FOejWFYPQGeaR5NSlv3Ozi7eQU2uNk21gWmruWPjZj31TkpGuYKKG84SjZvWat68sB89RvAM4uvU8rIWGjt6hb8OlItqc2wpVE7sn7hBMmxwgk0KQ5v/Zq3rPPyPcocJiI2gjF9SELLIjPyDtOV+aePJKmV1RKUX2V9iCCc4PfXYbmZ95rUiYUs5+/njyAopMehI+j1rMACEXbHYu/q0/Dqra90w1sxyh/8QGFT81REX3bdpeAhlyl+v9b7o2CXvLy99QAme4VwuQx4AZuwg2wKVhI7qP3SyB6ppASMY4UItq74QIDAQAB");

        // Generate PrivateKey from decoded bytes
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        PrivateKey privateKey = null;
        try {
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        // Generate PublicKey from decoded bytes
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = null;
        try {
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        // Create and return the KeyPair
        return new KeyPair(publicKey, privateKey);
    }


    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Encode the keys to Base64 strings
            String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            // Print the keys
            System.out.println("Private Key: " + privateKeyString);
            System.out.println("Public Key: " + publicKeyString);
            return keyPair;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
