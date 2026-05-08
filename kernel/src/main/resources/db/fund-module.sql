CREATE TABLE IF NOT EXISTS `fxy_financial_fund_account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL COMMENT '账户编码',
  `name` varchar(64) NOT NULL COMMENT '账户名称',
  `account_type` varchar(16) NOT NULL COMMENT '账户类型：CASH现金，BANK银行账户',
  `bank_name` varchar(128) DEFAULT NULL COMMENT '开户行',
  `bank_account_no` varchar(64) DEFAULT NULL COMMENT '银行账号',
  `subject_id` int DEFAULT NULL COMMENT '关联会计科目',
  `opening_balance` decimal(18,2) DEFAULT '0.00' COMMENT '期初余额',
  `current_balance` decimal(18,2) DEFAULT '0.00' COMMENT '当前余额',
  `status` tinyint(1) DEFAULT '1' COMMENT '启用状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `account_sets_id` int NOT NULL COMMENT '账套ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_fund_account_sets` (`account_sets_id`),
  UNIQUE KEY `uk_fund_account_code` (`account_sets_id`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金账户';

CREATE TABLE IF NOT EXISTS `fxy_financial_fund_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL COMMENT '类别编码',
  `name` varchar(64) NOT NULL COMMENT '类别名称',
  `category_type` varchar(16) NOT NULL COMMENT '类别类型：IN收入，OUT支出',
  `subject_id` int DEFAULT NULL COMMENT '生成凭证时使用的对方科目',
  `status` tinyint(1) DEFAULT '1' COMMENT '启用状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `account_sets_id` int NOT NULL COMMENT '账套ID',
  PRIMARY KEY (`id`),
  KEY `idx_fund_category_sets` (`account_sets_id`),
  UNIQUE KEY `uk_fund_category_code` (`account_sets_id`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金类别';

CREATE TABLE IF NOT EXISTS `fxy_financial_fund_flow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flow_no` varchar(64) NOT NULL COMMENT '流水号',
  `flow_date` datetime NOT NULL COMMENT '流水日期',
  `account_id` int NOT NULL COMMENT '资金账户ID',
  `category_id` int DEFAULT NULL COMMENT '资金类别ID',
  `direction` varchar(8) NOT NULL COMMENT '方向：IN收入，OUT支出',
  `amount` decimal(18,2) NOT NULL COMMENT '金额',
  `balance` decimal(18,2) DEFAULT NULL COMMENT '发生后余额',
  `source_type` varchar(16) DEFAULT NULL COMMENT '来源：RECEIPT收款，PAYMENT付款，MANUAL手工',
  `source_id` int DEFAULT NULL COMMENT '来源单据ID',
  `voucher_id` int DEFAULT NULL COMMENT '关联凭证ID',
  `counterparty` varchar(128) DEFAULT NULL COMMENT '往来单位/个人',
  `summary` varchar(255) DEFAULT NULL COMMENT '摘要',
  `reconciled` tinyint(1) DEFAULT '0' COMMENT '是否已对账',
  `account_sets_id` int NOT NULL COMMENT '账套ID',
  PRIMARY KEY (`id`),
  KEY `idx_fund_flow_sets_date` (`account_sets_id`,`flow_date`),
  KEY `idx_fund_flow_account` (`account_id`),
  KEY `idx_fund_flow_voucher` (`voucher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金流水';

CREATE TABLE IF NOT EXISTS `fxy_financial_receipt_bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(64) NOT NULL COMMENT '单据编号',
  `bill_date` datetime NOT NULL COMMENT '单据日期',
  `account_id` int NOT NULL COMMENT '收款账户ID',
  `category_id` int DEFAULT NULL COMMENT '资金类别ID',
  `payer` varchar(128) DEFAULT NULL COMMENT '付款方',
  `amount` decimal(18,2) NOT NULL COMMENT '金额',
  `summary` varchar(255) DEFAULT NULL COMMENT '摘要',
  `status` varchar(16) DEFAULT 'DRAFT' COMMENT '状态：DRAFT草稿，AUDITED已审核',
  `voucher_id` int DEFAULT NULL COMMENT '关联凭证ID',
  `audit_member_id` int DEFAULT NULL COMMENT '审核人ID',
  `audit_member_name` varchar(64) DEFAULT NULL COMMENT '审核人姓名',
  `audit_date` datetime DEFAULT NULL COMMENT '审核时间',
  `account_sets_id` int NOT NULL COMMENT '账套ID',
  PRIMARY KEY (`id`),
  KEY `idx_receipt_sets_date` (`account_sets_id`,`bill_date`),
  UNIQUE KEY `uk_receipt_bill_no` (`account_sets_id`,`bill_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收款单';

CREATE TABLE IF NOT EXISTS `fxy_financial_payment_bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(64) NOT NULL COMMENT '单据编号',
  `bill_date` datetime NOT NULL COMMENT '单据日期',
  `account_id` int NOT NULL COMMENT '付款账户ID',
  `category_id` int DEFAULT NULL COMMENT '资金类别ID',
  `payee` varchar(128) DEFAULT NULL COMMENT '收款方',
  `amount` decimal(18,2) NOT NULL COMMENT '金额',
  `summary` varchar(255) DEFAULT NULL COMMENT '摘要',
  `status` varchar(16) DEFAULT 'DRAFT' COMMENT '状态：DRAFT草稿，AUDITED已审核',
  `voucher_id` int DEFAULT NULL COMMENT '关联凭证ID',
  `audit_member_id` int DEFAULT NULL COMMENT '审核人ID',
  `audit_member_name` varchar(64) DEFAULT NULL COMMENT '审核人姓名',
  `audit_date` datetime DEFAULT NULL COMMENT '审核时间',
  `account_sets_id` int NOT NULL COMMENT '账套ID',
  PRIMARY KEY (`id`),
  KEY `idx_payment_sets_date` (`account_sets_id`,`bill_date`),
  UNIQUE KEY `uk_payment_bill_no` (`account_sets_id`,`bill_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='付款单';

CREATE TABLE IF NOT EXISTS `fxy_financial_bank_statement` (
  `id` int NOT NULL AUTO_INCREMENT,
  `statement_no` varchar(64) NOT NULL COMMENT '对账单编号',
  `statement_date` datetime NOT NULL COMMENT '交易日期',
  `account_id` int NOT NULL COMMENT '银行账户ID',
  `direction` varchar(8) NOT NULL COMMENT '方向：IN收入，OUT支出',
  `amount` decimal(18,2) NOT NULL COMMENT '金额',
  `balance` decimal(18,2) DEFAULT NULL COMMENT '银行余额',
  `counterparty` varchar(128) DEFAULT NULL COMMENT '对方户名',
  `summary` varchar(255) DEFAULT NULL COMMENT '摘要',
  `fund_flow_id` int DEFAULT NULL COMMENT '匹配资金流水ID',
  `reconcile_status` varchar(16) DEFAULT 'UNRECONCILED' COMMENT '对账状态',
  `account_sets_id` int NOT NULL COMMENT '账套ID',
  PRIMARY KEY (`id`),
  KEY `idx_bank_statement_sets_date` (`account_sets_id`,`statement_date`),
  KEY `idx_bank_statement_flow` (`fund_flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='银行对账单';
