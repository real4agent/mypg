package com.realaicy.pg.core.repository.hibernate.type;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 将List转换为指定分隔符分隔的字符串存储 List的元素类型只支持常见的数据类型
 * 可参考{@link org.apache.commons.beanutils.ConvertUtilsBean}
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class HashMapToStringUserType implements UserType, ParameterizedType, Serializable {

    /**
     * 默认 java.lang.String
     */
    private Class keyType;

    @Override
    public void setParameterValues(Properties parameters) {
        String keyType = (String) parameters.get("keyType");
        if (!StringUtils.isEmpty(keyType)) {
            try {
                this.keyType = Class.forName(keyType);
            } catch (ClassNotFoundException e) {
                throw new HibernateException(e);
            }
        } else {
            this.keyType = String.class;
        }

    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class returnedClass() {
        return HashMap.class;
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
        String valueStr = rs.getString(names[0]);
        if (StringUtils.isEmpty(valueStr)) {
            return newMap();
        }

        Map map = JSONObject.parseObject(valueStr);
        Map result = newMap();
        try {
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                result.put(keyType.getConstructor(String.class).newInstance(key), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HibernateException(e);
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        String valueStr;
        if (value == null) {
            valueStr = "";
        } else {
            valueStr = JSONObject.toJSONString(value, SerializerFeature.WriteClassName);
        }
        st.setString(index, valueStr);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object deepCopy(Object o) throws HibernateException {
        if (o == null) return null;
        Map copyMap = newMap();
        copyMap.putAll((Map) o);
        return copyMap;
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

    private Map newMap() {
        try {
            return HashMap.class.newInstance();
        } catch (Exception e) {
            throw new HibernateException(e);
        }
    }

}
