package com.hcq.elion.api.service;

public interface Listener {
    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}