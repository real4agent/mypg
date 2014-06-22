package com.realaicy.pg.demo.util.xml;

import com.google.common.collect.Maps;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Map;

/**
 * 为使Map<String,String> houses转化为有业务意义的xml的巨大努力,
 * 分别定义了一个Adapter--HouseMapAdapter, 一个List<HouseEntry> Wrapper类--HouseMap, 一个MapEntry表达类--HouseEntry.
 * 最后的劳动成果是：
 * <p/>
 * <pre>
 * <houses>
 * 		<house key="bj">house1</item>
 * 		<hosue key="gz">house2</item>
 * </houses>
 * </pre>
 * <p/>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class HouseMapAdapter extends XmlAdapter<HouseMap, Map<String, String>> {

    @Override
    public HouseMap marshal(Map<String, String> map) throws Exception {
        HouseMap houseMap = new HouseMap();
        for (Map.Entry<String, String> e : map.entrySet()) {
            houseMap.entries.add(new HouseEntry(e));
        }
        return houseMap;
    }

    @Override
    public Map<String, String> unmarshal(HouseMap houseMap) throws Exception {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (HouseEntry e : houseMap.entries) {
            map.put(e.key, e.value);
        }
        return map;
    }

}