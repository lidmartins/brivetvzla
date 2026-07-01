USE brivetvzla;

-- Usuario veterinario de prueba
-- email: vet@brivetvzla.com | password: demo1234
INSERT INTO user (us_ro_cd_role, us_nm_first_name, us_nm_last_name, us_de_email, us_de_phone, us_de_password_hash, us_in_veterinarian, us_st_user, us_dt_last_login)
SELECT ro_cd_role, 'Vet', 'Demo', 'vet@brivetvzla.com', '0000000000', '$2b$12$TWUrD6UBzp4oR7RVHvZoRuFGyxPV5juKPcoLYzscAz383pkGKw/me', 'S', 'A', NOW()
FROM role WHERE ro_nm_role = 'VET';
