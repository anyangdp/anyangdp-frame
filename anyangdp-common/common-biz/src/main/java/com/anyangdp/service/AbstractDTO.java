package com.anyangdp.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author william
 */
@Setter
@Getter
public abstract class AbstractDTO implements IdentifierAwareDTO, Serializable {

    private String id;
}
