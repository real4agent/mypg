package com.realaicy.pg.core.repository.hibernate.type;

import com.alibaba.fastjson.JSON;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Json字符串---->Map
 * Map----->Json字符串
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class JsonMapUserType implements UserType, Serializable {

//    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
//        objectMapper.enableDefaultTyping();
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class returnedClass() {
        return JsonMap.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return o == o1 || !(o == null || o1 == null) && o.equals(o1);

    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String json = rs.getString(names[0]);
//        try {
//            Map<Object, Object> map = objectMapper.readValue(json, HashMap.class);
//            return new JsonMap(map);
//        } catch (IOException e) {
//            throw new HibernateException(e);
//        }
        Map<Object, Object> map = JSON.parseObject(json, HashMap.class);
        return new JsonMap(map);
    }

    /**
     * 本方法将在Hibernate进行数据保存时被调用
     * 我们可以通过PreparedStateme将自定义数据写入到对应的数据库表字段
     */
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
//            try {
//                st.setString(index, objectMapper.writeValueAsString((((JsonMap) value).getMap())));
//            } catch (JsonProcessingException e) {
//                throw new HibernateException(e);
//            }
            st.setString(index, JSON.toJSONString((((JsonMap) value).getMap())));
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        if (o == null) return null;
        JsonMap map = new JsonMap();
        map.setMap(((JsonMap) o).getMap());
        return map;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    /* 序列化 */
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return ((Serializable) value);
    }

    /* 反序列化 */
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
