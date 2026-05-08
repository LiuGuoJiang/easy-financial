<template>
	<fund-crud title="资产类别" api-name="category" api-module="fixedAssets" :fields="fields" :form-fields="formFields"></fund-crud>
</template>
<script>
	import FundCrud from '../funds/FundCrud';
	export default {
		name: 'FixedAssetCategories',
		components: {FundCrud},
		data() {
			return {
				fields: [
					{title: '编码', prop: 'code'}, {title: '名称', prop: 'name'}, {title: '折旧方法', prop: 'depreciationMethod'},
					{title: '使用月数', prop: 'usefulMonths'}, {title: '残值率%', prop: 'netSalvageRate'}, {title: '启用', prop: 'status', type: 'boolean'}
				],
				formFields: [
					{title: '编码', prop: 'code', required: true}, {title: '名称', prop: 'name', required: true},
					{title: '折旧方法', prop: 'depreciationMethod', required: true, options: [{title: '平均年限法', key: 'STRAIGHT_LINE'}, {title: '双倍余额递减法', key: 'DOUBLE_DECLINING'}, {title: '年数总和法', key: 'SUM_OF_YEARS_DIGITS'}]},
					{title: '使用月数', prop: 'usefulMonths', type: 'number'}, {title: '残值率%', prop: 'netSalvageRate', type: 'number'},
					{title: '固定资产科目ID', prop: 'fixedAssetSubjectId', type: 'number'}, {title: '累计折旧科目ID', prop: 'accumulatedDepreciationSubjectId', type: 'number'},
					{title: '折旧费用科目ID', prop: 'depreciationExpenseSubjectId', type: 'number'}, {title: '启用', prop: 'status', type: 'boolean', default: true}, {title: '备注', prop: 'remark'}
				]
			}
		}
	}
</script>
