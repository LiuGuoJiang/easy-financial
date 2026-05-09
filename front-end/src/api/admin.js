import Ajax from '../js/common/ajax';

function crud(path) {
	return {
		list(params = {}) {
			return Ajax.get(path, params);
		},
		load(id) {
			return Ajax.get(`${path}/${id}`);
		},
		save(params = {}) {
			return Ajax.post(path, params);
		},
		update(params = {}) {
			return Ajax.put(path, params);
		},
		delete(id) {
			return Ajax.delete(`${path}/${id}`);
		}
	};
}

export default {
	merchants: crud('/admin/merchants'),
	tenants: crud('/admin/tenants'),
	plans: crud('/admin/plans'),
	subscriptions: crud('/admin/subscriptions'),
	orders: crud('/admin/orders'),
	authorizations: crud('/admin/authorizations'),
	accountSets: crud('/account-sets'),
	users: crud('/user'),
	auditLogs: crud('/admin/audit-logs')
};
