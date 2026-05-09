package cn.gson.financial.kernel.common;

/**
 * 平台级后台权限角色。
 */
public enum PlatformRoles {
    PlatformAdmin("平台管理员"),
    MerchantAdmin("商户管理员"),
    AccountSetAdmin("账套管理员"),
    VoucherMaker("制单人"),
    Viewer("查看人");

    public String display;

    PlatformRoles(String display) {
        this.display = display;
    }
}
