import Ajax from '../js/common/ajax';

function crud(path) {
	return {
		list(params) { return Ajax.get(path, params) },
		load(id) { return Ajax.get(`${path}/${id}`) },
		save(params = {}) { return Ajax.post(path, params) },
		update(params = {}) { return Ajax.put(path, params) },
		delete(id) { return Ajax.delete(`${path}/${id}`) }
	}
}

export default {
	employee: Object.assign(crud('/payroll/employee'), {
		departments() { return Ajax.get('/payroll/employee/departments') },
		saveDepartment(params = {}) { return Ajax.post('/payroll/employee/departments', params) },
		updateDepartment(params = {}) { return Ajax.put('/payroll/employee/departments', params) },
		deleteDepartment(id) { return Ajax.delete(`/payroll/employee/departments/${id}`) }
	}),
	item: Object.assign(crud('/payroll/items'), {
		init() { return Ajax.post('/payroll/items/init') }
	}),
	config: crud('/payroll/configs'),
	sheet: Object.assign(crud('/payroll/sheets'), {
		calculate(year, month) { return Ajax.post(`/payroll/calculate?year=${year}&month=${month}`) },
		audit(id) { return Ajax.post(`/payroll/sheets/${id}/audit`) },
		cancelAudit(id) { return Ajax.post(`/payroll/sheets/${id}/cancelAudit`) },
		generateVoucher(id) { return Ajax.post(`/payroll/sheets/${id}/voucher`) }
	})
}
