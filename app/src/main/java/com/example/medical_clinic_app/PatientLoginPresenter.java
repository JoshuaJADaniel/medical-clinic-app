package com.example.medical_clinic_app;

import com.example.medical_clinic_app.services.ClinicDao;

public class PatientLoginPresenter implements LoginContract.Presenter {
    private final ClinicDao model;
    private final LoginContract.View view;

    public PatientLoginPresenter(LoginContract.View view, ClinicDao model) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void validateLogin() {
        String username = view.getUsername().trim();
        String password = view.getPassword();

        if (username.length() == 0 || password.length() == 0) {
            view.emptyFieldsError();
            return;
        }

        model.getPatient(username, patient -> {
            if (patient == null || !patient.getPassword().equals(password)) {
                view.loginFailed();
            } else {
                view.loginSuccess(patient.getUsername());
            }
        });
    }
}
