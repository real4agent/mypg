package com.realaicy.pg.demo.util.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@XmlType(name = "houses")
public class HouseMap {
    @XmlElement(name = "house")
    List<HouseEntry> entries = new ArrayList<HouseEntry>();
}
