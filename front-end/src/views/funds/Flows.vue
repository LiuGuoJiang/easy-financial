<template>
	<fund-crud title="资金流水" api-name="flow" :fields="fields" :form-fields="formFields"></fund-crud>
</template>
<script>
	import FundCrud from './FundCrud';
	export default {
		name: 'FundFlows',
		components: {FundCrud},
		data() {
			return {
				fields: [{title: '流水号', prop: 'flowNo'}, {title: '日期', prop: 'flowDate'}, {title: '账户ID', prop: 'accountId'}, {title: '方向', prop: 'direction'}, {title: '金额', prop: 'amount'}, {title: '余额', prop: 'balance'}, {title: '来源', prop: 'sourceType'}, {title: '凭证ID', prop: 'voucherId'}, {title: '已对账', prop: 'reconciled', type: 'boolean'}],
				formFields: [{title: '流水号', prop: 'flowNo', required: true}, {title: '日期', prop: 'flowDate', type: 'date', required: true}, {title: '账户ID', prop: 'accountId', type: 'number', required: true}, {title: '类别ID', prop: 'categoryId', type: 'number'}, {title: '方向', prop: 'direction', options: [{title: '收入', key: 'IN'}, {title: '支出', key: 'OUT'}], required: true}, {title: '金额', prop: 'amount', type: 'number', required: true}, {title: '摘要', prop: 'summary'}]
			}
		}
	}
</script>
