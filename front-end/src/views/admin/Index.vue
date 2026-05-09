<template>
	<app-content class="admin-console">
		<div class="h-panel">
			<div class="h-panel-bar">
				<span class="h-panel-title">商业化运营后台</span>
			</div>
			<div class="h-panel-body">
				<Tabs v-model="active" @change="loadCurrent">
					<TabItem v-for="item in modules" :key="item.key" :name="item.key" :label="item.title"></TabItem>
				</Tabs>
				<div class="admin-toolbar">
					<Button color="primary" @click="openForm">新增{{current.title}}</Button>
					<Button @click="loadCurrent">刷新</Button>
				</div>
				<Table :datas="datas" :border="true">
					<TableItem v-for="column in current.columns" :key="column.prop" :title="column.title" :prop="column.prop" :width="column.width"></TableItem>
					<TableItem title="操作" :width="120" align="center">
						<div class="actions" slot-scope="{data}">
							<span @click="edit(data)">编辑</span>
							<span @click="remove(data)">删除</span>
						</div>
					</TableItem>
				</Table>
			</div>
		</div>

		<Modal v-model="showForm" type="drawer-right" hasCloseIcon>
			<div slot="header">{{form.id ? '编辑' : '新增'}}{{current.title}}</div>
			<Form ref="form" v-width="760" mode="twocolumn" :labelWidth="130" :model="form">
				<FormItem v-for="field in current.fields" :key="field.prop" :label="field.title + ':'">
					<input v-if="field.type !== 'textarea'" v-model="form[field.prop]" type="text">
					<textarea v-else v-model="form[field.prop]"></textarea>
				</FormItem>
			</Form>
			<div class="text-center">
				<Button color="green" @click="submit" :loading="loading">保存</Button>
				<Button @click="showForm=false">取消</Button>
			</div>
		</Modal>
	</app-content>
</template>

<script>
	const modules = [
		{ key: 'merchants', title: '商户列表', columns: [{title: 'ID', prop: 'id', width: 70}, {title: '商户名称', prop: 'name'}, {title: '编码', prop: 'code'}, {title: '联系人', prop: 'contactName'}, {title: '状态', prop: 'status'}], fields: [{title: '商户名称', prop: 'name'}, {title: '编码', prop: 'code'}, {title: '联系人', prop: 'contactName'}, {title: '联系电话', prop: 'contactMobile'}, {title: '状态', prop: 'status'}, {title: '备注', prop: 'remark', type: 'textarea'}] },
		{ key: 'plans', title: '套餐管理', columns: [{title: 'ID', prop: 'id', width: 70}, {title: '套餐名称', prop: 'name'}, {title: '价格', prop: 'price'}, {title: '账套数', prop: 'accountSetLimit'}, {title: '用户数', prop: 'userLimit'}, {title: '授权额度', prop: 'voucherLimit'}], fields: [{title: '套餐名称', prop: 'name'}, {title: '编码', prop: 'code'}, {title: '价格', prop: 'price'}, {title: '计费周期', prop: 'billingCycle'}, {title: '账套数量限制', prop: 'accountSetLimit'}, {title: '用户数量限制', prop: 'userLimit'}, {title: '授权额度', prop: 'voucherLimit'}, {title: '状态', prop: 'status'}] },
		{ key: 'orders', title: '订单管理', columns: [{title: 'ID', prop: 'id', width: 70}, {title: '订单号', prop: 'orderNo'}, {title: '商户', prop: 'merchantId'}, {title: '租户', prop: 'tenantId'}, {title: '金额', prop: 'amount'}, {title: '状态', prop: 'status'}], fields: [{title: '商户 ID', prop: 'merchantId'}, {title: '租户 ID', prop: 'tenantId'}, {title: '订阅 ID', prop: 'subscriptionId'}, {title: '订单号', prop: 'orderNo'}, {title: '金额', prop: 'amount'}, {title: '状态', prop: 'status'}] },
		{ key: 'authorizations', title: '授权管理', columns: [{title: 'ID', prop: 'id', width: 70}, {title: '商户', prop: 'merchantId'}, {title: '租户', prop: 'tenantId'}, {title: '额度类型', prop: 'quotaType'}, {title: '额度上限', prop: 'quotaLimit'}, {title: '已用额度', prop: 'usedQuota'}], fields: [{title: '商户 ID', prop: 'merchantId'}, {title: '租户 ID', prop: 'tenantId'}, {title: '订阅 ID', prop: 'subscriptionId'}, {title: '额度类型', prop: 'quotaType'}, {title: '额度上限', prop: 'quotaLimit'}, {title: '已用额度', prop: 'usedQuota'}] },
		{ key: 'accountSets', title: '账套管理', columns: [{title: 'ID', prop: 'id', width: 70}, {title: '租户', prop: 'tenantId'}, {title: '单位名称', prop: 'companyName'}, {title: '启用年月', prop: 'enableDate'}, {title: '创建人', prop: 'creatorId'}], fields: [{title: '租户 ID', prop: 'tenantId'}, {title: '单位名称', prop: 'companyName'}, {title: '统一社会信用代码', prop: 'creditCode'}, {title: '地址', prop: 'address'}] },
		{ key: 'users', title: '用户管理', columns: [{title: 'ID', prop: 'id', width: 70}, {title: '租户', prop: 'tenantId'}, {title: '手机号', prop: 'mobile'}, {title: '姓名', prop: 'realName'}, {title: '平台角色', prop: 'platformRole'}], fields: [{title: '租户 ID', prop: 'tenantId'}, {title: '手机号', prop: 'mobile'}, {title: '姓名', prop: 'realName'}, {title: '邮箱', prop: 'email'}, {title: '平台角色', prop: 'platformRole'}] },
		{ key: 'auditLogs', title: '审计日志', columns: [{title: 'ID', prop: 'id', width: 70}, {title: '租户', prop: 'tenantId'}, {title: '操作人', prop: 'operatorName'}, {title: '对象', prop: 'targetType'}, {title: '动作', prop: 'action'}, {title: '时间', prop: 'createDate'}], fields: [{title: '租户 ID', prop: 'tenantId'}, {title: '操作人', prop: 'operatorName'}, {title: '对象类型', prop: 'targetType'}, {title: '对象 ID', prop: 'targetId'}, {title: '动作', prop: 'action'}] }
	];

	export default {
		name: 'AdminIndex',
		data() {
			return { active: 'merchants', modules, datas: [], form: {}, showForm: false, loading: false };
		},
		computed: {
			current() {
				return this.modules.find(item => item.key === this.active) || this.modules[0];
			}
		},
		mounted() {
			this.loadCurrent();
		},
		methods: {
			loadCurrent() {
				this.$api.admin[this.active].list().then(({data}) => {
					this.datas = data && data.records ? data.records : (data || []);
				});
			},
			openForm() {
				this.form = {};
				this.showForm = true;
			},
			edit(data) {
				this.form = Object.assign({}, data);
				this.showForm = true;
			},
			remove(data) {
				this.$Confirm(`确认删除 ${this.current.title} #${data.id}？`, '删除确认').then(() => {
					this.$api.admin[this.active].delete(data.id).then(() => this.loadCurrent());
				});
			},
			submit() {
				this.loading = true;
				let request = this.form.id ? this.$api.admin[this.active].update(this.form) : this.$api.admin[this.active].save(this.form);
				request.then(() => {
					this.showForm = false;
					this.loadCurrent();
				}).finally(() => {
					this.loading = false;
				});
			}
		}
	};
</script>

<style lang="less" scoped>
	.admin-console {
		.admin-toolbar {
			margin: 16px 0;
			button + button { margin-left: 8px; }
		}
		.actions span {
			color: #2d8cf0;
			cursor: pointer;
			margin: 0 6px;
		}
	}
</style>
