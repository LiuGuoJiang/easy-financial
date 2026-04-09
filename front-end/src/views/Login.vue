<template>
	<div class="login-wrapper">
		<div class="login-brand">
			<div class="brand-bg-effects">
				<div class="grid-pattern"></div>
				<div class="gradient-overlay"></div>
				<div class="glow-orb orb-1"></div>
				<div class="glow-orb orb-2"></div>
			</div>
			<div class="brand-content">
				<div class="brand-main">
					<div class="brand-header">
						<div class="brand-logo">
							<img src="../assets/logo.png" alt="纷析云">
						</div>
						<div class="brand-titles">
							<h1 class="brand-name">纷析云财税</h1>
							<p class="brand-slogan">智能财税管理平台</p>
						</div>
					</div>
					<div class="brand-features">
						<div class="feature-item">
							<div class="feature-icon"><i class="fa fa-shield"></i></div>
							<span class="feature-title">数据安全</span>
						</div>
						<div class="feature-item">
							<div class="feature-icon"><i class="fa fa-bolt"></i></div>
							<span class="feature-title">高效智能</span>
						</div>
						<div class="feature-item">
							<div class="feature-icon"><i class="fa fa-cloud"></i></div>
							<span class="feature-title">云端同步</span>
						</div>
					</div>
				</div>
				<div class="brand-stats">
					<div class="stat-item">
						<span class="stat-number">10万+</span>
						<span class="stat-label">企业用户</span>
					</div>
					<div class="stat-divider"></div>
					<div class="stat-item">
						<span class="stat-number">99.9%</span>
						<span class="stat-label">服务可用性</span>
					</div>
					<div class="stat-divider"></div>
					<div class="stat-item">
						<span class="stat-number">7×24</span>
						<span class="stat-label">专属客服</span>
					</div>
				</div>
			</div>
		</div>
		<div class="login-form-wrapper">
			<div class="login-form-container">
				<div class="login-header">
					<h2>欢迎回来</h2>
					<p>请登录您的账户继续</p>
				</div>
				<component v-model="currentComponent" :is="currentComponent"></component>
				<p class="copyright"> Copyright © 2019 <a href="https://doc.fenxi365.com" target="_blank">纷析云（杭州）科技有限公司 </a></p>
			</div>
		</div>
	</div>
</template>
<script>
	import ForgotPassword from "./login/ForgotPassword";
	import Registered from "./login/Registered";
	import LoginForm from "./login/LoginForm";

	export default {
		name: 'Login',
		components: {LoginForm, Registered, ForgotPassword},
		data() {
			return {
				currentComponent: 'LoginForm'
			};
		}
	};
</script>
<style lang="less">
	@brand-blue: #3788ee;
	@brand-blue-dark: #1a5fc9;
	@brand-blue-light: #5a9cf5;
	@text-primary: #1a1a1a;
	@text-secondary: #666666;
	@border-color: #e5e5e5;

	.login-wrapper {
		display: flex;
		min-height: 100vh;
		background: #fff;
	}

	.login-brand {
		flex: 1;
		background: linear-gradient(160deg, @brand-blue-dark 0%, @brand-blue 60%, @brand-blue-light 100%);
		position: relative;
		overflow: hidden;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 60px;
	}

	.brand-bg-effects {
		position: absolute;
		inset: 0;
		pointer-events: none;
		overflow: hidden;

		.grid-pattern {
			position: absolute;
			inset: 0;
			background-image: 
				linear-gradient(rgba(255,255,255,0.025) 1px, transparent 1px),
				linear-gradient(90deg, rgba(255,255,255,0.025) 1px, transparent 1px);
			background-size: 50px 50px;
		}

		.gradient-overlay {
			position: absolute;
			inset: 0;
			background: 
				radial-gradient(ellipse at 30% 20%, rgba(90, 156, 245, 0.45) 0%, transparent 50%),
				radial-gradient(ellipse at 70% 80%, rgba(26, 95, 201, 0.35) 0%, transparent 50%);
		}

		.glow-orb {
			position: absolute;
			border-radius: 50%;
			filter: blur(80px);
			opacity: 0.6;

			&.orb-1 {
				width: 350px;
				height: 350px;
				background: radial-gradient(circle, rgba(90, 156, 245, 0.5) 0%, transparent 70%);
				top: -120px;
				right: -80px;
				animation: float-orb 12s ease-in-out infinite;
			}

			&.orb-2 {
				width: 280px;
				height: 280px;
				background: radial-gradient(circle, rgba(255, 255, 255, 0.12) 0%, transparent 70%);
				bottom: 25%;
				left: -80px;
				animation: float-orb 9s ease-in-out infinite 3s;
			}
		}
	}

	.brand-content {
		position: relative;
		z-index: 2;
		width: 100%;
		max-width: 480px;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 48px;
	}

	.brand-main {
		display: flex;
		flex-direction: column;
		align-items: center;
		text-align: center;
		width: 100%;
	}

	.brand-header {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 24px;
		margin-bottom: 48px;
	}

	.brand-logo {
		width: 200px;
		height: auto;

		img {
			width: 100%;
			height: auto;
			object-fit: contain;
			filter: brightness(0) invert(1);
		}
	}

	.brand-titles {
		.brand-name {
			font-size: 36px;
			font-weight: 700;
			color: #fff;
			margin: 0 0 12px;
			letter-spacing: 4px;
		}

		.brand-slogan {
			font-size: 16px;
			color: rgba(255, 255, 255, 0.85);
			margin: 0;
			letter-spacing: 2px;
			font-weight: 300;
		}
	}

	.brand-features {
		display: flex;
		flex-direction: row;
		gap: 32px;
		justify-content: center;

		.feature-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			gap: 12px;
			animation: fadeIn 0.8s ease-out backwards;

			&:nth-child(1) { animation-delay: 0.25s; }
			&:nth-child(2) { animation-delay: 0.45s; }
			&:nth-child(3) { animation-delay: 0.65s; }

			.feature-icon {
				width: 56px;
				height: 56px;
				background: rgba(255,255,255,0.1);
				border-radius: 14px;
				display: flex;
				align-items: center;
				justify-content: center;
				backdrop-filter: blur(8px);
				border: 1px solid rgba(255,255,255,0.15);

				i {
					font-size: 24px;
					color: #fff;
				}
			}

			.feature-title {
				font-size: 14px;
				font-weight: 600;
				color: #fff;
			}
		}
	}

	.brand-stats {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 40px;
		padding: 24px 40px;
		background: rgba(255,255,255,0.08);
		border-radius: 16px;
		backdrop-filter: blur(20px);
		border: 1px solid rgba(255,255,255,0.12);
		animation: fadeInUp 0.8s ease-out 0.5s backwards;

		.stat-item {
			text-align: center;

			.stat-number {
				display: block;
				font-size: 24px;
				font-weight: 700;
				color: #fff;
				margin-bottom: 4px;
			}

			.stat-label {
				display: block;
				font-size: 12px;
				color: rgba(255,255,255,0.8);
				letter-spacing: 1px;
			}
		}

		.stat-divider {
			width: 1px;
			height: 36px;
			background: rgba(255,255,255,0.2);
		}
	}

	.login-form-wrapper {
		width: 520px;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 60px 40px;
		background: #fff;
	}

	.login-form-container {
		width: 100%;
		max-width: 360px;
		animation: fadeInUp 0.8s ease-out 0.2s backwards;

		.login-header {
			margin-bottom: 48px;
			text-align: center;

			h2 {
				font-size: 28px;
				font-weight: 600;
				color: @text-primary;
				margin: 0 0 8px;
				letter-spacing: 1px;
			}

			p {
				font-size: 14px;
				color: @text-secondary;
				margin: 0;
			}
		}

		.copyright {
			text-align: center;
			margin-top: 48px;
			font-size: 12px;
			color: @text-secondary;

			a {
				color: @text-secondary;
				text-decoration: none;

				&:hover {
					color: @brand-blue;
				}
			}
		}
	}

	@keyframes fadeInUp {
		from {
			opacity: 0;
			transform: translateY(30px);
		}
		to {
			opacity: 1;
			transform: translateY(0);
		}
	}

	@keyframes fadeIn {
		from { opacity: 0; }
		to { opacity: 1; }
	}

	@keyframes float-orb {
		0%, 100% { transform: translate(0, 0); }
		50% { transform: translate(12px, -12px); }
	}

	@media (max-width: 1024px) {
		.login-brand {
			padding: 40px 30px;
		}
	}

	@media (max-width: 768px) {
		.login-wrapper {
			flex-direction: column;
		}

		.login-brand {
			flex: none;
			min-height: auto;
			padding: 40px 24px;

			.brand-content {
				max-width: 100%;
				gap: 32px;
			}

			.brand-header {
				margin-bottom: 32px;
			}

			.brand-logo {
				width: 140px;
			}

			.brand-titles {
				.brand-name {
					font-size: 28px;
				}

				.brand-slogan {
					font-size: 14px;
				}
			}

			.brand-features {
				gap: 24px;

				.feature-item {
					.feature-icon {
						width: 48px;
						height: 48px;

						i {
							font-size: 20px;
						}
					}

					.feature-title {
						font-size: 13px;
					}
				}
			}

			.brand-stats {
				padding: 20px 24px;
				gap: 24px;

				.stat-item {
					.stat-number {
						font-size: 20px;
					}

					.stat-label {
						font-size: 11px;
					}
				}
			}
		}

		.login-form-wrapper {
			width: 100%;
			padding: 40px 24px;
		}
	}
</style>
