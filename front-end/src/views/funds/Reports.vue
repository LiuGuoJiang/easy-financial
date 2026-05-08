<template>
	<app-content class="h-panel">
		<div class="h-panel-bar"><span class="h-panel-title">资金报表</span></div>
		<div class="h-panel-body">
			<Row :space="20">
				<Cell :width="8"><div class="fund-card income">收入合计：{{report.income || 0}}</div></Cell>
				<Cell :width="8"><div class="fund-card expense">支出合计：{{report.expense || 0}}</div></Cell>
				<Cell :width="8"><div class="fund-card net">净流入：{{report.net || 0}}</div></Cell>
			</Row>
			<h4 class="margin-top">账户余额</h4>
			<Table :datas="report.accounts || []" :border="true">
				<TableItem title="编码" prop="code"></TableItem>
				<TableItem title="名称" prop="name"></TableItem>
				<TableItem title="类型" prop="accountType"></TableItem>
				<TableItem title="当前余额" prop="currentBalance"></TableItem>
			</Table>
		</div>
	</app-content>
</template>
<script>
	export default {
		name: 'FundReports',
		data() {
			return {report: {}}
		},
		mounted() {
			this.$api.funds.flow.report().then(({data}) => {
				this.report = data || {};
			})
		}
	}
</script>
<style scoped>
	.fund-card { padding: 20px; color: #fff; border-radius: 4px; font-size: 16px; }
	.income { background: #19be6b; }
	.expense { background: #ed4014; }
	.net { background: #2d8cf0; }
</style>
