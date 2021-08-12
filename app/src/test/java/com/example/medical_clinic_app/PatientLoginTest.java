package com.example.medical_clinic_app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.medical_clinic_app.services.ClinicDao;
import com.example.medical_clinic_app.user.Patient;
import com.example.medical_clinic_app.user.PatientObj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PatientLoginTest {
    @Mock
    ClinicDao model;

    @Mock
    LoginContract.View view;

    @Test
    public void emptyUsernameTest() {
        when(view.getUsername()).thenReturn("");
        when(view.getPassword()).thenReturn("abc");
        LoginContract.Presenter presenter = new PatientLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view, times(1)).emptyFieldsError();
    }

    @Test
    public void emptyPasswordTest() {
        when(view.getUsername()).thenReturn("abc");
        when(view.getPassword()).thenReturn("");
        LoginContract.Presenter presenter = new PatientLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view, times(1)).emptyFieldsError();
    }

    @Test
    public void emptyUsernameAndPasswordTest() {
        when(view.getUsername()).thenReturn("");
        when(view.getPassword()).thenReturn("");
        LoginContract.Presenter presenter = new PatientLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view, times(1)).emptyFieldsError();
    }

    @Test
    public void failedLogin() {
        doAnswer(invocation -> {
            ClinicDao.PatientListener patientListener = invocation.getArgument(1);
            Patient patient = new PatientObj();
            patient.setUsername("abc");
            patient.setPassword("xyz");
            patientListener.callback(patient);
            return null;
        }).when(model).getPatient(anyString(), any(ClinicDao.PatientListener.class));

        when(view.getUsername()).thenReturn("abc");
        when(view.getPassword()).thenReturn("abc");
        LoginContract.Presenter presenter = new PatientLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view).loginFailed();
    }

    @Test
    public void successfulLogin() {
        doAnswer(invocation -> {
            ClinicDao.PatientListener patientListener = invocation.getArgument(1);
            Patient patient = new PatientObj();
            patient.setUsername("abc");
            patient.setPassword("abc");
            patientListener.callback(patient);
            return null;
        }).when(model).getPatient(anyString(), any(ClinicDao.PatientListener.class));

        when(view.getUsername()).thenReturn("abc");
        when(view.getPassword()).thenReturn("abc");
        LoginContract.Presenter presenter = new PatientLoginPresenter(view, model);
        presenter.validateLogin();
        verify(view).loginSuccess("abc");
    }
}
