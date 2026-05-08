<template>
	<app-content class="h-panel">
		<div class="h-panel-bar"><span class="h-panel-title">报表模板/自定义报表</span></div>
		<div class="margin-top margin-left">
			<Button color="primary" @click="showTemplateForm()">新增模板</Button>
		</div>
		<div class="h-panel-body">
			<Table :datas="dataList" :loading="loading">
				<TableItem prop="name" title="名称"/>
				<TableItem prop="templateKey" title="标识"/>
				<TableItem prop="type" title="类型" dict="reportTemplateType"/>
				<TableItem title="状态">
					<template slot-scope="{data}">
						<span :class="data.isDefault?'text-success':'text-muted'">{{data.isDefault ? '已发布' : '已停用'}}</span>
					</template>
				</TableItem>
				<TableItem title="排序" prop="pos" :width="80" align="center"/>
				<TableItem :width="320">
					<div class="actions" slot-scope="{data}">
						<router-link :to="{name:'ReportView',params:{reportId:data.id}}" tag="span" v-if="data.isDefault">预览</router-link>
						<span @click="preview(data)" v-else>预览</span>
						<router-link tag="span" :to="{name:'TemplateForm',params:{templateId:data.id}}">设计</router-link>
						<span @click="showTemplateForm(data)">编辑</span>
						<span @click="copy(data)">复制</span>
						<span @click="validate(data)">校验</span>
						<span @click="togglePublish(data)">{{data.isDefault?'停用':'发布'}}</span>
						<span @click="remove(data)">删除</span>
					</div>
				</TableItem>
			</Table>
		</div>
		<Modal v-model="templateModel" hasCloseIcon hasDivider :closeOnMask="false">
			<div slot="header">{{form.id?'编辑模板':'新增模板'}}</div>
			<Form :labelWidth="80" ref="templateForm" :rules="validationRules" :model="form">
				<FormItem label="名称" prop="name">
					<input v-model="form.name" type="text">
				</FormItem>
				<FormItem label="标识" prop="templateKey">
					<input v-model="form.templateKey" type="text" :disabled="!!form.id">
				</FormItem>
				<FormItem label="类型">
					<Radio v-model="form.type" dict="reportTemplateType"></Radio>
				</FormItem>
				<FormItem label="发布状态">
					<Radio v-model="form.isDefault" :datas="publishOptions"></Radio>
				</FormItem>
				<FormItem label="排序">
					<NumberInput :min="0" v-model="form.pos" useOperate/>
				</FormItem>
			</Form>
			<div slot="footer">
				<Button @click="templateModel=false" :loading="loading">取消</Button>
				<Button color="primary" :loading="loading" @click="save()">保存</Button>
			</div>
		</Modal>
	</app-content>
</template>

<script>
	export default {
		name: "TemplateList",
		data() {
			return {
				loading: false,
				templateModel: false,
				publishOptions: [{title: '停用', key: false}, {title: '发布', key: true}],
				form: this.defaultForm(),
				validationRules: {
					required: ['name', 'templateKey'],
					rules: {
						templateKey: {
							valid: {
								pattern: /^[a-z][a-z0-9_]*$/i,
								message: '只能为字母、数字或下划线，且以字母开头'
							}
						}
					}
				},
				dataList: []
			}
		},
		methods: {
			defaultForm() {
				return {name: '', templateKey: '', type: 0, isDefault: false, pos: 0};
			},
			loadList() {
				this.loading = true;
				this.$api.report.template.list().then(({data}) => {
					this.dataList = data;
				}).finally(() => this.loading = false);
			},
			showTemplateForm(data) {
				this.form = data ? Object.assign({}, data) : this.defaultForm();
				this.templateModel = true;
			},
			save() {
				let validResult = this.$refs.templateForm.valid();
				if (validResult.result) {
					this.loading = true;
					this.$api.report.template[this.form.id ? 'update' : 'save'](this.form).then(() => {
						this.loadList();
						this.templateModel = false;
					}).finally(() => {
						this.loading = false;
					});
				}
			},
			copy(data) {
				this.$api.report.template.copy(data.id).then(() => {
					this.$Message.success('复制成功，新模板默认停用，请校验后发布');
					this.loadList();
				});
			},
			validate(data) {
				this.$api.report.template.validate(data.id).then(({data: errors}) => {
					if (errors.length === 0) {
						this.$Message.success('公式校验通过');
					} else {
						this.$Message(errors.join('；'));
					}
				});
			},
			togglePublish(data) {
				let enabled = !data.isDefault;
				this.$Confirm(`确认${enabled ? '发布' : '停用'}该模板?`).then(() => {
					this.$api.report.template.publish(data.id, enabled).then(() => {
						this.loadList();
					});
				});
			},
			preview(data) {
				this.$router.push({name: 'TemplateForm', params: {templateId: data.id}, query: {preview: 1}});
			},
			remove(data) {
				this.$Confirm("确认删除? 删除后模板项目与公式将同步删除。 ").then(() => {
					this.$api.report.template.delete(data.id).then(() => {
						this.loadList();
					})
				})
			}
		},
		mounted() {
			this.loadList();
		}
	}
</script>

<style scoped>
	.text-success { color: #19be6b; }
	.text-muted { color: #999; }
</style>
