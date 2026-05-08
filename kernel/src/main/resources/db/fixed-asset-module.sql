CREATE TABLE IF NOT EXISTS `fxy_financial_fixed_asset_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `name` varchar(128) NOT NULL,
  `depreciation_method` varchar(32) DEFAULT 'STRAIGHT_LINE',
  `useful_months` int DEFAULT NULL,
  `net_salvage_rate` double DEFAULT 0,
  `fixed_asset_subject_id` int DEFAULT NULL,
  `accumulated_depreciation_subject_id` int DEFAULT NULL,
  `depreciation_expense_subject_id` int DEFAULT NULL,
  `status` bit(1) DEFAULT b'1',
  `remark` varchar(255) DEFAULT NULL,
  `account_sets_id` int NOT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fa_category_account_sets` (`account_sets_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `fxy_financial_depreciation_method` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `name` varchar(128) NOT NULL,
  `formula_type` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `system_default` bit(1) DEFAULT b'0',
  `account_sets_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `fxy_financial_fixed_asset_card` (
  `id` int NOT NULL AUTO_INCREMENT,
  `asset_no` varchar(64) NOT NULL,
  `asset_name` varchar(128) NOT NULL,
  `category_id` int DEFAULT NULL,
  `specification` varchar(128) DEFAULT NULL,
  `department` varchar(128) DEFAULT NULL,
  `custodian` varchar(128) DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `start_use_date` date DEFAULT NULL,
  `original_value` double DEFAULT 0,
  `net_salvage_rate` double DEFAULT 0,
  `expected_salvage` double DEFAULT 0,
  `useful_months` int DEFAULT NULL,
  `depreciation_method` varchar(32) DEFAULT 'STRAIGHT_LINE',
  `depreciated_months` int DEFAULT 0,
  `accumulated_depreciation` double DEFAULT 0,
  `net_value` double DEFAULT 0,
  `fixed_asset_subject_id` int DEFAULT NULL,
  `accumulated_depreciation_subject_id` int DEFAULT NULL,
  `depreciation_expense_subject_id` int DEFAULT NULL,
  `status` varchar(32) DEFAULT 'DRAFT',
  `voucher_id` int DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `account_sets_id` int NOT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fa_card_account_sets` (`account_sets_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `fxy_financial_fixed_asset_change` (
  `id` int NOT NULL AUTO_INCREMENT,
  `asset_id` int NOT NULL,
  `change_date` date DEFAULT NULL,
  `change_type` varchar(64) DEFAULT NULL,
  `before_value` double DEFAULT NULL,
  `after_value` double DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `voucher_id` int DEFAULT NULL,
  `account_sets_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fa_change_account_sets` (`account_sets_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `fxy_financial_fixed_asset_disposal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `asset_id` int NOT NULL,
  `disposal_date` date DEFAULT NULL,
  `disposal_type` varchar(64) DEFAULT NULL,
  `disposal_income` double DEFAULT 0,
  `disposal_expense` double DEFAULT 0,
  `net_value` double DEFAULT 0,
  `gain_loss` double DEFAULT 0,
  `summary` varchar(255) DEFAULT NULL,
  `voucher_id` int DEFAULT NULL,
  `account_sets_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fa_disposal_account_sets` (`account_sets_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `fxy_financial_fixed_asset_depreciation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `asset_id` int NOT NULL,
  `depreciation_year` int NOT NULL,
  `depreciation_month` int NOT NULL,
  `depreciation_date` date DEFAULT NULL,
  `amount` double DEFAULT 0,
  `before_accumulated_depreciation` double DEFAULT 0,
  `after_accumulated_depreciation` double DEFAULT 0,
  `voucher_id` int DEFAULT NULL,
  `account_sets_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fa_depr_asset_period` (`asset_id`,`depreciation_year`,`depreciation_month`),
  KEY `idx_fa_depr_account_sets_period` (`account_sets_id`,`depreciation_year`,`depreciation_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
