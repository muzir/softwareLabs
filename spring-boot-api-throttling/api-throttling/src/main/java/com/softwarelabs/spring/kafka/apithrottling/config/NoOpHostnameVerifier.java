package com.softwarelabs.spring.kafka.apithrottling.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public final class NoOpHostnameVerifier implements HostnameVerifier {

    public static final HostnameVerifier INSTANCE = new NoOpHostnameVerifier();

    private NoOpHostnameVerifier() {
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

