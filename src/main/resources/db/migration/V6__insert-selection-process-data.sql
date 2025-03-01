INSERT INTO board_examiner (quantity_of_questions, quantity_of_selection_process, quantity_of_tests, cnpj, name,
                            site_url)
VALUES (50, 5, 10, '12345678000101', 'Examiner Board Alpha', 'https://alphaexaminer.com'),
       (100, 10, 20, '22345678000102', 'Examiner Board Beta', 'https://betaexaminer.com'),
       (75, 8, 15, '32345678000103', 'Examiner Board Gamma', 'https://gammaexaminer.com');

-- Inserindo dados necessários para as FK (caso ainda não existam)
INSERT INTO address (city_id, neighborhood_id, street_id, cep)
VALUES (1, 1, 1, '12345-678');

-- Inserção na tabela content_moderator
INSERT INTO content_moderator (address_id, birth_date, complement, cpf, gender_acronym, main_email, name, number,
                               recovery_email)
VALUES (1, '1990-05-15', 'Apto 101', '11122233344', 'M', 'moderator1@example.com', 'Carlos Silva', '100',
        'carlos.recovery@example.com'),
       (1, '1985-09-20', 'Casa', '55566677788', 'F', 'moderator2@example.com', 'Ana Pereira', '200',
        'ana.recovery@example.com'),
       (1, '1992-12-01', 'Bloco B', '99988877766', 'M', 'moderator3@example.com', 'Lucas Lima', '300', NULL);

INSERT INTO selection_process_status (description)
VALUES ('Aberto'),
       ('Em Andamento'),
       ('Concluído'),
       ('Cancelado');

INSERT INTO institution (quantity_of_questions,
                         quantity_of_selection_process,
                         quantity_of_tests,
                         cnpj,
                         name,
                         site_url)
VALUES (120, 5, 10, '12.345.678/0001-90', 'Instituição Alpha', 'https://www.instituicaoalpha.com.br'),
       (200, 8, 15, '23.456.789/0001-01', 'Instituição Beta', 'https://www.instituicaobeta.com.br'),
       (75, 3, 5, '34.567.890/0001-12', 'Instituição Gamma', 'https://www.instituicaogamma.com.br'),
       (150, 6, 12, '45.678.901/0001-23', 'Instituição Delta', 'https://www.instituicaodelta.com.br'),
       (95, 4, 7, '56.789.012/0001-34', 'Instituição Epsilon', 'https://www.instituicaoepsilon.com.br');


insert into selection_process(creation_date, opening_date, view_counter, board_examiner_id, institution_id, city_id,
                              content_moderator_id, selection_process_status_id, name, url)
values ('2025-03-01 14:30:45', '2025-02-24', 0, 1, 1, 1, 1, 1, 'Concurso dos Correios', 'www.correios.com.br');

-- Inserindo funções
INSERT INTO function(name, description) VALUES
                                            ('Fiscal de cartas', 'Fiscalizar o recebimento de cartas. Salário de R$ 3500,00.'),
                                            ('Analista de Sistemas', 'Desenvolver e manter sistemas de software. Salário de R$ 6000,00.'),
                                            ('Engenheiro Civil', 'Projetar e supervisionar obras de infraestrutura. Salário de R$ 8500,00.'),
                                            ('Médico Clínico Geral', 'Atendimento médico e diagnóstico de pacientes. Salário de R$ 12000,00.'),
                                            ('Professor de Matemática', 'Ensinar matemática para alunos do ensino médio. Salário de R$ 4500,00.'),
                                            ('Advogado', 'Atuar na defesa de clientes em processos jurídicos. Salário de R$ 8000,00.'),
                                            ('Designer Gráfico', 'Criar materiais visuais e identidade visual. Salário de R$ 4000,00.'),
                                            ('Cozinheiro', 'Preparar refeições e planejar cardápios. Salário de R$ 3200,00.'),
                                            ('Motorista', 'Transportar passageiros ou cargas. Salário de R$ 3000,00.'),
                                            ('Técnico em Enfermagem', 'Prestar assistência médica em hospitais e clínicas. Salário de R$ 3500,00.'),
                                            ('Atendente de Telemarketing', 'Realizar atendimento ao cliente por telefone. Salário de R$ 2500,00.'),
                                            ('Engenheiro de Software', 'Desenvolver e arquitetar soluções tecnológicas. Salário de R$ 9000,00.'),
                                            ('Gestor de Projetos', 'Gerenciar e planejar projetos em empresas. Salário de R$ 8500,00.'),
                                            ('Policial Militar', 'Garantir a segurança e ordem pública. Salário de R$ 5000,00.'),
                                            ('Eletricista', 'Instalar e reparar sistemas elétricos. Salário de R$ 3800,00.'),
                                            ('Bombeiro', 'Atuar no combate a incêndios e resgates. Salário de R$ 5500,00.'),
                                            ('Piloto de Avião', 'Conduzir aeronaves comerciais ou privadas. Salário de R$ 15000,00.'),
                                            ('Psicólogo', 'Atender pacientes e oferecer suporte emocional. Salário de R$ 7000,00.'),
                                            ('Arquiteto', 'Projetar e planejar edificações e espaços urbanos. Salário de R$ 7500,00.'),
                                            ('Jornalista', 'Produzir reportagens e notícias para mídias diversas. Salário de R$ 4800,00.'),
                                            ('Cientista de Dados', 'Analisar e interpretar grandes volumes de dados. Salário de R$ 10000,00.'),
                                            ('Agente de Viagens', 'Planejar e vender pacotes de turismo. Salário de R$ 4000,00.'),
                                            ('Farmacêutico', 'Trabalhar com medicamentos e farmácias. Salário de R$ 6500,00.'),
                                            ('Tradutor', 'Traduzir documentos e conteúdos entre idiomas. Salário de R$ 5000,00.');

insert into professional_level(description, name)
values
    ('Pessoas com ensino médio completo', 'Ensino Médio Completo'),
    ('Pessoas com ensino superior completo', 'Ensino superior Completo'),
    ('Pessoas com ensino médio completo ou em andamento', 'Ensino Médio'),
    ('Pessoas com ensino superior completo ou em andamento', 'Ensino superior');

insert into test_question_category(name)
values ('objetiva');

-- Inserindo as matérias
INSERT INTO subject(description, name)
VALUES ('Estudo dos números, funções e geometria', 'Matemática'),
       ('Estudo da língua portuguesa e literatura', 'Português'),
       ('Estudo de línguas estrangeiras como Inglês e Espanhol', 'Língua Estrangeira'),
       ('Estudo de eventos históricos e sociedades', 'História'),
       ('Estudo da organização política e econômica', 'Geografia'),
       ('Estudo de fenômenos naturais e físicos', 'Física'),
       ('Estudo das reações químicas e elementos', 'Química'),
       ('Estudo dos seres vivos e suas interações', 'Biologia'),
       ('Estudo dos princípios e aplicações da computação', 'Tecnologia da Informação'),
       ('Estudo da sociedade, cultura e comportamento humano', 'Sociologia'),
       ('Estudo dos princípios da política e da cidadania', 'Filosofia'),
       ('Compreensão de textos e interpretação de enunciados', 'Interpretação de Textos');

-- Inserindo os subtópicos para cada matéria
INSERT INTO subject_topic(subject_id, description, name)
VALUES
-- Matemática
(1, 'Estudo de triângulos e razões trigonométricas', 'Trigonometria'),
(1, 'Resolução de equações e inequações', 'Álgebra'),
(1, 'Conjuntos numéricos e suas propriedades', 'Números Reais'),
(1, 'Áreas, volumes e formas geométricas', 'Geometria Plana e Espacial'),
(1, 'Estudo das funções e seus gráficos', 'Funções'),
(1, 'Estatística, médias e probabilidades', 'Estatística e Probabilidade'),

-- Português
(2, 'Estrutura e organização textual', 'Coesão e Coerência'),
(2, 'Análise de tempos e modos verbais', 'Verbos'),
(2, 'Figuras de linguagem e interpretação', 'Literatura'),
(2, 'Ortografia e gramática normativa', 'Gramática'),
(2, 'Leitura crítica e interpretação textual', 'Interpretação de Textos'),

-- Língua Estrangeira
(3, 'Leitura e interpretação de textos', 'Compreensão Textual'),
(3, 'Regras gramaticais e vocabulário', 'Gramática Inglesa/Espanhola'),

-- História
(4, 'Eventos históricos do Brasil', 'História do Brasil'),
(4, 'Principais acontecimentos mundiais', 'História Geral'),
(4, 'Regimes políticos e governança', 'História Política'),

-- Geografia
(5, 'Climas, solos e ecossistemas', 'Geografia Física'),
(5, 'Mapas, fusos horários e cartografia', 'Cartografia'),
(5, 'Aspectos econômicos e sociais', 'Geopolítica'),

-- Física
(6, 'Movimento, força e energia', 'Cinemática e Dinâmica'),
(6, 'Leis da eletricidade e magnetismo', 'Eletromagnetismo'),
(6, 'Óptica e ondas', 'Óptica'),

-- Química
(7, 'Tabela periódica e ligações químicas', 'Química Geral'),
(7, 'Reações químicas e equilíbrio', 'Química Inorgânica'),
(7, 'Moléculas orgânicas e funções', 'Química Orgânica'),

-- Biologia
(8, 'Seres vivos e classificação biológica', 'Citologia e Genética'),
(8, 'Relações ecológicas e biomas', 'Ecologia'),
(8, 'Sistemas do corpo humano', 'Fisiologia Humana'),

-- Tecnologia da Informação
(9, 'Fundamentos de programação e lógica', 'Programação'),
(9, 'Redes de computadores e segurança', 'Redes e Segurança da Informação'),

-- Sociologia
(10, 'Teorias e conceitos sociológicos', 'Teoria Sociológica'),
(10, 'Sociedade e globalização', 'Sociologia e Cultura'),

-- Filosofia
(11, 'Escolas filosóficas e pensamento crítico', 'História da Filosofia'),
(11, 'Ética, política e cidadania', 'Ética e Filosofia Política'),

-- Interpretação de Textos
(12, 'Compreensão e análise textual', 'Leitura e Interpretação'),
(12, 'Estilos de escrita e intenção do autor', 'Gêneros Textuais');

INSERT INTO question_difficulty_level(description, name) VALUES
                                                             ('Questões consideradas fáceis, geralmente com maior taxa de acertos.', 'Fácil'),
                                                             ('Questões de dificuldade intermediária, exigindo conhecimento moderado.', 'Média'),
                                                             ('Questões consideradas difíceis com maior número de erros.', 'Difícil');