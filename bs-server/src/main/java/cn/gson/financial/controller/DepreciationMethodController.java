package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.model.entity.DepreciationMethod;
import cn.gson.financial.kernel.service.DepreciationMethodService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixed-assets/depreciation-method")
public class DepreciationMethodController extends BaseCrudController<DepreciationMethodService, DepreciationMethod> {
}
