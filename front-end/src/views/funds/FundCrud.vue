<template>
	<app-content class="h-panel funds-page">
		<div class="h-panel-bar"><span class="h-panel-title">{{title}}</span></div>
		<div class="margin-top margin-left funds-toolbar">
			<Button color="primary" v-if="editable" @click="openForm()">新增</Button>
			<Button @click="loadList">刷新</Button>
		</div>
		<div class="h-panel-body">
			<Table :datas="datas" :border="true">
				<TableItem v-for="field in fields" :key="field.prop" :title="field.title" :prop="field.prop">
					<template slot-scope="{data}">{{format(data, field)}}</template>
				</TableItem>
				<TableItem title="操作" :width="operationWidth" align="center" v-if="editable || auditable || reconcile">
					<div class="actions" slot-scope="{data}">
						<span v-if="editable" @click="openForm(data)">编辑</span>
						<span v-if="editable" @click="remove(data)">删除</span>
						<span v-if="auditable && data.status!=='AUDITED'" @click="audit(data)">审核并生成凭证</span>
						<span v-if="auditable && data.status==='AUDITED'" @click="cancelAudit(data)">反审核</span>
						<span v-if="reconcile && data.reconcileStatus!=='RECONCILED'" @click="openReconcile(data)">对账</span>
						<span v-if="reconcile && data.reconcileStatus==='RECONCILED'" @click="cancelReconcile(data)">取消对账</span>
					</div>
				</TableItem>
			</Table>
		</div>
		<Modal v-model="showForm" type="drawer-right" hasCloseIcon>
			<div slot="header">{{title}}</div>
			<Form ref="form" v-width="420" :labelWidth="110" :model="form" :rules="validationRules">
				<FormItem v-for="field in formFields" :key="field.prop" :label="field.title" :prop="field.prop">
					<NumberInput v-if="field.type==='number'" v-model="form[field.prop]"></NumberInput>
					<SwitchList v-else-if="field.type==='boolean'" v-model="form[field.prop]" :datas="booleanOptions"></SwitchList>
					<DatePicker v-else-if="field.type==='date'" v-model="form[field.prop]"></DatePicker>
					<Select v-else-if="field.options" v-model="form[field.prop]" :datas="field.options"></Select>
					<input v-else type="text" v-model="form[field.prop]">
				</FormItem>
			</Form>
			<div class="text-center">
				<Button color="green" @click="submit" :loading="loading">{{form.id?'更新':'保存'}}</Button>
				<Button @click="showForm=false" :loading="loading">取消</Button>
			</div>
		</Modal>
		<Modal v-model="showReconcile" hasCloseIcon>
			<div slot="header">银行对账</div>
			<Form v-width="360" :labelWidth="100" :model="reconcileForm">
				<FormItem label="资金流水ID"><NumberInput v-model="reconcileForm.flowId"></NumberInput></FormItem>
			</Form>
			<div class="text-center">
				<Button color="green" @click="submitReconcile">确认对账</Button>
				<Button @click="showReconcile=false">取消</Button>
			</div>
		</Modal>
	</app-content>
</template>

<script>
	export default {
		name: 'FundCrud',
		props: {
			title: String,
			apiName: String,
			fields: Array,
			formFields: Array,
			editable: {
				type: Boolean,
				default: true
			},
			auditable: Boolean,
			reconcile: Boolean
		},
		data() {
			return {
				datas: [],
				form: {},
				showForm: false,
				showReconcile: false,
				reconcileForm: {},
				loading: false,
				booleanOptions: [{title: '是', key: true}, {title: '否', key: false}],
				validationRules: {required: (this.formFields || []).filter(f => f.required).map(f => f.prop)}
			}
		},
		computed: {
			api() {
				return this.$api.funds[this.apiName]
			},
			operationWidth() {
				return this.auditable ? 220 : 160
			}
		},
		methods: {
			loadList() {
				this.api.list().then(({data}) => {
					this.datas = data.records || data || [];
				})
			},
			format(row, field) {
				let val = row[field.prop];
				if (field.formatter) {
					return field.formatter(val, row)
				}
				if (field.type === 'boolean') {
					return val ? '是' : '否'
				}
				return val === null || val === undefined ? '' : val
			},
			openForm(data) {
				this.form = Object.assign({}, data || this.formFields.reduce((obj, field) => {
					if (field.default !== undefined) obj[field.prop] = field.default;
					return obj
				}, {}));
				this.showForm = true;
			},
			submit() {
				let validResult = this.$refs.form.valid();
				if (!validResult.result) return;
				this.loading = true;
				this.api[this.form.id ? 'update' : 'save'](this.form).then(() => {
					this.showForm = false;
					this.loadList();
				}).finally(() => {
					this.loading = false;
				})
			},
			remove(data) {
				this.$Confirm('确认删除?').then(() => this.api.delete(data.id).then(() => this.loadList()))
			},
			audit(data) {
				this.api.audit(data.id, true).then(() => this.loadList())
			},
			cancelAudit(data) {
				this.api.cancelAudit(data.id).then(() => this.loadList())
			},
			openReconcile(data) {
				this.reconcileForm = {id: data.id, flowId: data.fundFlowId};
				this.showReconcile = true;
			},
			submitReconcile() {
				this.api.reconcile(this.reconcileForm.id, this.reconcileForm.flowId).then(() => {
					this.showReconcile = false;
					this.loadList();
				})
			},
			cancelReconcile(data) {
				this.api.cancelReconcile(data.id).then(() => this.loadList())
			}
		},
		mounted() {
			this.loadList();
		}
	}
</script>

<style scoped>
	.funds-toolbar button { margin-right: 8px; }
	.actions span { margin: 0 6px; color: #3788ee; cursor: pointer; }
</style>
