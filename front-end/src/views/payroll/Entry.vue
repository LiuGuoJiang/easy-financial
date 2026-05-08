<template><app-content class="h-panel payroll-page"><div class="h-panel-bar"><span class="h-panel-title">工资录入</span></div><div class="h-panel-body"><p>工资录入以员工档案中的基本工资和薪资项目公式为基础，补贴、其他扣款等可在薪资项目中维护公式或固定金额。</p><Employees></Employees></div></app-content></template>
<script>import Employees from './Employees'; export default { name: 'PayrollEntry', components: {Employees} }</script>
