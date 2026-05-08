<template>
	<app-content class="h-panel">
		<div class="h-panel-bar">
			<span class="h-panel-title">{{template.name}} - 自定义报表设计器</span>
			<button @click="$router.back()" class="h-btn float-right">返回</button>
		</div>
		<div class="designer-toolbar margin-top margin-left margin-right">
			<Button color="primary" @click="showForm()">添加项目</Button>
			<Button @click="validateTemplate">校验公式</Button>
			<Button @click="loadPreview">预览</Button>
			<Button :color="template.isDefault?'red':'green'" @click="togglePublish">{{template.isDefault?'停用':'发布'}}</Button>
			<account-date-choose class="float-right" v-model="accountDate"/>
		</div>
		<div class="h-panel-body">
			<Row :space="20">
				<Cell :width="previewVisible ? 7 : 12">
					<div class="section-title">行列与取数配置</div>
					<Table :datas="templateItems" selectRow>
						<TableItem :width="80" align="center">
							<div slot-scope="{data}">{{data.isClassified?'归类':''}}</div>
						</TableItem>
						<TableItem title="名称/层级">
							<div slot-scope="{data}" :class="{'font-bold':data.isBolder}" :style="{paddingLeft:((data.level-1)*15)+'px'}">{{data.title}}</div>
						</TableItem>
						<TableItem title="行次" align="center" :width="70">
							<div slot-scope="{data}">{{data.isClassified?'':data.lineNum}}</div>
						</TableItem>
						<TableItem title="列类型" v-if="template.type == 1">
							<div slot-scope="{data}">{{data.isClassified?'':data.type| dictMapping('reportTemplateItemType')}}</div>
						</TableItem>
						<TableItem title="取数来源">
							<div slot-scope="{data}">{{data.isClassified?'':data.sources| dictMapping('reportTemplateItemSources')}}</div>
						</TableItem>
						<TableItem title="公式" :width="80" align="center">
							<div slot-scope="{data}">{{data.formulas ? data.formulas.length : 0}}</div>
						</TableItem>
						<TableItem title="排序" prop="pos" :width="70" align="center"></TableItem>
						<TableItem :width="210">
							<div class="actions" slot-scope="{data}">
								<span @click="showForm(data)">添加</span>
								<span @click="showForm({id:data.parentId},data)">编辑</span>
								<span v-if="!data.isClassified" @click="showFormulaModal(data,template.type)">公式</span>
								<span @click="remove(data)">删除</span>
							</div>
						</TableItem>
					</Table>
				</Cell>
				<Cell :width="5" v-if="previewVisible">
					<div class="section-title">预览结果</div>
					<Table class="small-td" :datas="templateItems" border v-if="template.type==0 || template.type==2">
						<TableItem title="项目">
							<div slot-scope="{data}" :class="{'font-bold':data.isBolder}" :style="{paddingLeft:((data.level-1)*15)+'px'}">{{data.title}}</div>
						</TableItem>
						<TableItem title="行次" :width="60"><template slot-scope="{data}">{{data.isClassified?'':data.lineNum}}</template></TableItem>
						<TableItem title="本年" align="right"><template slot-scope="{data}">{{previewData[data.id]?previewData[data.id].currentYearAmount:''|numFormat}}</template></TableItem>
						<TableItem title="本期" align="right"><template slot-scope="{data}">{{previewData[data.id]?previewData[data.id].currentPeriodAmount:''|numFormat}}</template></TableItem>
					</Table>
					<Table class="small-td" :datas="templateItems" border v-else>
						<TableItem title="项目"><div slot-scope="{data}" :class="{'font-bold':data.isBolder}" :style="{paddingLeft:((data.level-1)*15)+'px'}">{{data.title}}</div></TableItem>
						<TableItem title="行次" :width="60"><template slot-scope="{data}">{{data.isClassified?'':data.lineNum}}</template></TableItem>
						<TableItem title="期末/本期" align="right"><template slot-scope="{data}">{{previewData[data.id]?previewData[data.id].currentPeriodAmount:''|numFormat}}</template></TableItem>
						<TableItem title="年初/本年" align="right"><template slot-scope="{data}">{{previewData[data.id]?previewData[data.id].currentYearAmount:''|numFormat}}</template></TableItem>
					</Table>
				</Cell>
			</Row>
		</div>
		<TemplateItemModal @success="loadTemplate" :template="template" ref="templateItemModal" :templateItems="templateItems"/>
		<TemplateItemFormulaModal @success="loadTemplate" ref="templateItemFormulaModal" :templateItems="formulaSourceItems"/>
	</app-content>
</template>

<script>

	import TemplateItemModal from "./TemplateItemModal";
	import TemplateItemFormulaModal from "./TemplateItemFormulaModal";

	export default {
		name: "TemplateForm",
		components: {TemplateItemFormulaModal, TemplateItemModal},
		props: {
			templateId: [Number, String]
		},
		data() {
			return {
				template: {},
				accountDate: null,
				previewVisible: this.$route.query.preview === '1',
				previewData: {}
			}
		},
		computed: {
			templateItems() {
				return this.template.items || [];
			},
			formulaSourceItems() {
				return this.templateItems.filter(item => !item.isClassified);
			}
		},
		methods: {
			showForm(parent, org) {
				this.$refs.templateItemModal.toggle(parent, org);
			},
			showFormulaModal(item, type) {
				this.$refs.templateItemFormulaModal.toggle(item, type);
			},
			loadTemplate() {
				Api.report.template.load(this.templateId).then(({data}) => {
					this.template = data;
					if (this.previewVisible) {
						this.loadPreview();
					}
				});
			},
			validateTemplate() {
				Api.report.template.validate(this.templateId).then(({data}) => {
					if (data.length === 0) {
						this.$Message.success('模板公式校验通过');
					} else {
						this.$Message(data.join('；'));
					}
				});
			},
			loadPreview() {
				this.previewVisible = true;
				Api.report.template.preview(this.templateId, {accountDate: this.accountDate}).then(({data}) => {
					this.previewData = data;
				});
			},
			togglePublish() {
				let enabled = !this.template.isDefault;
				this.$Confirm(`确认${enabled ? '发布' : '停用'}该模板?`).then(() => {
					Api.report.template.publish(this.templateId, enabled).then(() => {
						this.loadTemplate();
					});
				});
			},
			remove(data) {
				this.$Confirm("确认删除? 该项目公式将同步删除。 ").then(() => {
					Api.report.template.items.delete(data.id).then(() => {
						this.loadTemplate();
					})
				})
			}
		},
		mounted() {
			this.loadTemplate()
		}
	}
</script>

<style scoped>
	.section-title {
		font-weight: bold;
		margin-bottom: 10px;
	}
</style>
