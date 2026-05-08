import Ajax from '../js/common/ajax';

function crud(path) {
	return {
		list(params) {
			return Ajax.get(path, params)
		},
		load(id) {
			return Ajax.get(`${path}/${id}`)
		},
		save(params = {}) {
			return Ajax.post(path, params)
		},
		update(params = {}) {
			return Ajax.put(path, params)
		},
		delete(id) {
			return Ajax.delete(`${path}/${id}`)
		}
	}
}

export default {
	account: crud('/fund/account'),
	category: crud('/fund/category'),
	flow: Object.assign(crud('/fund/flow'), {
		report() {
			return Ajax.get('/fund/flow/report')
		}
	}),
	receipt: Object.assign(crud('/fund/receipt'), {
		audit(id, generateVoucher = true) {
			return Ajax.post(`/fund/receipt/${id}/audit?generateVoucher=${generateVoucher}`)
		},
		cancelAudit(id) {
			return Ajax.post(`/fund/receipt/${id}/cancelAudit`)
		}
	}),
	payment: Object.assign(crud('/fund/payment'), {
		audit(id, generateVoucher = true) {
			return Ajax.post(`/fund/payment/${id}/audit?generateVoucher=${generateVoucher}`)
		},
		cancelAudit(id) {
			return Ajax.post(`/fund/payment/${id}/cancelAudit`)
		}
	}),
	bankStatement: Object.assign(crud('/fund/bank-statement'), {
		reconcile(id, flowId) {
			return Ajax.post(`/fund/bank-statement/${id}/reconcile?flowId=${flowId}`)
		},
		cancelReconcile(id) {
			return Ajax.post(`/fund/bank-statement/${id}/cancelReconcile`)
		}
	})
}
