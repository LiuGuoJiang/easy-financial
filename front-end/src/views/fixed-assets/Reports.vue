<template>
	<app-content class="h-panel fixed-assets-page">
		<div class="h-panel-bar"><span class="h-panel-title">资产报表</span></div>
		<div class="margin-top margin-left"><Button @click="loadList">刷新</Button></div>
		<div class="h-panel-body">
			<Row :space="16"><Cell :width="6"><div class="stat">资产原值：{{totalOriginal}}</div></Cell><Cell :width="6"><div class="stat">累计折旧：{{totalDepreciation}}</div></Cell><Cell :width="6"><div class="stat">资产净值：{{totalNet}}</div></Cell></Row>
			<Table :datas="datas" :border="true">
				<TableItem title="资产编码" prop="assetNo"></TableItem><TableItem title="资产名称" prop="assetName"></TableItem><TableItem title="原值" prop="originalValue"></TableItem><TableItem title="累计折旧" prop="accumulatedDepreciation"></TableItem><TableItem title="净值" prop="netValue"></TableItem><TableItem title="状态" prop="status"></TableItem>
			</Table>
		</div>
	</app-content>
</template>
<script>
	import fixedAssets from '../../api/fixedAssets';
	export default {name: 'FixedAssetReports', data() { return {datas: []} }, computed: {totalOriginal() { return this.sum('originalValue') }, totalDepreciation() { return this.sum('accumulatedDepreciation') }, totalNet() { return this.sum('netValue') }}, methods: {sum(prop) { return this.datas.reduce((n, row) => n + (row[prop] || 0), 0).toFixed(2) }, loadList() { fixedAssets.card.list().then(({data}) => this.datas = data.records || data || []) }}, mounted() { this.loadList() }}
</script>
<style scoped>.stat{padding:16px;background:#f7f8fa;border-radius:4px;margin-bottom:16px}</style>
