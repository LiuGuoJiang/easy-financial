package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.model.entity.FundCategory;
import cn.gson.financial.kernel.service.FundCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fund/category")
public class FundCategoryController extends BaseCrudController<FundCategoryService, FundCategory> {
}
