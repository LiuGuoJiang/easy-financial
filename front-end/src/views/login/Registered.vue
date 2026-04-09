<template>
  <div class="register-form">
    <div class="form-group">
      <input 
        type="text" 
        name="mobile" 
        v-model="reg.mobile" 
        autocomplete="off"
        class="text-input"
        placeholder="输入手机号"
      />
    </div>
    <div class="form-group">
      <input 
        type="text" 
        name="email" 
        v-model="reg.email" 
        autocomplete="off"
        class="text-input"
        placeholder="输入邮箱"
      />
    </div>
    <div class="form-group">
      <input 
        type="password" 
        name="password" 
        v-model="reg.password" 
        autocomplete="off"
        class="text-input"
        placeholder="输入密码"
      />
    </div>
    <div class="form-group code-group">
      <input 
        v-model="reg.code" 
        type="text" 
        maxlength="4"
        class="text-input"
        placeholder="邮箱验证码"
      />
      <div class="sms-btn">
        <SmsVerificationCode api="emailMsg" :email="reg.email" :mobile="reg.mobile"/>
      </div>
    </div>
    <div class="form-submit">
      <Button :loading="loading" block color="primary" size="l" @click="regSubmit">注 册</Button>
    </div>
    <div class="form-footer">
      <span>已有账户？</span>
      <span class="back-link text-hover" @click="$emit('input','LoginForm')">返回登录</span>
    </div>
  </div>
</template>
<script>
import SmsVerificationCode from "../../components/SmsVerificationCode"

export default {
  name: 'Registered',
  components: {SmsVerificationCode},
  data() {
    return {
      reg: {
        mobile: "",
        email: "",
        password: "",
        code: "",
      },
      loading: false
    };
  },
  methods: {
    regSubmit() {
      if (this.reg.mobile && this.reg.email && this.reg.password && this.reg.code) {
        this.loading = true;
        this.$api.common.register(this.reg).then(() => {
          window.location.replace('/');
        }).finally(() => {
          this.loading = false
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

.register-form {
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
    font-size: 14px;
    color: @text-secondary;

    .back-link {
      color: @brand-blue;
      margin-left: 4px;
      cursor: pointer;
      font-weight: 500;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>
