-- Remover a constraint de chave estrangeira da tabela question
alter table if exists question
drop constraint if exists FKldv2um6t26uj2w83u1450bepq;

-- Remover a coluna test_question_category_id da tabela question
alter table if exists question
drop column if exists test_question_category_id;

-- Remover a constraint de chave estrangeira da tabela selection_process_test
alter table if exists selection_process_test
drop constraint if exists FKc3y0ebhd8s4ojhwdsblqh8xrv;

-- Remover a coluna test_question_category_id da tabela selection_process_test
alter table if exists selection_process_test
drop column if exists test_question_category_id;
