package com.anyangdp.service;

public interface CRUDServiceAware<S> {

    <SS extends S> SS getService();
}
