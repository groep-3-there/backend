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
values (true,true,'2020-01-01', null, '2020-01-01', 1, 1, null, 5, 'matchmaker@mail.com', null, 'info', 'MatchMaker', '0630384453', 'tag1,tag2');
insert into users(is_email_public, is_phone_number_public, accepted_tos_date, avatar_image_id, created_at,
                  department_id, id, last_seen, role_id, email, firebase_id, info, name, phone_number, tags)
values (true,true,'2020-01-01', null, '2020-01-01', 1, 2, null, 2, 'Jan.Bakker@Bakker.Jan.com', null, 'info', 'Jan Bakker', '0630384453', 'tag1,tag2');
update companies
set  owner_id=1
where id=1;
insert into companyrequests (branch_id, id, owner_id, requested_at, name, tags)
values (1, 1, 1, '2020-01-01', 'Bakker Jan', 'Brood, ICT');
insert into companyrequests (branch_id, id, owner_id, requested_at, name, tags)
values (2, 2, 2, '2020-01-01', 'Bakker Bart', 'Brood, ICT');

insert into challenges (status, visibility, author_id, banner_image_id, created_at, department_id, end_date, id, contact_information, description, concluding_remarks, summary, tags, title)
    values (0, 2, 1, null, '2023-01-01', 1, '2024-02-02', 1, 'Mocht je contact op willen nemen buiten het platform, stuur mij een email op Eline@mail.com', 'Ik ben Eline, de trotse eigenaar van Kapperszaak Eline, een bloeiende kapsalon waar we met liefde en passie werken om onze klanten er op hun best uit te laten zien en zich geweldig te laten voelen. Sinds de opening van mijn salon heb ik het voorrecht gehad om vele geweldige mensen te ontmoeten en hun haarwensen te vervullen. Echter, in de altijd veranderende wereld van vandaag staan we voor de uitdaging om onze klanten vaker terug te laten komen en hun tevredenheid te vergroten.

Om deze uitdaging aan te gaan, ben ik op zoek naar de hulp en ideeën van experts op het gebied van informatietechnologie (ICT). Ik geloof sterk in de kracht van technologie om ons bedrijf te verbeteren en de klantbeleving te verrijken. Daarom roep ik jullie op om met innovatieve ideeën te komen die ons kunnen helpen klanten vaker terug te laten komen en de band met onze salon te versterken.
<h2>Enkele van de vragen waar ik graag jullie inzicht over zou willen</h2>

Loyaliteitsprogramma''s: Hoe kunnen we effectieve loyaliteitsprogramma''s opzetten die klanten aanmoedigen om regelmatig terug te komen voor onze diensten?
Online boekingssystemen: Welke geavanceerde online boekingssystemen kunnen we implementeren om het boekingsproces voor klanten te verbeteren en hun ervaring te vereenvoudigen?
Klantcommunicatie: Wat zijn de beste manieren om met onze klanten in contact te blijven via digitale kanalen, zoals e-mailmarketing, sociale media en sms-herinneringen?
Feedbackverzameling: Hoe kunnen we gestructureerd feedback verzamelen van klanten om onze diensten continu te verbeteren?
Personalisatie: Op welke manieren kunnen we personalisatie gebruiken om de klanttevredenheid te verhogen?
 Als expert op het gebied van informatietechnologie of als iemand met waardevolle inzichten over klantretentie, nodig ik jullie uit om jullie ideeën, suggesties en ervaringen te delen. Samen kunnen we de toekomst van Kapperszaak Eline vormgeven en onze klanten de best mogelijke ervaring bieden.
Ik kijk uit naar jullie bijdragen en ben enthousiast om te zien welke innovatieve oplossingen we kunnen bedenken om ons bedrijf te laten groeien.
Hartelijk dank voor jullie betrokkenheid en expertise.

<b>Het beste idee zal ik belonen met 45,-</b>
en een mogelijkheid om betrokken te zijn bij ontwikkelingsfase', '', 'Ik ben Eline, de eigenaar van Kapperszaak Eline, en ik zoek hulp van ICT-experts om de klantretentie te verbeteren. Ik geloof sterk in technologie om de klantbeleving te verbeteren en heb enkele vragen over loyaliteitsprogramma''s, online boekingssystemen, klantcommunicatie, feedbackverzameling en personalisatie. Ik nodig experts uit om hun ideeën en suggesties te delen, en het beste idee wordt beloond met €45,- en een kans om betrokken te zijn bij de ontwikkelingsfase. Bedankt voor jullie betrokkenheid en expertise.', 'kapper,website,personalisatie', 'Innovatie kapperszaak');
