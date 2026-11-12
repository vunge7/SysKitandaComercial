-- =====================================================================
-- Contagem numérica diária das vendas (tb_venda.cont), impressa no
-- talão A6 ("Nº do dia").  Instala o trigger em kitanda_db e kitanda_db_1.
-- =====================================================================
--
-- As vendas NÃO são espelhadas entre as duas bases (cada uma tem as suas),
-- por isso o trigger tem de existir em AMBAS. Sem ele, as vendas ficam com
-- cont = 0 (o talão esconde o Nº) e dataVenda sem hora (00:00).
--
-- Regras:
--  * Facturas (FT, FR, ...) recebem o próximo número do dia: 1, 2, 3, ...
--  * Proformas, notas de débito/crédito e recibos (fk_documento 3,4,5,6)
--    ficam com cont = 0 e não gastam número.
--  * dataVenda é sempre a data/hora do servidor no momento da inserção.
--  * A pesquisa usa um intervalo em dataVenda (índice idx_tb_venda_dataVenda).
--
-- Execução (MySQL 5.1, cliente mysql ou outro que aceite DELIMITER):
--   mysql -u root -p < bd/trg_venda_numero_diario.sql
-- Pode ser executado várias vezes: o trigger é recriado e o índice só é
-- criado se faltar.
-- =====================================================================

DELIMITER $$

-- ---------------------------------------------------------------------
-- 1) kitanda_db
-- ---------------------------------------------------------------------
USE `kitanda_db`$$

DROP PROCEDURE IF EXISTS `tmp_cria_idx_dataVenda`$$
CREATE PROCEDURE `tmp_cria_idx_dataVenda`()
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.STATISTICS
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME   = 'tb_venda'
                      AND INDEX_NAME   = 'idx_tb_venda_dataVenda') THEN
        ALTER TABLE `tb_venda` ADD INDEX `idx_tb_venda_dataVenda` (`dataVenda`);
    END IF;
END$$
CALL `tmp_cria_idx_dataVenda`()$$
DROP PROCEDURE `tmp_cria_idx_dataVenda`$$

DROP TRIGGER IF EXISTS `trg_venda_numero_diario`$$
CREATE TRIGGER `trg_venda_numero_diario` BEFORE INSERT ON `tb_venda` FOR EACH ROW
BEGIN
    DECLARE v_numero_diario INT;

    SET NEW.dataVenda = NOW(); -- força data/hora actual

    IF NEW.fk_documento IN (3, 4, 5, 6) THEN
        -- PP, ND, NC, RE: fora da contagem diária
        SET NEW.cont = 0;
    ELSE
        SELECT IFNULL(MAX(cont), 0) + 1
          INTO v_numero_diario
          FROM tb_venda
         WHERE dataVenda >= CURDATE()
           AND dataVenda <  CURDATE() + INTERVAL 1 DAY
           AND fk_documento NOT IN (3, 4, 5, 6);

        SET NEW.cont = v_numero_diario;
    END IF;
END$$

-- ---------------------------------------------------------------------
-- 2) kitanda_db_1
-- ---------------------------------------------------------------------
USE `kitanda_db_1`$$

DROP PROCEDURE IF EXISTS `tmp_cria_idx_dataVenda`$$
CREATE PROCEDURE `tmp_cria_idx_dataVenda`()
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.STATISTICS
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME   = 'tb_venda'
                      AND INDEX_NAME   = 'idx_tb_venda_dataVenda') THEN
        ALTER TABLE `tb_venda` ADD INDEX `idx_tb_venda_dataVenda` (`dataVenda`);
    END IF;
END$$
CALL `tmp_cria_idx_dataVenda`()$$
DROP PROCEDURE `tmp_cria_idx_dataVenda`$$

DROP TRIGGER IF EXISTS `trg_venda_numero_diario`$$
CREATE TRIGGER `trg_venda_numero_diario` BEFORE INSERT ON `tb_venda` FOR EACH ROW
BEGIN
    DECLARE v_numero_diario INT;

    SET NEW.dataVenda = NOW(); -- força data/hora actual

    IF NEW.fk_documento IN (3, 4, 5, 6) THEN
        -- PP, ND, NC, RE: fora da contagem diária
        SET NEW.cont = 0;
    ELSE
        SELECT IFNULL(MAX(cont), 0) + 1
          INTO v_numero_diario
          FROM tb_venda
         WHERE dataVenda >= CURDATE()
           AND dataVenda <  CURDATE() + INTERVAL 1 DAY
           AND fk_documento NOT IN (3, 4, 5, 6);

        SET NEW.cont = v_numero_diario;
    END IF;
END$$

DELIMITER ;

-- ---------------------------------------------------------------------
-- Verificação: deve devolver 2 linhas (kitanda_db e kitanda_db_1)
-- ---------------------------------------------------------------------
SELECT TRIGGER_SCHEMA, TRIGGER_NAME, ACTION_TIMING, EVENT_MANIPULATION
  FROM information_schema.TRIGGERS
 WHERE TRIGGER_NAME = 'trg_venda_numero_diario'
   AND TRIGGER_SCHEMA IN ('kitanda_db', 'kitanda_db_1');
