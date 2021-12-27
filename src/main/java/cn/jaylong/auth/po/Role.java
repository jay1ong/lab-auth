package cn.jaylong.auth.po;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/10/15
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "role", schema = "auth")
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = Role.EG_DEFAULT)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Role.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Role implements Serializable {

    public static final String EG_DEFAULT = "Role.default";
    private static final long serialVersionUID = -644676274808089433L;

    @Id
    @Column(length = 64)
    private String id;

    /**
     * 编码
     */
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    /**
     * 描述
     */
    @Column
    private String description;

}
