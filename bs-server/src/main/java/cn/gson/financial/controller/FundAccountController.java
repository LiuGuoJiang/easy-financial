package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.model.entity.FundAccount;
import cn.gson.financial.kernel.service.FundAccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fund/account")
public class FundAccountController extends BaseCrudController<FundAccountService, FundAccount> {
}
