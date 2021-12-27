package cn.jaylong.auth.po;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cn.jaylong.auth.enums.StatusEnum;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/23
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "user", schema = "auth", uniqueConstraints = {
        // 用户域下的唯一键
        @UniqueConstraint(name = "uk_zone_username", columnNames = {"zoneId", "username"}),
        @UniqueConstraint(name = "uk_zone_email", columnNames = {"zoneId", "email"}),
        @UniqueConstraint(name = "uk_zone_phone", columnNames = {"zoneId", "phone_number"}),
})
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = User.EG_DEFAULT)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = User.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class User implements Serializable {

    public static final String EG_DEFAULT = "User.default";

    private static final long serialVersionUID = -3070795154220615014L;

    /**
     * 用户ID
     */
    @Id
    @Column(length = 64)
    private String id;

    /**
     * 用户域ID
     */
    @Column(length = 64, nullable = false)
    private String zoneId;

    /**
     * 外部ID
     */
    @Column
    private String externalId;

    /**
     * 使用第三方身份源或社会化登录的用户，该字段为用户在第三方的 ID。
     */
    @Column
    private String unionId;

    /**
     * 第三方身份源用户，返回的 openid。
     */
    @Column
    private String openId;
    // endregion

    /**
     * 用户资源描述符
     */
    @Column(length = 128)
    private String irn;

    /**
     * 密码
     */
    @Column
    private String password;

    /**
     * 账户是否过期
     */
    @Column
    boolean accountNonExpired = true;

    /**
     * 账户是否锁定
     */
    @Column
    boolean accountNonLocked = true;

    /**
     * 凭证是否过期
     */
    @Column
    boolean credentialsNonExpired = true;

    /**
     * 用户是否可用
     */
    @Column
    boolean enabled = true;

    /**
     * 昵称
     */
    @Column
    private String nickname;

    /**
     * 用户所属公司
     */
    @Column
    private String company;

    /**
     * 头像地址
     */
    @Column(length = 2048)
    private String avatar;

    // region 兼容OIDC 用户信息，抽离一些信息出来方便操作
    /**
     * 登录账号
     */
    @Column(length = 64)
    private String username;

    /**
     * 邮箱
     */
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "email")),
            @AttributeOverride(name = "verified", column = @Column(nullable = false, name = "email_verified"))})
    private VerifierEmbed email;

    /**
     * 手机号码
     */
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "phone_number")),
            @AttributeOverride(name = "verified", column = @Column(nullable = false, name = "phone_number_verified"))})
    private VerifierEmbed phoneNumber;

    /**
     * 地址(json)
     */
    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private AddressData address;

    // endregion

    /**
     * 注册来源，可以多选（导入、来自XX应用注册）
     */
    @Column
    private String source;

    /**
     * 用户状态
     */
    @Column
    private StatusEnum status = StatusEnum.NORMAL;

    /**
     * 注册信息
     */
    @Embedded
    private RegisterInfoEmbed registerInfo;

    /**
     * 最近登录信息
     */
    @Embedded
    private LastLoginInfoEmbed lastLoginInfo;

    // region DB
    /**
     * 创建时间
     */
    @Column
    @CreatedDate
    private LocalDateTime createAt;

    @Column
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            schema = "auth",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
