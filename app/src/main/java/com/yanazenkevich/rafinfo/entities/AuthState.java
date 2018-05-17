package com.yanazenkevich.rafinfo.entities;


public class AuthState {

    private boolean isSigned;

    public boolean isSigned() {
        return isSigned;
    }

    public AuthState(boolean isSigned) {
        this.isSigned = isSigned;
    }
}
