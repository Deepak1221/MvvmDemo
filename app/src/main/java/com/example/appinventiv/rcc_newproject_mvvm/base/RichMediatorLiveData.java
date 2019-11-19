package com.example.appinventiv.rcc_newproject_mvvm.base;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;

public abstract class RichMediatorLiveData<T> extends MediatorLiveData<T> {

    private MutableLiveData<Throwable> errorLiveData;
    private MutableLiveData<FailureResponse> failureResponseLiveData;

    private void initLiveData() {
        errorLiveData = new MutableLiveData<>();
        failureResponseLiveData = new MutableLiveData<>();
    }

    protected abstract Observer<FailureResponse> getFailureObserver();

    protected abstract Observer<Throwable> getErrorObserver();

    @Override
    protected void onInactive() {
        super.onInactive();
        removeSource(failureResponseLiveData);
        removeSource(errorLiveData);
    }

    @Override
    protected void onActive() {
        super.onActive();
        initLiveData();
        addSource(failureResponseLiveData, getFailureObserver());
        addSource(errorLiveData, getErrorObserver());
    }

    public void setFailure(FailureResponse failureResponse) {
        failureResponseLiveData.setValue(failureResponse);
    }

    public void setError(Throwable t) {
        errorLiveData.setValue(t);
    }
}
