//package com.wizvera.templet.model;
//
//import com.wizvera.templet.util.DateUtils;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.OrderBy;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//
////@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity
//@Table(name = "USER_GROUP_TB", uniqueConstraints = {
//		@UniqueConstraint(columnNames = { "GROUP_NAME" }, name = "UK_USER_GROUP_TB_01"),
//		@UniqueConstraint(columnNames = { "ROLE" }, name = "UK_USER_GROUP_TB_02") })
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class UserGroup {
//	@Id
//	@Column(name = "ID", length = 20, precision = 20, scale = 0)
//	@SequenceGenerator(name = "ADMGRP_ID_GNRTR", sequenceName = "SEQ_ADMGRP_01", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADMGRP_ID_GNRTR")
////	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@Column(name = "GROUP_NAME", unique = true, length = 256, nullable = false)
//	private String groupName;
//
//	@Column(name = "CREATE_DATE", length = 17, nullable = false)
//	private String createDate = DateUtils.dateToString(new Date(), DateUtils.DATE_PATTERN_17);
//
//	@Column(name = "DESCRIPTION", length = 512, nullable = true)
//	private String description;
//
//	@Column(name = "ROLE", length = 64, unique = true, nullable = false)
//	private String accessRole;
//
//	/**
//	 * Create, Read, Update, Delete 실행 권한 정보를 나타냄.
//	 *
//	 * <pre>
//	 * Read:1, Create:2, Update:4, Delete:8 의 값을 가지며, 각 값에 대한 XOR 연산된 값으로 권한들의 정보를 얻을 수 있음
//	 * - ex> Read/Create/Update : 7
//	 * </pre>
//	 */
//	@Column(name = "ACTION_PERMISSION", length = 2, nullable = false)
//	private String actionPermission;
//
//	// admin_group가 가진 admin 리스트
//	@OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@OrderBy(clause = "id ASC")
//	private Collection<User> users = new ArrayList<User>(0);
//
//	// @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//	// @JoinTable(name = "ADMGRP_ACCPERM_REL_TB", joinColumns = @JoinColumn(name = "admin_group_id",
//	// referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "admin_permission_id", referencedColumnName
//	// = "id"))
//	// @JsonIgnore
//	// private Collection<AccessPermission> accessPermissions;
//
//	@OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@OrderBy(clause = "id ASC")
//	private Collection<UserGroupAccessPermissionRel> accessPermissions = new ArrayList<>();
//
//	public void addAccessPermission(UserGroupAccessPermissionRel userGroupAccessPermissionRel) {
//		userGroupAccessPermissionRel.setUserGroup(this);
//		accessPermissions.add(userGroupAccessPermissionRel);
//	}
//
//	public void addAdmin(User user) {
//		users.add(user);
//	}
//}