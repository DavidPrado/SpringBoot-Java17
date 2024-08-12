insert into USUARIOS (id,username,password,role) values (100, 'ana@email.com','$2a$10$HlKPmzJhOQyK/lvRcS0HR.as6UC6iIp2SQEplV3lhagvMMzDnaWu2','ROLE_ADMIN');
insert into USUARIOS (id,username,password,role) values (101, 'bia@email.com','$2a$10$HlKPmzJhOQyK/lvRcS0HR.as6UC6iIp2SQEplV3lhagvMMzDnaWu2','ROLE_CLIENTE');
insert into USUARIOS (id,username,password,role) values (102, 'bob@email.com','$2a$10$HlKPmzJhOQyK/lvRcS0HR.as6UC6iIp2SQEplV3lhagvMMzDnaWu2','ROLE_CLIENTE');
insert into USUARIOS (id,username,password,role) values (103, 'toby@email.com','$2a$10$HlKPmzJhOQyK/lvRcS0HR.as6UC6iIp2SQEplV3lhagvMMzDnaWu2','ROLE_CLIENTE');

insert into CLIENTES (id, nome, cpf, id_usuario) values (10, 'Bianca Silva', '88931770057', 101);
insert into CLIENTES (id, nome, cpf, id_usuario) values (20, 'Roberto Gomes', '78390362015', 102);
