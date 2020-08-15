package ru.javamentor.springbootcrud.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;


public enum Role implements GrantedAuthority {

    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
