INSERT INTO gender (acronym, description) VALUES
                                        ('M', 'Masculino'),
                                        ('F', 'Feminino'),
                                        ('O', 'Outro');

INSERT INTO app_user(password, person_role, username, is_email_verified) VALUES
                                                          ('$2a$10$G5mWSx6w6wYbC79sdMOFzectDP48aEK9XFJYbUu8iljkAo//ZoaAi', 'ROLE_ADMIN', 'admin', true);