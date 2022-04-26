//package com.wizvera.templet.model;
//
//import com.wizvera.templet.util.DateUtils;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//
///**
// * 자원에 대한 접근 권한 정보를 가진 도메인 클래스.
// *
// * @author jeehoon.song
// * @since 2022. 3. 14.
// */
//@Entity
//@Table(name = "ACCESS_PERMISSION_TB")
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class AccessPermission {
//	@Id
//	@Column(name = "ID", length = 20, precision = 20, scale = 0)
//	@SequenceGenerator(name = "AP_ID_GNRTR", sequenceName = "SEQ_AP_01", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AP_ID_GNRTR")
//	// @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@Column(name = "ACCESS_PERMISSION", unique = true, length = 512, nullable = false)
//	private String accessPermission;
//
//	@Column(name = "CREATE_DATE", length = 17, nullable = false)
//	private String createDate = DateUtils.dateToString(new Date(), DateUtils.DATE_PATTERN_17);
//
//	@Column(name = "NAME", length = 64, nullable = true)
//	private String name;
//
//	@OneToMany(mappedBy = "accessPermission", fetch = FetchType.EAGER)
//	private Collection<UserGroupAccessPermissionRel> userGroups = new ArrayList<>();
//
//	public void addAdminGroup(UserGroupAccessPermissionRel adminGroupAccessPermissionRel) {
//		adminGroupAccessPermissionRel.setAccessPermission(this);
//		userGroups.add(adminGroupAccessPermissionRel);
//	}
//}