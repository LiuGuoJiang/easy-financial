<template>
	<div class="forgot-form">
		<div class="form-group">
			<input 
				type="text" 
				name="mobile" 
				v-model="form.mobile" 
				autocomplete="off"
				class="text-input"
				placeholder="输入手机号，快速找回密码"
			/>
		</div>
		<div class="form-group code-group">
			<input 
				v-model="form.code" 
				type="text" 
				maxlength="4"
				class="text-input"
				placeholder="验证码"
			/>
			<div class="sms-btn">
				<SmsVerificationCode :mobile="form.mobile"/>
			</div>
		</div>
		<div class="form-group">
			<input 
				type="password" 
				v-model="form.newPassword" 
				autocomplete="off"
				class="text-input"
				placeholder="新密码"
			/>
		</div>
		<div class="form-group">
			<input 
				type="password" 
				v-model="form.repeatPassword" 
				autocomplete="off"
				class="text-input"
				placeholder="确认密码"
			/>
		</div>
		<div class="form-submit">
			<Button :loading="loading" block color="primary" size="l" @click="regSubmit">修改密码</Button>
		</div>
		<div class="form-footer">
			<span class="back-link text-hover" @click="$emit('input','LoginForm')">返回登录</span>
		</div>
	</div>
</template>
<script>
	import SmsVerificationCode from "../../components/SmsVerificationCode"

	export default {
		name: 'ForgotPassword',
		components: {SmsVerificationCode},
		data() {
			return {
				form: {
					mobile: "",
					code: "",
					newPassword: "",
					repeatPassword: "",
				},
				loading: false
			};
		},
		computed: {
			isValid() {
				return Object.keys(this.form).every(key => !!this.form[key]) && this.form.newPassword === this.form.repeatPassword;
			}
		},
		methods: {
			regSubmit() {
				if (this.isValid) {
					this.loading = true;
					this.$api.common.resetPassword(this.form).then(() => {
						this.$Message("密码重置成功！");
						this.$emit('input', 'LoginForm');
					}).finally(() => {
						this.loading = false;
					});
				}
			}
		}
	}
</script>
<style lang="less">
@brand-blue: #3788ee;
@text-primary: #1a1a1a;
@text-secondary: #999999;
@border-color: #e5e5e5;

.forgot-form {
	.form-group {
		margin-bottom: 20px;

		&.code-group {
			position: relative;
		}
	}

	.text-input {
		width: 100%;
		height: 48px;
		border: 1px solid @border-color;
		border-radius: 8px;
		padding: 0 16px;
		font-size: 15px;
		color: @text-primary;
		outline: none;
		transition: all 0.3s ease;
		box-sizing: border-box;
		background: #fff;

		&::placeholder {
			color: @text-secondary;
		}

		&:focus {
			border-color: @brand-blue;
			box-shadow: 0 0 0 3px rgba(55, 136, 238, 0.1);
		}
	}

	.code-group .text-input {
		padding-right: 100px;
	}

	.sms-btn {
		position: absolute;
		right: 8px;
		top: 50%;
		transform: translateY(-50%);
	}

	.form-submit {
		margin-top: 24px;

		.h-btn {
			height: 48px;
			font-size: 16px;
			font-weight: 500;
			letter-spacing: 2px;
			border-radius: 8px;
			background: @brand-blue;
			border-color: @brand-blue;

			&:hover {
				opacity: 0.9;
				background: @brand-blue;
				border-color: @brand-blue;
			}

			&:active {
				transform: scale(0.98);
			}
		}
	}

	.form-footer {
		margin-top: 20px;
		text-align: center;

		.back-link {
			font-size: 14px;
			color: @text-secondary;
			cursor: pointer;

			&:hover {
				color: @brand-blue;
			}
		}
	}
}
</style>
