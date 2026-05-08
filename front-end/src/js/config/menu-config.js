const Manager = [
	{
		title: '主页',
		key: 'Home',
		icon: 'h-icon-home'
	},
	{
		title: '凭证',
		key: 'vouchers',
		icon: 'h-icon-star',
		children: [
			{
				title: '新增凭证',
				key: 'VoucherForm'
			},
			{
				title: '凭证列表',
				key: 'Voucher'
			}
		]
	},
	{
		title: '帐薄',
		key: 'AccountBooks',
		icon: 'h-icon-task',
		children: [
			{
				title: '明细账',
				key: 'DetailedAccounts'
			},
			{
				title: '总账',
				key: 'GeneralLedger'
			},
			{
				title: '科目余额',
				key: 'SubjectBalance'
			},
			{
				title: '科目汇总',
				key: 'SubjectSummary'
			},
			{
				title: '核算项目明细账',
				key: 'AuxiliaryAccountingDetail'
			},
			{
				title: '核算项目余额',
				key: 'AuxiliaryAccountingBalance'
			}
		]
	},

	{
		title: '资金',
		key: 'Funds',
		icon: 'h-icon-safe',
		children: [
			{ title: '账户管理', key: 'FundAccounts' },
			{ title: '资金类别', key: 'FundCategories' },
			{ title: '收款', key: 'FundReceipts' },
			{ title: '付款', key: 'FundPayments' },
			{ title: '资金流水', key: 'FundFlows' },
			{ title: '银行对账', key: 'FundBankReconciliation' },
			{ title: '资金报表', key: 'FundReports' }
		]
	},


	{
		title: '工资',
		key: 'Payroll',
		icon: 'h-icon-user',
		children: [
			{ title: '员工档案', key: 'PayrollEmployees' },
			{ title: '薪资项目', key: 'PayrollItems' },
			{ title: '工资录入', key: 'PayrollEntry' },
			{ title: '工资计算', key: 'PayrollCalculate' },
			{ title: '工资发放', key: 'PayrollPayment' },
			{ title: '生成凭证', key: 'PayrollVoucher' }
		]
	},
	{
		title: '固定资产',
		key: 'FixedAssets',
		icon: 'h-icon-inbox',
		children: [
			{ title: '资产卡片', key: 'FixedAssetCards' },
			{ title: '资产类别', key: 'FixedAssetCategories' },
			{ title: '折旧计提', key: 'FixedAssetDepreciation' },
			{ title: '资产变动', key: 'FixedAssetChanges' },
			{ title: '资产处置', key: 'FixedAssetDisposals' },
			{ title: '资产报表', key: 'FixedAssetReports' }
		]
	},
	{
		title: '报表',
		key: 'reports',
		icon: 'h-icon-search',
		children: [
			{
				title: '报表查看',
				key: 'ReportList'
			},
			{
				title: '报表模板/自定义报表',
				key: 'ReportTemplate'
			}
		]
	},
	{
		title: '结账',
		key: 'CheckList',
		icon: 'h-icon-complete',
	},
	{
		title: '设置',
		key: 'Setting',
		icon: 'h-icon-setting',
		children: [
			{
				title: '账套',
				key: 'Account'
			},
			{
				title: '科目',
				key: 'Subject'
			},
			{
				title: '期初',
				key: 'Initial'
			},
			{
				title: '币别',
				key: 'Currency'
			},
			{
				title: '凭证字',
				key: 'VoucherWord'
			}, {
				title: '辅助核算',
				key: 'AccountingCategory'
			}/*, {
				title: '凭证模板',
				key: 'TemplateManager'
			}*/, {
				title: '权限设置',
				key: 'PermissionSetting'
			}

		]
	}
];

const Making = [
	{
		title: '主页',
		key: 'Home',
		icon: 'icon-monitor'
	},
	{
		title: '凭证',
		key: 'vouchers',
		icon: 'icon-grid-2',
		children: [
			{
				title: '新增凭证',
				key: 'VoucherForm'
			},
			{
				title: '凭证列表',
				key: 'Voucher'
			}
		]
	},
	{
		title: '结账',
		key: 'CheckList',
		icon: 'icon-disc',
	},
	{
		title: '帐薄',
		key: 'AccountBooks',
		icon: 'icon-paper',
		children: [
			{
				title: '明细账',
				key: 'DetailedAccounts'
			},
			{
				title: '总账',
				key: 'GeneralLedger'
			},
			{
				title: '科目余额',
				key: 'SubjectBalance'
			},
			{
				title: '科目汇总',
				key: 'SubjectSummary'
			},
			{
				title: '核算项目明细账',
				key: 'AuxiliaryAccountingDetail'
			},
			{
				title: '核算项目余额',
				key: 'AuxiliaryAccountingBalance'
			}
		]
	},

	{
		title: '资金',
		key: 'Funds',
		icon: 'h-icon-safe',
		children: [
			{ title: '账户管理', key: 'FundAccounts' },
			{ title: '资金类别', key: 'FundCategories' },
			{ title: '收款', key: 'FundReceipts' },
			{ title: '付款', key: 'FundPayments' },
			{ title: '资金流水', key: 'FundFlows' },
			{ title: '银行对账', key: 'FundBankReconciliation' },
			{ title: '资金报表', key: 'FundReports' }
		]
	},


	{
		title: '工资',
		key: 'Payroll',
		icon: 'h-icon-user',
		children: [
			{ title: '员工档案', key: 'PayrollEmployees' },
			{ title: '薪资项目', key: 'PayrollItems' },
			{ title: '工资录入', key: 'PayrollEntry' },
			{ title: '工资计算', key: 'PayrollCalculate' },
			{ title: '工资发放', key: 'PayrollPayment' },
			{ title: '生成凭证', key: 'PayrollVoucher' }
		]
	},
	{
		title: '固定资产',
		key: 'FixedAssets',
		icon: 'h-icon-inbox',
		children: [
			{ title: '资产卡片', key: 'FixedAssetCards' },
			{ title: '资产类别', key: 'FixedAssetCategories' },
			{ title: '折旧计提', key: 'FixedAssetDepreciation' },
			{ title: '资产变动', key: 'FixedAssetChanges' },
			{ title: '资产处置', key: 'FixedAssetDisposals' },
			{ title: '资产报表', key: 'FixedAssetReports' }
		]
	},
	{
		title: '报表',
		key: 'ReportList',
		icon: 'icon-bar-graph-2'
	},
	{
		title: '设置',
		key: 'Setting',
		icon: 'icon-cog',
		children: [
			{
				title: '账套',
				key: 'Account'
			}
		]
	}
];

const View = [
	{
		title: '主页',
		key: 'Home',
		icon: 'icon-monitor'
	},
	{
		title: '凭证',
		key: 'vouchers',
		icon: 'icon-grid-2',
		children: [
			{
				title: '凭证列表',
				key: 'Voucher'
			}
		]
	},
	{
		title: '帐薄',
		key: 'AccountBooks',
		icon: 'icon-paper',
		children: [
			{
				title: '明细账',
				key: 'DetailedAccounts'
			},
			{
				title: '总账',
				key: 'GeneralLedger'
			},
			{
				title: '科目余额',
				key: 'SubjectBalance'
			},
			{
				title: '科目汇总',
				key: 'SubjectSummary'
			},
			{
				title: '核算项目明细账',
				key: 'AuxiliaryAccountingDetail'
			},
			{
				title: '核算项目余额',
				key: 'AuxiliaryAccountingBalance'
			}
		]
	},

	{
		title: '资金',
		key: 'Funds',
		icon: 'h-icon-safe',
		children: [
			{ title: '账户管理', key: 'FundAccounts' },
			{ title: '资金类别', key: 'FundCategories' },
			{ title: '收款', key: 'FundReceipts' },
			{ title: '付款', key: 'FundPayments' },
			{ title: '资金流水', key: 'FundFlows' },
			{ title: '银行对账', key: 'FundBankReconciliation' },
			{ title: '资金报表', key: 'FundReports' }
		]
	},


	{
		title: '工资',
		key: 'Payroll',
		icon: 'h-icon-user',
		children: [
			{ title: '员工档案', key: 'PayrollEmployees' },
			{ title: '薪资项目', key: 'PayrollItems' },
			{ title: '工资录入', key: 'PayrollEntry' },
			{ title: '工资计算', key: 'PayrollCalculate' },
			{ title: '工资发放', key: 'PayrollPayment' },
			{ title: '生成凭证', key: 'PayrollVoucher' }
		]
	},
	{
		title: '固定资产',
		key: 'FixedAssets',
		icon: 'h-icon-inbox',
		children: [
			{ title: '资产卡片', key: 'FixedAssetCards' },
			{ title: '资产类别', key: 'FixedAssetCategories' },
			{ title: '折旧计提', key: 'FixedAssetDepreciation' },
			{ title: '资产变动', key: 'FixedAssetChanges' },
			{ title: '资产处置', key: 'FixedAssetDisposals' },
			{ title: '资产报表', key: 'FixedAssetReports' }
		]
	},
	{
		title: '报表',
		key: 'ReportList',
		icon: 'icon-bar-graph-2'
	},
	{
		title: '设置',
		key: 'Setting',
		icon: 'icon-cog',
		children: [
			{
				title: '账套',
				key: 'Account'
			}
		]
	}
];

export default {
	Manager,
	Making,
	View
};
