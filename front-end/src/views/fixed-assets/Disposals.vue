<template>
	<fund-crud title="资产处置" api-name="disposalRecord" api-module="fixedAssets" :editable="false" :fields="fields" :form-fields="[]"></fund-crud>
</template>
<script>
	import FundCrud from '../funds/FundCrud';
	export default {name: 'FixedAssetDisposals', components: {FundCrud}, data() { return {fields: [{title: '资产ID', prop: 'assetId'}, {title: '处置日期', prop: 'disposalDate'}, {title: '处置方式', prop: 'disposalType'}, {title: '处置收入', prop: 'disposalIncome'}, {title: '处置费用', prop: 'disposalExpense'}, {title: '处置损益', prop: 'gainLoss'}, {title: '凭证ID', prop: 'voucherId'}]}}}
</script>
