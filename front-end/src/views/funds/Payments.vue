<template>
	<fund-crud title="付款单" api-name="payment" :fields="fields" :form-fields="formFields" auditable></fund-crud>
</template>
<script>
	import FundCrud from './FundCrud';
	export default {
		name: 'FundPayments',
		components: {FundCrud},
		data() {
			return {
				fields: [{title: '单号', prop: 'billNo'}, {title: '日期', prop: 'billDate'}, {title: '账户ID', prop: 'accountId'}, {title: '类别ID', prop: 'categoryId'}, {title: '收款方', prop: 'payee'}, {title: '金额', prop: 'amount'}, {title: '状态', prop: 'status'}, {title: '凭证ID', prop: 'voucherId'}],
				formFields: [{title: '单号', prop: 'billNo', required: true}, {title: '日期', prop: 'billDate', type: 'date', required: true}, {title: '账户ID', prop: 'accountId', type: 'number', required: true}, {title: '类别ID', prop: 'categoryId', type: 'number'}, {title: '收款方', prop: 'payee'}, {title: '金额', prop: 'amount', type: 'number', required: true}, {title: '摘要', prop: 'summary'}]
			}
		}
	}
</script>
