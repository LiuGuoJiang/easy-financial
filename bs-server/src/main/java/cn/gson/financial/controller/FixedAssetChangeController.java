package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.model.entity.FixedAssetChangeRecord;
import cn.gson.financial.kernel.service.FixedAssetChangeRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixed-assets/change")
public class FixedAssetChangeController extends BaseCrudController<FixedAssetChangeRecordService, FixedAssetChangeRecord> {
}
