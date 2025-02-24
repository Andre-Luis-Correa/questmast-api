INSERT INTO board_examiner (quantity_of_questions, quantity_of_selection_process, quantity_of_tests, cnpj, name, site_url) VALUES
                                                                                                                               (50, 5, 10, '12345678000101', 'Examiner Board Alpha', 'https://alphaexaminer.com'),
                                                                                                                               (100, 10, 20, '22345678000102', 'Examiner Board Beta', 'https://betaexaminer.com'),
                                                                                                                               (75, 8, 15, '32345678000103', 'Examiner Board Gamma', 'https://gammaexaminer.com');

-- Inserindo dados necessários para as FK (caso ainda não existam)
INSERT INTO address (city_id, neighborhood_id, street_id, cep) VALUES (1, 1, 1, '12345-678');

-- Inserção na tabela content_moderator
INSERT INTO content_moderator (address_id, birth_date, complement, cpf, gender_acronym, main_email, name, number, recovery_email) VALUES
                                                                                                                                      (1, '1990-05-15', 'Apto 101', '11122233344', 'M', 'moderator1@example.com', 'Carlos Silva', '100', 'carlos.recovery@example.com'),
                                                                                                                                      (1, '1985-09-20', 'Casa', '55566677788', 'F', 'moderator2@example.com', 'Ana Pereira', '200', 'ana.recovery@example.com'),
                                                                                                                                      (1, '1992-12-01', 'Bloco B', '99988877766', 'M', 'moderator3@example.com', 'Lucas Lima', '300', NULL);

INSERT INTO selection_process_status (description) VALUES
                                                       ('Aberto'),
                                                       ('Em Andamento'),
                                                       ('Concluído'),
                                                       ('Cancelado');

INSERT INTO institution (
    quantity_of_questions,
    quantity_of_selection_process,
    quantity_of_tests,
    cnpj,
    name,
    site_url
) VALUES
      (120, 5, 10, '12.345.678/0001-90', 'Instituição Alpha', 'https://www.instituicaoalpha.com.br'),
      (200, 8, 15, '23.456.789/0001-01', 'Instituição Beta', 'https://www.instituicaobeta.com.br'),
      (75, 3, 5, '34.567.890/0001-12', 'Instituição Gamma', 'https://www.instituicaogamma.com.br'),
      (150, 6, 12, '45.678.901/0001-23', 'Instituição Delta', 'https://www.instituicaodelta.com.br'),
      (95, 4, 7, '56.789.012/0001-34', 'Instituição Epsilon', 'https://www.instituicaoepsilon.com.br');


insert into selection_process(date, view_counter, board_examiner_id, institution_id, city_id, content_moderator_id, selection_process_status_id, name, url) values ('2025-02-24', 0, 1, 1, 1, 1, 1, 'Concurso dos Correios', 'www.correios.com.br');

insert into function(name, description) values ('Fiscal de cartas', 'Fiscalizar o recebimento de cartas. Salário de R$ 3500,00.');

insert into professional_level(description, name) values ('Ensino médio', 'Ensino Médio');

insert into test_question_category(name) values ('objetiva');

insert into subject(description, name) values ('description', 'Matemática'),
                                              ('description', 'Português');

insert into subject_topic(subject_id, description, name) values(1, 'Lorem ipsum', 'Trigonometria'),
                                             (2, 'Coesão e coerência textual', 'Português');

insert into question_difficulty_level(description, name) values ('Questões consideradas difíceis com maior números de erros', 'Difícil');