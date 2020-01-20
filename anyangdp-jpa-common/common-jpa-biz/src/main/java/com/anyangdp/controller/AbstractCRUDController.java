package com.anyangdp.controller;

import com.anyangdp.dao.RangeQuery;
import com.anyangdp.dao.SearchQuery;
import com.anyangdp.domain.dto.AbstractDTO;
import com.anyangdp.handler.GenericHandler;
import com.anyangdp.handler.GenericResponse;
import com.anyangdp.handler.PageDTO;
import com.anyangdp.service.CRUDServiceAware;
import com.anyangdp.service.PageableService;
import com.anyangdp.utils.ReflectionUtils;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 抽象controller类，完成基础crud
 * @param <ID>
 * @param <DTO>
 * @param <S>
 */
public abstract class AbstractCRUDController<ID, DTO extends AbstractDTO, S extends PageableService<ID, DTO>> extends GenericHandler
        implements CRUDServiceAware<S>, BeanFactoryAware {

    private Class<S> defaultServiceClass;

    @Setter
    protected BeanFactory beanFactory;

    public AbstractCRUDController() {
        defaultServiceClass = ReflectionUtils.getClassGenricType(getClass(), 2);
    }

    @Override
    public S getService() {
        return beanFactory.getBean("default" + defaultServiceClass.getSimpleName(), defaultServiceClass);
    }

    /**
     * 根据主键id查询
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/{id}")
    public GenericResponse<DTO> retrieve(@PathVariable("id") ID id) throws Exception {
        return ControllerTemplate.call((GenericResponse<DTO> response) -> {
            response.setData(getService().retrieve(id));
            response.setResult(true);
        });
    }

    /**
     * 添加数据
     * @param request
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @PostMapping
    public GenericResponse<DTO> insert(@RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {
        return ControllerTemplate.call(bindingResult, (GenericResponse<DTO> response) -> {
            response.setData(getService().insert(request));
            response.setResult(true);
        });
    }

    /**
     * 查询数据(实体对象查询)
     * @param request
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/find")
    public GenericResponse<DTO> retrieve(@RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {
        return ControllerTemplate.call(bindingResult, response -> {
            setDefaultStatus(request);
            response.setData(getService().retrieveByCondition(request));
            response.setResult(true);
        });
    }

    /**
     * 修改数据
     * @param request
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @PutMapping
    public GenericResponse<Void> update(@RequestBody @Valid DTO request, BindingResult bindingResult) throws
            Exception {
        return ControllerTemplate.call(bindingResult, (GenericResponse<Void> response) -> {
            response.setResult(getService().update(request));
        });
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/{id}")
    public GenericResponse<Void> delete(@PathVariable("id") ID id) throws Exception {
        return ControllerTemplate.call(response ->
            response.setResult(getService().delete(id))
        );
    }

    @GetMapping(value = "/listAll")
    public GenericResponse<List<DTO>> listAll() throws Exception {
        return ControllerTemplate.call(response -> {
            response.setResult(true);
            response.setData(getService().listAllActive());
        });
    }

    @GetMapping(value = "/list")
    public GenericResponse<List<DTO>> list(@RequestBody @Valid DTO request) throws Exception {
        return ControllerTemplate.call(response -> {
            setDefaultStatus(request);
            response.setData(getService().list(request));
            response.setResult(true);
        });
    }

    @GetMapping(value = "/list/{num}")
    public GenericResponse<List<DTO>> list(@PathVariable(value = "num") Integer num) throws Exception {
        return internalQuery(num,null,null);
    }

    @GetMapping(value = "/list/{num}/{page_size}")
    public GenericResponse<List<DTO>> list(@PathVariable(value = "num") Integer num,
                                           @PathVariable(value = "page_size") Integer pageSize) throws Exception {
        return internalQuery(num, pageSize, listBefore(null));
    }

    @GetMapping(value = "/page")
    public GenericResponse<List<DTO>> page(DTO request,Integer page,
                                            Integer rows) throws Exception {
        setDefaultStatus(request);
        return internalQuery(page, rows, listBefore(null));
    }

    @PostMapping(value = "/search")
    public GenericResponse<List<DTO>> query(@RequestBody @Valid DTO request, BindingResult bindingResult) throws
            Exception {

        return query(null, null, request, bindingResult);
    }


    @PostMapping(value = "/search/{num}")
    public GenericResponse<List<DTO>> query(@PathVariable(value = "num") Integer num,
                                          @RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {
        setDefaultStatus(request);
        return query(num, null, request, bindingResult);
    }


    @PostMapping(value = "/search/{num}/{page_size}")
    public GenericResponse<List<DTO>> query(@PathVariable(value = "num") Integer num,
                                          @PathVariable(value = "page_size") Integer pageSize,
                                          @RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {
        setDefaultStatus(request);
        return internalQuery(num, pageSize, listBefore(request));
    }


    private GenericResponse<List<DTO>> internalQuery(Integer pageNumber, Integer pageSize, DTO request) throws Exception {

        return ControllerTemplate.call((GenericResponse<List<DTO>> response) -> {

            PageRequest pr = new PageRequest(setAndGetPageNumber(pageNumber), setAndGetPageSize(pageSize));
            PageableService service = (PageableService) getService();

//            List<RangeQuery> rangeQueries = new ArrayList<>();
//            RangeQuery rangeQuery = new RangeQuery("creationDate", null, new Date(), false);
//            rangeQueries.add(rangeQuery);
//            request.setRangeQuerys(rangeQueries);
//            RangeQuery aa = (RangeQuery) request.getRangeQuerys().get(0);
//            aa.setUpperBound(new Date());

            Page<DTO> data;
            if (request != null) {
                if (request.getLikeQuery() != null) {
                    data = service.list(request, request.getRangeQuerys(), request.getLikeQuery(), pr);
                } else {
                    data = service.list(request, request.getRangeQuerys(), request.getSearchQuery(), pr);
                }
            } else {
                data = service.listActive(pr);
            }

            extractResponse(response, data);
        });
    }

    protected DTO listBefore(DTO dto) {
        return dto;
    }

    private void setDefaultStatus(DTO request) {
        if (null != request) {
            if (StringUtils.isEmpty(request.getDeleted())) {
                request.setDeleted("0");
            }
            if (StringUtils.isEmpty(request.getEnabled())) {
                request.setEnabled("1");
            }
        }
    }


}
