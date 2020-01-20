/**
 * For commerce and communication.
 * <p>
 * Copyright 2015 Fara, Org. All rights reserved.
 * Use is subject to license terms.
 * <p>
 * Create At 2015年12月2日
 */
package com.anyangdp.handler;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;

import java.util.List;

/**
 * @author william
 */
public abstract class GenericHandler {

    protected int defaultPageSize() {
        return 10;
    }

    protected int setAndGetPageSize(Integer pageSize) {
        if (null == pageSize || 0 == pageSize) {
            return defaultPageSize();
        } else {
            return pageSize;
        }
    }

    protected int defaultPageNumber() {
        return 0;
    }

    protected int setAndGetPageNumber(Integer pageNumber) {
        if (null == pageNumber) {
            return defaultPageNumber();
        } else {
            if (pageNumber > 0) {
                return pageNumber - 1;
            }
            return pageNumber;
        }
    }


    protected <T> void extractResponse(GenericResponse<List<T>> response, Page<T> data) {

        response.setData(data.getContent());

        PageDTO pageDTO = new PageDTO();

        pageDTO.setSize(setAndGetPageSize(data.getSize()));
        pageDTO.setNumber(data.getNumber()+1);
        pageDTO.setTotalPages(data.getTotalPages());
        pageDTO.setNumberOfElements(data.getNumberOfElements());
        pageDTO.setTotalElements(data.getTotalElements());

        response.setPage(pageDTO) ;
        response.setResult(true);
    }

    //	@Resource
//    private ValidatorManager validatorManager;

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
