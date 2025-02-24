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