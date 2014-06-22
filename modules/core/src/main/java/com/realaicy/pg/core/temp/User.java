package com.realaicy.pg.core.temp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 使用JAXB2.0标注的待转换Java Bean.
 */
// 根节点
@XmlRootElement
// 指定子节点的顺序
@XmlType(propOrder = { "name", "roles", "interests", "houses" })
public class User {

	private Long id;
	private String name;
	private String password;

	private List<Role> roles = Lists.newArrayList();
	private List<String> interests = Lists.newArrayList();
	private Map<String, String> houses = Maps.newHashMap();

	// 设置转换为xml节点中的属性
	@XmlAttribute
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// 设置不转换为xml
	@XmlTransient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 设置对List<Object>的映射, xml为<roles><role id="1" name="admin"/></roles>
	@XmlElementWrapper
	@XmlElement(name = "role")
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	// 设置对List<String>的映射, xml为<interests><interest>movie</interest></interests>
	@XmlElementWrapper
	@XmlElement(name = "interest")
	public List<String> getInterests() {
		return interests;
	}

	public void setInterests(List<String> interests) {
		this.interests = interests;
	}


	public Map<String, String> getHouses() {
		return houses;
	}

	public void setHouses(Map<String, String> houses) {
		this.houses = houses;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}