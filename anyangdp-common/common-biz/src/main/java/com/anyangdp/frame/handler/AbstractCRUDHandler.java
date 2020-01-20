package com.anyangdp.frame.handler;

import com.anyangdp.frame.service.CRUDService;
import com.anyangdp.frame.util.ReflectionUtils;
import com.anyangdp.frame.util.SpringContextUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author anyang
 * @CreateTime 2019/11/25
 * @Des
 */
public class AbstractCRUDHandler<ID, DTO extends AbstractDTO, S extends CRUDService> implements CRUDServiceAware<S> {

    private Class<S> defaultServiceClass;

    public AbstractCRUDHandler() {
        defaultServiceClass = ReflectionUtils.getClassGenricType(getClass(), 2);
    }

    @Override
    public S getService() {
        return SpringContextUtil.getBean(defaultServiceClass);
    }

//    @PostMapping("/create")
    public GenericResponse<DTO> create(@RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<DTO> response) -> {
            DTO insert = (DTO) getService().insert(request);
            response.setData(insert).setResult(true);
        });
    }

//    @PutMapping("/update")
    public GenericResponse<Void> update(@RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<Void> response) -> {
            response.setResult(getService().update(request));
        });
    }

//    @GetMapping("/retrieve/{id}")
    public GenericResponse<DTO> retrieve(@PathVariable Long id) throws Exception {

        return ControllerTemplate.call((GenericResponse<DTO> response) -> {
            response.setData((DTO) getService().retrieve(id)).setResult(true);
        });
    }

//    @PostMapping(value = "/find")
    public GenericResponse<DTO> retrieve(@RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<DTO> response) -> {

            response.setData((DTO) getService().retrieveByCondition(request));
            response.setResult(true);
        });
    }

//    @DeleteMapping(value = "/{id}")
    public GenericResponse<DTO> delete(@PathVariable Long id) throws Exception {

        return ControllerTemplate.call((GenericResponse<DTO> response) -> {
            response.setResult(getService().delete(id));
        });
    }

//    @DeleteMapping(value = "/logic/{id}")
    public GenericResponse<DTO> logicDelete(@PathVariable Long id) throws Exception {

        return ControllerTemplate.call((GenericResponse<DTO> response) -> {
            response.setResult(getService().logicDelete(id));
        });
    }

//    @GetMapping(value = "/active/{id}")
    public GenericResponse<DTO> active(@PathVariable Long id) throws Exception {

        return ControllerTemplate.call((GenericResponse<DTO> response) -> {
            response.setResult(getService().active(id));
        });
    }

//    @GetMapping(value = "/deActive/{id}")
    public GenericResponse<DTO> deActive(@PathVariable Long id) throws Exception {

        return ControllerTemplate.call((GenericResponse<DTO> response) -> {
            response.setResult(getService().deActive(id));
        });
    }

//    @PostMapping(value = "/list")
    public GenericResponse<List<DTO>> List(@RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<List<DTO>> response) -> {
            response.setData((List<DTO>) getService().list(request));
            response.setResult(true);
        });
    }

//    @PostMapping(value = "/page/{page}/{pageSize}")
    public GenericResponse<Page<DTO>> Page(@PathVariable Long page, @PathVariable Long pageSize, @RequestBody @Valid DTO request, BindingResult bindingResult) throws Exception {

        return ControllerTemplate.call(bindingResult, (GenericResponse<Page<DTO>> response) -> {
            Page page1 = new Page();
            page1.setCurrent(page);
            page1.setSize(pageSize);
            response.setData((Page<DTO>) getService().page(page1, request));
            response.setResult(true);
        });
    }


}
