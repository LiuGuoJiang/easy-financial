<template>
  <div class="login-form">
    <div class="form-actions">
      <DropdownCustom placement="top" trigger="hover" :toggleIcon="false">
        <Button size="xs" icon="fa fa-wechat" style="margin-right: 12px">源码咨询</Button>
        <div slot="content">
          <img width="200" src="../../assets/code.jpeg" alt="">
        </div>
      </DropdownCustom>
      <a class="h-btn h-btn-xs" href="https://gitee.com/flyemu/public-financial" target="_blank"> <i class="fa fa-github"></i> gitee</a>
    </div>
    <div class="form-group">
      <input 
        type="text" 
        name="mobile" 
        v-model="login.mobile" 
        autocomplete="off"
        class="text-input"
        placeholder="手机号"
      />
    </div>
    <div class="form-group">
      <input 
        type="password" 
        name="password" 
        v-model="login.password" 
        @keyup.enter="submit" 
        autocomplete="off"
        class="text-input"
        placeholder="密码"
      />
    </div>
    <div class="form-options">
      <span class="forgot-link text-hover" @click="$emit('input','ForgotPassword')">忘记密码？</span>
    </div>
    <div class="form-submit">
      <Button :loading="loading" block color="primary" size="l" @click="submit">登 录</Button>
    </div>
    <div class="form-footer">
      <span>还没有账户？</span>
      <span class="register-link text-hover" @click="$emit('input','Registered')">立即注册</span>
    </div>
  </div>
</template>
<script>
export default {
  name: 'LoginForm',
  data() {
    return {
      login: {
        mobile: "",
        password: "",
      },
      loading: false
    }
  },
  methods: {
    submit() {
      if (this.login.mobile && this.login.password) {
        this.loading = true
        this.$api.common.login(this.login).then(() => {
          window.location.replace('/');
        }).catch(() => {
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

.login-form {
  .form-actions {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 32px;
  }

  .form-group {
    margin-bottom: 20px;
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

  .form-options {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 24px;

    .forgot-link {
      font-size: 13px;
      color: @text-secondary;
      cursor: pointer;
      transition: color 0.2s;

      &:hover {
        color: @brand-blue;
      }
    }
  }

  .form-submit {
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
    margin-top: 24px;
    text-align: center;
    font-size: 14px;
    color: @text-secondary;

    .register-link {
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
