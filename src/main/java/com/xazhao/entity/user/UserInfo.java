package com.xazhao.entity.user;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @ClassName UserInfo.java
 * @Author AnZhaoxu
 * @Create 2024.03.27
 * @UpdateUser
 * @UpdateDate 2024.03.27
 * @Version 2024.0.1
 * @Description
 */

@Data
@Table(name = "sys_user_info")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1257006167843555028L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "team_code")
    private String teamCode;
    @Column(name = "com_code")
    private String comCode;

    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "password")
    private String password;

    @Column(name = "id_card_no")
    private String idCardNo;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "bank_info")
    private String bankInfo;

    @Column(name = "use_flag")
    private String useFlag;

    @Column(name = "salt")
    private String salt;

    @Column(name = "index_user")
    private String indexUser;

    @Column(name = "duty_block")
    private String dutyBlock;

    @Column(name = "remark")
    private String remark;

    @Column(name = "created_time")
    private String createdTime;

    /**
     * 用户首次登录
     */
    @Column(name = "first_login")
    private Long firstLogin;

    /**
     * 登录错误次数
     */
    @Column(name = "login_err_time")
    private long loginErrTime;

    /**
     * 密码修改时间
     */
    @Column(name = "pass_change_time")
    private String passChangeTime;

    /**
     * 锁定状态
     */
    @Column(name = "state")
    private String state;

    /**
     * 验证码
     */
    @Column(name = "v_code")
    private String vCode;

    private String token;

    private Long roleId;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public UserInfo(String userCode, String password) {
        checkUserAccount(userCode);
        this.salt = UUID.randomUUID().toString();
        this.password = password;
        this.userCode = userCode;
    }

    private void checkUserAccount(String userAccount) {
        Assert.notBlank(userAccount, "用户代码不能为空.");
    }

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(16);
        }
        return params;
    }
}
