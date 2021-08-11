package com.example.medical_clinic_app;

public interface LoginContract {
    interface Presenter {
        void validateLogin();
    }

    interface View {
        void loginFailed();
        void loginSuccess(String username);
        void emptyFieldsError();
        String getUsername();
        String getPassword();
    }
}
