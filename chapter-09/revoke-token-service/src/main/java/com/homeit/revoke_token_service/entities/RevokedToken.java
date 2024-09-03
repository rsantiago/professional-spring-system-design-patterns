package com.homeit.revoke_token_service.entities;
import org.springframework.data.relational.core.mapping.Table;

@Table("revoked_tokens")
public class RevokedToken {

    private String revoked;

    public RevokedToken(String revoked) {
        this.revoked = revoked;
    }

    public String getRevoked() {
        return revoked;
    }

    public void setRevoked(String token) {
        this.revoked = token;
    }
}