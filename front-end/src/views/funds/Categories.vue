<template>
	<fund-crud title="资金类别" api-name="category" :fields="fields" :form-fields="formFields"></fund-crud>
</template>
<script>
	import FundCrud from './FundCrud';
	export default {
		name: 'FundCategories',
		components: {FundCrud},
		data() {
			return {
				fields: [{title: '编码', prop: 'code'}, {title: '名称', prop: 'name'}, {title: '类型', prop: 'categoryType'}, {title: '会计科目ID', prop: 'subjectId'}, {title: '启用', prop: 'status', type: 'boolean'}],
				formFields: [
					{title: '编码', prop: 'code', required: true}, {title: '名称', prop: 'name', required: true},
					{title: '类型', prop: 'categoryType', required: true, options: [{title: '收入', key: 'IN'}, {title: '支出', key: 'OUT'}]},
					{title: '会计科目ID', prop: 'subjectId', type: 'number'}, {title: '启用', prop: 'status', type: 'boolean', default: true}, {title: '备注', prop: 'remark'}
				]
			}
		}
	}
</script>
