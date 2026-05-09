-- 商业化运营与多租户模块
-- 1) 平台商户、租户、套餐、订阅、订单、授权额度
-- 2) 在既有账套/用户/用户账套关系和业务表上补充 tenant_id，用于租户 + 账套双重隔离

CREATE TABLE IF NOT EXISTS `fxy_financial_merchant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL COMMENT '商户名称',
  `code` varchar(64) NOT NULL COMMENT '商户编码',
  `contact_name` varchar(60) DEFAULT NULL COMMENT '联系人',
  `contact_mobile` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `status` varchar(32) DEFAULT 'Enabled' COMMENT '状态',
  `tenant_id` int DEFAULT NULL COMMENT '默认租户 ID',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fxy_merchant_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户';

CREATE TABLE IF NOT EXISTS `fxy_financial_tenant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int NOT NULL COMMENT '商户 ID',
  `name` varchar(120) NOT NULL COMMENT '租户名称',
  `code` varchar(64) NOT NULL COMMENT '租户编码',
  `admin_user_id` int DEFAULT NULL COMMENT '租户管理员',
  `status` varchar(32) DEFAULT 'Enabled',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `expire_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fxy_tenant_code` (`code`),
  KEY `idx_fxy_tenant_merchant` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户';

CREATE TABLE IF NOT EXISTS `fxy_financial_plan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL COMMENT '套餐名称',
  `code` varchar(64) NOT NULL COMMENT '套餐编码',
  `price` decimal(12,2) DEFAULT 0.00,
  `billing_cycle` varchar(32) DEFAULT 'Monthly',
  `account_set_limit` int DEFAULT 1 COMMENT '账套数量限制',
  `user_limit` int DEFAULT 5 COMMENT '用户数量限制',
  `voucher_limit` int DEFAULT 0 COMMENT '授权额度',
  `status` varchar(32) DEFAULT 'Enabled',
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fxy_plan_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐';

CREATE TABLE IF NOT EXISTS `fxy_financial_subscription` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int NOT NULL,
  `tenant_id` int NOT NULL,
  `plan_id` int NOT NULL,
  `status` varchar(32) DEFAULT 'Active',
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `account_set_limit` int DEFAULT 1,
  `user_limit` int DEFAULT 5,
  `voucher_limit` int DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_fxy_subscription_tenant` (`tenant_id`),
  KEY `idx_fxy_subscription_merchant` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅';

CREATE TABLE IF NOT EXISTS `fxy_financial_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int NOT NULL,
  `tenant_id` int NOT NULL,
  `subscription_id` int DEFAULT NULL,
  `order_no` varchar(64) NOT NULL,
  `amount` decimal(12,2) DEFAULT 0.00,
  `status` varchar(32) DEFAULT 'Created',
  `pay_time` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fxy_order_no` (`order_no`),
  KEY `idx_fxy_order_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

CREATE TABLE IF NOT EXISTS `fxy_financial_authorization_quota` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int NOT NULL,
  `tenant_id` int NOT NULL,
  `subscription_id` int DEFAULT NULL,
  `quota_type` varchar(32) NOT NULL COMMENT 'AccountSet/User/Voucher 等',
  `quota_limit` int DEFAULT 0,
  `used_quota` int DEFAULT 0,
  `expire_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fxy_quota_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授权额度';

CREATE TABLE IF NOT EXISTS `fxy_financial_operation_audit_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tenant_id` int DEFAULT NULL,
  `operator_id` int DEFAULT NULL,
  `operator_name` varchar(120) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL COMMENT 'MERCHANT/TENANT/SUBSCRIPTION/ACCOUNT_SET',
  `target_id` int DEFAULT NULL,
  `action` varchar(32) NOT NULL,
  `before_value` text,
  `after_value` text,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `ip` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fxy_audit_target` (`target_type`, `target_id`),
  KEY `idx_fxy_audit_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运营审计日志';

ALTER TABLE `fxy_financial_user` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_user` ADD COLUMN IF NOT EXISTS `platform_role` varchar(32) DEFAULT NULL COMMENT '平台角色';
ALTER TABLE `fxy_financial_account_sets` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_user_account_sets` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';

-- 所有以账套隔离的业务表同步增加 tenant_id，运行后请执行一次历史数据回填。
ALTER TABLE `fxy_financial_voucher` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_voucher_details` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_voucher_details_auxiliary` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_subject` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_accounting_category` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_accounting_category_details` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_voucher_word` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_report_template` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_report_template_items` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_report_template_items_formula` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_checkout` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_currency` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_voucher_template` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_voucher_template_details` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_fixed_asset_card` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_fixed_asset_category` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_fixed_asset_change_record` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_fixed_asset_disposal_record` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_depreciation_method` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_depreciation_voucher_record` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_fund_account` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_fund_category` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_fund_flow` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_receipt_bill` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_payment_bill` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_bank_statement` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_employee` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_payroll_department` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_payroll_item` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_payroll_config` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_payroll_period` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_payroll_sheet` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
ALTER TABLE `fxy_financial_payroll_detail` ADD COLUMN IF NOT EXISTS `tenant_id` int DEFAULT NULL COMMENT '租户 ID';
