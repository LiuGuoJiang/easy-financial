<template>
	<app-content class="h-panel fixed-assets-page">
		<div class="h-panel-bar"><span class="h-panel-title">折旧计提</span></div>
		<div class="margin-top margin-left fixed-assets-toolbar">
			<NumberInput v-model="year" :useOperate="false" placeholder="年度"></NumberInput>
			<NumberInput v-model="month" :useOperate="false" placeholder="月份"></NumberInput>
			<Button color="primary" @click="generate">生成当期折旧并生成凭证</Button>
			<Button @click="loadList">刷新</Button>
		</div>
		<div class="h-panel-body">
			<Table :datas="datas" :border="true">
				<TableItem title="资产ID" prop="assetId"></TableItem>
				<TableItem title="年度" prop="depreciationYear"></TableItem>
				<TableItem title="月份" prop="depreciationMonth"></TableItem>
				<TableItem title="折旧日期" prop="depreciationDate"></TableItem>
				<TableItem title="本期折旧" prop="amount"></TableItem>
				<TableItem title="折后累计折旧" prop="afterAccumulatedDepreciation"></TableItem>
				<TableItem title="凭证ID" prop="voucherId"></TableItem>
			</Table>
		</div>
	</app-content>
</template>
<script>
	import fixedAssets from '../../api/fixedAssets';
	export default {
		name: 'FixedAssetDepreciation',
		data() { const now = new Date(); return {year: now.getFullYear(), month: now.getMonth() + 1, datas: []} },
		methods: {
			loadList() { fixedAssets.depreciation.list().then(({data}) => this.datas = data.records || data || []) },
			generate() { fixedAssets.depreciation.generate(this.year, this.month, true).then(() => this.loadList()) }
		},
		mounted() { this.loadList() }
	}
</script>
<style scoped>.fixed-assets-toolbar>*{margin-right:8px}</style>
