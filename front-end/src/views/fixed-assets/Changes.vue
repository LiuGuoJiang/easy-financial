<template>
	<fund-crud title="资产变动" api-name="changeRecord" api-module="fixedAssets" :editable="false" :fields="fields" :form-fields="[]"></fund-crud>
</template>
<script>
	import FundCrud from '../funds/FundCrud';
	export default {name: 'FixedAssetChanges', components: {FundCrud}, data() { return {fields: [{title: '资产ID', prop: 'assetId'}, {title: '变动日期', prop: 'changeDate'}, {title: '变动类型', prop: 'changeType'}, {title: '变动前原值', prop: 'beforeValue'}, {title: '变动后原值', prop: 'afterValue'}, {title: '凭证ID', prop: 'voucherId'}]}}}
</script>
