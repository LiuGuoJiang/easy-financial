package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.model.entity.FixedAssetDisposalRecord;
import cn.gson.financial.kernel.service.FixedAssetDisposalRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixed-assets/disposal")
public class FixedAssetDisposalController extends BaseCrudController<FixedAssetDisposalRecordService, FixedAssetDisposalRecord> {
}
