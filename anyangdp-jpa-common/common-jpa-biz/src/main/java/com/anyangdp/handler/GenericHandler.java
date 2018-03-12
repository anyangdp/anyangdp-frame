/**
 * For commerce and communication.
 * <p>
 * Copyright 2015 Fara, Org. All rights reserved.
 * Use is subject to license terms.
 * <p>
 * Create At 2015年12月2日
 */
package com.anyangdp.handler;

import com.anyangdp.validator.ValidatorManager;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;

import java.util.List;

/**
 * @author william
 */
public abstract class GenericHandler {

    protected <T> void extractResponse(GenericResponse<List<T>> response, Page<T> data) {

        response.setData(data.getContent());

        PageDTO pageDTO = new PageDTO();

        pageDTO.setNumber(data.getNumber() + 1);
        pageDTO.setNumberOfElements(data.getNumberOfElements());
        pageDTO.setSize(data.getSize());
        pageDTO.setTotalPages(data.getTotalPages());
        pageDTO.setTotalElements(data.getTotalElements());

        response.setPage(pageDTO) ;
        response.setResult(true);
    }

    //	@Resource
    private ValidatorManager validatorManager;

    //	@InitBinder
    protected void initBinder(WebDataBinder binder) {

//		binder.setValidator(validatorManager.getValidator());
//
//		binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
//			@Override
//			public void setAsText(String text) {
//				setValue(LocalDateTime.parse(text));
//			}
//		});

    }
}
