<template>
	<fund-crud title="银行对账" api-name="bankStatement" :fields="fields" :form-fields="formFields" reconcile></fund-crud>
</template>
<script>
	import FundCrud from './FundCrud';
	export default {
		name: 'FundBankReconciliation',
		components: {FundCrud},
		data() {
			return {
				fields: [{title: '对账单号', prop: 'statementNo'}, {title: '日期', prop: 'statementDate'}, {title: '账户ID', prop: 'accountId'}, {title: '方向', prop: 'direction'}, {title: '金额', prop: 'amount'}, {title: '银行余额', prop: 'balance'}, {title: '流水ID', prop: 'fundFlowId'}, {title: '状态', prop: 'reconcileStatus'}],
				formFields: [{title: '对账单号', prop: 'statementNo', required: true}, {title: '日期', prop: 'statementDate', type: 'date', required: true}, {title: '账户ID', prop: 'accountId', type: 'number', required: true}, {title: '方向', prop: 'direction', options: [{title: '收入', key: 'IN'}, {title: '支出', key: 'OUT'}], required: true}, {title: '金额', prop: 'amount', type: 'number', required: true}, {title: '银行余额', prop: 'balance', type: 'number'}, {title: '对方户名', prop: 'counterparty'}, {title: '摘要', prop: 'summary'}]
			}
		}
	}
</script>
