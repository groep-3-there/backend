insert into companies (id, banner_image_id, branch, created_at, info, name, profile_image_id, tags, owner_id)
values (1, null, null, '2020-01-01', 'Medische Software', 'ChipSoft', null, 'tag1', null),
       (2, null, null, '2020-01-02', 'Fintech', 'PayPal', null, 'tag2', null),
       (3, null, null, '2020-01-03', 'Educatie', 'NHL Stenden', null, 'tag3', null),
       (4, null, null, '2020-01-04', 'Social Media', 'Meta', null, 'tag4', null),
       (5, null, null, '2020-01-05', 'Internet', 'AT&T', null, 'tag5', null),
       (6, null, null, '2020-01-06', 'PFAS', '3M', null, 'tag6', null),
       (7, null, null, '2020-01-07', 'Internet & Hardware', 'Apple', null, 'tag7', null),
       (8, null, null, '2020-01-08', 'Educatie', 'Inholland Alkmaar', null, 'tag8', null),
       (9, null, null, '2020-01-09', 'Rijksoverheid', 'CJIB', null, 'tag9', null),
       (10, null, null, '2020-01-10', 'Rijksoverheid', 'DUO', null, 'tag10', null),
       (11, null, null, '2020-01-11', 'Coole dingen', 'Aeroscan', null, 'tag11', null);

--Insert test data for departments
insert into departments (id, created_at, name, parent_company_id)
values (1, '2021-02-01', 'ICT', 1),
       (2, '2022-02-11', 'Bakkers', 1),
       (3, '2023-02-21', 'HR', 1),
       (4, '2024-02-01', 'Management', 1),
       (5, '2025-02-02', 'Sales', 1),
       (6, '2026-02-03', 'Development', 1),
       (7, '2027-02-04', 'Finance', 1),
       (8, '2028-02-05', 'Marketing', 1),
       (9, '2029-02-06', 'Legal', 1),
       (10, '2030-02-07', 'Research', 1),
       (11, '2011-01-02', 'Testing', 2),
       (12, '2012-01-03', 'Design', 2),
       (13, '2013-01-04', 'Sales', 2),
       (14, '2014-01-05', 'Marketing', 2),
       (15, '2015-01-06', 'Finance', 2),
       (16, '2016-01-07', 'Management', 2),
       (17, '2017-01-08', 'Development', 2),
       (18, '2018-01-09', 'HR', 2),
       (19, '2019-01-10', 'Legal', 2),
       (20, '2020-01-11', 'Research', 2),
       (21, '2021-01-12', 'ICT', 3),
       (22, '2022-01-13', 'Bakkers', 5),
       (23, '2023-01-14', 'HR', 3),
       (24, '2024-01-15', 'Management', 4),
       (25, '2025-01-16', 'Sales', 3),
       (26, '2026-01-17', 'Development', 2),
       (27, '2027-01-18', 'Finance', 3),
       (28, '2028-01-19', 'Marketing', 3),
       (29, '2029-01-20', 'Legal', 6),
       (30, '2030-01-21', 'Research', 3),
       (31, '2011-01-22', 'Testing', 4),
       (32, '2012-01-23', 'Design', 7),
       (33, '2013-01-24', 'Sales', 8),
       (34, '2014-01-25', 'Marketing', 9),
       (35, '2015-01-26', 'Finance', 10),
       (36, '2016-01-27', 'Management', 4),
       (37, '2017-01-28', 'Development', 4),
       (38, '2018-01-29', 'HR', 4);

insert into roles (id, created_at, is_matchmaker, name, company_id, department_id)
values (1, '2020-01-01', false, 'Medewerker', 1, 1),
       (2, '2020-01-02', false, 'Challenger', 2, 2),
       (3, '2020-01-03', false, 'Department beheerder', 3, 3),
       (4, '2020-01-04', false, 'Company beheerder', 4, 4),
       (5, '2020-01-05', true, 'MatchMaker', 5, 5);

insert into permissions (id, code_name, description, fancy_name)
values (1, 'CHALLENGE_READ', 'Het bekijken van een challenge en reacties achterlaten', 'Challenge bekijken'),
--        (2, 'CHALLENGE_REACT', 'Het reageren op een challenge', 'Reageren op Challenge'),
       (3, 'CHALLENGE_MANAGE', 'Het beheren van een challenge & kiezen van reacties', 'Beheer challenge'),
--        (4, 'CHALLENGE_MARK_REACTION', 'Het markeren van een reactie', 'Markeer reactie'),
       (5, 'DEPARTMENT_CREATE', 'Het creeeren van een department', 'Creeer department'),
       (6, 'COMPANY_EDIT', 'Het bewerken van een bedrijf', 'Bewerk bedrijf'),
       (7, 'COMPANY_GRADE', 'Het goedkeuren van een bedrijfsaanvraag', 'Goedkeuren bedrijfsaanvraag');


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

-- Insert test data for the User table
INSERT INTO users (id, name, info, tags, created_at, last_seen, avatar_image_id, is_email_public,
                   is_phone_number_public, accepted_tos_date, role_id, email, phone_number)
VALUES (1, 'Jan Bakker', 'Info1', 'tag1,tag2', '2023-01-01', '2023-01-01', NULL, true, true, '2023-01-01', 1,
        'jan.bakker@email.com', '0612345678'),
       (2, 'Johan de Vries', 'Info2', 'tag3,tag4', '2023-01-02', '2023-01-02', NULL, false, false, '2023-01-02', 2,
        'johan.de.vries@email.com', '0612345678'),
       (3, 'Florijn Munster', 'Info3', 'tag5,tag6', '2023-01-03', '2023-01-03', NULL, true, false, '2023-01-03', 3,
        'florijn.munster@email.com', '0612345678'),
       (4, 'Rik Wildschut', 'Info4', 'tag7,tag8', '2023-01-04', '2023-01-04', NULL, false, true, '2023-01-04', 4,
        'rik.wildschut@email.com', '0612345678'),
       (5, 'Luke van de Pol', 'Info5', 'tag9,tag10', '2023-01-05', '2023-01-05', NULL, true, false, '2023-01-05', 5,
        'luke.van.de.pol@email.com', '0612345678'),
       (6, 'Eelco Jansma', 'Info6', 'tag11,tag12', '2023-01-06', '2023-01-06', NULL, true, true, '2023-01-06', 1,
        'eelco.jansma@email.com', '0612345678'),
       (7, 'Jelle Blaeser', 'Info7', 'tag13,tag14', '2023-01-07', '2023-01-07', NULL, false, false, '2023-01-07', 2,
        'jelle.blaeser@email.com', '0612345678'),
       (8, 'Tjerk Venema', 'Info8', 'tag15,tag16', '2023-01-08', '2023-01-08', NULL, true, true, '2023-01-08', 3,
        'tjerk.venema@email.com', '0612345678'),
       (9, 'Anniek de Boer', 'Info9', 'tag17,tag18', '2023-01-09', '2023-01-09', NULL, false, true, '2023-01-09', 4,
        'anniek.de.boer@email.com', '0612345678'),
       (10, 'Piet de Wit', 'Info10', 'tag19,tag20', '2023-01-10', '2023-01-10', NULL, true, false, '2023-01-10', 5,
        'piet.de.wit@email.com', '0612345678');

insert into challenges (id, banner_image_id, concluding_remarks, contact_information, created_at,
                        description, end_date, status, summary, tags, title, visibility, author_id,
                        company_id, department_id)

values (1, null, 'Dit zijn de mooie concluding remarks', 'Contact informatie', '2023-01-01',
        'Dit is de challenge description',
        '2023-09-11', 1, 'Summary', 'prototype,website', 'Innovatie kapperszaak', 2, 1, null, 1);

values (1, null, 'Dit zijn de mooie concluding remarks', 'Contact informatie', '2023-01-01', 'Dit is de challenge description',
        '2023-09-11', 0, 'Summary', 'prototype,website', 'Innovatie kapperszaak', 2, 1, 1, 1);

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


insert into challengeinputs (id, created_at, is_chosen_answer, text, type, author_id, challenge_id)
values (1, '2023-01-01', false, 'Wow, wat een mooie challenge heb jij daar!', 1, 1, 1),
       (2, '2023-01-01', false, 'Dit vind ik een erg rare challenge.', 2, 2, 1);


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
