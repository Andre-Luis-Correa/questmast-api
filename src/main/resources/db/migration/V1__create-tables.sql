-- Tabela ADDRESS
create table address (
                         city_id bigint not null,
                         id bigint generated by default as identity,
                         neighborhood_id bigint not null,
                         street_id bigint not null,
                         cep varchar(255) not null,
                         primary key (id)
);

-- Tabela ADMIN
create table admin (
                       address_id bigint not null,
                       birth_date timestamp(6) not null,
                       id bigint generated by default as identity,
                       complement varchar(255),
                       cpf varchar(255) not null unique,
                       gender_acronym varchar(255) not null,
                       main_email varchar(255) not null unique,
                       name varchar(255) not null,
                       number varchar(255) not null,
                       recovery_email varchar(255) unique,
                       primary key (id)
);

-- Tabela ADMIN_PHONE_LIST
create table admin_phone_list (
                                  admin_id bigint not null,
                                  phone_list_number varchar(255) not null unique
);

-- Tabela APP_USER
create table app_user (
                          is_main_email_verified boolean not null,
                          is_recovery_email_verified boolean,
                          reset_password_code_expire_date timestamp(6),
                          cpf varchar(255) not null,
                          name varchar(255) not null,
                          password varchar(255) not null,
                          person_role varchar(255) not null
                              check (person_role in ('ROLE_STUDENT','ROLE_ADMIN','ROLE_CONTENT_MODERATOR')),
                          recovery_email varchar(255) unique,
                          reset_password_code varchar(255),
                          username varchar(255) not null,
                          verification_email_code varchar(255),
                          primary key (username)
);

-- Tabela BOARD_EXAMINER
create table board_examiner (
                                quantity_of_questions integer not null,
                                quantity_of_selection_process integer not null,
                                quantity_of_tests integer not null,
                                id bigint generated by default as identity,
                                cnpj varchar(255) not null unique,
                                name varchar(255) not null,
                                site_url varchar(255) not null,
                                primary key (id)
);

-- Tabela CITY
create table city (
                      id bigint generated by default as identity,
                      federate_unit_acronym varchar(255) not null,
                      name varchar(255) not null,
                      primary key (id)
);

-- Tabela CONTENT_MODERATOR
create table content_moderator (
                                   address_id bigint not null,
                                   birth_date timestamp(6) not null,
                                   id bigint generated by default as identity,
                                   complement varchar(255),
                                   cpf varchar(255) not null unique,
                                   gender_acronym varchar(255) not null,
                                   main_email varchar(255) not null unique,
                                   name varchar(255) not null,
                                   number varchar(255) not null,
                                   recovery_email varchar(255) unique,
                                   primary key (id)
);

-- Tabela CONTENT_MODERATOR_PHONE_LIST
create table content_moderator_phone_list (
                                              content_moderator_id bigint not null,
                                              phone_list_number varchar(255) not null unique
);

-- Tabela DDD
create table ddd (
                     ddd integer generated by default as identity,
                     primary key (ddd)
);

-- Tabela DDI
create table ddi (
                     ddi integer generated by default as identity,
                     primary key (ddi)
);

-- Tabela FEDERATE_UNIT
create table federate_unit (
                               acronym varchar(255) not null,
                               name varchar(255) not null,
                               primary key (acronym)
);

-- Tabela FUNCTION
create table function (
                          id bigint generated by default as identity,
                          description varchar(255) not null,
                          name varchar(255) not null,
                          primary key (id)
);

-- Tabela GENDER
create table gender (
                        acronym varchar(255) not null,
                        description varchar(255) not null,
                        primary key (acronym)
);

-- Tabela INSTITUTION
create table institution (
                             quantity_of_questions integer not null,
                             quantity_of_selection_process integer not null,
                             quantity_of_tests integer not null,
                             id bigint generated by default as identity,
                             cnpj varchar(255) not null unique,
                             name varchar(255) not null,
                             site_url varchar(255) not null,
                             primary key (id)
);

-- Tabela NEIGHBORHOOD
create table neighborhood (
                              id bigint generated by default as identity,
                              name varchar(255) not null,
                              primary key (id)
);

-- Tabela PHONE
create table phone (
                       ddd_ddd integer not null,
                       ddi_ddi integer not null,
                       number varchar(255) not null,
                       primary key (number)
);

-- Tabela PROFESSIONAL_LEVEL
create table professional_level (
                                    id bigint generated by default as identity,
                                    description varchar(255) not null,
                                    name varchar(255) not null,
                                    primary key (id)
);

-- Tabela QUESTION
create table question (
                          application_date date not null,
                          quantity_of_correct_answers integer not null,
                          quantity_of_tries integer not null,
                          quantity_of_wrong_answers integer not null,
                          id bigint generated by default as identity,
                          question_difficulty_level_id bigint not null,
                          subject_id bigint not null,
                          test_question_category_id bigint not null,
                          explanation varchar(255) not null,
                          statement varchar(255) not null,
                          video_explanation_url varchar(255) not null,
                          primary key (id)
);

-- Tabela QUESTION_ALTERNATIVE
create table question_alternative (
                                      is_correct boolean not null,
                                      id bigint generated by default as identity,
                                      statement varchar(255) not null,
                                      primary key (id)
);

-- Tabela QUESTION_DIFFICULTY_LEVEL
create table question_difficulty_level (
                                           id bigint generated by default as identity,
                                           description varchar(255) not null,
                                           name varchar(255) not null,
                                           primary key (id)
);

-- Tabela QUESTION_QUESTION_ALTERNATIVE_LIST
create table question_question_alternative_list (
                                                    question_alternative_list_id bigint not null unique,
                                                    question_id bigint not null
);

-- Tabela QUESTION_SUBJECT_TOPIC_LIST
create table question_subject_topic_list (
                                             question_id bigint not null,
                                             subject_topic_list_id bigint not null unique,
                                             primary key (question_id, subject_topic_list_id)
);

-- Tabela QUESTIONNAIRE
create table questionnaire (
                               is_public boolean not null,
                               view_counter integer not null,
                               id bigint generated by default as identity,
                               student_id bigint not null,
                               name varchar(255) not null,
                               primary key (id)
);

-- Tabela QUESTIONNAIRE_QUESTION_LIST
create table questionnaire_question_list (
                                             question_list_id bigint not null unique,
                                             questionnaire_id bigint not null
);

-- Tabela SELECTION_PROCESS
create table selection_process (
                                   opening_date date not null,
                                   view_counter integer not null,
                                   board_examiner_id bigint not null,
                                   city_id bigint not null,
                                   content_moderator_id bigint not null,
                                   id bigint generated by default as identity,
                                   institution_id bigint not null,
                                   selection_process_status_id bigint not null,
                                   name varchar(255) not null,
                                   url varchar(255),
                                   primary key (id)
);

-- Tabela SELECTION_PROCESS_STATUS
create table selection_process_status (
                                          id bigint generated by default as identity,
                                          description varchar(255) not null,
                                          primary key (id)
);

-- Tabela SELECTION_PROCESS_TEST
create table selection_process_test (
                                        application_date date not null,
                                        view_counter integer not null,
                                        function_id bigint not null,
                                        id bigint generated by default as identity,
                                        professional_level_id bigint not null,
                                        selection_process_id bigint not null,
                                        test_question_category_id bigint not null,
                                        name varchar(255) not null,
                                        primary key (id)
);

-- Tabela SELECTION_PROCESS_TEST_QUESTION_LIST
create table selection_process_test_question_list (
                                                      question_list_id bigint not null unique,
                                                      selection_process_test_id bigint not null
);

-- Tabela SOLVED_EVALUATION_TEST_QUESTION
create table solved_evaluation_test_question (
                                                 is_correct boolean not null,
                                                 end_date_time timestamp(6) not null,
                                                 id bigint generated by default as identity,
                                                 question_alternative_id bigint not null,
                                                 question_id bigint not null,
                                                 start_date_time timestamp(6) not null,
                                                 primary key (id)
);

-- Tabela SOLVED_QUESTIONNAIRE
create table solved_questionnaire (
                                      quantity_of_correct_answers integer not null,
                                      end_date_time timestamp(6) not null,
                                      id bigint generated by default as identity,
                                      questionnaire_id bigint not null,
                                      start_date_time timestamp(6) not null,
                                      student_id bigint not null,
                                      primary key (id)
);

-- Tabela SOLVED_QUESTIONNAIRE_SOLVED_QUESTION_LIST
create table solved_questionnaire_solved_question_list (
                                                           solved_question_list_id bigint not null unique,
                                                           solved_questionnaire_id bigint not null
);

-- Tabela SOLVED_SELECTION_PROCESS_TEST
create table solved_selection_process_test (
                                               quantity_of_correct_answers integer not null,
                                               end_date_time timestamp(6) not null,
                                               id bigint generated by default as identity,
                                               selection_process_test_id bigint not null,
                                               start_date_time timestamp(6) not null,
                                               student_id bigint not null,
                                               primary key (id)
);

-- Tabela SOLVED_SELECTION_PROCESS_TEST_SOLVED_QUESTION_LIST
create table solved_selection_process_test_solved_question_list (
                                                                    solved_question_list_id bigint not null unique,
                                                                    solved_selection_process_test_id bigint not null
);

-- Tabela STREET
create table street (
                        id bigint generated by default as identity,
                        name varchar(255) not null,
                        street_type_acronym varchar(255) not null,
                        primary key (id)
);

-- Tabela STREET_TYPE
create table street_type (
                             acronym varchar(255) not null,
                             name varchar(255) not null,
                             primary key (acronym)
);

-- Tabela STUDENT
create table student (
                         address_id bigint not null,
                         birth_date timestamp(6) not null,
                         id bigint generated by default as identity,
                         complement varchar(255),
                         cpf varchar(255) not null unique,
                         gender_acronym varchar(255) not null,
                         main_email varchar(255) not null unique,
                         name varchar(255) not null,
                         number varchar(255) not null,
                         recovery_email varchar(255) unique,
                         primary key (id)
);

-- Tabela STUDENT_PHONE_LIST
create table student_phone_list (
                                    student_id bigint not null,
                                    phone_list_number varchar(255) not null unique
);

-- Tabela SUBJECT
create table subject (
                         id bigint generated by default as identity,
                         description varchar(255) not null,
                         name varchar(255) not null,
                         primary key (id)
);

-- Tabela SUBJECT_TOPIC
create table subject_topic (
                               id bigint generated by default as identity,
                               subject_id bigint not null,
                               description varchar(255) not null,
                               name varchar(255) not null,
                               primary key (id)
);

-- Tabela TEST_QUESTION_CATEGORY
create table test_question_category (
                                        id bigint generated by default as identity,
                                        name varchar(255) not null,
                                        primary key (id)
);


-- TABLE: ADDRESS
alter table if exists address
    add constraint FKpo044ng5x4gynb291cv24vtea
    foreign key (city_id) references city;

alter table if exists address
    add constraint FKdxhmuat5wh4dhiynylp7nh76y
    foreign key (neighborhood_id) references neighborhood;

alter table if exists address
    add constraint FKft0pt0eni21ptl8s37dtq8s9
    foreign key (street_id) references street;

-- TABLE: ADMIN
alter table if exists admin
    add constraint FKjpklr1k3hma6fhgx39cj45dfs
    foreign key (address_id) references address;

alter table if exists admin
    add constraint FKt4yq4gogae07od5m6kpjaamxb
    foreign key (gender_acronym) references gender;

-- TABLE: ADMIN_PHONE_LIST
alter table if exists admin_phone_list
    add constraint FKcam3f7nbrksdcgop5atfeahct
    foreign key (phone_list_number) references phone;

alter table if exists admin_phone_list
    add constraint FKpfpxne7gpomb5aok9em4y3ybw
    foreign key (admin_id) references admin;

-- TABLE: CITY
alter table if exists city
    add constraint FKfwovldcx56js97hho7wfdmvqb
    foreign key (federate_unit_acronym) references federate_unit;

-- TABLE: CONTENT_MODERATOR
alter table if exists content_moderator
    add constraint FKqlk55wb4lrnxjfbhct0c9vwr9
    foreign key (address_id) references address;

alter table if exists content_moderator
    add constraint FKqsnns43awm7if1e387ub45bke
    foreign key (gender_acronym) references gender;

-- TABLE: CONTENT_MODERATOR_PHONE_LIST
alter table if exists content_moderator_phone_list
    add constraint FKdhoflp39xdfj3whd4o03bbkw1
    foreign key (phone_list_number) references phone;

alter table if exists content_moderator_phone_list
    add constraint FKscuhmytj59imiw99eqvwbb8e2
    foreign key (content_moderator_id) references content_moderator;

-- TABLE: PHONE
alter table if exists phone
    add constraint FKsngt6wcmcltvmmjanjjpwg3w5
    foreign key (ddd_ddd) references ddd;

alter table if exists phone
    add constraint FKpvq0lbhpplhawdhbbg6bo1s4s
    foreign key (ddi_ddi) references ddi;

-- TABLE: QUESTION
alter table if exists question
    add constraint FKd84iyfnspxoeb6oo7q2f0kk5
    foreign key (question_difficulty_level_id) references question_difficulty_level;

alter table if exists question
    add constraint FKkfvh71q42645g7p9cgxjygbgc
    foreign key (subject_id) references subject;

alter table if exists question
    add constraint FKldv2um6t26uj2w83u1450bepq
    foreign key (test_question_category_id) references test_question_category;

-- TABLE: QUESTION_QUESTION_ALTERNATIVE_LIST
alter table if exists question_question_alternative_list
    add constraint FKaa63vb9ue1dhfg8xrlpynk3bf
    foreign key (question_alternative_list_id) references question_alternative;

alter table if exists question_question_alternative_list
    add constraint FK2whai33v1ymbdb9lchd9ode7g
    foreign key (question_id) references question;

-- TABLE: QUESTION_SUBJECT_TOPIC_LIST
alter table if exists question_subject_topic_list
    add constraint FKsxr9s1o93xr9gsjcj4jrgls4l
    foreign key (subject_topic_list_id) references subject_topic;

alter table if exists question_subject_topic_list
    add constraint FKbuiw2wtvdn2bnme8p8hc7wcxr
    foreign key (question_id) references question;

-- TABLE: QUESTIONNAIRE
alter table if exists questionnaire
    add constraint FKinenp83jeaky1ybwggmajly8g
    foreign key (student_id) references student;

-- TABLE: QUESTIONNAIRE_QUESTION_LIST
alter table if exists questionnaire_question_list
    add constraint FKhs07vb3o1l0j0onmmwftycmkl
    foreign key (question_list_id) references question;

alter table if exists questionnaire_question_list
    add constraint FKslca7ndtqb3bj0wt2s27ul5n9
    foreign key (questionnaire_id) references questionnaire;

-- TABLE: SELECTION_PROCESS
alter table if exists selection_process
    add constraint FKr6psdgcixtb2we7sacg2m3qdh
    foreign key (board_examiner_id) references board_examiner;

alter table if exists selection_process
    add constraint FKagma780ehfyy4c9fd93butk8m
    foreign key (city_id) references city;

alter table if exists selection_process
    add constraint FKnr960e7r05im3xho4bpvwroef
    foreign key (content_moderator_id) references content_moderator;

alter table if exists selection_process
    add constraint FKgncb679wb3bg9kmkvjp2emdkw
    foreign key (institution_id) references institution;

alter table if exists selection_process
    add constraint FKlqlo9p3bxs3orgjfn81g1217
    foreign key (selection_process_status_id) references selection_process_status;

-- TABLE: SELECTION_PROCESS_TEST
alter table if exists selection_process_test
    add constraint FKqcj950q3rjuvg7kqf18h13a8r
    foreign key (function_id) references function;

alter table if exists selection_process_test
    add constraint FKijpa5fj1huf527tju6audoui5
    foreign key (professional_level_id) references professional_level;

alter table if exists selection_process_test
    add constraint FKnushjou5yewjq5rx0kku3eulr
    foreign key (selection_process_id) references selection_process;

alter table if exists selection_process_test
    add constraint FKc3y0ebhd8s4ojhwdsblqh8xrv
    foreign key (test_question_category_id) references test_question_category;

-- TABLE: SELECTION_PROCESS_TEST_QUESTION_LIST
alter table if exists selection_process_test_question_list
    add constraint FKqopguf573d4075lbsi8yap1ej
    foreign key (question_list_id) references question;

alter table if exists selection_process_test_question_list
    add constraint FKoonvyw7w8ftbs5pma0y4qqpcw
    foreign key (selection_process_test_id) references selection_process_test;

-- TABLE: SOLVED_EVALUATION_TEST_QUESTION
alter table if exists solved_evaluation_test_question
    add constraint FK2ha5ssug08qdjgaypvavynt5q
    foreign key (question_id) references question;

alter table if exists solved_evaluation_test_question
    add constraint FK9j0naimib2ui8tjnrhysrqq42
    foreign key (question_alternative_id) references question_alternative;

-- TABLE: SOLVED_QUESTIONNAIRE
alter table if exists solved_questionnaire
    add constraint FK2gfb36srhv578emcth2ndufy4
    foreign key (student_id) references student;

alter table if exists solved_questionnaire
    add constraint FKk772or7q3n4l0hhe7nx5u45b4
    foreign key (questionnaire_id) references questionnaire;

-- TABLE: SOLVED_QUESTIONNAIRE_SOLVED_QUESTION_LIST
alter table if exists solved_questionnaire_solved_question_list
    add constraint FK5ebr9ag7qno8hf2kdw8vjct2c
    foreign key (solved_question_list_id) references solved_evaluation_test_question;

alter table if exists solved_questionnaire_solved_question_list
    add constraint FK550ntpr553oehg0l2cewcbtg9
    foreign key (solved_questionnaire_id) references solved_questionnaire;

-- TABLE: SOLVED_SELECTION_PROCESS_TEST
alter table if exists solved_selection_process_test
    add constraint FKra2vmw2acewerhnnk8proc844
    foreign key (student_id) references student;

alter table if exists solved_selection_process_test
    add constraint FKjtg89rgmgjpfuqu71ku3vk9e
    foreign key (selection_process_test_id) references selection_process_test;

-- TABLE: SOLVED_SELECTION_PROCESS_TEST_SOLVED_QUESTION_LIST
alter table if exists solved_selection_process_test_solved_question_list
    add constraint FK8xe44putbheu8xg51a1k05b4y
    foreign key (solved_question_list_id) references solved_evaluation_test_question;

alter table if exists solved_selection_process_test_solved_question_list
    add constraint FKmoyd32ronmesuypyvuu0hrhf0
    foreign key (solved_selection_process_test_id) references solved_selection_process_test;

-- TABLE: STREET
alter table if exists street
    add constraint FKqqml66pjs5if8okvq99xx8vm9
    foreign key (street_type_acronym) references street_type;

-- TABLE: STUDENT
alter table if exists student
    add constraint FKcaf6ht0hfw93lwc13ny0sdmvo
    foreign key (address_id) references address;

alter table if exists student
    add constraint FKe1gl2mo3958p8ug0lpvecl0r
    foreign key (gender_acronym) references gender;

-- TABLE: STUDENT_PHONE_LIST
alter table if exists student_phone_list
    add constraint FK7osb0a5reh66pinm0hxitb0t6
    foreign key (phone_list_number) references phone;

alter table if exists student_phone_list
    add constraint FKjcuok4e2sds27o092n6f8175o
    foreign key (student_id) references student;

-- TABLE: SUBJECT_TOPIC
alter table if exists subject_topic
    add constraint FKrkqlb2sjd23jw0jic6blsbbdh
    foreign key (subject_id) references subject;
