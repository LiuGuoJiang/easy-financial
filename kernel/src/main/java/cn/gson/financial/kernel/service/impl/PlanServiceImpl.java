package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.Plan;
import cn.gson.financial.kernel.model.mapper.PlanMapper;
import cn.gson.financial.kernel.service.PlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {
}
