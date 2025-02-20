-- ==============================================
-- INSERÇÕES DAS UNIDADES FEDERATIVAS (UFs)
-- ==============================================
INSERT INTO federate_unit (acronym, name) VALUES ('AC', 'Acre');
INSERT INTO federate_unit (acronym, name) VALUES ('AL', 'Alagoas');
INSERT INTO federate_unit (acronym, name) VALUES ('AP', 'Amapá');
INSERT INTO federate_unit (acronym, name) VALUES ('AM', 'Amazonas');
INSERT INTO federate_unit (acronym, name) VALUES ('BA', 'Bahia');
INSERT INTO federate_unit (acronym, name) VALUES ('CE', 'Ceará');
INSERT INTO federate_unit (acronym, name) VALUES ('DF', 'Distrito Federal');
INSERT INTO federate_unit (acronym, name) VALUES ('ES', 'Espírito Santo');
INSERT INTO federate_unit (acronym, name) VALUES ('GO', 'Goiás');
INSERT INTO federate_unit (acronym, name) VALUES ('MA', 'Maranhão');
INSERT INTO federate_unit (acronym, name) VALUES ('MT', 'Mato Grosso');
INSERT INTO federate_unit (acronym, name) VALUES ('MS', 'Mato Grosso do Sul');
INSERT INTO federate_unit (acronym, name) VALUES ('MG', 'Minas Gerais');
INSERT INTO federate_unit (acronym, name) VALUES ('PA', 'Pará');
INSERT INTO federate_unit (acronym, name) VALUES ('PB', 'Paraíba');
INSERT INTO federate_unit (acronym, name) VALUES ('PR', 'Paraná');
INSERT INTO federate_unit (acronym, name) VALUES ('PE', 'Pernambuco');
INSERT INTO federate_unit (acronym, name) VALUES ('PI', 'Piauí');
INSERT INTO federate_unit (acronym, name) VALUES ('RJ', 'Rio de Janeiro');
INSERT INTO federate_unit (acronym, name) VALUES ('RN', 'Rio Grande do Norte');
INSERT INTO federate_unit (acronym, name) VALUES ('RS', 'Rio Grande do Sul');
INSERT INTO federate_unit (acronym, name) VALUES ('RO', 'Rondônia');
INSERT INTO federate_unit (acronym, name) VALUES ('RR', 'Roraima');
INSERT INTO federate_unit (acronym, name) VALUES ('SC', 'Santa Catarina');
INSERT INTO federate_unit (acronym, name) VALUES ('SP', 'São Paulo');
INSERT INTO federate_unit (acronym, name) VALUES ('SE', 'Sergipe');
INSERT INTO federate_unit (acronym, name) VALUES ('TO', 'Tocantins');

-- ==============================================
-- INSERÇÕES DE ALGUMAS CIDADES (EXEMPLOS)
-- ==============================================
INSERT INTO city (id, name, federate_unit_acronym) VALUES (1, 'Rio Branco', 'AC');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (2, 'Maceió', 'AL');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (3, 'Macapá', 'AP');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (4, 'Manaus', 'AM');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (5, 'Salvador', 'BA');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (6, 'Fortaleza', 'CE');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (7, 'Brasília', 'DF');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (8, 'Vitória', 'ES');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (9, 'Goiânia', 'GO');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (10, 'São Luís', 'MA');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (11, 'Cuiabá', 'MT');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (12, 'Campo Grande', 'MS');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (13, 'Belo Horizonte', 'MG');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (14, 'Belém', 'PA');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (15, 'João Pessoa', 'PB');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (16, 'Curitiba', 'PR');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (17, 'Recife', 'PE');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (18, 'Teresina', 'PI');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (19, 'Rio de Janeiro', 'RJ');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (20, 'Natal', 'RN');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (21, 'Porto Alegre', 'RS');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (22, 'Porto Velho', 'RO');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (23, 'Boa Vista', 'RR');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (24, 'Florianópolis', 'SC');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (25, 'São Paulo', 'SP');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (26, 'Aracaju', 'SE');
INSERT INTO city (id, name, federate_unit_acronym) VALUES (27, 'Palmas', 'TO');

-- ==============================================
-- INSERÇÕES DE BAIRROS (EXEMPLOS)
-- ==============================================
INSERT INTO neighborhood (id, name) VALUES (1, 'Centro');
INSERT INTO neighborhood (id, name) VALUES (2, 'Copacabana');
INSERT INTO neighborhood (id, name) VALUES (3, 'Itaim Bibi');
INSERT INTO neighborhood (id, name) VALUES (4, 'Boa Viagem');
INSERT INTO neighborhood (id, name) VALUES (5, 'Asa Sul');

-- ==============================================
-- INSERÇÕES DE TIPOS DE LOGRADOUROS
-- ==============================================
INSERT INTO street_type (acronym, name) VALUES ('R', 'Rua');
INSERT INTO street_type (acronym, name) VALUES ('AV', 'Avenida');
INSERT INTO street_type (acronym, name) VALUES ('AL', 'Alameda');
INSERT INTO street_type (acronym, name) VALUES ('TR', 'Travessa');
INSERT INTO street_type (acronym, name) VALUES ('PR', 'Praça');
INSERT INTO street_type (acronym, name) VALUES ('ROD', 'Rodovia');
INSERT INTO street_type (acronym, name) VALUES ('EST', 'Estrada');
INSERT INTO street_type (acronym, name) VALUES ('VLA', 'Vila');
INSERT INTO street_type (acronym, name) VALUES ('PC', 'Ponte');
INSERT INTO street_type (acronym, name) VALUES ('LGO', 'Largo');

-- ==============================================
-- INSERÇÕES DE LOGRADOUROS (EXEMPLOS)
-- ==============================================
INSERT INTO street (id, name, street_type_acronym) VALUES (1, 'Paulista', 'AV');
INSERT INTO street (id, name, street_type_acronym) VALUES (2, 'Brasil', 'R');
INSERT INTO street (id, name, street_type_acronym) VALUES (3, 'Independência', 'AL');
INSERT INTO street (id, name, street_type_acronym) VALUES (4, 'Liberdade', 'TR');
INSERT INTO street (id, name, street_type_acronym) VALUES (5, 'Sete de Setembro', 'PR');
