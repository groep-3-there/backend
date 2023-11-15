insert into tags(id, name)
values (1, 'Website'),
       (2, 'App'),
       (3, 'Digitaal ecosysteem'),
       (4, 'Digitale Veiligheid'),
       (5, 'Marketing'),
       (6, 'Social Media'),
       (7, 'Educatie'),
       (8, 'Digitale Strategie'),
       (9, 'Kunstmatige intelligentie (AI)'),
       (10, 'Online betalingen'),
       (11, 'Verduurzaming'),
       (12, 'Digitalisering'),
       (13, 'Design'),
       (14, 'Data'),
       (15, 'Blockchain'),
       (16, 'Prototype');

insert into branches(id, name)
values (1, 'Advies en consultancy'),
       (2, 'Agrosector'),
       (3, 'Bouw, installatie en infrastructuur'),
       (4, 'Cultuur en sport'),
       (5, 'Delfstoffen'),
       (6, 'Financiële dienstverlening'),
       (7, 'Gezondheidszorg en maatschappelijke dienstverlening'),
       (8, 'Autohandel, groothandel en detailhandel'),
       (9, 'Horeca'),
       (10, 'ICT, media en communicatie'),
       (11, 'Industrie'),
       (12, 'Onderwijs en training'),
       (13, 'Onroerend goed'),
       (14, 'Persoonlijke dienstverleningen en not-for-profit'),
       (15, 'Vervoer, post en opslag'),
       (16, 'Water en afval'),
       (17, 'Zakelijke dienstverlening');

insert into permissions (id, code_name, description, fancy_name)
values (1, 'CHALLENGE_READ', 'Het bekijken van een challenge en reacties achterlaten', 'Challenge bekijken'),
--        (2, ''CHALLENGE_REACT'', ''Het reageren op een challenge'', ''Reageren op Challenge''),
       (3, 'CHALLENGE_MANAGE', 'Het beheren van een challenge & kiezen van reacties', 'Beheer challenge'),
--        (4, ''CHALLENGE_MARK_REACTION'', ''Het markeren van een reactie'', ''Markeer reactie''),
       (5, 'DEPARTMENT_CREATE', 'Het creeeren van een department', 'Creeer department'),
       (6, 'COMPANY_EDIT', 'Het bewerken van een bedrijf', 'Bewerk bedrijf'),
       (7, 'COMPANY_GRADE', 'Het goedkeuren van een bedrijfsaanvraag', 'Goedkeuren bedrijfsaanvraag');

insert into roles (id, created_at, is_matchmaker, name)
values (1, '2020-01-01', true, 'Medewerker'),
       (2, '2020-01-02 ', false, 'Challenger'),
       (3, '2020-01-03 ', false, 'Department beheerder'),
       (4, '2020-01-04' , false, 'Company beheerder'),
       (5, '2020-01-01', true, 'MatchMaker');

insert into roles_permissions (role_id, permissions_id)
values (1, 1),
--        (1,2),
       (2, 1),
--        (2,2),
       (2, 3),
--        (2,4),
       (3, 1),
--        (3,2),
       (3, 3),
--        (3,4),
       (4, 1),
--        (4,2),
       (4, 3),
--        (4,4),
       (4, 5),
       (4, 6),
       (5, 1),
--        (5,2),
       (5, 3),
--        (5,4),
       (5, 5),
       (5, 6),
       (5, 7);
insert into companies (banner_image_id, branch_id, created_at, id, owner_id, profile_image_id, info, name, tags)
values (null, 1, '2020-01-01', 1, null, null, 'info', 'name', 'tag1,tag2');
insert into departments (created_at, id, parent_company_id, name)
values ('2020-01-01', 1, 1, 'department');
insert into users (is_email_public, is_phone_number_public, accepted_tos_date, avatar_image_id, created_at,
                   department_id, id, last_seen, role_id, email, firebase_id, info, name, phone_number, tags)
values (true,true,'2020-01-01', null, '2020-01-01', 1, 1, null, 1, 'matchmaker@mail.com', null, 'info', 'name', '0630384453', 'tag1,tag2');
update companies
set  owner_id=1
where id=1;