-- =====================================================================
-- Permissões de edição na tabela de vendas (FormVendaResponsivaVisaoTop)
-- tb_dados_instituicao: alterar_preco, alterar_iva, alterar_descricao, alterar_desconto
-- Mesmo ENUM para todas: 'Permitir' / 'Bloquear' (NULL = Permitir).
-- =====================================================================
--
-- Seleccione antes a base da aplicação, por exemplo:
--   mysql -u root -p kitanda_db_fe < bd/tb_dados_instituicao_permissoes_tabela_vendas.sql
--
-- Pode ser executado mais do que uma vez: só cria as colunas em falta.
-- =====================================================================

DROP PROCEDURE IF EXISTS sp_add_permissao_dados_instituicao;

DELIMITER $$
CREATE PROCEDURE sp_add_permissao_dados_instituicao( IN p_coluna VARCHAR(64) )
BEGIN
    IF NOT EXISTS ( SELECT 1 FROM information_schema.COLUMNS
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME   = 'tb_dados_instituicao'
                      AND COLUMN_NAME  = p_coluna ) THEN
        SET @sql = CONCAT( 'ALTER TABLE `tb_dados_instituicao` ADD COLUMN `', p_coluna,
                           '` ENUM(''Permitir'',''Bloquear'') DEFAULT ''Permitir''' );
        PREPARE st FROM @sql;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    END IF;
END$$
DELIMITER ;

CALL sp_add_permissao_dados_instituicao( 'alterar_preco' );
CALL sp_add_permissao_dados_instituicao( 'alterar_iva' );
CALL sp_add_permissao_dados_instituicao( 'alterar_descricao' );
CALL sp_add_permissao_dados_instituicao( 'alterar_desconto' );

DROP PROCEDURE IF EXISTS sp_add_permissao_dados_instituicao;

-- registos existentes ficam com 'Permitir' (comportamento actual)
UPDATE tb_dados_instituicao SET alterar_iva = 'Permitir' WHERE alterar_iva IS NULL;
UPDATE tb_dados_instituicao SET alterar_descricao = 'Permitir' WHERE alterar_descricao IS NULL;
UPDATE tb_dados_instituicao SET alterar_desconto = 'Permitir' WHERE alterar_desconto IS NULL;
