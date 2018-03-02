/**
 * For Business Activities.
 * <p>
 * Copyright 2016 zhongxin, BSS Team. All rights reserved.
 * May not be used without authorization.
 * <p>
 * Create At 2016年8月28日
 */
package com.anyangdp.handler;

import com.anyangdp.service.AbstractDTO;
import com.anyangdp.service.CRUDService;
import com.anyangdp.service.CRUDServiceAware;
import com.anyangdp.service.PageableService;
import com.anyangdp.utils.ReflectionUtils;
import com.anyangdp.utils.ValueUtil;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public abstract class AbstractCRUDHandler<ID, T extends AbstractDTO, S extends CRUDService<ID, T>> extends GenericHandler
        implements CRUDServiceAware<S>, BeanFactoryAware {

    private Class<S> defaultServiceClass;

    @Setter
    private BeanFactory beanFactory;

    @SuppressWarnings("unchecked")
    protected AbstractCRUDHandler() {
        defaultServiceClass = ReflectionUtils.getClassGenricType(getClass(), 2);
    }

    @Override
    public S getService() {
        return beanFactory.getBean(defaultServiceClass);
    }

    @PostMapping
    public GenericResponse<T> create(@RequestBody @Valid T request, BindingResult bindingResult) throws Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<T> response) -> {

            response.setData(getService().insert(request));
            response.setResult(true);
        });
    }

    @GetMapping(value = "/{id}")
    public GenericResponse<T> retrieve(@PathVariable("id") String id) throws Exception {

        return ControllerTemplate.call((GenericResponse<T> response) -> {
            ID transferId = (ID) ValueUtil.idDecode(id);
            if (null != transferId) {
                response.setData(getService().searchByPk(transferId));
            }
            response.setResult(true);
        });
    }

    @PostMapping(value = "/find")
    public GenericResponse<T> retrieve(@RequestBody @Valid T request, BindingResult bindingResult) throws Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<T> response) -> {

            response.setData(getService().searchByCondition(request));
            response.setResult(true);
        });
    }

    @PutMapping
    public GenericResponse<Void> update(@RequestBody @Valid T request, BindingResult bindingResult) throws
            Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<Void> response) -> {

            response.setResult(getService().update(request));
        });
    }

    @DeleteMapping(value = "/{id}")
    public GenericResponse<Void> delete(@PathVariable("id") String id) throws Exception {

        return ControllerTemplate.call((GenericResponse<Void> response) -> {

            response.setResult(getService().delete((ID) ValueUtil.idDecode(id)));
        });
    }

    @GetMapping(value = "/listAll")
    public GenericResponse<List<T>> listAll() throws Exception {

        return ControllerTemplate.call((GenericResponse<List<T>> response) -> {

            List allData = ((PageableService) getService()).listActive();
            extractResponse(response, new PageImpl(allData));
        });
    }

//    @GetMapping(value = "/list")
//    public GenericResponse<List<T>> list() throws Exception {
//
//        GenericResponse<List<T>> response = list(null, null);
//
//        response.setPage(null);
//
//        return response;
//    }

//    @GetMapping(value = "/list/{num}")
//    public GenericResponse<List<T>> list(@PathVariable(value = "num") Integer num) throws Exception {
//
//        return list(num, null);
//    }

//    @GetMapping(value = "/list/{num}/{page_size}")
//    public GenericResponse<List<T>> list(@PathVariable(value = "num") Integer num,
//                                         @PathVariable(value = "page_size") Integer pageSize) throws Exception {
//        return internalQuery(num, pageSize, listBefore(null));
//    }
//
//    @PostMapping(value = "/search")
//    public GenericResponse<List<T>> query(@RequestBody @Valid T request, BindingResult bindingResult) throws
//            Exception {
//
//        return query(null, null, request, bindingResult);
//    }
//
//    @PostMapping(value = "/search/{num}")
//    public GenericResponse<List<T>> query(@PathVariable(value = "num") Integer num,
//                                          @RequestBody @Valid T request, BindingResult bindingResult) throws Exception {
//        return query(num, null, request, bindingResult);
//    }
//
//    @PostMapping(value = "/search/{num}/{page_size}")
//    public GenericResponse<List<T>> query(@PathVariable(value = "num") Integer num,
//                                          @PathVariable(value = "page_size") Integer pageSize,
//                                          @RequestBody @Valid T request, BindingResult bindingResult) throws Exception {
//        return internalQuery(num, pageSize, listBefore(request));
//    }

//    @PostMapping("/exportExcel")
//	public GenericResponse<Void> exportExcel(@RequestBody T request,HttpServletResponse httpResponse) throws Exception {
//		return ControllerTemplate.call((GenericResponse<Void> response) -> {
//
//			try {
//			OutputStream outputStream = null;
//			WritableWorkbook workbook = null;
//				// 获得导出数据
//				List<T> list = query(0,Integer.MAX_VALUE,request,null).getData();
//				// 设置导出头信息
//				String fileName = excelTitle();
//				outputStream = httpResponse.getOutputStream();
//				httpResponse.setCharacterEncoding("UTF-8");
////				httpResponse.setHeader("Content-disposition",
////						"attachment;filename=" + new String(fileName.getBytes("GBK"), "iso-8859-1") + ".xls");
//				httpResponse.setContentType("application/vnd.ms-excel");
//
//				workbook = Workbook.createWorkbook(outputStream);
//
//				@SuppressWarnings("unchecked")
//				JxlExportExcel<T> jxlReport = new JxlExportExcel<T>(workbook, fileName, fileName, list, (Class<T>) request.getClass(),null);
//
//				jxlReport.setNeedNumber(false);
//				jxlReport.buildExcel();
//
//				workbook.write();
//				workbook.close(); // 写入到客户端浏览器
//				outputStream.flush();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		});
//	}

    int defaultPageSize() {
        return 10;
    }

    int defaultPageNumber() {
        return 0;
    }

    protected T listBefore(T dto) {
        return dto;
    }
    
    protected String excelTitle() {
		return null;
	}

    protected int setAndGetPageSize(Integer pageSize) {
        if (null == pageSize) {
            return defaultPageSize();
        } else {
            return pageSize;
        }
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

}