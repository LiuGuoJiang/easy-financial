package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.model.entity.FixedAssetCategory;
import cn.gson.financial.kernel.service.FixedAssetCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixed-assets/category")
public class FixedAssetCategoryController extends BaseCrudController<FixedAssetCategoryService, FixedAssetCategory> {
}
