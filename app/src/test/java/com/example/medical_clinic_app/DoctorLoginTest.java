package com.example.medical_clinic_app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.user.Doctor;
import com.example.medical_clinic_app.user.DoctorObj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DoctorLoginTest {
    @Mock
    ClinicDao model;

    @Mock
    LoginContract.View view;

    @Test
    public void emptyUsernameTest() {
        when(view.getUsername()).thenReturn("");
        when(view.getPassword()).thenReturn("abc");
        LoginContract.Presenter presenter = new DoctorLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view).emptyFieldsError();
    }

    @Test
    public void emptyPasswordTest() {
        when(view.getUsername()).thenReturn("abc");
        when(view.getPassword()).thenReturn("");
        LoginContract.Presenter presenter = new DoctorLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view).emptyFieldsError();
    }

    @Test
    public void emptyUsernameAndPasswordTest() {
        when(view.getUsername()).thenReturn("");
        when(view.getPassword()).thenReturn("");
        LoginContract.Presenter presenter = new DoctorLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view).emptyFieldsError();
    }

    @Test
    public void failedLoginTest() {
        doAnswer(invocation -> {
            ClinicDao.DoctorListener doctorListener = invocation.getArgument(1);
            Doctor doctor = new DoctorObj();
            doctor.setUsername("abc");
            doctor.setPassword("xyz");
            doctorListener.callback(doctor);
            return null;
        }).when(model).getDoctor(anyString(), any(ClinicDao.DoctorListener.class));

        when(view.getUsername()).thenReturn("abc");
        when(view.getPassword()).thenReturn("abc");
        LoginContract.Presenter presenter = new DoctorLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view).loginFailed();
    }

    @Test
    public void successfulLoginTest() {
        doAnswer(invocation -> {
            ClinicDao.DoctorListener doctorListener = invocation.getArgument(1);
            Doctor doctor = new DoctorObj();
            doctor.setUsername("abc");
            doctor.setPassword("abc");
            doctorListener.callback(doctor);
            return null;
        }).when(model).getDoctor(anyString(), any(ClinicDao.DoctorListener.class));

        when(view.getUsername()).thenReturn("abc");
        when(view.getPassword()).thenReturn("abc");
        LoginContract.Presenter presenter = new DoctorLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view).loginSuccess("abc");
    }
}
