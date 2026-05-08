<template>
	<app-content class="h-panel fixed-assets-page">
		<div class="h-panel-bar"><span class="h-panel-title">资产卡片</span></div>
		<div class="margin-top margin-left fixed-assets-toolbar">
			<Button color="primary" @click="openForm()">新增</Button>
			<Button @click="loadList">刷新</Button>
		</div>
		<div class="h-panel-body">
			<Table :datas="datas" :border="true">
				<TableItem title="资产编码" prop="assetNo"></TableItem>
				<TableItem title="资产名称" prop="assetName"></TableItem>
				<TableItem title="类别ID" prop="categoryId"></TableItem>
				<TableItem title="原值" prop="originalValue"></TableItem>
				<TableItem title="累计折旧" prop="accumulatedDepreciation"></TableItem>
				<TableItem title="净值" prop="netValue"></TableItem>
				<TableItem title="状态" prop="status"></TableItem>
				<TableItem title="操作" :width="220" align="center">
					<div class="actions" slot-scope="{data}">
						<span @click="openForm(data)">编辑</span><span @click="remove(data)">删除</span>
						<span v-if="data.status==='DRAFT'" @click="acquire(data)">购入入账</span>
						<span v-if="data.status==='IN_USE'" @click="openChange(data)">变动</span>
						<span v-if="data.status==='IN_USE'" @click="openDisposal(data)">处置</span>
					</div>
				</TableItem>
			</Table>
		</div>
		<Modal v-model="showForm" type="drawer-right" hasCloseIcon>
			<div slot="header">资产卡片</div>
			<Form ref="form" v-width="440" :labelWidth="120" :model="form">
				<FormItem label="资产编码"><input v-model="form.assetNo"></FormItem>
				<FormItem label="资产名称"><input v-model="form.assetName"></FormItem>
				<FormItem label="类别ID"><NumberInput v-model="form.categoryId"></NumberInput></FormItem>
				<FormItem label="购入日期"><DatePicker v-model="form.purchaseDate"></DatePicker></FormItem>
				<FormItem label="开始使用日期"><DatePicker v-model="form.startUseDate"></DatePicker></FormItem>
				<FormItem label="原值"><NumberInput v-model="form.originalValue"></NumberInput></FormItem>
				<FormItem label="使用月数"><NumberInput v-model="form.usefulMonths"></NumberInput></FormItem>
				<FormItem label="残值率%"><NumberInput v-model="form.netSalvageRate"></NumberInput></FormItem>
				<FormItem label="折旧方法"><Select v-model="form.depreciationMethod" :datas="methodOptions"></Select></FormItem>
				<FormItem label="固定资产科目ID"><NumberInput v-model="form.fixedAssetSubjectId"></NumberInput></FormItem>
				<FormItem label="累计折旧科目ID"><NumberInput v-model="form.accumulatedDepreciationSubjectId"></NumberInput></FormItem>
				<FormItem label="折旧费用科目ID"><NumberInput v-model="form.depreciationExpenseSubjectId"></NumberInput></FormItem>
			</Form>
			<div class="text-center"><Button color="green" @click="submit">保存</Button><Button @click="showForm=false">取消</Button></div>
		</Modal>
		<Modal v-model="showChange" hasCloseIcon>
			<div slot="header">资产变动</div>
			<Form v-width="380" :labelWidth="110" :model="changeForm">
				<FormItem label="变动日期"><DatePicker v-model="changeForm.changeDate"></DatePicker></FormItem>
				<FormItem label="变动类型"><input v-model="changeForm.changeType"></FormItem>
				<FormItem label="变动后原值"><NumberInput v-model="changeForm.afterValue"></NumberInput></FormItem>
				<FormItem label="摘要"><input v-model="changeForm.summary"></FormItem>
			</Form>
			<div class="text-center"><Button color="green" @click="submitChange">确认</Button><Button @click="showChange=false">取消</Button></div>
		</Modal>
		<Modal v-model="showDisposal" hasCloseIcon>
			<div slot="header">资产处置</div>
			<Form v-width="380" :labelWidth="110" :model="disposalForm">
				<FormItem label="处置日期"><DatePicker v-model="disposalForm.disposalDate"></DatePicker></FormItem>
				<FormItem label="处置方式"><input v-model="disposalForm.disposalType"></FormItem>
				<FormItem label="处置收入"><NumberInput v-model="disposalForm.disposalIncome"></NumberInput></FormItem>
				<FormItem label="处置费用"><NumberInput v-model="disposalForm.disposalExpense"></NumberInput></FormItem>
				<FormItem label="摘要"><input v-model="disposalForm.summary"></FormItem>
			</Form>
			<div class="text-center"><Button color="green" @click="submitDisposal">确认</Button><Button @click="showDisposal=false">取消</Button></div>
		</Modal>
	</app-content>
</template>
<script>
	import fixedAssets from '../../api/fixedAssets';
	export default {
		name: 'FixedAssetCards',
		data() { return {datas: [], form: {}, showForm: false, showChange: false, showDisposal: false, changeForm: {}, disposalForm: {}, methodOptions: [{title: '平均年限法', key: 'STRAIGHT_LINE'}, {title: '双倍余额递减法', key: 'DOUBLE_DECLINING'}, {title: '年数总和法', key: 'SUM_OF_YEARS_DIGITS'}]} },
		methods: {
			loadList() { fixedAssets.card.list().then(({data}) => this.datas = data || []) },
			openForm(row) { this.form = row ? Object.assign({}, row) : {status: 'DRAFT', depreciationMethod: 'STRAIGHT_LINE'}; this.showForm = true },
			submit() { const api = this.form.id ? fixedAssets.card.update : fixedAssets.card.save; api(this.form).then(() => { this.showForm = false; this.loadList() }) },
			remove(row) { fixedAssets.card.delete(row.id).then(() => this.loadList()) },
			acquire(row) { fixedAssets.card.acquire(row.id).then(() => this.loadList()) },
			openChange(row) { this.changeForm = {assetId: row.id, afterValue: row.originalValue}; this.showChange = true },
			submitChange() { fixedAssets.card.change(this.changeForm).then(() => { this.showChange = false; this.loadList() }) },
			openDisposal(row) { this.disposalForm = {assetId: row.id}; this.showDisposal = true },
			submitDisposal() { fixedAssets.card.dispose(this.disposalForm).then(() => { this.showDisposal = false; this.loadList() }) }
		},
		mounted() { this.loadList() }
	}
</script>
<style scoped>.fixed-assets-toolbar button{margin-right:8px}.actions span{margin:0 5px;color:#3788ee;cursor:pointer}</style>
