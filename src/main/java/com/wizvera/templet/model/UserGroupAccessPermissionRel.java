//package com.wizvera.templet.model;
//
//import lombok.*;
//
//import javax.persistence.*;
//
///**
// * 관리자 그룹과 접근 권한 정보와의 관계 정보를 가진 도메인 클래스.
// */
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Builder
//@Table(name = "ADMGRP_ACCPERM_REL_TB", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_GROUP_ID",
//		"ACCESS_PERMISSION_ID" }, name = "UK_ADMGRP_ACCPERM_REL_TB_01") })
//public class UserGroupAccessPermissionRel {
//	@Id
//	@Column(name = "ID", length = 20, precision = 20, scale = 0)
//	@SequenceGenerator(name = "AGAPR_ID_GNRTR", sequenceName = "SEQ_AGAPR_01", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGAPR_ID_GNRTR")
////	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	// admin-group 정보
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "USER_GROUP_ID", nullable = false)
//	private UserGroup userGroup;
//
//	// access-permission 정보
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "ACCESS_PERMISSION_ID", nullable = false)
//	private AccessPermission accessPermission;
//}