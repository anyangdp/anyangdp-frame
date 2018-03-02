package com.anyangdp.damain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author wjwjtftf
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> extends AbstractPersistableEntity<ID> {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;
}
