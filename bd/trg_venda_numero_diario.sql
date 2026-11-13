-- =====================================================================
-- Contagem numérica diária das vendas (tb_venda.cont), impressa no
-- talão A6 (facturaA6, quadro "Nº DO DIA").  Ramo dev-fe.
-- =====================================================================
--
-- Instala o trigger na base ACTIVA. Seleccione antes a base da aplicação
-- (a de util.BDConexao), por exemplo:
--   mysql -u root -p kitanda_db_fe < bd/trg_venda_numero_diario.sql
--
-- Regras:
--  * Facturas (FT, FR, ...) recebem o próximo número do dia: 1, 2, 3, ...
--  * Proformas, notas de débito/crédito e recibos (fk_documento 3,4,5,6)
--    ficam com cont = 0 e não gastam número (o talão esconde o quadro).
--  * O "dia" é o da dataVenda da própria venda; a pesquisa usa um intervalo
--    em dataVenda (índice idx_tb_venda_dataVenda).
--
-- Diferença em relação ao ramo dev: aqui NÃO se força dataVenda = NOW().
-- No dev-fe a aplicação grava a data/hora completa e usa-a na assinatura
-- (hash) da Factura Electrónica; reescrevê-la no servidor faria a data
-- gravada divergir da data assinada. Só se preenche se vier a NULL.
--
-- Pode ser executado várias vezes: o trigger é recriado e o índice só é
-- criado se faltar. Requer MySQL 5.1+ (cliente que aceite DELIMITER).
-- Nota: o MySQL 5.1 só permite um trigger BEFORE INSERT por tabela.
-- =====================================================================

DELIMITER $$

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

    IF NEW.dataVenda IS NULL THEN
        SET NEW.dataVenda = NOW();
    END IF;

    IF NEW.fk_documento IN (3, 4, 5, 6) THEN
        -- PP, ND, NC, RC: fora da contagem diária
        SET NEW.cont = 0;
    ELSE
        SELECT IFNULL(MAX(cont), 0) + 1
          INTO v_numero_diario
          FROM tb_venda
         WHERE dataVenda >= DATE(NEW.dataVenda)
           AND dataVenda <  DATE(NEW.dataVenda) + INTERVAL 1 DAY
           AND fk_documento NOT IN (3, 4, 5, 6);

        SET NEW.cont = v_numero_diario;
    END IF;
END$$

DELIMITER ;

-- Verificação: deve devolver 1 linha
SELECT TRIGGER_SCHEMA, TRIGGER_NAME, ACTION_TIMING, EVENT_MANIPULATION
  FROM information_schema.TRIGGERS
 WHERE TRIGGER_NAME = 'trg_venda_numero_diario'
   AND TRIGGER_SCHEMA = DATABASE();
