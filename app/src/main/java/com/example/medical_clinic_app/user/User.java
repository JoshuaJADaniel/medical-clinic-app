package com.example.medical_clinic_app.user;

import java.io.Serializable;

public interface User extends Serializable {
    String getGender();
    void setGender(String gender);

    String getUsername();
    void setUsername(String username);

    String getPassword();
    void setPassword(String password);

    String getName();
    void setName(String name);
}
