<template>
	<app-content class="h-panel payroll-page">
		<div class="h-panel-bar"><span class="h-panel-title">员工档案</span></div>
		<div class="margin-top margin-left payroll-toolbar"><Button color="primary" @click="openForm()">新增员工</Button><Button @click="loadList">刷新</Button></div>
		<div class="h-panel-body">
			<Table :datas="datas" :border="true">
				<TableItem title="工号" prop="employeeNo"></TableItem><TableItem title="姓名" prop="employeeName"></TableItem><TableItem title="部门" prop="departmentName"></TableItem><TableItem title="手机" prop="phone"></TableItem><TableItem title="基本工资" prop="baseSalary"></TableItem><TableItem title="状态" prop="status"></TableItem>
				<TableItem title="操作" :width="120" align="center"><div class="actions" slot-scope="{data}"><span @click="openForm(data)">编辑</span><span @click="remove(data)">删除</span></div></TableItem>
			</Table>
		</div>
		<Modal v-model="showForm" type="drawer-right" hasCloseIcon><div slot="header">员工档案</div>
			<Form v-width="460" :labelWidth="110" :model="form">
				<FormItem label="工号"><input v-model="form.employeeNo"></FormItem><FormItem label="姓名"><input v-model="form.employeeName"></FormItem><FormItem label="部门"><Select v-model="form.departmentId" :datas="departmentOptions" @change="changeDepartment"></Select></FormItem><FormItem label="手机"><input v-model="form.phone"></FormItem><FormItem label="身份证"><input v-model="form.idCardNo"></FormItem><FormItem label="入职日期"><DatePicker v-model="form.entryDate"></DatePicker></FormItem><FormItem label="基本工资"><NumberInput v-model="form.baseSalary"></NumberInput></FormItem><FormItem label="费用科目ID"><NumberInput v-model="form.expenseSubjectId"></NumberInput></FormItem><FormItem label="状态"><Select v-model="form.status" :datas="statusOptions"></Select></FormItem><FormItem label="银行卡号"><input v-model="form.bankAccount"></FormItem>
			</Form><div class="text-center"><Button color="green" @click="submit">保存</Button><Button @click="showForm=false">取消</Button></div>
		</Modal>
	</app-content>
</template>
<script>
import payroll from '../../api/payroll';
export default { name: 'PayrollEmployees', data() { return {datas: [], departments: [], departmentOptions: [], form: {}, showForm: false, statusOptions: [{title: '在职', key: 'ACTIVE'}, {title: '离职', key: 'LEAVED'}]} }, methods: { loadList() { payroll.employee.list().then(({data}) => this.datas = data || []) }, loadDepartments() { payroll.employee.departments().then(({data}) => { this.departments = data || []; this.departmentOptions = this.departments.map(d => ({title: d.deptName, key: d.id})) }) }, openForm(row) { this.form = row ? Object.assign({}, row) : {status: 'ACTIVE', baseSalary: 0}; this.showForm = true }, changeDepartment(id) { const d = this.departments.find(item => item.id === id); if (d) this.form.departmentName = d.deptName }, submit() { const api = this.form.id ? payroll.employee.update : payroll.employee.save; api(this.form).then(() => { this.showForm = false; this.loadList() }) }, remove(row) { payroll.employee.delete(row.id).then(() => this.loadList()) } }, mounted() { this.loadDepartments(); this.loadList() } }
</script>
<style scoped>.payroll-toolbar button{margin-right:8px}.actions span{margin:0 5px;color:#3788ee;cursor:pointer}</style>
