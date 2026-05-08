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
	category: crud('/fixed-assets/category'),
	method: crud('/fixed-assets/depreciation-method'),
	changeRecord: crud('/fixed-assets/change'),
	disposalRecord: crud('/fixed-assets/disposal'),
	card: Object.assign(crud('/fixed-assets/card'), {
		acquire(id, generateVoucher = true) { return Ajax.post(`/fixed-assets/card/${id}/acquire?generateVoucher=${generateVoucher}`) },
		change(params = {}, generateVoucher = true) { return Ajax.post(`/fixed-assets/card/change?generateVoucher=${generateVoucher}`, params) },
		dispose(params = {}, generateVoucher = true) { return Ajax.post(`/fixed-assets/card/dispose?generateVoucher=${generateVoucher}`, params) }
	}),
	depreciation: Object.assign(crud('/fixed-assets/depreciation'), {
		generate(year, month, generateVoucher = true) { return Ajax.post(`/fixed-assets/depreciation/generate?year=${year}&month=${month}&generateVoucher=${generateVoucher}`) },
		check(year, month) { return Ajax.get('/fixed-assets/depreciation/check', {year, month}) }
	})
}
