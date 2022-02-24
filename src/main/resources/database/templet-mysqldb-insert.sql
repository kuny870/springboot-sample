-- user dump data
INSERT INTO `user` (depart_id, login_id, login_pw, `name`) VALUES ('1', 'dev1','6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', '개발자1');
INSERT INTO `user` (depart_id, login_id, login_pw, `name`) VALUES ('1', 'dev2','6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', '개발자2');

INSERT INTO `user` (login_id, login_pw, `name`) VALUES ('admin1','1234', '최건희');
INSERT INTO `user` (login_id, login_pw, `name`) VALUES ('admin2','1234', '최건희');

-- member dump data
INSERT INTO member (`name`) VALUES ('최건희');
INSERT INTO member (`name`) VALUES ('차민희');
