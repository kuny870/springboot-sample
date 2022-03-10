-- safe 모드 해제
SET SQL_SAFE_UPDATES = 0;

create database templet;

-- table 삭제
drop table `user`;


-- -----------------------------------------------------
-- Table `user` 사용자
-- -----------------------------------------------------
CREATE TABLE user (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,     -- 고유번호
  `login_id` VARCHAR(50) NOT NULL,          -- 아이디
  `login_pw` VARCHAR(100) NOT NULL,         -- 비밀번호
  `name` VARCHAR(50) NOT NULL,              -- 이름
  `del_yn` VARCHAR(10) NULL DEFAULT 'N',            -- 탈퇴여부
  PRIMARY KEY (`id`)
);

