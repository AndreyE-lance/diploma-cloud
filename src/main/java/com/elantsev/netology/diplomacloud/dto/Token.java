package com.elantsev.netology.diplomacloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("auth-token")
    private final String authToken;



    public Token(String authToken) {
        this.authToken = authToken;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Token)) return false;
        final Token other = (Token) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$authToken = this.getAuthToken();
        final Object other$authToken = other.getAuthToken();
        if (this$authToken == null ? other$authToken != null : !this$authToken.equals(other$authToken)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Token;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $authToken = this.getAuthToken();
        result = result * PRIME + ($authToken == null ? 43 : $authToken.hashCode());
        return result;
    }

    public String toString() {
        return "Token(authToken=" + this.getAuthToken() + ")";
    }

    public static class TokenBuilder {
        private String authToken;

        TokenBuilder() {
        }

        public TokenBuilder authToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public Token build() {
            return new Token(authToken);
        }

        public String toString() {
            return "Token.TokenBuilder(authToken=" + this.authToken + ")";
        }
    }
}