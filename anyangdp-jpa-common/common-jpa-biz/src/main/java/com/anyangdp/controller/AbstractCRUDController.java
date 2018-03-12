package com.anyangdp.controller;

import com.anyangdp.domain.dto.AbstractDTO;
import com.anyangdp.handler.GenericResponse;
import com.anyangdp.service.CRUDService;
import com.anyangdp.service.CRUDServiceAware;
import com.anyangdp.utils.ReflectionUtils;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 抽象controller类，完成基础crud
 * @param <ID>
 * @param <DTO>
 * @param <S>
 */
public abstract class AbstractCRUDController<ID, DTO extends AbstractDTO, S extends CRUDService<ID, DTO>>
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
        return null;
    }

    @GetMapping(value = "/list")
    public GenericResponse<List<DTO>> list() throws Exception {
        return null;
    }

    @GetMapping(value = "/list/{num}")
    public GenericResponse<List<DTO>> list(@PathVariable(value = "num") Integer num) throws Exception {
        return null;
    }

    @GetMapping(value = "/list/{num}/{page_size}")
    public GenericResponse<List<DTO>> list(@PathVariable(value = "num") Integer num,
                                           @PathVariable(value = "page_size") Integer pageSize) throws Exception {
        return null;
    }
}
