package com.realaicy.pg.demo.util.xml;

/**
 *
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.util.Map;

/**
 * HouseMap中的Entry类.
 */
public class HouseEntry {
    @XmlAttribute
    String key;

    @XmlValue
    String value;

    public HouseEntry() {
    }

    public HouseEntry(Map.Entry<String, String> e) {
        key = e.getKey();
        value = e.getValue();
    }
}
