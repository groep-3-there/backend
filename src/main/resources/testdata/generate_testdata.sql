insert into roles (id, created_at, is_matchmaker, name)
values (1, '2020-01-01', true, 'Medewerker'),
       (2, '2020-01-02', false, 'Challenger'),
       (3, '2020-01-03', false, 'Department beheerder'),
       (4, '2020-01-04', false, 'Company beheerder');
insert into companies (banner_image_id, created_at, id, owner_id, profile_image_id, branch, info, name, tags)
values (null,null,1,null,null,2,'info','company1','tags1');
insert into departments (created_at, id, parent_company_id, name)
values (null, 1, 1, 'department1');
insert into users (is_email_public, is_phone_number_public, accepted_tos_date, avatar_image_id, created_at, id,
                   last_seen, role_id, email, info, name, phone_number, tags, department_id)
values (false,false,null,null,null,1,null,1,'mail.com@mail.com', 'information','Jan Bakker', '0612345678', 'tags1', 1);