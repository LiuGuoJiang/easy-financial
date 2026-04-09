<template>
	<div class="login-wrapper">
		<div class="login-brand">
			<div class="brand-bg-effects">
				<!-- 网格背景 -->
				<div class="grid-pattern"></div>
				<!-- 渐变光晕 -->
				<div class="gradient-overlay"></div>
				<!-- 大光球 -->
				<div class="glow-orb orb-1"></div>
				<div class="glow-orb orb-2"></div>
				<div class="glow-orb orb-3"></div>
				<div class="glow-orb orb-4"></div>
				<!-- 浮动几何图形 -->
				<div class="floating-shape shape-1"></div>
				<div class="floating-shape shape-2"></div>
				<div class="floating-shape shape-3"></div>
				<div class="floating-shape shape-4"></div>
				<!-- 粒子 -->
				<div class="particle particle-1"></div>
				<div class="particle particle-2"></div>
				<div class="particle particle-3"></div>
				<div class="particle particle-4"></div>
				<div class="particle particle-5"></div>
				<div class="particle particle-6"></div>
				<div class="particle particle-7"></div>
				<div class="particle particle-8"></div>
				<!-- 流动线条 -->
				<div class="flow-line line-1"></div>
				<div class="flow-line line-2"></div>
				<div class="flow-line line-3"></div>
			</div>
			<div class="brand-content">
				<div class="brand-main">
					<div class="brand-header">
						<div class="brand-logo">
							<img src="../assets/logo.png" alt="纷析云">
						</div>
						<div class="brand-titles">
							<h1 class="brand-name">纷析云财税</h1>
							<p class="brand-slogan">让每个决策都有数据支撑，让记账更轻松，更智能</p>
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
			animation: grid-move 20s linear infinite;
		}

		.gradient-overlay {
			position: absolute;
			inset: 0;
			background: 
				radial-gradient(ellipse at 30% 20%, rgba(90, 156, 245, 0.45) 0%, transparent 50%),
				radial-gradient(ellipse at 70% 80%, rgba(26, 95, 201, 0.35) 0%, transparent 50%);
			animation: gradient-pulse 8s ease-in-out infinite;
		}

		.glow-orb {
			position: absolute;
			border-radius: 50%;
			filter: blur(80px);

			&.orb-1 {
				width: 400px;
				height: 400px;
				background: radial-gradient(circle, rgba(90, 156, 245, 0.6) 0%, transparent 70%);
				top: -150px;
				right: -100px;
				animation: orb-float-1 15s ease-in-out infinite;
			}

			&.orb-2 {
				width: 300px;
				height: 300px;
				background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, transparent 70%);
				bottom: 30%;
				left: -100px;
				animation: orb-float-2 12s ease-in-out infinite;
			}

			&.orb-3 {
				width: 250px;
				height: 250px;
				background: radial-gradient(circle, rgba(90, 156, 245, 0.5) 0%, transparent 70%);
				bottom: -80px;
				right: 20%;
				animation: orb-float-3 18s ease-in-out infinite;
			}

			&.orb-4 {
				width: 200px;
				height: 200px;
				background: radial-gradient(circle, rgba(255, 255, 255, 0.15) 0%, transparent 70%);
				top: 40%;
				right: 10%;
				animation: orb-float-4 10s ease-in-out infinite;
			}
		}

		.floating-shape {
			position: absolute;
			border: 2px solid rgba(255,255,255,0.1);
			border-radius: 50%;

			&.shape-1 {
				width: 60px;
				height: 60px;
				top: 15%;
				left: 10%;
				animation: shape-float 20s ease-in-out infinite;
			}

			&.shape-2 {
				width: 40px;
				height: 40px;
				top: 60%;
				right: 15%;
				border-radius: 8px;
				transform: rotate(45deg);
				animation: shape-float 15s ease-in-out infinite reverse;
			}

			&.shape-3 {
				width: 80px;
				height: 80px;
				bottom: 20%;
				left: 20%;
				border: 1px solid rgba(255,255,255,0.08);
				animation: shape-float 25s ease-in-out infinite;
			}

			&.shape-4 {
				width: 30px;
				height: 30px;
				top: 30%;
				right: 25%;
				border-radius: 4px;
				transform: rotate(15deg);
				animation: shape-float 18s ease-in-out infinite reverse;
			}
		}

		.particle {
			position: absolute;
			width: 4px;
			height: 4px;
			background: rgba(255,255,255,0.6);
			border-radius: 50%;

			&.particle-1 {
				top: 20%;
				left: 30%;
				animation: particle-float 10s ease-in-out infinite;
			}

			&.particle-2 {
				top: 40%;
				left: 70%;
				animation: particle-float 12s ease-in-out infinite 1s;
			}

			&.particle-3 {
				top: 60%;
				left: 20%;
				animation: particle-float 8s ease-in-out infinite 2s;
			}

			&.particle-4 {
				top: 80%;
				left: 60%;
				animation: particle-float 14s ease-in-out infinite 0.5s;
			}

			&.particle-5 {
				top: 25%;
				left: 85%;
				animation: particle-float 11s ease-in-out infinite 1.5s;
			}

			&.particle-6 {
				top: 70%;
				left: 40%;
				animation: particle-float 9s ease-in-out infinite 3s;
			}

			&.particle-7 {
				top: 10%;
				left: 50%;
				animation: particle-float 13s ease-in-out infinite 2.5s;
			}

			&.particle-8 {
				top: 50%;
				left: 90%;
				animation: particle-float 10s ease-in-out infinite 4s;
			}
		}

		.flow-line {
			position: absolute;
			background: linear-gradient(90deg, transparent, rgba(255,255,255,0.1), transparent);
			height: 1px;

			&.line-1 {
				width: 200px;
				top: 25%;
				left: 0;
				animation: flow-right 8s ease-in-out infinite;
			}

			&.line-2 {
				width: 150px;
				top: 55%;
				left: 0;
				animation: flow-right 10s ease-in-out infinite 2s;
			}

			&.line-3 {
				width: 180px;
				top: 75%;
				left: 0;
				animation: flow-right 9s ease-in-out infinite 4s;
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
		width: 100%;
	}

	.brand-header {
		display: flex;
		flex-direction: column;
		margin-bottom: 48px;
	}

	.brand-logo {
		width: 130px;
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
    width: 100%;
		display: flex;
		flex-direction: row;
		gap: 32px;
		justify-content: space-evenly;

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

	/* 动画定义 */
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

	@keyframes grid-move {
		0% { transform: translate(0, 0); }
		100% { transform: translate(50px, 50px); }
	}

	@keyframes gradient-pulse {
		0%, 100% { opacity: 1; }
		50% { opacity: 0.7; }
	}

	@keyframes orb-float-1 {
		0%, 100% { transform: translate(0, 0) scale(1); }
		25% { transform: translate(30px, -20px) scale(1.1); }
		50% { transform: translate(0, -40px) scale(1); }
		75% { transform: translate(-30px, -20px) scale(0.9); }
	}

	@keyframes orb-float-2 {
		0%, 100% { transform: translate(0, 0) scale(1); }
		33% { transform: translate(20px, 30px) scale(1.05); }
		66% { transform: translate(-20px, 15px) scale(0.95); }
	}

	@keyframes orb-float-3 {
		0%, 100% { transform: translate(0, 0) scale(1); }
		50% { transform: translate(25px, -25px) scale(1.08); }
	}

	@keyframes orb-float-4 {
		0%, 100% { transform: translate(0, 0) scale(1); }
		40% { transform: translate(-15px, 20px) scale(1.05); }
		80% { transform: translate(15px, -10px) scale(0.95); }
	}

	@keyframes shape-float {
		0%, 100% { 
			transform: translate(0, 0) rotate(0deg); 
		}
		33% { 
			transform: translate(15px, -15px) rotate(120deg); 
		}
		66% { 
			transform: translate(-10px, 10px) rotate(240deg); 
		}
	}

	@keyframes particle-float {
		0%, 100% { 
			opacity: 0;
			transform: translate(0, 0);
		}
		10% { 
			opacity: 0.8;
		}
		90% { 
			opacity: 0.8;
		}
		50% { 
			transform: translate(30px, -50px);
		}
	}

	@keyframes flow-right {
		0% { 
			transform: translateX(-100%);
			opacity: 0;
		}
		10% { 
			opacity: 1;
		}
		90% { 
			opacity: 1;
		}
		100% { 
			transform: translateX(calc(100vw));
			opacity: 0;
		}
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
