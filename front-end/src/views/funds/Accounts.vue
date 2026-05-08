<template>
	<fund-crud title="资金账户" api-name="account" :fields="fields" :form-fields="formFields"></fund-crud>
</template>
<script>
	import FundCrud from './FundCrud';
	export default {
		name: 'FundAccounts',
		components: {FundCrud},
		data() {
			return {
				fields: [
					{title: '编码', prop: 'code'}, {title: '名称', prop: 'name'},
					{title: '类型', prop: 'accountType'}, {title: '开户行', prop: 'bankName'},
					{title: '账号', prop: 'bankAccountNo'}, {title: '当前余额', prop: 'currentBalance'},
					{title: '启用', prop: 'status', type: 'boolean'}
				],
				formFields: [
					{title: '编码', prop: 'code', required: true}, {title: '名称', prop: 'name', required: true},
					{title: '类型', prop: 'accountType', required: true, options: [{title: '现金', key: 'CASH'}, {title: '银行账户', key: 'BANK'}]},
					{title: '开户行', prop: 'bankName'}, {title: '账号', prop: 'bankAccountNo'},
					{title: '会计科目ID', prop: 'subjectId', type: 'number'}, {title: '期初余额', prop: 'openingBalance', type: 'number', default: 0},
					{title: '启用', prop: 'status', type: 'boolean', default: true}, {title: '备注', prop: 'remark'}
				]
			}
		}
	}
</script>
