/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2019 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : </li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2019年08月25日</li>
 * <li>@author     : ____′↘夏悸</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
import Ajax from '../js/common/ajax';

export default {
	template: {
		list(params = {}) {
			return Ajax.get("/report/template", params)
		},
		enabled() {
			return Ajax.get("/report/template/enabled")
		},
		load(id) {
			return Ajax.get(`/report/template/${id}`)
		},
		delete(id) {
			return Ajax.delete(`/report/template/${id}`)
		},
		save(params = {}) {
			return Ajax.post(`/report/template`, params)
		},
		update(params = {}) {
			return Ajax.put(`/report/template`, params)
		},
		copy(id) {
			return Ajax.post(`/report/template/copy/${id}`)
		},
		publish(id, enabled) {
			return Ajax.post(`/report/template/publish/${id}`, {enabled})
		},
		validate(id) {
			return Ajax.get(`/report/template/validate/${id}`)
		},
		preview(id, params = {}) {
			return Ajax.get(`/report/template/preview/${id}`, params)
		},
		items: {
			save(params = {}) {
				return Ajax.post(`/report/template/items`, params)
			},
			update(params = {}) {
				return Ajax.put(`/report/template/items`, params)
			},
			delete(id) {
				return Ajax.delete(`/report/template/items/${id}`)
			},
			formula(params) {
				return Ajax.post(`/report/template/items/formula`, params)
			},
			validateFormula(params) {
				return Ajax.post(`/report/template/items/formula/validate`, params)
			}
		}
	},
	view(id, params = {}) {
		return Ajax.get(`/report/template/view/${id}`, params)
	}
}