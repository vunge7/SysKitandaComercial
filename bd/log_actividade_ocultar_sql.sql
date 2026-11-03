-- =============================================================
-- Remove o SQL gravado nos registos antigos de log_actividade
-- (antes da correcção, a camada automática guardava o comando
--  INSERT/UPDATE/... em valor_novo e o WHERE na descricao).
--
-- Expor SQL revela a estrutura da base de dados. A partir desta
-- versão o SQL já não é gravado (ver util.AuditoriaJdbc); este
-- script só converte os registos antigos para o formato actual.
-- Pode ser executado mais do que uma vez sem efeitos adicionais.
-- =============================================================

-- 1) valores com o comando SQL completo
UPDATE log_actividade
SET valor_novo = NULL
WHERE valor_novo REGEXP '^[[:space:]]*(INSERT|UPDATE|DELETE|REPLACE|TRUNCATE|DROP|ALTER|CREATE)[[:space:]]'
   OR valor_novo LIKE '[comando com senha%';

-- 2) descrição "INSERT em tb_x (1 linha) | WHERE ..." -> "Criou registo em tb_x (1 linha)"
UPDATE log_actividade
SET descricao = REPLACE(CONCAT(
        CASE UPPER(SUBSTRING_INDEX(descricao, ' ', 1))
            WHEN 'INSERT'   THEN 'Criou registo em'
            WHEN 'UPDATE'   THEN 'Alterou registo em'
            WHEN 'REPLACE'  THEN 'Alterou registo em'
            WHEN 'DELETE'   THEN 'Eliminou registo em'
            WHEN 'TRUNCATE' THEN 'Eliminou registo em'
            WHEN 'DROP'     THEN 'Eliminou registo em'
            ELSE 'Alterou a estrutura de'
        END,
        ' ',
        TRIM(SUBSTRING(SUBSTRING_INDEX(descricao, ' | WHERE', 1), LOCATE(' em ', descricao) + 4))
    ), '(JPA, id ', '(id ')
WHERE descricao REGEXP '^(INSERT|UPDATE|DELETE|REPLACE|TRUNCATE|DROP|ALTER|CREATE) em ';

-- 3) acção com o nome do comando -> acção legível
UPDATE log_actividade
SET accao = CASE accao
        WHEN 'INSERT'   THEN 'CRIAR'
        WHEN 'UPDATE'   THEN 'ALTERAR'
        WHEN 'REPLACE'  THEN 'ALTERAR'
        WHEN 'DELETE'   THEN 'ELIMINAR'
        WHEN 'TRUNCATE' THEN 'ELIMINAR'
        WHEN 'DROP'     THEN 'ELIMINAR'
        ELSE 'ESTRUTURA'
    END
WHERE accao IN ('INSERT', 'UPDATE', 'REPLACE', 'DELETE', 'TRUNCATE', 'DROP', 'ALTER', 'CREATE');
