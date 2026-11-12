-- =====================================================================
-- Triggers do espelho kitanda_db_1 -> kitanda_db
-- Gerado em Wed Nov 11 14:36:03 WAT 2026 a partir da estrutura actual da BD
--
-- Pré-requisitos: as duas bases já existem com as MESMAS tabelas/colunas
-- (e de preferência com os mesmos dados). kitanda_db fica só com o trg_venda_numero_diario
-- (espelho passivo); em kitanda_db_1 ficam os BEFORE originais e os
-- esp_<tabela>_ai/_au/_ad que repetem cada INSERT/UPDATE/DELETE em kitanda_db.
-- Remove também os triggers do sentido contrário (kitanda_db -> kitanda_db_1):
-- os dois sentidos ao mesmo tempo dão erro 1442 e bloqueiam as gravações.
--
-- SEM ESPELHO (cada base tem as suas vendas): amortizacao, amortizacao_divida, detalhes_documento, forma_pagamento_item, notas_item, pagamento_mensalidade, tb_item_venda, tb_pagamento_credito, tb_venda
-- Contagem diária (trg_venda_numero_diario): PP/ND/NC/RE (fk_documento 3,4,5,6)
-- ficam com cont = 0 e não consomem número; pesquisa por intervalo em dataVenda.
-- Índice recomendado (uma vez, se ainda não existir):
--   ALTER TABLE `kitanda_db_1`.`tb_venda` ADD INDEX `idx_tb_venda_dataVenda` (`dataVenda`);
-- Após qualquer ALTER TABLE este script tem de ser gerado de novo.
-- Correr com: mysql -u root -p < este_ficheiro.sql  (ou SQLyog/Workbench)
-- =====================================================================

USE `kitanda_db_1`;

-- 0) Diagnóstico: triggers AFTER que não são deste script. O MySQL 5.1 só aceita
--    UM trigger por tabela/momento/evento; se o script falhar com o erro
--    "multiple triggers with the same action time and event", o culpado está nesta lista
--    (os conhecidos são removidos no passo 1a).
SELECT TRIGGER_SCHEMA, TRIGGER_NAME, EVENT_OBJECT_TABLE, ACTION_TIMING, EVENT_MANIPULATION
  FROM information_schema.TRIGGERS
 WHERE TRIGGER_SCHEMA IN ('kitanda_db', 'kitanda_db_1')
   AND ACTION_TIMING = 'AFTER'
   AND TRIGGER_NAME NOT LIKE 'esp\_%';

DELIMITER $$

-- 1a) Triggers do espelho antigo (trg_*_main_to_1 / trg_*_rev): só copiavam para a
--     outra base e actualizavam o stock_diario, o que o esp_tb_stock_au já faz.
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_stock_insert_rev`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_stock_update_rev`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_stock_delete_rev`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_produto_insert_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_produto_update_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_produto_delete_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_preco_insert_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_preco_update_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_tb_preco_delete_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_stock_insert_rev`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_stock_update_rev`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_stock_delete_rev`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_produto_insert_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_produto_update_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_produto_delete_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_preco_insert_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_preco_update_main_to_1`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_tb_preco_delete_main_to_1`$$

-- 1) Remover triggers antigos (kitanda_db inteiro, AFTER de kitanda_db_1, esp_*)
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_accesso_armazem_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_accesso_armazem_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_accesso_armazem_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_acerto_stock_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_acerto_stock_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_acerto_stock_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_agregado_familiar_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_agregado_familiar_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_agregado_familiar_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_amortizacao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_amortizacao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_amortizacao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_amortizacao_divida_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_amortizacao_divida_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_amortizacao_divida_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_anexos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_anexos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_anexos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_ano_economico_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_ano_economico_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_ano_economico_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_caixa_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_caixa_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_caixa_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_cambio_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_cambio_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_cambio_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_configuracao_mes_comeco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_configuracao_mes_comeco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_configuracao_mes_comeco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_movimentos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_movimentos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_movimentos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_operacoes_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_operacoes_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_operacoes_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_permissoes_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_permissoes_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_conta_permissoes_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_contas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_contas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_contas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_db_backup_schedule_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_db_backup_schedule_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_db_backup_schedule_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_deposito_bancario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_deposito_bancario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_deposito_bancario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_destino_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_destino_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_destino_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_detalhes_documento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_detalhes_documento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_detalhes_documento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_detalhes_documento_seccao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_detalhes_documento_seccao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_detalhes_documento_seccao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_documento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_documento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_documento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_empresa_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_empresa_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_empresa_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_entrada_tesouraria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_entrada_tesouraria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_entrada_tesouraria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_extrato_conta_cliente_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_extrato_conta_cliente_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_extrato_conta_cliente_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_extrato_conta_fornecedor_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_extrato_conta_fornecedor_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_extrato_conta_fornecedor_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_familia_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_familia_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_familia_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_fecho_contrato_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_fecho_contrato_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_fecho_contrato_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_fecho_periodo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_fecho_periodo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_fecho_periodo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_ficha_tecnica_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_ficha_tecnica_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_ficha_tecnica_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_forma_pagamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_forma_pagamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_forma_pagamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_forma_pagamento_item_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_forma_pagamento_item_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_forma_pagamento_item_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_grupo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_grupo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_grupo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_imposto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_imposto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_imposto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_caixa_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_caixa_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_caixa_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_recibo_rh_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_recibo_rh_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_recibo_rh_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_salario_extra_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_salario_extra_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_item_salario_extra_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_itens_nota_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_itens_nota_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_itens_nota_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_levantamento_bancario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_levantamento_bancario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_levantamento_bancario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_linha_ficha_tecnica_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_linha_ficha_tecnica_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_linha_ficha_tecnica_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_linha_transferencia_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_linha_transferencia_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_linha_transferencia_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_log_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_log_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_log_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_log_actividade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_log_actividade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_log_actividade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_lote_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_lote_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_lote_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_lote_movimento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_lote_movimento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_lote_movimento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_marca_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_marca_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_marca_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_master_table_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_master_table_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_master_table_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_modalidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_modalidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_modalidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_modelo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_modelo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_modelo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_moeda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_moeda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_moeda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_movimentacao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_movimentacao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_movimentacao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_multa_servico_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_multa_servico_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_multa_servico_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_item_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_item_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_item_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_item_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_item_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_notas_item_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pagamento_mensalidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pagamento_mensalidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pagamento_mensalidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pagamento_subsidio_feria_natal_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pagamento_subsidio_feria_natal_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pagamento_subsidio_feria_natal_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pedido_feria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pedido_feria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_pedido_feria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_previo_aviso_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_previo_aviso_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_previo_aviso_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_productos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_productos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_productos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produto_imposto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produto_imposto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produto_imposto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produto_isento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produto_isento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produto_isento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produtos_motivos_isensao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produtos_motivos_isensao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_produtos_motivos_isensao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_promocao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_promocao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_promocao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_recibo_rh_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_recibo_rh_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_recibo_rh_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_referencias_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_referencias_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_referencias_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_regime_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_regime_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_regime_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_relatorio_mensal_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_relatorio_mensal_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_relatorio_mensal_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_retencao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_retencao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_retencao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_saidas_tesouraria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_saidas_tesouraria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_saidas_tesouraria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_seccao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_seccao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_seccao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_servico_retencao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_servico_retencao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_servico_retencao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_stock_diario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_stock_diario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_stock_diario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_acerto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_acerto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_acerto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_adiantamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_adiantamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_adiantamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_ano_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_ano_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_ano_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_armazem_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_armazem_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_armazem_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_balanco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_balanco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_balanco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_banco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_banco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_banco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_cliente_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_cliente_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_cliente_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_clientes_encomenda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_clientes_encomenda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_clientes_encomenda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_conta_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_conta_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_conta_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_dados_instituicao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_dados_instituicao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_dados_instituicao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_departamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_departamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_departamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_desconto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_desconto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_desconto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_encomenda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_encomenda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_encomenda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_entrada_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_entrada_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_entrada_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_entrada_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_entrada_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_entrada_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_especialidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_especialidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_especialidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_estado_civil_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_estado_civil_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_estado_civil_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_estorno_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_estorno_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_estorno_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_falta_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_falta_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_falta_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_forma_pagamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_forma_pagamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_forma_pagamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_fornecedor_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_fornecedor_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_fornecedor_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_funcao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_funcao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_funcao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_funcionario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_funcionario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_funcionario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_grau_academico_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_grau_academico_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_grau_academico_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_desconto_funcionario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_desconto_funcionario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_desconto_funcionario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_encomenda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_encomenda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_encomenda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_entrada_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_entrada_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_entrada_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_entradas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_entradas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_entradas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_estorno_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_estorno_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_estorno_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_pedidos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_pedidos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_pedidos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_permissao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_permissao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_permissao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_pro_forma_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_pro_forma_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_pro_forma_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_saida_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_saida_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_saida_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_saidas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_saidas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_saidas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_subsidios_funcionario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_subsidios_funcionario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_subsidios_funcionario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_venda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_venda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_item_venda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_local_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_local_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_local_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_lugares_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_lugares_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_lugares_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_mes_rh_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_mes_rh_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_mes_rh_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_mesas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_mesas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_mesas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_municipio_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_municipio_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_municipio_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_operacao_sistema_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_operacao_sistema_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_operacao_sistema_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pagamento_credito_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pagamento_credito_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pagamento_credito_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pais_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pais_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pais_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_parametros_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_parametros_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_parametros_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pedido_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pedido_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pedido_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_permissao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_permissao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_permissao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_preco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_preco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_preco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pro_forma_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pro_forma_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_pro_forma_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_produto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_produto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_produto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_profissao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_profissao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_profissao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_provincia_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_provincia_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_provincia_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_saida_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_saida_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_saida_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_saidas_produtos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_saidas_produtos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_saidas_produtos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_salario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_salario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_salario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_sexo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_sexo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_sexo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_status_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_status_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_status_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_stock_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_stock_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_stock_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_stock_mirrow_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_stock_mirrow_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_stock_mirrow_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_subsidios_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_subsidios_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_subsidios_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tempo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tempo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tempo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tipo_produto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tipo_produto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tipo_produto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tipo_usuario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tipo_usuario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_tipo_usuario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_usuario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_usuario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_usuario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_validade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_validade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_validade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_venda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_venda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tb_venda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tipo_conta_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tipo_conta_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_tipo_conta_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_transferencia_armazem_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_transferencia_armazem_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_transferencia_armazem_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_transferencia_bancaria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_transferencia_bancaria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_transferencia_bancaria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_turno_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_turno_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_turno_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_unidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_unidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`esp_unidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db_1`.`trg_venda_numero_diario`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_accesso_armazem_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_accesso_armazem_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_accesso_armazem_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_acerto_stock_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_acerto_stock_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_acerto_stock_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_agregado_familiar_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_agregado_familiar_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_agregado_familiar_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_amortizacao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_amortizacao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_amortizacao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_amortizacao_divida_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_amortizacao_divida_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_amortizacao_divida_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_anexos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_anexos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_anexos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_ano_economico_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_ano_economico_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_ano_economico_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_caixa_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_caixa_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_caixa_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_cambio_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_cambio_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_cambio_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_configuracao_mes_comeco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_configuracao_mes_comeco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_configuracao_mes_comeco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_movimentos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_movimentos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_movimentos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_operacoes_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_operacoes_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_operacoes_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_permissoes_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_permissoes_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_conta_permissoes_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_contas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_contas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_contas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_db_backup_schedule_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_db_backup_schedule_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_db_backup_schedule_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_deposito_bancario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_deposito_bancario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_deposito_bancario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_destino_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_destino_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_destino_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_detalhes_documento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_detalhes_documento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_detalhes_documento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_detalhes_documento_seccao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_detalhes_documento_seccao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_detalhes_documento_seccao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_documento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_documento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_documento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_empresa_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_empresa_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_empresa_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_entrada_tesouraria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_entrada_tesouraria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_entrada_tesouraria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_extrato_conta_cliente_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_extrato_conta_cliente_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_extrato_conta_cliente_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_extrato_conta_fornecedor_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_extrato_conta_fornecedor_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_extrato_conta_fornecedor_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_familia_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_familia_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_familia_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_fecho_contrato_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_fecho_contrato_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_fecho_contrato_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_fecho_periodo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_fecho_periodo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_fecho_periodo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_ficha_tecnica_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_ficha_tecnica_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_ficha_tecnica_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_forma_pagamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_forma_pagamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_forma_pagamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_forma_pagamento_item_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_forma_pagamento_item_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_forma_pagamento_item_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_grupo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_grupo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_grupo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_imposto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_imposto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_imposto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_caixa_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_caixa_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_caixa_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_recibo_rh_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_recibo_rh_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_recibo_rh_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_salario_extra_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_salario_extra_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_item_salario_extra_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_itens_nota_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_itens_nota_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_itens_nota_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_levantamento_bancario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_levantamento_bancario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_levantamento_bancario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_linha_ficha_tecnica_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_linha_ficha_tecnica_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_linha_ficha_tecnica_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_linha_transferencia_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_linha_transferencia_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_linha_transferencia_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_log_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_log_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_log_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_log_actividade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_log_actividade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_log_actividade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_lote_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_lote_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_lote_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_lote_movimento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_lote_movimento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_lote_movimento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_marca_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_marca_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_marca_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_master_table_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_master_table_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_master_table_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_modalidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_modalidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_modalidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_modelo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_modelo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_modelo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_moeda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_moeda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_moeda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_movimentacao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_movimentacao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_movimentacao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_multa_servico_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_multa_servico_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_multa_servico_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_item_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_item_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_item_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_item_compras_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_item_compras_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_notas_item_compras_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pagamento_mensalidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pagamento_mensalidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pagamento_mensalidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pagamento_subsidio_feria_natal_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pagamento_subsidio_feria_natal_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pagamento_subsidio_feria_natal_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pedido_feria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pedido_feria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_pedido_feria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_previo_aviso_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_previo_aviso_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_previo_aviso_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_productos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_productos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_productos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produto_imposto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produto_imposto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produto_imposto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produto_isento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produto_isento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produto_isento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produtos_motivos_isensao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produtos_motivos_isensao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_produtos_motivos_isensao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_promocao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_promocao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_promocao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_recibo_rh_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_recibo_rh_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_recibo_rh_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_referencias_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_referencias_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_referencias_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_regime_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_regime_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_regime_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_relatorio_mensal_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_relatorio_mensal_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_relatorio_mensal_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_retencao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_retencao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_retencao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_saidas_tesouraria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_saidas_tesouraria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_saidas_tesouraria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_seccao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_seccao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_seccao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_servico_retencao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_servico_retencao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_servico_retencao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_stock_diario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_stock_diario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_stock_diario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_acerto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_acerto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_acerto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_adiantamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_adiantamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_adiantamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_ano_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_ano_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_ano_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_armazem_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_armazem_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_armazem_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_balanco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_balanco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_balanco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_banco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_banco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_banco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_cliente_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_cliente_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_cliente_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_clientes_encomenda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_clientes_encomenda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_clientes_encomenda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_conta_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_conta_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_conta_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_dados_instituicao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_dados_instituicao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_dados_instituicao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_departamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_departamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_departamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_desconto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_desconto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_desconto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_encomenda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_encomenda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_encomenda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_entrada_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_entrada_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_entrada_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_entrada_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_entrada_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_entrada_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_especialidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_especialidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_especialidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_estado_civil_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_estado_civil_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_estado_civil_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_estorno_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_estorno_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_estorno_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_falta_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_falta_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_falta_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_forma_pagamento_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_forma_pagamento_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_forma_pagamento_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_fornecedor_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_fornecedor_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_fornecedor_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_funcao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_funcao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_funcao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_funcionario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_funcionario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_funcionario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_grau_academico_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_grau_academico_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_grau_academico_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_desconto_funcionario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_desconto_funcionario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_desconto_funcionario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_encomenda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_encomenda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_encomenda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_entrada_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_entrada_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_entrada_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_entradas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_entradas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_entradas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_estorno_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_estorno_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_estorno_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_pedidos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_pedidos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_pedidos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_permissao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_permissao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_permissao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_pro_forma_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_pro_forma_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_pro_forma_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_saida_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_saida_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_saida_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_saidas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_saidas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_saidas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_subsidios_funcionario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_subsidios_funcionario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_subsidios_funcionario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_venda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_venda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_item_venda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_local_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_local_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_local_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_lugares_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_lugares_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_lugares_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_mes_rh_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_mes_rh_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_mes_rh_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_mesas_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_mesas_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_mesas_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_municipio_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_municipio_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_municipio_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_operacao_sistema_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_operacao_sistema_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_operacao_sistema_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pagamento_credito_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pagamento_credito_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pagamento_credito_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pais_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pais_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pais_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_parametros_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_parametros_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_parametros_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pedido_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pedido_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pedido_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_permissao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_permissao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_permissao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_preco_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_preco_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_preco_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pro_forma_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pro_forma_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_pro_forma_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_produto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_produto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_produto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_profissao_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_profissao_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_profissao_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_provincia_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_provincia_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_provincia_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_saida_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_saida_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_saida_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_saidas_produtos_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_saidas_produtos_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_saidas_produtos_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_salario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_salario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_salario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_sexo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_sexo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_sexo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_status_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_status_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_status_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_stock_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_stock_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_stock_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_stock_mirrow_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_stock_mirrow_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_stock_mirrow_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_subsidios_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_subsidios_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_subsidios_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tempo_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tempo_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tempo_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tipo_produto_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tipo_produto_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tipo_produto_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tipo_usuario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tipo_usuario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_tipo_usuario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_usuario_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_usuario_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_usuario_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_validade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_validade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_validade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_vasilhame_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_vasilhame_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_vasilhame_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_venda_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_venda_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tb_venda_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tipo_conta_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tipo_conta_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_tipo_conta_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_transferencia_armazem_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_transferencia_armazem_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_transferencia_armazem_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_transferencia_bancaria_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_transferencia_bancaria_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_transferencia_bancaria_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_turno_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_turno_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_turno_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_unidade_ai`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_unidade_au`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`esp_unidade_ad`$$
DROP TRIGGER IF EXISTS `kitanda_db`.`trg_venda_numero_diario`$$

-- 2) Triggers BEFORE de kitanda_db_1
CREATE TRIGGER `kitanda_db_1`.`trg_venda_numero_diario` BEFORE INSERT ON `kitanda_db_1`.`tb_venda` FOR EACH ROW
BEGIN
    DECLARE v_numero_diario INT;

    SET NEW.dataVenda = NOW(); -- força data/hora actual

    IF NEW.fk_documento IN (3, 4, 5, 6) THEN
        -- PP, ND, NC, RE: fora da contagem diária
        SET NEW.cont = 0;
    ELSE
        SELECT IFNULL(MAX(cont), 0) + 1
          INTO v_numero_diario
          FROM `kitanda_db_1`.`tb_venda`
         WHERE dataVenda >= CURDATE()
           AND dataVenda <  CURDATE() + INTERVAL 1 DAY
           AND fk_documento NOT IN (3, 4, 5, 6);

        SET NEW.cont = v_numero_diario;
    END IF;
END$$

-- 2b) kitanda_db também precisa da contagem diária (as vendas não são espelhadas)
CREATE TRIGGER `kitanda_db`.`trg_venda_numero_diario` BEFORE INSERT ON `kitanda_db`.`tb_venda` FOR EACH ROW
BEGIN
    DECLARE v_numero_diario INT;

    SET NEW.dataVenda = NOW(); -- força data/hora actual

    IF NEW.fk_documento IN (3, 4, 5, 6) THEN
        -- PP, ND, NC, RE: fora da contagem diária
        SET NEW.cont = 0;
    ELSE
        SELECT IFNULL(MAX(cont), 0) + 1
          INTO v_numero_diario
          FROM `kitanda_db`.`tb_venda`
         WHERE dataVenda >= CURDATE()
           AND dataVenda <  CURDATE() + INTERVAL 1 DAY
           AND fk_documento NOT IN (3, 4, 5, 6);

        SET NEW.cont = v_numero_diario;
    END IF;
END$$

-- 3) Triggers do espelho (411)
CREATE TRIGGER `kitanda_db_1`.`esp_accesso_armazem_ai` AFTER INSERT ON `kitanda_db_1`.`accesso_armazem` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`accesso_armazem` (`pk_accesso_armazem`, `fk_usuario`, `fk_armazem`, `data_time`) VALUES (NEW.`pk_accesso_armazem`, NEW.`fk_usuario`, NEW.`fk_armazem`, NEW.`data_time`) ON DUPLICATE KEY UPDATE `pk_accesso_armazem` = VALUES(`pk_accesso_armazem`), `fk_usuario` = VALUES(`fk_usuario`), `fk_armazem` = VALUES(`fk_armazem`), `data_time` = VALUES(`data_time`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_accesso_armazem_au` AFTER UPDATE ON `kitanda_db_1`.`accesso_armazem` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`accesso_armazem` SET `pk_accesso_armazem` = NEW.`pk_accesso_armazem`, `fk_usuario` = NEW.`fk_usuario`, `fk_armazem` = NEW.`fk_armazem`, `data_time` = NEW.`data_time` WHERE `pk_accesso_armazem` = OLD.`pk_accesso_armazem`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`accesso_armazem` (`pk_accesso_armazem`, `fk_usuario`, `fk_armazem`, `data_time`) VALUES (NEW.`pk_accesso_armazem`, NEW.`fk_usuario`, NEW.`fk_armazem`, NEW.`data_time`) ON DUPLICATE KEY UPDATE `pk_accesso_armazem` = VALUES(`pk_accesso_armazem`), `fk_usuario` = VALUES(`fk_usuario`), `fk_armazem` = VALUES(`fk_armazem`), `data_time` = VALUES(`data_time`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_accesso_armazem_ad` AFTER DELETE ON `kitanda_db_1`.`accesso_armazem` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`accesso_armazem` WHERE `pk_accesso_armazem` = OLD.`pk_accesso_armazem`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_acerto_stock_ai` AFTER INSERT ON `kitanda_db_1`.`acerto_stock` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`acerto_stock` (`pk_acerto_stock`, `data_hora`, `cod_produto`, `designacao_produto`, `cod_usuario`, `nome_usuario`, `qtd_anterior`, `qtd_acerto`, `qtd_depois`, `cod_armazem`, `designcao_armazem`, `motivo_acerto`) VALUES (NEW.`pk_acerto_stock`, NEW.`data_hora`, NEW.`cod_produto`, NEW.`designacao_produto`, NEW.`cod_usuario`, NEW.`nome_usuario`, NEW.`qtd_anterior`, NEW.`qtd_acerto`, NEW.`qtd_depois`, NEW.`cod_armazem`, NEW.`designcao_armazem`, NEW.`motivo_acerto`) ON DUPLICATE KEY UPDATE `pk_acerto_stock` = VALUES(`pk_acerto_stock`), `data_hora` = VALUES(`data_hora`), `cod_produto` = VALUES(`cod_produto`), `designacao_produto` = VALUES(`designacao_produto`), `cod_usuario` = VALUES(`cod_usuario`), `nome_usuario` = VALUES(`nome_usuario`), `qtd_anterior` = VALUES(`qtd_anterior`), `qtd_acerto` = VALUES(`qtd_acerto`), `qtd_depois` = VALUES(`qtd_depois`), `cod_armazem` = VALUES(`cod_armazem`), `designcao_armazem` = VALUES(`designcao_armazem`), `motivo_acerto` = VALUES(`motivo_acerto`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_acerto_stock_au` AFTER UPDATE ON `kitanda_db_1`.`acerto_stock` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`acerto_stock` SET `pk_acerto_stock` = NEW.`pk_acerto_stock`, `data_hora` = NEW.`data_hora`, `cod_produto` = NEW.`cod_produto`, `designacao_produto` = NEW.`designacao_produto`, `cod_usuario` = NEW.`cod_usuario`, `nome_usuario` = NEW.`nome_usuario`, `qtd_anterior` = NEW.`qtd_anterior`, `qtd_acerto` = NEW.`qtd_acerto`, `qtd_depois` = NEW.`qtd_depois`, `cod_armazem` = NEW.`cod_armazem`, `designcao_armazem` = NEW.`designcao_armazem`, `motivo_acerto` = NEW.`motivo_acerto` WHERE `pk_acerto_stock` = OLD.`pk_acerto_stock`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`acerto_stock` (`pk_acerto_stock`, `data_hora`, `cod_produto`, `designacao_produto`, `cod_usuario`, `nome_usuario`, `qtd_anterior`, `qtd_acerto`, `qtd_depois`, `cod_armazem`, `designcao_armazem`, `motivo_acerto`) VALUES (NEW.`pk_acerto_stock`, NEW.`data_hora`, NEW.`cod_produto`, NEW.`designacao_produto`, NEW.`cod_usuario`, NEW.`nome_usuario`, NEW.`qtd_anterior`, NEW.`qtd_acerto`, NEW.`qtd_depois`, NEW.`cod_armazem`, NEW.`designcao_armazem`, NEW.`motivo_acerto`) ON DUPLICATE KEY UPDATE `pk_acerto_stock` = VALUES(`pk_acerto_stock`), `data_hora` = VALUES(`data_hora`), `cod_produto` = VALUES(`cod_produto`), `designacao_produto` = VALUES(`designacao_produto`), `cod_usuario` = VALUES(`cod_usuario`), `nome_usuario` = VALUES(`nome_usuario`), `qtd_anterior` = VALUES(`qtd_anterior`), `qtd_acerto` = VALUES(`qtd_acerto`), `qtd_depois` = VALUES(`qtd_depois`), `cod_armazem` = VALUES(`cod_armazem`), `designcao_armazem` = VALUES(`designcao_armazem`), `motivo_acerto` = VALUES(`motivo_acerto`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_acerto_stock_ad` AFTER DELETE ON `kitanda_db_1`.`acerto_stock` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`acerto_stock` WHERE `pk_acerto_stock` = OLD.`pk_acerto_stock`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_agregado_familiar_ai` AFTER INSERT ON `kitanda_db_1`.`agregado_familiar` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`agregado_familiar` (`pk_agregado_familiar`, `nome_filho`, `data_nascimento`, `valor`, `fk_funcionario`) VALUES (NEW.`pk_agregado_familiar`, NEW.`nome_filho`, NEW.`data_nascimento`, NEW.`valor`, NEW.`fk_funcionario`) ON DUPLICATE KEY UPDATE `pk_agregado_familiar` = VALUES(`pk_agregado_familiar`), `nome_filho` = VALUES(`nome_filho`), `data_nascimento` = VALUES(`data_nascimento`), `valor` = VALUES(`valor`), `fk_funcionario` = VALUES(`fk_funcionario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_agregado_familiar_au` AFTER UPDATE ON `kitanda_db_1`.`agregado_familiar` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`agregado_familiar` SET `pk_agregado_familiar` = NEW.`pk_agregado_familiar`, `nome_filho` = NEW.`nome_filho`, `data_nascimento` = NEW.`data_nascimento`, `valor` = NEW.`valor`, `fk_funcionario` = NEW.`fk_funcionario` WHERE `pk_agregado_familiar` = OLD.`pk_agregado_familiar`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`agregado_familiar` (`pk_agregado_familiar`, `nome_filho`, `data_nascimento`, `valor`, `fk_funcionario`) VALUES (NEW.`pk_agregado_familiar`, NEW.`nome_filho`, NEW.`data_nascimento`, NEW.`valor`, NEW.`fk_funcionario`) ON DUPLICATE KEY UPDATE `pk_agregado_familiar` = VALUES(`pk_agregado_familiar`), `nome_filho` = VALUES(`nome_filho`), `data_nascimento` = VALUES(`data_nascimento`), `valor` = VALUES(`valor`), `fk_funcionario` = VALUES(`fk_funcionario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_agregado_familiar_ad` AFTER DELETE ON `kitanda_db_1`.`agregado_familiar` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`agregado_familiar` WHERE `pk_agregado_familiar` = OLD.`pk_agregado_familiar`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_anexos_ai` AFTER INSERT ON `kitanda_db_1`.`anexos` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`anexos` (`pk_anexos`, `data_hora`, `nome_ficheiro`, `descricao`, `fk_funcionario`, `fk_usuario`, `caminho`, `caminho_ficheiro`) VALUES (NEW.`pk_anexos`, NEW.`data_hora`, NEW.`nome_ficheiro`, NEW.`descricao`, NEW.`fk_funcionario`, NEW.`fk_usuario`, NEW.`caminho`, NEW.`caminho_ficheiro`) ON DUPLICATE KEY UPDATE `pk_anexos` = VALUES(`pk_anexos`), `data_hora` = VALUES(`data_hora`), `nome_ficheiro` = VALUES(`nome_ficheiro`), `descricao` = VALUES(`descricao`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`), `caminho` = VALUES(`caminho`), `caminho_ficheiro` = VALUES(`caminho_ficheiro`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_anexos_au` AFTER UPDATE ON `kitanda_db_1`.`anexos` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`anexos` SET `pk_anexos` = NEW.`pk_anexos`, `data_hora` = NEW.`data_hora`, `nome_ficheiro` = NEW.`nome_ficheiro`, `descricao` = NEW.`descricao`, `fk_funcionario` = NEW.`fk_funcionario`, `fk_usuario` = NEW.`fk_usuario`, `caminho` = NEW.`caminho`, `caminho_ficheiro` = NEW.`caminho_ficheiro` WHERE `pk_anexos` = OLD.`pk_anexos`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`anexos` (`pk_anexos`, `data_hora`, `nome_ficheiro`, `descricao`, `fk_funcionario`, `fk_usuario`, `caminho`, `caminho_ficheiro`) VALUES (NEW.`pk_anexos`, NEW.`data_hora`, NEW.`nome_ficheiro`, NEW.`descricao`, NEW.`fk_funcionario`, NEW.`fk_usuario`, NEW.`caminho`, NEW.`caminho_ficheiro`) ON DUPLICATE KEY UPDATE `pk_anexos` = VALUES(`pk_anexos`), `data_hora` = VALUES(`data_hora`), `nome_ficheiro` = VALUES(`nome_ficheiro`), `descricao` = VALUES(`descricao`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`), `caminho` = VALUES(`caminho`), `caminho_ficheiro` = VALUES(`caminho_ficheiro`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_anexos_ad` AFTER DELETE ON `kitanda_db_1`.`anexos` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`anexos` WHERE `pk_anexos` = OLD.`pk_anexos`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_ano_economico_ai` AFTER INSERT ON `kitanda_db_1`.`ano_economico` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`ano_economico` (`pk_ano_economico`, `designacao`, `serie`, `data_inicio`, `data_fim`) VALUES (NEW.`pk_ano_economico`, NEW.`designacao`, NEW.`serie`, NEW.`data_inicio`, NEW.`data_fim`) ON DUPLICATE KEY UPDATE `pk_ano_economico` = VALUES(`pk_ano_economico`), `designacao` = VALUES(`designacao`), `serie` = VALUES(`serie`), `data_inicio` = VALUES(`data_inicio`), `data_fim` = VALUES(`data_fim`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_ano_economico_au` AFTER UPDATE ON `kitanda_db_1`.`ano_economico` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`ano_economico` SET `pk_ano_economico` = NEW.`pk_ano_economico`, `designacao` = NEW.`designacao`, `serie` = NEW.`serie`, `data_inicio` = NEW.`data_inicio`, `data_fim` = NEW.`data_fim` WHERE `pk_ano_economico` = OLD.`pk_ano_economico`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`ano_economico` (`pk_ano_economico`, `designacao`, `serie`, `data_inicio`, `data_fim`) VALUES (NEW.`pk_ano_economico`, NEW.`designacao`, NEW.`serie`, NEW.`data_inicio`, NEW.`data_fim`) ON DUPLICATE KEY UPDATE `pk_ano_economico` = VALUES(`pk_ano_economico`), `designacao` = VALUES(`designacao`), `serie` = VALUES(`serie`), `data_inicio` = VALUES(`data_inicio`), `data_fim` = VALUES(`data_fim`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_ano_economico_ad` AFTER DELETE ON `kitanda_db_1`.`ano_economico` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`ano_economico` WHERE `pk_ano_economico` = OLD.`pk_ano_economico`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_caixa_ai` AFTER INSERT ON `kitanda_db_1`.`caixa` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`caixa` (`pk_caixa`, `data_abertura`, `data_fecho`, `total_vendas`, `numero_vendas`, `valor_inicial`, `usuario_fecho`, `usuario_abertura`, `cod_usuario_abertura`, `cod_usuario_fecho`, `total_desconto`, `total_iva`, `total_iliquido`) VALUES (NEW.`pk_caixa`, NEW.`data_abertura`, NEW.`data_fecho`, NEW.`total_vendas`, NEW.`numero_vendas`, NEW.`valor_inicial`, NEW.`usuario_fecho`, NEW.`usuario_abertura`, NEW.`cod_usuario_abertura`, NEW.`cod_usuario_fecho`, NEW.`total_desconto`, NEW.`total_iva`, NEW.`total_iliquido`) ON DUPLICATE KEY UPDATE `pk_caixa` = VALUES(`pk_caixa`), `data_abertura` = VALUES(`data_abertura`), `data_fecho` = VALUES(`data_fecho`), `total_vendas` = VALUES(`total_vendas`), `numero_vendas` = VALUES(`numero_vendas`), `valor_inicial` = VALUES(`valor_inicial`), `usuario_fecho` = VALUES(`usuario_fecho`), `usuario_abertura` = VALUES(`usuario_abertura`), `cod_usuario_abertura` = VALUES(`cod_usuario_abertura`), `cod_usuario_fecho` = VALUES(`cod_usuario_fecho`), `total_desconto` = VALUES(`total_desconto`), `total_iva` = VALUES(`total_iva`), `total_iliquido` = VALUES(`total_iliquido`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_caixa_au` AFTER UPDATE ON `kitanda_db_1`.`caixa` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`caixa` SET `pk_caixa` = NEW.`pk_caixa`, `data_abertura` = NEW.`data_abertura`, `data_fecho` = NEW.`data_fecho`, `total_vendas` = NEW.`total_vendas`, `numero_vendas` = NEW.`numero_vendas`, `valor_inicial` = NEW.`valor_inicial`, `usuario_fecho` = NEW.`usuario_fecho`, `usuario_abertura` = NEW.`usuario_abertura`, `cod_usuario_abertura` = NEW.`cod_usuario_abertura`, `cod_usuario_fecho` = NEW.`cod_usuario_fecho`, `total_desconto` = NEW.`total_desconto`, `total_iva` = NEW.`total_iva`, `total_iliquido` = NEW.`total_iliquido` WHERE `pk_caixa` = OLD.`pk_caixa`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`caixa` (`pk_caixa`, `data_abertura`, `data_fecho`, `total_vendas`, `numero_vendas`, `valor_inicial`, `usuario_fecho`, `usuario_abertura`, `cod_usuario_abertura`, `cod_usuario_fecho`, `total_desconto`, `total_iva`, `total_iliquido`) VALUES (NEW.`pk_caixa`, NEW.`data_abertura`, NEW.`data_fecho`, NEW.`total_vendas`, NEW.`numero_vendas`, NEW.`valor_inicial`, NEW.`usuario_fecho`, NEW.`usuario_abertura`, NEW.`cod_usuario_abertura`, NEW.`cod_usuario_fecho`, NEW.`total_desconto`, NEW.`total_iva`, NEW.`total_iliquido`) ON DUPLICATE KEY UPDATE `pk_caixa` = VALUES(`pk_caixa`), `data_abertura` = VALUES(`data_abertura`), `data_fecho` = VALUES(`data_fecho`), `total_vendas` = VALUES(`total_vendas`), `numero_vendas` = VALUES(`numero_vendas`), `valor_inicial` = VALUES(`valor_inicial`), `usuario_fecho` = VALUES(`usuario_fecho`), `usuario_abertura` = VALUES(`usuario_abertura`), `cod_usuario_abertura` = VALUES(`cod_usuario_abertura`), `cod_usuario_fecho` = VALUES(`cod_usuario_fecho`), `total_desconto` = VALUES(`total_desconto`), `total_iva` = VALUES(`total_iva`), `total_iliquido` = VALUES(`total_iliquido`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_caixa_ad` AFTER DELETE ON `kitanda_db_1`.`caixa` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`caixa` WHERE `pk_caixa` = OLD.`pk_caixa`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_cambio_ai` AFTER INSERT ON `kitanda_db_1`.`cambio` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`cambio` (`pk_cambio`, `valor`, `data_hora`, `fk_moeda`) VALUES (NEW.`pk_cambio`, NEW.`valor`, NEW.`data_hora`, NEW.`fk_moeda`) ON DUPLICATE KEY UPDATE `pk_cambio` = VALUES(`pk_cambio`), `valor` = VALUES(`valor`), `data_hora` = VALUES(`data_hora`), `fk_moeda` = VALUES(`fk_moeda`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_cambio_au` AFTER UPDATE ON `kitanda_db_1`.`cambio` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`cambio` SET `pk_cambio` = NEW.`pk_cambio`, `valor` = NEW.`valor`, `data_hora` = NEW.`data_hora`, `fk_moeda` = NEW.`fk_moeda` WHERE `pk_cambio` = OLD.`pk_cambio`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`cambio` (`pk_cambio`, `valor`, `data_hora`, `fk_moeda`) VALUES (NEW.`pk_cambio`, NEW.`valor`, NEW.`data_hora`, NEW.`fk_moeda`) ON DUPLICATE KEY UPDATE `pk_cambio` = VALUES(`pk_cambio`), `valor` = VALUES(`valor`), `data_hora` = VALUES(`data_hora`), `fk_moeda` = VALUES(`fk_moeda`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_cambio_ad` AFTER DELETE ON `kitanda_db_1`.`cambio` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`cambio` WHERE `pk_cambio` = OLD.`pk_cambio`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_compras_ai` AFTER INSERT ON `kitanda_db_1`.`compras` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`compras` (`pk_compra`, `data_compra`, `total_compra`, `nome_fornecedor`, `hash_cod`, `cod_fact`, `ref_cod_fact`, `total_iva`, `assinatura`, `total_por_extenso`, `codigo_usuario`, `desconto_comercial`, `desconto_financeiro`, `desconto_total`, `total_incidencia`, `obs`, `total_geral`, `valor_entregue`, `troco`, `total_incidencia_isento`, `data_limite_levantamento`, `fk_documento`, `fk_fornecedor`, `idArmazemFK`, `fornecedor_nif`, `fk_ano_economico`, `autorizado`, `despacho_encomenda`, `encomendado`, `status_eliminado`, `status_recibo`, `valor_por_pagar`, `valor_pago`, `doc_vosso`, `doc_vosso_numero`, `doc_vosso_data`, `doc_vosso_data_vencimento`) VALUES (NEW.`pk_compra`, NEW.`data_compra`, NEW.`total_compra`, NEW.`nome_fornecedor`, NEW.`hash_cod`, NEW.`cod_fact`, NEW.`ref_cod_fact`, NEW.`total_iva`, NEW.`assinatura`, NEW.`total_por_extenso`, NEW.`codigo_usuario`, NEW.`desconto_comercial`, NEW.`desconto_financeiro`, NEW.`desconto_total`, NEW.`total_incidencia`, NEW.`obs`, NEW.`total_geral`, NEW.`valor_entregue`, NEW.`troco`, NEW.`total_incidencia_isento`, NEW.`data_limite_levantamento`, NEW.`fk_documento`, NEW.`fk_fornecedor`, NEW.`idArmazemFK`, NEW.`fornecedor_nif`, NEW.`fk_ano_economico`, NEW.`autorizado`, NEW.`despacho_encomenda`, NEW.`encomendado`, NEW.`status_eliminado`, NEW.`status_recibo`, NEW.`valor_por_pagar`, NEW.`valor_pago`, NEW.`doc_vosso`, NEW.`doc_vosso_numero`, NEW.`doc_vosso_data`, NEW.`doc_vosso_data_vencimento`) ON DUPLICATE KEY UPDATE `pk_compra` = VALUES(`pk_compra`), `data_compra` = VALUES(`data_compra`), `total_compra` = VALUES(`total_compra`), `nome_fornecedor` = VALUES(`nome_fornecedor`), `hash_cod` = VALUES(`hash_cod`), `cod_fact` = VALUES(`cod_fact`), `ref_cod_fact` = VALUES(`ref_cod_fact`), `total_iva` = VALUES(`total_iva`), `assinatura` = VALUES(`assinatura`), `total_por_extenso` = VALUES(`total_por_extenso`), `codigo_usuario` = VALUES(`codigo_usuario`), `desconto_comercial` = VALUES(`desconto_comercial`), `desconto_financeiro` = VALUES(`desconto_financeiro`), `desconto_total` = VALUES(`desconto_total`), `total_incidencia` = VALUES(`total_incidencia`), `obs` = VALUES(`obs`), `total_geral` = VALUES(`total_geral`), `valor_entregue` = VALUES(`valor_entregue`), `troco` = VALUES(`troco`), `total_incidencia_isento` = VALUES(`total_incidencia_isento`), `data_limite_levantamento` = VALUES(`data_limite_levantamento`), `fk_documento` = VALUES(`fk_documento`), `fk_fornecedor` = VALUES(`fk_fornecedor`), `idArmazemFK` = VALUES(`idArmazemFK`), `fornecedor_nif` = VALUES(`fornecedor_nif`), `fk_ano_economico` = VALUES(`fk_ano_economico`), `autorizado` = VALUES(`autorizado`), `despacho_encomenda` = VALUES(`despacho_encomenda`), `encomendado` = VALUES(`encomendado`), `status_eliminado` = VALUES(`status_eliminado`), `status_recibo` = VALUES(`status_recibo`), `valor_por_pagar` = VALUES(`valor_por_pagar`), `valor_pago` = VALUES(`valor_pago`), `doc_vosso` = VALUES(`doc_vosso`), `doc_vosso_numero` = VALUES(`doc_vosso_numero`), `doc_vosso_data` = VALUES(`doc_vosso_data`), `doc_vosso_data_vencimento` = VALUES(`doc_vosso_data_vencimento`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_compras_au` AFTER UPDATE ON `kitanda_db_1`.`compras` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`compras` SET `pk_compra` = NEW.`pk_compra`, `data_compra` = NEW.`data_compra`, `total_compra` = NEW.`total_compra`, `nome_fornecedor` = NEW.`nome_fornecedor`, `hash_cod` = NEW.`hash_cod`, `cod_fact` = NEW.`cod_fact`, `ref_cod_fact` = NEW.`ref_cod_fact`, `total_iva` = NEW.`total_iva`, `assinatura` = NEW.`assinatura`, `total_por_extenso` = NEW.`total_por_extenso`, `codigo_usuario` = NEW.`codigo_usuario`, `desconto_comercial` = NEW.`desconto_comercial`, `desconto_financeiro` = NEW.`desconto_financeiro`, `desconto_total` = NEW.`desconto_total`, `total_incidencia` = NEW.`total_incidencia`, `obs` = NEW.`obs`, `total_geral` = NEW.`total_geral`, `valor_entregue` = NEW.`valor_entregue`, `troco` = NEW.`troco`, `total_incidencia_isento` = NEW.`total_incidencia_isento`, `data_limite_levantamento` = NEW.`data_limite_levantamento`, `fk_documento` = NEW.`fk_documento`, `fk_fornecedor` = NEW.`fk_fornecedor`, `idArmazemFK` = NEW.`idArmazemFK`, `fornecedor_nif` = NEW.`fornecedor_nif`, `fk_ano_economico` = NEW.`fk_ano_economico`, `autorizado` = NEW.`autorizado`, `despacho_encomenda` = NEW.`despacho_encomenda`, `encomendado` = NEW.`encomendado`, `status_eliminado` = NEW.`status_eliminado`, `status_recibo` = NEW.`status_recibo`, `valor_por_pagar` = NEW.`valor_por_pagar`, `valor_pago` = NEW.`valor_pago`, `doc_vosso` = NEW.`doc_vosso`, `doc_vosso_numero` = NEW.`doc_vosso_numero`, `doc_vosso_data` = NEW.`doc_vosso_data`, `doc_vosso_data_vencimento` = NEW.`doc_vosso_data_vencimento` WHERE `pk_compra` = OLD.`pk_compra`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`compras` (`pk_compra`, `data_compra`, `total_compra`, `nome_fornecedor`, `hash_cod`, `cod_fact`, `ref_cod_fact`, `total_iva`, `assinatura`, `total_por_extenso`, `codigo_usuario`, `desconto_comercial`, `desconto_financeiro`, `desconto_total`, `total_incidencia`, `obs`, `total_geral`, `valor_entregue`, `troco`, `total_incidencia_isento`, `data_limite_levantamento`, `fk_documento`, `fk_fornecedor`, `idArmazemFK`, `fornecedor_nif`, `fk_ano_economico`, `autorizado`, `despacho_encomenda`, `encomendado`, `status_eliminado`, `status_recibo`, `valor_por_pagar`, `valor_pago`, `doc_vosso`, `doc_vosso_numero`, `doc_vosso_data`, `doc_vosso_data_vencimento`) VALUES (NEW.`pk_compra`, NEW.`data_compra`, NEW.`total_compra`, NEW.`nome_fornecedor`, NEW.`hash_cod`, NEW.`cod_fact`, NEW.`ref_cod_fact`, NEW.`total_iva`, NEW.`assinatura`, NEW.`total_por_extenso`, NEW.`codigo_usuario`, NEW.`desconto_comercial`, NEW.`desconto_financeiro`, NEW.`desconto_total`, NEW.`total_incidencia`, NEW.`obs`, NEW.`total_geral`, NEW.`valor_entregue`, NEW.`troco`, NEW.`total_incidencia_isento`, NEW.`data_limite_levantamento`, NEW.`fk_documento`, NEW.`fk_fornecedor`, NEW.`idArmazemFK`, NEW.`fornecedor_nif`, NEW.`fk_ano_economico`, NEW.`autorizado`, NEW.`despacho_encomenda`, NEW.`encomendado`, NEW.`status_eliminado`, NEW.`status_recibo`, NEW.`valor_por_pagar`, NEW.`valor_pago`, NEW.`doc_vosso`, NEW.`doc_vosso_numero`, NEW.`doc_vosso_data`, NEW.`doc_vosso_data_vencimento`) ON DUPLICATE KEY UPDATE `pk_compra` = VALUES(`pk_compra`), `data_compra` = VALUES(`data_compra`), `total_compra` = VALUES(`total_compra`), `nome_fornecedor` = VALUES(`nome_fornecedor`), `hash_cod` = VALUES(`hash_cod`), `cod_fact` = VALUES(`cod_fact`), `ref_cod_fact` = VALUES(`ref_cod_fact`), `total_iva` = VALUES(`total_iva`), `assinatura` = VALUES(`assinatura`), `total_por_extenso` = VALUES(`total_por_extenso`), `codigo_usuario` = VALUES(`codigo_usuario`), `desconto_comercial` = VALUES(`desconto_comercial`), `desconto_financeiro` = VALUES(`desconto_financeiro`), `desconto_total` = VALUES(`desconto_total`), `total_incidencia` = VALUES(`total_incidencia`), `obs` = VALUES(`obs`), `total_geral` = VALUES(`total_geral`), `valor_entregue` = VALUES(`valor_entregue`), `troco` = VALUES(`troco`), `total_incidencia_isento` = VALUES(`total_incidencia_isento`), `data_limite_levantamento` = VALUES(`data_limite_levantamento`), `fk_documento` = VALUES(`fk_documento`), `fk_fornecedor` = VALUES(`fk_fornecedor`), `idArmazemFK` = VALUES(`idArmazemFK`), `fornecedor_nif` = VALUES(`fornecedor_nif`), `fk_ano_economico` = VALUES(`fk_ano_economico`), `autorizado` = VALUES(`autorizado`), `despacho_encomenda` = VALUES(`despacho_encomenda`), `encomendado` = VALUES(`encomendado`), `status_eliminado` = VALUES(`status_eliminado`), `status_recibo` = VALUES(`status_recibo`), `valor_por_pagar` = VALUES(`valor_por_pagar`), `valor_pago` = VALUES(`valor_pago`), `doc_vosso` = VALUES(`doc_vosso`), `doc_vosso_numero` = VALUES(`doc_vosso_numero`), `doc_vosso_data` = VALUES(`doc_vosso_data`), `doc_vosso_data_vencimento` = VALUES(`doc_vosso_data_vencimento`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_compras_ad` AFTER DELETE ON `kitanda_db_1`.`compras` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`compras` WHERE `pk_compra` = OLD.`pk_compra`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_configuracao_mes_comeco_ai` AFTER INSERT ON `kitanda_db_1`.`configuracao_mes_comeco` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`configuracao_mes_comeco` (`id`, `data_cadastro`, `mes_id`, `produto_id`, `usuario_id`, `duracao`, `cliente_id`) VALUES (NEW.`id`, NEW.`data_cadastro`, NEW.`mes_id`, NEW.`produto_id`, NEW.`usuario_id`, NEW.`duracao`, NEW.`cliente_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `data_cadastro` = VALUES(`data_cadastro`), `mes_id` = VALUES(`mes_id`), `produto_id` = VALUES(`produto_id`), `usuario_id` = VALUES(`usuario_id`), `duracao` = VALUES(`duracao`), `cliente_id` = VALUES(`cliente_id`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_configuracao_mes_comeco_au` AFTER UPDATE ON `kitanda_db_1`.`configuracao_mes_comeco` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`configuracao_mes_comeco` SET `id` = NEW.`id`, `data_cadastro` = NEW.`data_cadastro`, `mes_id` = NEW.`mes_id`, `produto_id` = NEW.`produto_id`, `usuario_id` = NEW.`usuario_id`, `duracao` = NEW.`duracao`, `cliente_id` = NEW.`cliente_id` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`configuracao_mes_comeco` (`id`, `data_cadastro`, `mes_id`, `produto_id`, `usuario_id`, `duracao`, `cliente_id`) VALUES (NEW.`id`, NEW.`data_cadastro`, NEW.`mes_id`, NEW.`produto_id`, NEW.`usuario_id`, NEW.`duracao`, NEW.`cliente_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `data_cadastro` = VALUES(`data_cadastro`), `mes_id` = VALUES(`mes_id`), `produto_id` = VALUES(`produto_id`), `usuario_id` = VALUES(`usuario_id`), `duracao` = VALUES(`duracao`), `cliente_id` = VALUES(`cliente_id`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_configuracao_mes_comeco_ad` AFTER DELETE ON `kitanda_db_1`.`configuracao_mes_comeco` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`configuracao_mes_comeco` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_movimentos_ai` AFTER INSERT ON `kitanda_db_1`.`conta_movimentos` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`conta_movimentos` (`pk_conta_movimento`, `data_hora`, `conta_id`, `conta_designacao`, `saldo_anterior`, `valor_entrada`, `valor_saida`, `saldo_final`, `tipo`, `descricao`, `documento`, `cod_operacao`) VALUES (NEW.`pk_conta_movimento`, NEW.`data_hora`, NEW.`conta_id`, NEW.`conta_designacao`, NEW.`saldo_anterior`, NEW.`valor_entrada`, NEW.`valor_saida`, NEW.`saldo_final`, NEW.`tipo`, NEW.`descricao`, NEW.`documento`, NEW.`cod_operacao`) ON DUPLICATE KEY UPDATE `pk_conta_movimento` = VALUES(`pk_conta_movimento`), `data_hora` = VALUES(`data_hora`), `conta_id` = VALUES(`conta_id`), `conta_designacao` = VALUES(`conta_designacao`), `saldo_anterior` = VALUES(`saldo_anterior`), `valor_entrada` = VALUES(`valor_entrada`), `valor_saida` = VALUES(`valor_saida`), `saldo_final` = VALUES(`saldo_final`), `tipo` = VALUES(`tipo`), `descricao` = VALUES(`descricao`), `documento` = VALUES(`documento`), `cod_operacao` = VALUES(`cod_operacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_movimentos_au` AFTER UPDATE ON `kitanda_db_1`.`conta_movimentos` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`conta_movimentos` SET `pk_conta_movimento` = NEW.`pk_conta_movimento`, `data_hora` = NEW.`data_hora`, `conta_id` = NEW.`conta_id`, `conta_designacao` = NEW.`conta_designacao`, `saldo_anterior` = NEW.`saldo_anterior`, `valor_entrada` = NEW.`valor_entrada`, `valor_saida` = NEW.`valor_saida`, `saldo_final` = NEW.`saldo_final`, `tipo` = NEW.`tipo`, `descricao` = NEW.`descricao`, `documento` = NEW.`documento`, `cod_operacao` = NEW.`cod_operacao` WHERE `pk_conta_movimento` = OLD.`pk_conta_movimento`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`conta_movimentos` (`pk_conta_movimento`, `data_hora`, `conta_id`, `conta_designacao`, `saldo_anterior`, `valor_entrada`, `valor_saida`, `saldo_final`, `tipo`, `descricao`, `documento`, `cod_operacao`) VALUES (NEW.`pk_conta_movimento`, NEW.`data_hora`, NEW.`conta_id`, NEW.`conta_designacao`, NEW.`saldo_anterior`, NEW.`valor_entrada`, NEW.`valor_saida`, NEW.`saldo_final`, NEW.`tipo`, NEW.`descricao`, NEW.`documento`, NEW.`cod_operacao`) ON DUPLICATE KEY UPDATE `pk_conta_movimento` = VALUES(`pk_conta_movimento`), `data_hora` = VALUES(`data_hora`), `conta_id` = VALUES(`conta_id`), `conta_designacao` = VALUES(`conta_designacao`), `saldo_anterior` = VALUES(`saldo_anterior`), `valor_entrada` = VALUES(`valor_entrada`), `valor_saida` = VALUES(`valor_saida`), `saldo_final` = VALUES(`saldo_final`), `tipo` = VALUES(`tipo`), `descricao` = VALUES(`descricao`), `documento` = VALUES(`documento`), `cod_operacao` = VALUES(`cod_operacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_movimentos_ad` AFTER DELETE ON `kitanda_db_1`.`conta_movimentos` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`conta_movimentos` WHERE `pk_conta_movimento` = OLD.`pk_conta_movimento`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_operacoes_ai` AFTER INSERT ON `kitanda_db_1`.`conta_operacoes` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`conta_operacoes` (`pk_conta_operacao`, `data_hora`, `tipo`, `valor`, `user_id`, `user_name`, `beneficiario`) VALUES (NEW.`pk_conta_operacao`, NEW.`data_hora`, NEW.`tipo`, NEW.`valor`, NEW.`user_id`, NEW.`user_name`, NEW.`beneficiario`) ON DUPLICATE KEY UPDATE `pk_conta_operacao` = VALUES(`pk_conta_operacao`), `data_hora` = VALUES(`data_hora`), `tipo` = VALUES(`tipo`), `valor` = VALUES(`valor`), `user_id` = VALUES(`user_id`), `user_name` = VALUES(`user_name`), `beneficiario` = VALUES(`beneficiario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_operacoes_au` AFTER UPDATE ON `kitanda_db_1`.`conta_operacoes` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`conta_operacoes` SET `pk_conta_operacao` = NEW.`pk_conta_operacao`, `data_hora` = NEW.`data_hora`, `tipo` = NEW.`tipo`, `valor` = NEW.`valor`, `user_id` = NEW.`user_id`, `user_name` = NEW.`user_name`, `beneficiario` = NEW.`beneficiario` WHERE `pk_conta_operacao` = OLD.`pk_conta_operacao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`conta_operacoes` (`pk_conta_operacao`, `data_hora`, `tipo`, `valor`, `user_id`, `user_name`, `beneficiario`) VALUES (NEW.`pk_conta_operacao`, NEW.`data_hora`, NEW.`tipo`, NEW.`valor`, NEW.`user_id`, NEW.`user_name`, NEW.`beneficiario`) ON DUPLICATE KEY UPDATE `pk_conta_operacao` = VALUES(`pk_conta_operacao`), `data_hora` = VALUES(`data_hora`), `tipo` = VALUES(`tipo`), `valor` = VALUES(`valor`), `user_id` = VALUES(`user_id`), `user_name` = VALUES(`user_name`), `beneficiario` = VALUES(`beneficiario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_operacoes_ad` AFTER DELETE ON `kitanda_db_1`.`conta_operacoes` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`conta_operacoes` WHERE `pk_conta_operacao` = OLD.`pk_conta_operacao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_permissoes_ai` AFTER INSERT ON `kitanda_db_1`.`conta_permissoes` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`conta_permissoes` (`pk_conta_permissao`, `cod_usuario`, `cod_conta`, `entrada`, `saida`, `transferencia`, `vis_entrato`, `anulacao`) VALUES (NEW.`pk_conta_permissao`, NEW.`cod_usuario`, NEW.`cod_conta`, NEW.`entrada`, NEW.`saida`, NEW.`transferencia`, NEW.`vis_entrato`, NEW.`anulacao`) ON DUPLICATE KEY UPDATE `pk_conta_permissao` = VALUES(`pk_conta_permissao`), `cod_usuario` = VALUES(`cod_usuario`), `cod_conta` = VALUES(`cod_conta`), `entrada` = VALUES(`entrada`), `saida` = VALUES(`saida`), `transferencia` = VALUES(`transferencia`), `vis_entrato` = VALUES(`vis_entrato`), `anulacao` = VALUES(`anulacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_permissoes_au` AFTER UPDATE ON `kitanda_db_1`.`conta_permissoes` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`conta_permissoes` SET `pk_conta_permissao` = NEW.`pk_conta_permissao`, `cod_usuario` = NEW.`cod_usuario`, `cod_conta` = NEW.`cod_conta`, `entrada` = NEW.`entrada`, `saida` = NEW.`saida`, `transferencia` = NEW.`transferencia`, `vis_entrato` = NEW.`vis_entrato`, `anulacao` = NEW.`anulacao` WHERE `pk_conta_permissao` = OLD.`pk_conta_permissao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`conta_permissoes` (`pk_conta_permissao`, `cod_usuario`, `cod_conta`, `entrada`, `saida`, `transferencia`, `vis_entrato`, `anulacao`) VALUES (NEW.`pk_conta_permissao`, NEW.`cod_usuario`, NEW.`cod_conta`, NEW.`entrada`, NEW.`saida`, NEW.`transferencia`, NEW.`vis_entrato`, NEW.`anulacao`) ON DUPLICATE KEY UPDATE `pk_conta_permissao` = VALUES(`pk_conta_permissao`), `cod_usuario` = VALUES(`cod_usuario`), `cod_conta` = VALUES(`cod_conta`), `entrada` = VALUES(`entrada`), `saida` = VALUES(`saida`), `transferencia` = VALUES(`transferencia`), `vis_entrato` = VALUES(`vis_entrato`), `anulacao` = VALUES(`anulacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_conta_permissoes_ad` AFTER DELETE ON `kitanda_db_1`.`conta_permissoes` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`conta_permissoes` WHERE `pk_conta_permissao` = OLD.`pk_conta_permissao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_contas_ai` AFTER INSERT ON `kitanda_db_1`.`contas` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`contas` (`pk_contas`, `designacao`, `numero`, `iban`, `titular_1`, `titular_2`, `saldo`, `objecto`, `data_criacao`, `user_id`, `tipo_conta_id`) VALUES (NEW.`pk_contas`, NEW.`designacao`, NEW.`numero`, NEW.`iban`, NEW.`titular_1`, NEW.`titular_2`, NEW.`saldo`, NEW.`objecto`, NEW.`data_criacao`, NEW.`user_id`, NEW.`tipo_conta_id`) ON DUPLICATE KEY UPDATE `pk_contas` = VALUES(`pk_contas`), `designacao` = VALUES(`designacao`), `numero` = VALUES(`numero`), `iban` = VALUES(`iban`), `titular_1` = VALUES(`titular_1`), `titular_2` = VALUES(`titular_2`), `saldo` = VALUES(`saldo`), `objecto` = VALUES(`objecto`), `data_criacao` = VALUES(`data_criacao`), `user_id` = VALUES(`user_id`), `tipo_conta_id` = VALUES(`tipo_conta_id`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_contas_au` AFTER UPDATE ON `kitanda_db_1`.`contas` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`contas` SET `pk_contas` = NEW.`pk_contas`, `designacao` = NEW.`designacao`, `numero` = NEW.`numero`, `iban` = NEW.`iban`, `titular_1` = NEW.`titular_1`, `titular_2` = NEW.`titular_2`, `saldo` = NEW.`saldo`, `objecto` = NEW.`objecto`, `data_criacao` = NEW.`data_criacao`, `user_id` = NEW.`user_id`, `tipo_conta_id` = NEW.`tipo_conta_id` WHERE `pk_contas` = OLD.`pk_contas`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`contas` (`pk_contas`, `designacao`, `numero`, `iban`, `titular_1`, `titular_2`, `saldo`, `objecto`, `data_criacao`, `user_id`, `tipo_conta_id`) VALUES (NEW.`pk_contas`, NEW.`designacao`, NEW.`numero`, NEW.`iban`, NEW.`titular_1`, NEW.`titular_2`, NEW.`saldo`, NEW.`objecto`, NEW.`data_criacao`, NEW.`user_id`, NEW.`tipo_conta_id`) ON DUPLICATE KEY UPDATE `pk_contas` = VALUES(`pk_contas`), `designacao` = VALUES(`designacao`), `numero` = VALUES(`numero`), `iban` = VALUES(`iban`), `titular_1` = VALUES(`titular_1`), `titular_2` = VALUES(`titular_2`), `saldo` = VALUES(`saldo`), `objecto` = VALUES(`objecto`), `data_criacao` = VALUES(`data_criacao`), `user_id` = VALUES(`user_id`), `tipo_conta_id` = VALUES(`tipo_conta_id`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_contas_ad` AFTER DELETE ON `kitanda_db_1`.`contas` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`contas` WHERE `pk_contas` = OLD.`pk_contas`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_db_backup_schedule_ai` AFTER INSERT ON `kitanda_db_1`.`db_backup_schedule` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`db_backup_schedule` (`pk_db_backup_schedule`, `ano`, `mes`, `dia`, `hora`, `minuto`, `segundo`, `dataUltimoBackup`, `dataProximoBackup`) VALUES (NEW.`pk_db_backup_schedule`, NEW.`ano`, NEW.`mes`, NEW.`dia`, NEW.`hora`, NEW.`minuto`, NEW.`segundo`, NEW.`dataUltimoBackup`, NEW.`dataProximoBackup`) ON DUPLICATE KEY UPDATE `pk_db_backup_schedule` = VALUES(`pk_db_backup_schedule`), `ano` = VALUES(`ano`), `mes` = VALUES(`mes`), `dia` = VALUES(`dia`), `hora` = VALUES(`hora`), `minuto` = VALUES(`minuto`), `segundo` = VALUES(`segundo`), `dataUltimoBackup` = VALUES(`dataUltimoBackup`), `dataProximoBackup` = VALUES(`dataProximoBackup`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_db_backup_schedule_au` AFTER UPDATE ON `kitanda_db_1`.`db_backup_schedule` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`db_backup_schedule` SET `pk_db_backup_schedule` = NEW.`pk_db_backup_schedule`, `ano` = NEW.`ano`, `mes` = NEW.`mes`, `dia` = NEW.`dia`, `hora` = NEW.`hora`, `minuto` = NEW.`minuto`, `segundo` = NEW.`segundo`, `dataUltimoBackup` = NEW.`dataUltimoBackup`, `dataProximoBackup` = NEW.`dataProximoBackup` WHERE `pk_db_backup_schedule` = OLD.`pk_db_backup_schedule`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`db_backup_schedule` (`pk_db_backup_schedule`, `ano`, `mes`, `dia`, `hora`, `minuto`, `segundo`, `dataUltimoBackup`, `dataProximoBackup`) VALUES (NEW.`pk_db_backup_schedule`, NEW.`ano`, NEW.`mes`, NEW.`dia`, NEW.`hora`, NEW.`minuto`, NEW.`segundo`, NEW.`dataUltimoBackup`, NEW.`dataProximoBackup`) ON DUPLICATE KEY UPDATE `pk_db_backup_schedule` = VALUES(`pk_db_backup_schedule`), `ano` = VALUES(`ano`), `mes` = VALUES(`mes`), `dia` = VALUES(`dia`), `hora` = VALUES(`hora`), `minuto` = VALUES(`minuto`), `segundo` = VALUES(`segundo`), `dataUltimoBackup` = VALUES(`dataUltimoBackup`), `dataProximoBackup` = VALUES(`dataProximoBackup`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_db_backup_schedule_ad` AFTER DELETE ON `kitanda_db_1`.`db_backup_schedule` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`db_backup_schedule` WHERE `pk_db_backup_schedule` = OLD.`pk_db_backup_schedule`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_deposito_bancario_ai` AFTER INSERT ON `kitanda_db_1`.`deposito_bancario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`deposito_bancario` (`pk_deposito`, `data`, `hora`, `nborderaux`, `valor`, `fk_banco`, `fk_usuario`, `descricao`) VALUES (NEW.`pk_deposito`, NEW.`data`, NEW.`hora`, NEW.`nborderaux`, NEW.`valor`, NEW.`fk_banco`, NEW.`fk_usuario`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_deposito` = VALUES(`pk_deposito`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `nborderaux` = VALUES(`nborderaux`), `valor` = VALUES(`valor`), `fk_banco` = VALUES(`fk_banco`), `fk_usuario` = VALUES(`fk_usuario`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_deposito_bancario_au` AFTER UPDATE ON `kitanda_db_1`.`deposito_bancario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`deposito_bancario` SET `pk_deposito` = NEW.`pk_deposito`, `data` = NEW.`data`, `hora` = NEW.`hora`, `nborderaux` = NEW.`nborderaux`, `valor` = NEW.`valor`, `fk_banco` = NEW.`fk_banco`, `fk_usuario` = NEW.`fk_usuario`, `descricao` = NEW.`descricao` WHERE `pk_deposito` = OLD.`pk_deposito`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`deposito_bancario` (`pk_deposito`, `data`, `hora`, `nborderaux`, `valor`, `fk_banco`, `fk_usuario`, `descricao`) VALUES (NEW.`pk_deposito`, NEW.`data`, NEW.`hora`, NEW.`nborderaux`, NEW.`valor`, NEW.`fk_banco`, NEW.`fk_usuario`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_deposito` = VALUES(`pk_deposito`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `nborderaux` = VALUES(`nborderaux`), `valor` = VALUES(`valor`), `fk_banco` = VALUES(`fk_banco`), `fk_usuario` = VALUES(`fk_usuario`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_deposito_bancario_ad` AFTER DELETE ON `kitanda_db_1`.`deposito_bancario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`deposito_bancario` WHERE `pk_deposito` = OLD.`pk_deposito`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_destino_ai` AFTER INSERT ON `kitanda_db_1`.`destino` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`destino` (`id`, `designacao`) VALUES (NEW.`id`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_destino_au` AFTER UPDATE ON `kitanda_db_1`.`destino` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`destino` SET `id` = NEW.`id`, `designacao` = NEW.`designacao` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`destino` (`id`, `designacao`) VALUES (NEW.`id`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_destino_ad` AFTER DELETE ON `kitanda_db_1`.`destino` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`destino` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_detalhes_documento_seccao_ai` AFTER INSERT ON `kitanda_db_1`.`detalhes_documento_seccao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`detalhes_documento_seccao` (`pk_detalhes_documento_seccao`, `fk_detalhes_documento`, `fk_seccao`) VALUES (NEW.`pk_detalhes_documento_seccao`, NEW.`fk_detalhes_documento`, NEW.`fk_seccao`) ON DUPLICATE KEY UPDATE `pk_detalhes_documento_seccao` = VALUES(`pk_detalhes_documento_seccao`), `fk_detalhes_documento` = VALUES(`fk_detalhes_documento`), `fk_seccao` = VALUES(`fk_seccao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_detalhes_documento_seccao_au` AFTER UPDATE ON `kitanda_db_1`.`detalhes_documento_seccao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`detalhes_documento_seccao` SET `pk_detalhes_documento_seccao` = NEW.`pk_detalhes_documento_seccao`, `fk_detalhes_documento` = NEW.`fk_detalhes_documento`, `fk_seccao` = NEW.`fk_seccao` WHERE `pk_detalhes_documento_seccao` = OLD.`pk_detalhes_documento_seccao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`detalhes_documento_seccao` (`pk_detalhes_documento_seccao`, `fk_detalhes_documento`, `fk_seccao`) VALUES (NEW.`pk_detalhes_documento_seccao`, NEW.`fk_detalhes_documento`, NEW.`fk_seccao`) ON DUPLICATE KEY UPDATE `pk_detalhes_documento_seccao` = VALUES(`pk_detalhes_documento_seccao`), `fk_detalhes_documento` = VALUES(`fk_detalhes_documento`), `fk_seccao` = VALUES(`fk_seccao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_detalhes_documento_seccao_ad` AFTER DELETE ON `kitanda_db_1`.`detalhes_documento_seccao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`detalhes_documento_seccao` WHERE `pk_detalhes_documento_seccao` = OLD.`pk_detalhes_documento_seccao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_documento_ai` AFTER INSERT ON `kitanda_db_1`.`documento` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`documento` (`pk_documento`, `designacao`, `abreviacao`, `cod_ultimo_doc`, `descricao_ultimo_doc`, `ultima_data`) VALUES (NEW.`pk_documento`, NEW.`designacao`, NEW.`abreviacao`, NEW.`cod_ultimo_doc`, NEW.`descricao_ultimo_doc`, NEW.`ultima_data`) ON DUPLICATE KEY UPDATE `pk_documento` = VALUES(`pk_documento`), `designacao` = VALUES(`designacao`), `abreviacao` = VALUES(`abreviacao`), `cod_ultimo_doc` = VALUES(`cod_ultimo_doc`), `descricao_ultimo_doc` = VALUES(`descricao_ultimo_doc`), `ultima_data` = VALUES(`ultima_data`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_documento_au` AFTER UPDATE ON `kitanda_db_1`.`documento` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`documento` SET `pk_documento` = NEW.`pk_documento`, `designacao` = NEW.`designacao`, `abreviacao` = NEW.`abreviacao`, `cod_ultimo_doc` = NEW.`cod_ultimo_doc`, `descricao_ultimo_doc` = NEW.`descricao_ultimo_doc`, `ultima_data` = NEW.`ultima_data` WHERE `pk_documento` = OLD.`pk_documento`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`documento` (`pk_documento`, `designacao`, `abreviacao`, `cod_ultimo_doc`, `descricao_ultimo_doc`, `ultima_data`) VALUES (NEW.`pk_documento`, NEW.`designacao`, NEW.`abreviacao`, NEW.`cod_ultimo_doc`, NEW.`descricao_ultimo_doc`, NEW.`ultima_data`) ON DUPLICATE KEY UPDATE `pk_documento` = VALUES(`pk_documento`), `designacao` = VALUES(`designacao`), `abreviacao` = VALUES(`abreviacao`), `cod_ultimo_doc` = VALUES(`cod_ultimo_doc`), `descricao_ultimo_doc` = VALUES(`descricao_ultimo_doc`), `ultima_data` = VALUES(`ultima_data`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_documento_ad` AFTER DELETE ON `kitanda_db_1`.`documento` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`documento` WHERE `pk_documento` = OLD.`pk_documento`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_empresa_ai` AFTER INSERT ON `kitanda_db_1`.`empresa` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`empresa` (`pk_empresa`, `nome`, `telefone`, `endereco`, `NIF`, `email`, `logo_tipo`) VALUES (NEW.`pk_empresa`, NEW.`nome`, NEW.`telefone`, NEW.`endereco`, NEW.`NIF`, NEW.`email`, NEW.`logo_tipo`) ON DUPLICATE KEY UPDATE `pk_empresa` = VALUES(`pk_empresa`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `endereco` = VALUES(`endereco`), `NIF` = VALUES(`NIF`), `email` = VALUES(`email`), `logo_tipo` = VALUES(`logo_tipo`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_empresa_au` AFTER UPDATE ON `kitanda_db_1`.`empresa` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`empresa` SET `pk_empresa` = NEW.`pk_empresa`, `nome` = NEW.`nome`, `telefone` = NEW.`telefone`, `endereco` = NEW.`endereco`, `NIF` = NEW.`NIF`, `email` = NEW.`email`, `logo_tipo` = NEW.`logo_tipo` WHERE `pk_empresa` = OLD.`pk_empresa`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`empresa` (`pk_empresa`, `nome`, `telefone`, `endereco`, `NIF`, `email`, `logo_tipo`) VALUES (NEW.`pk_empresa`, NEW.`nome`, NEW.`telefone`, NEW.`endereco`, NEW.`NIF`, NEW.`email`, NEW.`logo_tipo`) ON DUPLICATE KEY UPDATE `pk_empresa` = VALUES(`pk_empresa`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `endereco` = VALUES(`endereco`), `NIF` = VALUES(`NIF`), `email` = VALUES(`email`), `logo_tipo` = VALUES(`logo_tipo`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_empresa_ad` AFTER DELETE ON `kitanda_db_1`.`empresa` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`empresa` WHERE `pk_empresa` = OLD.`pk_empresa`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_entrada_tesouraria_ai` AFTER INSERT ON `kitanda_db_1`.`entrada_tesouraria` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`entrada_tesouraria` (`pk_entrada_tesouraria`, `descricao`, `valor`, `data_entrada`, `hora_entrada`, `fk_usuario`, `fk_banco`) VALUES (NEW.`pk_entrada_tesouraria`, NEW.`descricao`, NEW.`valor`, NEW.`data_entrada`, NEW.`hora_entrada`, NEW.`fk_usuario`, NEW.`fk_banco`) ON DUPLICATE KEY UPDATE `pk_entrada_tesouraria` = VALUES(`pk_entrada_tesouraria`), `descricao` = VALUES(`descricao`), `valor` = VALUES(`valor`), `data_entrada` = VALUES(`data_entrada`), `hora_entrada` = VALUES(`hora_entrada`), `fk_usuario` = VALUES(`fk_usuario`), `fk_banco` = VALUES(`fk_banco`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_entrada_tesouraria_au` AFTER UPDATE ON `kitanda_db_1`.`entrada_tesouraria` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`entrada_tesouraria` SET `pk_entrada_tesouraria` = NEW.`pk_entrada_tesouraria`, `descricao` = NEW.`descricao`, `valor` = NEW.`valor`, `data_entrada` = NEW.`data_entrada`, `hora_entrada` = NEW.`hora_entrada`, `fk_usuario` = NEW.`fk_usuario`, `fk_banco` = NEW.`fk_banco` WHERE `pk_entrada_tesouraria` = OLD.`pk_entrada_tesouraria`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`entrada_tesouraria` (`pk_entrada_tesouraria`, `descricao`, `valor`, `data_entrada`, `hora_entrada`, `fk_usuario`, `fk_banco`) VALUES (NEW.`pk_entrada_tesouraria`, NEW.`descricao`, NEW.`valor`, NEW.`data_entrada`, NEW.`hora_entrada`, NEW.`fk_usuario`, NEW.`fk_banco`) ON DUPLICATE KEY UPDATE `pk_entrada_tesouraria` = VALUES(`pk_entrada_tesouraria`), `descricao` = VALUES(`descricao`), `valor` = VALUES(`valor`), `data_entrada` = VALUES(`data_entrada`), `hora_entrada` = VALUES(`hora_entrada`), `fk_usuario` = VALUES(`fk_usuario`), `fk_banco` = VALUES(`fk_banco`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_entrada_tesouraria_ad` AFTER DELETE ON `kitanda_db_1`.`entrada_tesouraria` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`entrada_tesouraria` WHERE `pk_entrada_tesouraria` = OLD.`pk_entrada_tesouraria`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_extrato_conta_cliente_ai` AFTER INSERT ON `kitanda_db_1`.`extrato_conta_cliente` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`extrato_conta_cliente` (`pk_extrato_conta_cliente`, `documento`, `referencia`, `descricao`, `data_hora`, `saldo_anterior`, `debito`, `credito`, `saldo_actual`, `tipo_extrato`, `cliente_nome`, `cliente_id`, `user_name`, `usuario_id`) VALUES (NEW.`pk_extrato_conta_cliente`, NEW.`documento`, NEW.`referencia`, NEW.`descricao`, NEW.`data_hora`, NEW.`saldo_anterior`, NEW.`debito`, NEW.`credito`, NEW.`saldo_actual`, NEW.`tipo_extrato`, NEW.`cliente_nome`, NEW.`cliente_id`, NEW.`user_name`, NEW.`usuario_id`) ON DUPLICATE KEY UPDATE `pk_extrato_conta_cliente` = VALUES(`pk_extrato_conta_cliente`), `documento` = VALUES(`documento`), `referencia` = VALUES(`referencia`), `descricao` = VALUES(`descricao`), `data_hora` = VALUES(`data_hora`), `saldo_anterior` = VALUES(`saldo_anterior`), `debito` = VALUES(`debito`), `credito` = VALUES(`credito`), `saldo_actual` = VALUES(`saldo_actual`), `tipo_extrato` = VALUES(`tipo_extrato`), `cliente_nome` = VALUES(`cliente_nome`), `cliente_id` = VALUES(`cliente_id`), `user_name` = VALUES(`user_name`), `usuario_id` = VALUES(`usuario_id`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_extrato_conta_cliente_au` AFTER UPDATE ON `kitanda_db_1`.`extrato_conta_cliente` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`extrato_conta_cliente` SET `pk_extrato_conta_cliente` = NEW.`pk_extrato_conta_cliente`, `documento` = NEW.`documento`, `referencia` = NEW.`referencia`, `descricao` = NEW.`descricao`, `data_hora` = NEW.`data_hora`, `saldo_anterior` = NEW.`saldo_anterior`, `debito` = NEW.`debito`, `credito` = NEW.`credito`, `saldo_actual` = NEW.`saldo_actual`, `tipo_extrato` = NEW.`tipo_extrato`, `cliente_nome` = NEW.`cliente_nome`, `cliente_id` = NEW.`cliente_id`, `user_name` = NEW.`user_name`, `usuario_id` = NEW.`usuario_id` WHERE `pk_extrato_conta_cliente` = OLD.`pk_extrato_conta_cliente`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`extrato_conta_cliente` (`pk_extrato_conta_cliente`, `documento`, `referencia`, `descricao`, `data_hora`, `saldo_anterior`, `debito`, `credito`, `saldo_actual`, `tipo_extrato`, `cliente_nome`, `cliente_id`, `user_name`, `usuario_id`) VALUES (NEW.`pk_extrato_conta_cliente`, NEW.`documento`, NEW.`referencia`, NEW.`descricao`, NEW.`data_hora`, NEW.`saldo_anterior`, NEW.`debito`, NEW.`credito`, NEW.`saldo_actual`, NEW.`tipo_extrato`, NEW.`cliente_nome`, NEW.`cliente_id`, NEW.`user_name`, NEW.`usuario_id`) ON DUPLICATE KEY UPDATE `pk_extrato_conta_cliente` = VALUES(`pk_extrato_conta_cliente`), `documento` = VALUES(`documento`), `referencia` = VALUES(`referencia`), `descricao` = VALUES(`descricao`), `data_hora` = VALUES(`data_hora`), `saldo_anterior` = VALUES(`saldo_anterior`), `debito` = VALUES(`debito`), `credito` = VALUES(`credito`), `saldo_actual` = VALUES(`saldo_actual`), `tipo_extrato` = VALUES(`tipo_extrato`), `cliente_nome` = VALUES(`cliente_nome`), `cliente_id` = VALUES(`cliente_id`), `user_name` = VALUES(`user_name`), `usuario_id` = VALUES(`usuario_id`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_extrato_conta_cliente_ad` AFTER DELETE ON `kitanda_db_1`.`extrato_conta_cliente` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`extrato_conta_cliente` WHERE `pk_extrato_conta_cliente` = OLD.`pk_extrato_conta_cliente`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_extrato_conta_fornecedor_ai` AFTER INSERT ON `kitanda_db_1`.`extrato_conta_fornecedor` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`extrato_conta_fornecedor` (`pk_extrato_conta_fornecedor`, `documento`, `referencia`, `data_hora`, `saldo_anterior`, `debito`, `credito`, `saldo_actual`, `tipo_extrato`, `fornecedor_id`, `fonecedor_nome`, `user_id`, `user_name`, `descricao`) VALUES (NEW.`pk_extrato_conta_fornecedor`, NEW.`documento`, NEW.`referencia`, NEW.`data_hora`, NEW.`saldo_anterior`, NEW.`debito`, NEW.`credito`, NEW.`saldo_actual`, NEW.`tipo_extrato`, NEW.`fornecedor_id`, NEW.`fonecedor_nome`, NEW.`user_id`, NEW.`user_name`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_extrato_conta_fornecedor` = VALUES(`pk_extrato_conta_fornecedor`), `documento` = VALUES(`documento`), `referencia` = VALUES(`referencia`), `data_hora` = VALUES(`data_hora`), `saldo_anterior` = VALUES(`saldo_anterior`), `debito` = VALUES(`debito`), `credito` = VALUES(`credito`), `saldo_actual` = VALUES(`saldo_actual`), `tipo_extrato` = VALUES(`tipo_extrato`), `fornecedor_id` = VALUES(`fornecedor_id`), `fonecedor_nome` = VALUES(`fonecedor_nome`), `user_id` = VALUES(`user_id`), `user_name` = VALUES(`user_name`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_extrato_conta_fornecedor_au` AFTER UPDATE ON `kitanda_db_1`.`extrato_conta_fornecedor` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`extrato_conta_fornecedor` SET `pk_extrato_conta_fornecedor` = NEW.`pk_extrato_conta_fornecedor`, `documento` = NEW.`documento`, `referencia` = NEW.`referencia`, `data_hora` = NEW.`data_hora`, `saldo_anterior` = NEW.`saldo_anterior`, `debito` = NEW.`debito`, `credito` = NEW.`credito`, `saldo_actual` = NEW.`saldo_actual`, `tipo_extrato` = NEW.`tipo_extrato`, `fornecedor_id` = NEW.`fornecedor_id`, `fonecedor_nome` = NEW.`fonecedor_nome`, `user_id` = NEW.`user_id`, `user_name` = NEW.`user_name`, `descricao` = NEW.`descricao` WHERE `pk_extrato_conta_fornecedor` = OLD.`pk_extrato_conta_fornecedor`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`extrato_conta_fornecedor` (`pk_extrato_conta_fornecedor`, `documento`, `referencia`, `data_hora`, `saldo_anterior`, `debito`, `credito`, `saldo_actual`, `tipo_extrato`, `fornecedor_id`, `fonecedor_nome`, `user_id`, `user_name`, `descricao`) VALUES (NEW.`pk_extrato_conta_fornecedor`, NEW.`documento`, NEW.`referencia`, NEW.`data_hora`, NEW.`saldo_anterior`, NEW.`debito`, NEW.`credito`, NEW.`saldo_actual`, NEW.`tipo_extrato`, NEW.`fornecedor_id`, NEW.`fonecedor_nome`, NEW.`user_id`, NEW.`user_name`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_extrato_conta_fornecedor` = VALUES(`pk_extrato_conta_fornecedor`), `documento` = VALUES(`documento`), `referencia` = VALUES(`referencia`), `data_hora` = VALUES(`data_hora`), `saldo_anterior` = VALUES(`saldo_anterior`), `debito` = VALUES(`debito`), `credito` = VALUES(`credito`), `saldo_actual` = VALUES(`saldo_actual`), `tipo_extrato` = VALUES(`tipo_extrato`), `fornecedor_id` = VALUES(`fornecedor_id`), `fonecedor_nome` = VALUES(`fonecedor_nome`), `user_id` = VALUES(`user_id`), `user_name` = VALUES(`user_name`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_extrato_conta_fornecedor_ad` AFTER DELETE ON `kitanda_db_1`.`extrato_conta_fornecedor` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`extrato_conta_fornecedor` WHERE `pk_extrato_conta_fornecedor` = OLD.`pk_extrato_conta_fornecedor`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_familia_ai` AFTER INSERT ON `kitanda_db_1`.`familia` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`familia` (`pk_familia`, `designacao`) VALUES (NEW.`pk_familia`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_familia` = VALUES(`pk_familia`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_familia_au` AFTER UPDATE ON `kitanda_db_1`.`familia` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`familia` SET `pk_familia` = NEW.`pk_familia`, `designacao` = NEW.`designacao` WHERE `pk_familia` = OLD.`pk_familia`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`familia` (`pk_familia`, `designacao`) VALUES (NEW.`pk_familia`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_familia` = VALUES(`pk_familia`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_familia_ad` AFTER DELETE ON `kitanda_db_1`.`familia` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`familia` WHERE `pk_familia` = OLD.`pk_familia`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_fecho_contrato_ai` AFTER INSERT ON `kitanda_db_1`.`fecho_contrato` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`fecho_contrato` (`pk_fecho_contrato`, `data_hora`, `salario_base`, `percentagem_feria`, `valor_feria`, `percentagem_natal`, `valor_natal`, `fk_funcionario`, `fk_usuario`) VALUES (NEW.`pk_fecho_contrato`, NEW.`data_hora`, NEW.`salario_base`, NEW.`percentagem_feria`, NEW.`valor_feria`, NEW.`percentagem_natal`, NEW.`valor_natal`, NEW.`fk_funcionario`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_fecho_contrato` = VALUES(`pk_fecho_contrato`), `data_hora` = VALUES(`data_hora`), `salario_base` = VALUES(`salario_base`), `percentagem_feria` = VALUES(`percentagem_feria`), `valor_feria` = VALUES(`valor_feria`), `percentagem_natal` = VALUES(`percentagem_natal`), `valor_natal` = VALUES(`valor_natal`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_fecho_contrato_au` AFTER UPDATE ON `kitanda_db_1`.`fecho_contrato` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`fecho_contrato` SET `pk_fecho_contrato` = NEW.`pk_fecho_contrato`, `data_hora` = NEW.`data_hora`, `salario_base` = NEW.`salario_base`, `percentagem_feria` = NEW.`percentagem_feria`, `valor_feria` = NEW.`valor_feria`, `percentagem_natal` = NEW.`percentagem_natal`, `valor_natal` = NEW.`valor_natal`, `fk_funcionario` = NEW.`fk_funcionario`, `fk_usuario` = NEW.`fk_usuario` WHERE `pk_fecho_contrato` = OLD.`pk_fecho_contrato`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`fecho_contrato` (`pk_fecho_contrato`, `data_hora`, `salario_base`, `percentagem_feria`, `valor_feria`, `percentagem_natal`, `valor_natal`, `fk_funcionario`, `fk_usuario`) VALUES (NEW.`pk_fecho_contrato`, NEW.`data_hora`, NEW.`salario_base`, NEW.`percentagem_feria`, NEW.`valor_feria`, NEW.`percentagem_natal`, NEW.`valor_natal`, NEW.`fk_funcionario`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_fecho_contrato` = VALUES(`pk_fecho_contrato`), `data_hora` = VALUES(`data_hora`), `salario_base` = VALUES(`salario_base`), `percentagem_feria` = VALUES(`percentagem_feria`), `valor_feria` = VALUES(`valor_feria`), `percentagem_natal` = VALUES(`percentagem_natal`), `valor_natal` = VALUES(`valor_natal`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_fecho_contrato_ad` AFTER DELETE ON `kitanda_db_1`.`fecho_contrato` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`fecho_contrato` WHERE `pk_fecho_contrato` = OLD.`pk_fecho_contrato`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_fecho_periodo_ai` AFTER INSERT ON `kitanda_db_1`.`fecho_periodo` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`fecho_periodo` (`pk_fecho_periodo`, `data_abertura`, `data_fecho`, `fk_usuario`, `fk_ano`, `fk_periodo`) VALUES (NEW.`pk_fecho_periodo`, NEW.`data_abertura`, NEW.`data_fecho`, NEW.`fk_usuario`, NEW.`fk_ano`, NEW.`fk_periodo`) ON DUPLICATE KEY UPDATE `pk_fecho_periodo` = VALUES(`pk_fecho_periodo`), `data_abertura` = VALUES(`data_abertura`), `data_fecho` = VALUES(`data_fecho`), `fk_usuario` = VALUES(`fk_usuario`), `fk_ano` = VALUES(`fk_ano`), `fk_periodo` = VALUES(`fk_periodo`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_fecho_periodo_au` AFTER UPDATE ON `kitanda_db_1`.`fecho_periodo` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`fecho_periodo` SET `pk_fecho_periodo` = NEW.`pk_fecho_periodo`, `data_abertura` = NEW.`data_abertura`, `data_fecho` = NEW.`data_fecho`, `fk_usuario` = NEW.`fk_usuario`, `fk_ano` = NEW.`fk_ano`, `fk_periodo` = NEW.`fk_periodo` WHERE `pk_fecho_periodo` = OLD.`pk_fecho_periodo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`fecho_periodo` (`pk_fecho_periodo`, `data_abertura`, `data_fecho`, `fk_usuario`, `fk_ano`, `fk_periodo`) VALUES (NEW.`pk_fecho_periodo`, NEW.`data_abertura`, NEW.`data_fecho`, NEW.`fk_usuario`, NEW.`fk_ano`, NEW.`fk_periodo`) ON DUPLICATE KEY UPDATE `pk_fecho_periodo` = VALUES(`pk_fecho_periodo`), `data_abertura` = VALUES(`data_abertura`), `data_fecho` = VALUES(`data_fecho`), `fk_usuario` = VALUES(`fk_usuario`), `fk_ano` = VALUES(`fk_ano`), `fk_periodo` = VALUES(`fk_periodo`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_fecho_periodo_ad` AFTER DELETE ON `kitanda_db_1`.`fecho_periodo` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`fecho_periodo` WHERE `pk_fecho_periodo` = OLD.`pk_fecho_periodo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_ficha_tecnica_ai` AFTER INSERT ON `kitanda_db_1`.`ficha_tecnica` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`ficha_tecnica` (`id`, `produto_id`, `data_criacao`, `usuario_id_criacao`, `data_actualizacao`, `usuario_id_actualizacao`, `custo_produto`, `percentagem_ganho`, `custo_venda`, `photo`, `qtd_composto`, `prato`) VALUES (NEW.`id`, NEW.`produto_id`, NEW.`data_criacao`, NEW.`usuario_id_criacao`, NEW.`data_actualizacao`, NEW.`usuario_id_actualizacao`, NEW.`custo_produto`, NEW.`percentagem_ganho`, NEW.`custo_venda`, NEW.`photo`, NEW.`qtd_composto`, NEW.`prato`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `produto_id` = VALUES(`produto_id`), `data_criacao` = VALUES(`data_criacao`), `usuario_id_criacao` = VALUES(`usuario_id_criacao`), `data_actualizacao` = VALUES(`data_actualizacao`), `usuario_id_actualizacao` = VALUES(`usuario_id_actualizacao`), `custo_produto` = VALUES(`custo_produto`), `percentagem_ganho` = VALUES(`percentagem_ganho`), `custo_venda` = VALUES(`custo_venda`), `photo` = VALUES(`photo`), `qtd_composto` = VALUES(`qtd_composto`), `prato` = VALUES(`prato`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_ficha_tecnica_au` AFTER UPDATE ON `kitanda_db_1`.`ficha_tecnica` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`ficha_tecnica` SET `id` = NEW.`id`, `produto_id` = NEW.`produto_id`, `data_criacao` = NEW.`data_criacao`, `usuario_id_criacao` = NEW.`usuario_id_criacao`, `data_actualizacao` = NEW.`data_actualizacao`, `usuario_id_actualizacao` = NEW.`usuario_id_actualizacao`, `custo_produto` = NEW.`custo_produto`, `percentagem_ganho` = NEW.`percentagem_ganho`, `custo_venda` = NEW.`custo_venda`, `photo` = NEW.`photo`, `qtd_composto` = NEW.`qtd_composto`, `prato` = NEW.`prato` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`ficha_tecnica` (`id`, `produto_id`, `data_criacao`, `usuario_id_criacao`, `data_actualizacao`, `usuario_id_actualizacao`, `custo_produto`, `percentagem_ganho`, `custo_venda`, `photo`, `qtd_composto`, `prato`) VALUES (NEW.`id`, NEW.`produto_id`, NEW.`data_criacao`, NEW.`usuario_id_criacao`, NEW.`data_actualizacao`, NEW.`usuario_id_actualizacao`, NEW.`custo_produto`, NEW.`percentagem_ganho`, NEW.`custo_venda`, NEW.`photo`, NEW.`qtd_composto`, NEW.`prato`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `produto_id` = VALUES(`produto_id`), `data_criacao` = VALUES(`data_criacao`), `usuario_id_criacao` = VALUES(`usuario_id_criacao`), `data_actualizacao` = VALUES(`data_actualizacao`), `usuario_id_actualizacao` = VALUES(`usuario_id_actualizacao`), `custo_produto` = VALUES(`custo_produto`), `percentagem_ganho` = VALUES(`percentagem_ganho`), `custo_venda` = VALUES(`custo_venda`), `photo` = VALUES(`photo`), `qtd_composto` = VALUES(`qtd_composto`), `prato` = VALUES(`prato`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_ficha_tecnica_ad` AFTER DELETE ON `kitanda_db_1`.`ficha_tecnica` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`ficha_tecnica` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_forma_pagamento_ai` AFTER INSERT ON `kitanda_db_1`.`forma_pagamento` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`forma_pagamento` (`pk_forma_pagamento`, `designacao`, `fk_conta_associada`) VALUES (NEW.`pk_forma_pagamento`, NEW.`designacao`, NEW.`fk_conta_associada`) ON DUPLICATE KEY UPDATE `pk_forma_pagamento` = VALUES(`pk_forma_pagamento`), `designacao` = VALUES(`designacao`), `fk_conta_associada` = VALUES(`fk_conta_associada`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_forma_pagamento_au` AFTER UPDATE ON `kitanda_db_1`.`forma_pagamento` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`forma_pagamento` SET `pk_forma_pagamento` = NEW.`pk_forma_pagamento`, `designacao` = NEW.`designacao`, `fk_conta_associada` = NEW.`fk_conta_associada` WHERE `pk_forma_pagamento` = OLD.`pk_forma_pagamento`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`forma_pagamento` (`pk_forma_pagamento`, `designacao`, `fk_conta_associada`) VALUES (NEW.`pk_forma_pagamento`, NEW.`designacao`, NEW.`fk_conta_associada`) ON DUPLICATE KEY UPDATE `pk_forma_pagamento` = VALUES(`pk_forma_pagamento`), `designacao` = VALUES(`designacao`), `fk_conta_associada` = VALUES(`fk_conta_associada`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_forma_pagamento_ad` AFTER DELETE ON `kitanda_db_1`.`forma_pagamento` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`forma_pagamento` WHERE `pk_forma_pagamento` = OLD.`pk_forma_pagamento`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_grupo_ai` AFTER INSERT ON `kitanda_db_1`.`grupo` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`grupo` (`pk_grupo`, `designacao`) VALUES (NEW.`pk_grupo`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_grupo` = VALUES(`pk_grupo`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_grupo_au` AFTER UPDATE ON `kitanda_db_1`.`grupo` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`grupo` SET `pk_grupo` = NEW.`pk_grupo`, `designacao` = NEW.`designacao` WHERE `pk_grupo` = OLD.`pk_grupo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`grupo` (`pk_grupo`, `designacao`) VALUES (NEW.`pk_grupo`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_grupo` = VALUES(`pk_grupo`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_grupo_ad` AFTER DELETE ON `kitanda_db_1`.`grupo` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`grupo` WHERE `pk_grupo` = OLD.`pk_grupo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_imposto_ai` AFTER INSERT ON `kitanda_db_1`.`imposto` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`imposto` (`pk_imposto`, `descricao`, `taxa`, `data_hora`) VALUES (NEW.`pk_imposto`, NEW.`descricao`, NEW.`taxa`, NEW.`data_hora`) ON DUPLICATE KEY UPDATE `pk_imposto` = VALUES(`pk_imposto`), `descricao` = VALUES(`descricao`), `taxa` = VALUES(`taxa`), `data_hora` = VALUES(`data_hora`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_imposto_au` AFTER UPDATE ON `kitanda_db_1`.`imposto` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`imposto` SET `pk_imposto` = NEW.`pk_imposto`, `descricao` = NEW.`descricao`, `taxa` = NEW.`taxa`, `data_hora` = NEW.`data_hora` WHERE `pk_imposto` = OLD.`pk_imposto`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`imposto` (`pk_imposto`, `descricao`, `taxa`, `data_hora`) VALUES (NEW.`pk_imposto`, NEW.`descricao`, NEW.`taxa`, NEW.`data_hora`) ON DUPLICATE KEY UPDATE `pk_imposto` = VALUES(`pk_imposto`), `descricao` = VALUES(`descricao`), `taxa` = VALUES(`taxa`), `data_hora` = VALUES(`data_hora`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_imposto_ad` AFTER DELETE ON `kitanda_db_1`.`imposto` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`imposto` WHERE `pk_imposto` = OLD.`pk_imposto`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_caixa_ai` AFTER INSERT ON `kitanda_db_1`.`item_caixa` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`item_caixa` (`pk_item_caixa`, `valor_declarado`, `valor_real`, `fk_caixa`, `fk_forma_pagamento`) VALUES (NEW.`pk_item_caixa`, NEW.`valor_declarado`, NEW.`valor_real`, NEW.`fk_caixa`, NEW.`fk_forma_pagamento`) ON DUPLICATE KEY UPDATE `pk_item_caixa` = VALUES(`pk_item_caixa`), `valor_declarado` = VALUES(`valor_declarado`), `valor_real` = VALUES(`valor_real`), `fk_caixa` = VALUES(`fk_caixa`), `fk_forma_pagamento` = VALUES(`fk_forma_pagamento`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_caixa_au` AFTER UPDATE ON `kitanda_db_1`.`item_caixa` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`item_caixa` SET `pk_item_caixa` = NEW.`pk_item_caixa`, `valor_declarado` = NEW.`valor_declarado`, `valor_real` = NEW.`valor_real`, `fk_caixa` = NEW.`fk_caixa`, `fk_forma_pagamento` = NEW.`fk_forma_pagamento` WHERE `pk_item_caixa` = OLD.`pk_item_caixa`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`item_caixa` (`pk_item_caixa`, `valor_declarado`, `valor_real`, `fk_caixa`, `fk_forma_pagamento`) VALUES (NEW.`pk_item_caixa`, NEW.`valor_declarado`, NEW.`valor_real`, NEW.`fk_caixa`, NEW.`fk_forma_pagamento`) ON DUPLICATE KEY UPDATE `pk_item_caixa` = VALUES(`pk_item_caixa`), `valor_declarado` = VALUES(`valor_declarado`), `valor_real` = VALUES(`valor_real`), `fk_caixa` = VALUES(`fk_caixa`), `fk_forma_pagamento` = VALUES(`fk_forma_pagamento`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_caixa_ad` AFTER DELETE ON `kitanda_db_1`.`item_caixa` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`item_caixa` WHERE `pk_item_caixa` = OLD.`pk_item_caixa`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_compras_ai` AFTER INSERT ON `kitanda_db_1`.`item_compras` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`item_compras` (`pk_itm_compras`, `preco_compra`, `quantidade`, `valor_iva`, `motivo_isensao`, `desconto`, `total`, `codigo_isensao`, `fk_produto`, `fk_compra`) VALUES (NEW.`pk_itm_compras`, NEW.`preco_compra`, NEW.`quantidade`, NEW.`valor_iva`, NEW.`motivo_isensao`, NEW.`desconto`, NEW.`total`, NEW.`codigo_isensao`, NEW.`fk_produto`, NEW.`fk_compra`) ON DUPLICATE KEY UPDATE `pk_itm_compras` = VALUES(`pk_itm_compras`), `preco_compra` = VALUES(`preco_compra`), `quantidade` = VALUES(`quantidade`), `valor_iva` = VALUES(`valor_iva`), `motivo_isensao` = VALUES(`motivo_isensao`), `desconto` = VALUES(`desconto`), `total` = VALUES(`total`), `codigo_isensao` = VALUES(`codigo_isensao`), `fk_produto` = VALUES(`fk_produto`), `fk_compra` = VALUES(`fk_compra`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_compras_au` AFTER UPDATE ON `kitanda_db_1`.`item_compras` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`item_compras` SET `pk_itm_compras` = NEW.`pk_itm_compras`, `preco_compra` = NEW.`preco_compra`, `quantidade` = NEW.`quantidade`, `valor_iva` = NEW.`valor_iva`, `motivo_isensao` = NEW.`motivo_isensao`, `desconto` = NEW.`desconto`, `total` = NEW.`total`, `codigo_isensao` = NEW.`codigo_isensao`, `fk_produto` = NEW.`fk_produto`, `fk_compra` = NEW.`fk_compra` WHERE `pk_itm_compras` = OLD.`pk_itm_compras`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`item_compras` (`pk_itm_compras`, `preco_compra`, `quantidade`, `valor_iva`, `motivo_isensao`, `desconto`, `total`, `codigo_isensao`, `fk_produto`, `fk_compra`) VALUES (NEW.`pk_itm_compras`, NEW.`preco_compra`, NEW.`quantidade`, NEW.`valor_iva`, NEW.`motivo_isensao`, NEW.`desconto`, NEW.`total`, NEW.`codigo_isensao`, NEW.`fk_produto`, NEW.`fk_compra`) ON DUPLICATE KEY UPDATE `pk_itm_compras` = VALUES(`pk_itm_compras`), `preco_compra` = VALUES(`preco_compra`), `quantidade` = VALUES(`quantidade`), `valor_iva` = VALUES(`valor_iva`), `motivo_isensao` = VALUES(`motivo_isensao`), `desconto` = VALUES(`desconto`), `total` = VALUES(`total`), `codigo_isensao` = VALUES(`codigo_isensao`), `fk_produto` = VALUES(`fk_produto`), `fk_compra` = VALUES(`fk_compra`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_compras_ad` AFTER DELETE ON `kitanda_db_1`.`item_compras` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`item_compras` WHERE `pk_itm_compras` = OLD.`pk_itm_compras`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_recibo_rh_ai` AFTER INSERT ON `kitanda_db_1`.`item_recibo_rh` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`item_recibo_rh` (`pk_item_recibo_rh`, `descricao`, `remuneracao`, `desconto`, `fk_recibo_rh`) VALUES (NEW.`pk_item_recibo_rh`, NEW.`descricao`, NEW.`remuneracao`, NEW.`desconto`, NEW.`fk_recibo_rh`) ON DUPLICATE KEY UPDATE `pk_item_recibo_rh` = VALUES(`pk_item_recibo_rh`), `descricao` = VALUES(`descricao`), `remuneracao` = VALUES(`remuneracao`), `desconto` = VALUES(`desconto`), `fk_recibo_rh` = VALUES(`fk_recibo_rh`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_recibo_rh_au` AFTER UPDATE ON `kitanda_db_1`.`item_recibo_rh` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`item_recibo_rh` SET `pk_item_recibo_rh` = NEW.`pk_item_recibo_rh`, `descricao` = NEW.`descricao`, `remuneracao` = NEW.`remuneracao`, `desconto` = NEW.`desconto`, `fk_recibo_rh` = NEW.`fk_recibo_rh` WHERE `pk_item_recibo_rh` = OLD.`pk_item_recibo_rh`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`item_recibo_rh` (`pk_item_recibo_rh`, `descricao`, `remuneracao`, `desconto`, `fk_recibo_rh`) VALUES (NEW.`pk_item_recibo_rh`, NEW.`descricao`, NEW.`remuneracao`, NEW.`desconto`, NEW.`fk_recibo_rh`) ON DUPLICATE KEY UPDATE `pk_item_recibo_rh` = VALUES(`pk_item_recibo_rh`), `descricao` = VALUES(`descricao`), `remuneracao` = VALUES(`remuneracao`), `desconto` = VALUES(`desconto`), `fk_recibo_rh` = VALUES(`fk_recibo_rh`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_recibo_rh_ad` AFTER DELETE ON `kitanda_db_1`.`item_recibo_rh` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`item_recibo_rh` WHERE `pk_item_recibo_rh` = OLD.`pk_item_recibo_rh`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_salario_extra_ai` AFTER INSERT ON `kitanda_db_1`.`item_salario_extra` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`item_salario_extra` (`pk_item_salario_extra`, `valor`, `data_hora`, `fk_master_table`, `fk_tb_funcionario`, `fk_tb_usuario`) VALUES (NEW.`pk_item_salario_extra`, NEW.`valor`, NEW.`data_hora`, NEW.`fk_master_table`, NEW.`fk_tb_funcionario`, NEW.`fk_tb_usuario`) ON DUPLICATE KEY UPDATE `pk_item_salario_extra` = VALUES(`pk_item_salario_extra`), `valor` = VALUES(`valor`), `data_hora` = VALUES(`data_hora`), `fk_master_table` = VALUES(`fk_master_table`), `fk_tb_funcionario` = VALUES(`fk_tb_funcionario`), `fk_tb_usuario` = VALUES(`fk_tb_usuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_salario_extra_au` AFTER UPDATE ON `kitanda_db_1`.`item_salario_extra` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`item_salario_extra` SET `pk_item_salario_extra` = NEW.`pk_item_salario_extra`, `valor` = NEW.`valor`, `data_hora` = NEW.`data_hora`, `fk_master_table` = NEW.`fk_master_table`, `fk_tb_funcionario` = NEW.`fk_tb_funcionario`, `fk_tb_usuario` = NEW.`fk_tb_usuario` WHERE `pk_item_salario_extra` = OLD.`pk_item_salario_extra`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`item_salario_extra` (`pk_item_salario_extra`, `valor`, `data_hora`, `fk_master_table`, `fk_tb_funcionario`, `fk_tb_usuario`) VALUES (NEW.`pk_item_salario_extra`, NEW.`valor`, NEW.`data_hora`, NEW.`fk_master_table`, NEW.`fk_tb_funcionario`, NEW.`fk_tb_usuario`) ON DUPLICATE KEY UPDATE `pk_item_salario_extra` = VALUES(`pk_item_salario_extra`), `valor` = VALUES(`valor`), `data_hora` = VALUES(`data_hora`), `fk_master_table` = VALUES(`fk_master_table`), `fk_tb_funcionario` = VALUES(`fk_tb_funcionario`), `fk_tb_usuario` = VALUES(`fk_tb_usuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_item_salario_extra_ad` AFTER DELETE ON `kitanda_db_1`.`item_salario_extra` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`item_salario_extra` WHERE `pk_item_salario_extra` = OLD.`pk_item_salario_extra`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_itens_nota_ai` AFTER INSERT ON `kitanda_db_1`.`itens_nota` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`itens_nota` (`pk_itens_nota`, `descricao`, `valor`, `fk_nota`) VALUES (NEW.`pk_itens_nota`, NEW.`descricao`, NEW.`valor`, NEW.`fk_nota`) ON DUPLICATE KEY UPDATE `pk_itens_nota` = VALUES(`pk_itens_nota`), `descricao` = VALUES(`descricao`), `valor` = VALUES(`valor`), `fk_nota` = VALUES(`fk_nota`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_itens_nota_au` AFTER UPDATE ON `kitanda_db_1`.`itens_nota` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`itens_nota` SET `pk_itens_nota` = NEW.`pk_itens_nota`, `descricao` = NEW.`descricao`, `valor` = NEW.`valor`, `fk_nota` = NEW.`fk_nota` WHERE `pk_itens_nota` = OLD.`pk_itens_nota`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`itens_nota` (`pk_itens_nota`, `descricao`, `valor`, `fk_nota`) VALUES (NEW.`pk_itens_nota`, NEW.`descricao`, NEW.`valor`, NEW.`fk_nota`) ON DUPLICATE KEY UPDATE `pk_itens_nota` = VALUES(`pk_itens_nota`), `descricao` = VALUES(`descricao`), `valor` = VALUES(`valor`), `fk_nota` = VALUES(`fk_nota`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_itens_nota_ad` AFTER DELETE ON `kitanda_db_1`.`itens_nota` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`itens_nota` WHERE `pk_itens_nota` = OLD.`pk_itens_nota`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_levantamento_bancario_ai` AFTER INSERT ON `kitanda_db_1`.`levantamento_bancario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`levantamento_bancario` (`pk_levantamento`, `data`, `hora`, `nborderaux`, `valor`, `fk_banco`, `fk_usuario`, `obs`) VALUES (NEW.`pk_levantamento`, NEW.`data`, NEW.`hora`, NEW.`nborderaux`, NEW.`valor`, NEW.`fk_banco`, NEW.`fk_usuario`, NEW.`obs`) ON DUPLICATE KEY UPDATE `pk_levantamento` = VALUES(`pk_levantamento`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `nborderaux` = VALUES(`nborderaux`), `valor` = VALUES(`valor`), `fk_banco` = VALUES(`fk_banco`), `fk_usuario` = VALUES(`fk_usuario`), `obs` = VALUES(`obs`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_levantamento_bancario_au` AFTER UPDATE ON `kitanda_db_1`.`levantamento_bancario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`levantamento_bancario` SET `pk_levantamento` = NEW.`pk_levantamento`, `data` = NEW.`data`, `hora` = NEW.`hora`, `nborderaux` = NEW.`nborderaux`, `valor` = NEW.`valor`, `fk_banco` = NEW.`fk_banco`, `fk_usuario` = NEW.`fk_usuario`, `obs` = NEW.`obs` WHERE `pk_levantamento` = OLD.`pk_levantamento`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`levantamento_bancario` (`pk_levantamento`, `data`, `hora`, `nborderaux`, `valor`, `fk_banco`, `fk_usuario`, `obs`) VALUES (NEW.`pk_levantamento`, NEW.`data`, NEW.`hora`, NEW.`nborderaux`, NEW.`valor`, NEW.`fk_banco`, NEW.`fk_usuario`, NEW.`obs`) ON DUPLICATE KEY UPDATE `pk_levantamento` = VALUES(`pk_levantamento`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `nborderaux` = VALUES(`nborderaux`), `valor` = VALUES(`valor`), `fk_banco` = VALUES(`fk_banco`), `fk_usuario` = VALUES(`fk_usuario`), `obs` = VALUES(`obs`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_levantamento_bancario_ad` AFTER DELETE ON `kitanda_db_1`.`levantamento_bancario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`levantamento_bancario` WHERE `pk_levantamento` = OLD.`pk_levantamento`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_linha_ficha_tecnica_ai` AFTER INSERT ON `kitanda_db_1`.`linha_ficha_tecnica` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`linha_ficha_tecnica` (`id`, `igrendiente_id`, `igrendiente_designacao`, `unidade`, `preco_unitario`, `qtd_bruto`, `qtd_liquido`, `factor_correcao`, `custo_total`, `ficha_tecnica_id`, `unidade_compra`) VALUES (NEW.`id`, NEW.`igrendiente_id`, NEW.`igrendiente_designacao`, NEW.`unidade`, NEW.`preco_unitario`, NEW.`qtd_bruto`, NEW.`qtd_liquido`, NEW.`factor_correcao`, NEW.`custo_total`, NEW.`ficha_tecnica_id`, NEW.`unidade_compra`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `igrendiente_id` = VALUES(`igrendiente_id`), `igrendiente_designacao` = VALUES(`igrendiente_designacao`), `unidade` = VALUES(`unidade`), `preco_unitario` = VALUES(`preco_unitario`), `qtd_bruto` = VALUES(`qtd_bruto`), `qtd_liquido` = VALUES(`qtd_liquido`), `factor_correcao` = VALUES(`factor_correcao`), `custo_total` = VALUES(`custo_total`), `ficha_tecnica_id` = VALUES(`ficha_tecnica_id`), `unidade_compra` = VALUES(`unidade_compra`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_linha_ficha_tecnica_au` AFTER UPDATE ON `kitanda_db_1`.`linha_ficha_tecnica` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`linha_ficha_tecnica` SET `id` = NEW.`id`, `igrendiente_id` = NEW.`igrendiente_id`, `igrendiente_designacao` = NEW.`igrendiente_designacao`, `unidade` = NEW.`unidade`, `preco_unitario` = NEW.`preco_unitario`, `qtd_bruto` = NEW.`qtd_bruto`, `qtd_liquido` = NEW.`qtd_liquido`, `factor_correcao` = NEW.`factor_correcao`, `custo_total` = NEW.`custo_total`, `ficha_tecnica_id` = NEW.`ficha_tecnica_id`, `unidade_compra` = NEW.`unidade_compra` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`linha_ficha_tecnica` (`id`, `igrendiente_id`, `igrendiente_designacao`, `unidade`, `preco_unitario`, `qtd_bruto`, `qtd_liquido`, `factor_correcao`, `custo_total`, `ficha_tecnica_id`, `unidade_compra`) VALUES (NEW.`id`, NEW.`igrendiente_id`, NEW.`igrendiente_designacao`, NEW.`unidade`, NEW.`preco_unitario`, NEW.`qtd_bruto`, NEW.`qtd_liquido`, NEW.`factor_correcao`, NEW.`custo_total`, NEW.`ficha_tecnica_id`, NEW.`unidade_compra`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `igrendiente_id` = VALUES(`igrendiente_id`), `igrendiente_designacao` = VALUES(`igrendiente_designacao`), `unidade` = VALUES(`unidade`), `preco_unitario` = VALUES(`preco_unitario`), `qtd_bruto` = VALUES(`qtd_bruto`), `qtd_liquido` = VALUES(`qtd_liquido`), `factor_correcao` = VALUES(`factor_correcao`), `custo_total` = VALUES(`custo_total`), `ficha_tecnica_id` = VALUES(`ficha_tecnica_id`), `unidade_compra` = VALUES(`unidade_compra`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_linha_ficha_tecnica_ad` AFTER DELETE ON `kitanda_db_1`.`linha_ficha_tecnica` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`linha_ficha_tecnica` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_linha_transferencia_ai` AFTER INSERT ON `kitanda_db_1`.`linha_transferencia` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`linha_transferencia` (`pk_linha_transferencia`, `qtd_before`, `qtd`, `qtd_after`, `fk_armazem_origem`, `armazem_origem`, `fk_armazem_destino`, `armazem_destino`, `fk_transferencia_armazem`, `fk_produto`, `produto`) VALUES (NEW.`pk_linha_transferencia`, NEW.`qtd_before`, NEW.`qtd`, NEW.`qtd_after`, NEW.`fk_armazem_origem`, NEW.`armazem_origem`, NEW.`fk_armazem_destino`, NEW.`armazem_destino`, NEW.`fk_transferencia_armazem`, NEW.`fk_produto`, NEW.`produto`) ON DUPLICATE KEY UPDATE `pk_linha_transferencia` = VALUES(`pk_linha_transferencia`), `qtd_before` = VALUES(`qtd_before`), `qtd` = VALUES(`qtd`), `qtd_after` = VALUES(`qtd_after`), `fk_armazem_origem` = VALUES(`fk_armazem_origem`), `armazem_origem` = VALUES(`armazem_origem`), `fk_armazem_destino` = VALUES(`fk_armazem_destino`), `armazem_destino` = VALUES(`armazem_destino`), `fk_transferencia_armazem` = VALUES(`fk_transferencia_armazem`), `fk_produto` = VALUES(`fk_produto`), `produto` = VALUES(`produto`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_linha_transferencia_au` AFTER UPDATE ON `kitanda_db_1`.`linha_transferencia` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`linha_transferencia` SET `pk_linha_transferencia` = NEW.`pk_linha_transferencia`, `qtd_before` = NEW.`qtd_before`, `qtd` = NEW.`qtd`, `qtd_after` = NEW.`qtd_after`, `fk_armazem_origem` = NEW.`fk_armazem_origem`, `armazem_origem` = NEW.`armazem_origem`, `fk_armazem_destino` = NEW.`fk_armazem_destino`, `armazem_destino` = NEW.`armazem_destino`, `fk_transferencia_armazem` = NEW.`fk_transferencia_armazem`, `fk_produto` = NEW.`fk_produto`, `produto` = NEW.`produto` WHERE `pk_linha_transferencia` = OLD.`pk_linha_transferencia`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`linha_transferencia` (`pk_linha_transferencia`, `qtd_before`, `qtd`, `qtd_after`, `fk_armazem_origem`, `armazem_origem`, `fk_armazem_destino`, `armazem_destino`, `fk_transferencia_armazem`, `fk_produto`, `produto`) VALUES (NEW.`pk_linha_transferencia`, NEW.`qtd_before`, NEW.`qtd`, NEW.`qtd_after`, NEW.`fk_armazem_origem`, NEW.`armazem_origem`, NEW.`fk_armazem_destino`, NEW.`armazem_destino`, NEW.`fk_transferencia_armazem`, NEW.`fk_produto`, NEW.`produto`) ON DUPLICATE KEY UPDATE `pk_linha_transferencia` = VALUES(`pk_linha_transferencia`), `qtd_before` = VALUES(`qtd_before`), `qtd` = VALUES(`qtd`), `qtd_after` = VALUES(`qtd_after`), `fk_armazem_origem` = VALUES(`fk_armazem_origem`), `armazem_origem` = VALUES(`armazem_origem`), `fk_armazem_destino` = VALUES(`fk_armazem_destino`), `armazem_destino` = VALUES(`armazem_destino`), `fk_transferencia_armazem` = VALUES(`fk_transferencia_armazem`), `fk_produto` = VALUES(`fk_produto`), `produto` = VALUES(`produto`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_linha_transferencia_ad` AFTER DELETE ON `kitanda_db_1`.`linha_transferencia` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`linha_transferencia` WHERE `pk_linha_transferencia` = OLD.`pk_linha_transferencia`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_log_ai` AFTER INSERT ON `kitanda_db_1`.`log` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`log` (`pk_log`, `ip_maquina`, `estado`, `nome_maquina`) VALUES (NEW.`pk_log`, NEW.`ip_maquina`, NEW.`estado`, NEW.`nome_maquina`) ON DUPLICATE KEY UPDATE `pk_log` = VALUES(`pk_log`), `ip_maquina` = VALUES(`ip_maquina`), `estado` = VALUES(`estado`), `nome_maquina` = VALUES(`nome_maquina`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_log_au` AFTER UPDATE ON `kitanda_db_1`.`log` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`log` SET `pk_log` = NEW.`pk_log`, `ip_maquina` = NEW.`ip_maquina`, `estado` = NEW.`estado`, `nome_maquina` = NEW.`nome_maquina` WHERE `pk_log` = OLD.`pk_log`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`log` (`pk_log`, `ip_maquina`, `estado`, `nome_maquina`) VALUES (NEW.`pk_log`, NEW.`ip_maquina`, NEW.`estado`, NEW.`nome_maquina`) ON DUPLICATE KEY UPDATE `pk_log` = VALUES(`pk_log`), `ip_maquina` = VALUES(`ip_maquina`), `estado` = VALUES(`estado`), `nome_maquina` = VALUES(`nome_maquina`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_log_ad` AFTER DELETE ON `kitanda_db_1`.`log` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`log` WHERE `pk_log` = OLD.`pk_log`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_log_actividade_ai` AFTER INSERT ON `kitanda_db_1`.`log_actividade` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`log_actividade` (`pk_log_actividade`, `data_hora`, `cod_usuario`, `nome_usuario`, `modulo`, `formulario`, `accao`, `entidade`, `id_registo`, `descricao`, `valor_anterior`, `valor_novo`, `nome_maquina`, `ip_maquina`) VALUES (NEW.`pk_log_actividade`, NEW.`data_hora`, NEW.`cod_usuario`, NEW.`nome_usuario`, NEW.`modulo`, NEW.`formulario`, NEW.`accao`, NEW.`entidade`, NEW.`id_registo`, NEW.`descricao`, NEW.`valor_anterior`, NEW.`valor_novo`, NEW.`nome_maquina`, NEW.`ip_maquina`) ON DUPLICATE KEY UPDATE `pk_log_actividade` = VALUES(`pk_log_actividade`), `data_hora` = VALUES(`data_hora`), `cod_usuario` = VALUES(`cod_usuario`), `nome_usuario` = VALUES(`nome_usuario`), `modulo` = VALUES(`modulo`), `formulario` = VALUES(`formulario`), `accao` = VALUES(`accao`), `entidade` = VALUES(`entidade`), `id_registo` = VALUES(`id_registo`), `descricao` = VALUES(`descricao`), `valor_anterior` = VALUES(`valor_anterior`), `valor_novo` = VALUES(`valor_novo`), `nome_maquina` = VALUES(`nome_maquina`), `ip_maquina` = VALUES(`ip_maquina`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_log_actividade_au` AFTER UPDATE ON `kitanda_db_1`.`log_actividade` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`log_actividade` SET `pk_log_actividade` = NEW.`pk_log_actividade`, `data_hora` = NEW.`data_hora`, `cod_usuario` = NEW.`cod_usuario`, `nome_usuario` = NEW.`nome_usuario`, `modulo` = NEW.`modulo`, `formulario` = NEW.`formulario`, `accao` = NEW.`accao`, `entidade` = NEW.`entidade`, `id_registo` = NEW.`id_registo`, `descricao` = NEW.`descricao`, `valor_anterior` = NEW.`valor_anterior`, `valor_novo` = NEW.`valor_novo`, `nome_maquina` = NEW.`nome_maquina`, `ip_maquina` = NEW.`ip_maquina` WHERE `pk_log_actividade` = OLD.`pk_log_actividade`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`log_actividade` (`pk_log_actividade`, `data_hora`, `cod_usuario`, `nome_usuario`, `modulo`, `formulario`, `accao`, `entidade`, `id_registo`, `descricao`, `valor_anterior`, `valor_novo`, `nome_maquina`, `ip_maquina`) VALUES (NEW.`pk_log_actividade`, NEW.`data_hora`, NEW.`cod_usuario`, NEW.`nome_usuario`, NEW.`modulo`, NEW.`formulario`, NEW.`accao`, NEW.`entidade`, NEW.`id_registo`, NEW.`descricao`, NEW.`valor_anterior`, NEW.`valor_novo`, NEW.`nome_maquina`, NEW.`ip_maquina`) ON DUPLICATE KEY UPDATE `pk_log_actividade` = VALUES(`pk_log_actividade`), `data_hora` = VALUES(`data_hora`), `cod_usuario` = VALUES(`cod_usuario`), `nome_usuario` = VALUES(`nome_usuario`), `modulo` = VALUES(`modulo`), `formulario` = VALUES(`formulario`), `accao` = VALUES(`accao`), `entidade` = VALUES(`entidade`), `id_registo` = VALUES(`id_registo`), `descricao` = VALUES(`descricao`), `valor_anterior` = VALUES(`valor_anterior`), `valor_novo` = VALUES(`valor_novo`), `nome_maquina` = VALUES(`nome_maquina`), `ip_maquina` = VALUES(`ip_maquina`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_log_actividade_ad` AFTER DELETE ON `kitanda_db_1`.`log_actividade` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`log_actividade` WHERE `pk_log_actividade` = OLD.`pk_log_actividade`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_lote_ai` AFTER INSERT ON `kitanda_db_1`.`lote` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`lote` (`id`, `fk_produto`, `numero_lote`, `data_entrada`, `data_fabrica`, `data_validade`, `qtd`, `estado`, `fk_armazem`) VALUES (NEW.`id`, NEW.`fk_produto`, NEW.`numero_lote`, NEW.`data_entrada`, NEW.`data_fabrica`, NEW.`data_validade`, NEW.`qtd`, NEW.`estado`, NEW.`fk_armazem`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `fk_produto` = VALUES(`fk_produto`), `numero_lote` = VALUES(`numero_lote`), `data_entrada` = VALUES(`data_entrada`), `data_fabrica` = VALUES(`data_fabrica`), `data_validade` = VALUES(`data_validade`), `qtd` = VALUES(`qtd`), `estado` = VALUES(`estado`), `fk_armazem` = VALUES(`fk_armazem`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_lote_au` AFTER UPDATE ON `kitanda_db_1`.`lote` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`lote` SET `id` = NEW.`id`, `fk_produto` = NEW.`fk_produto`, `numero_lote` = NEW.`numero_lote`, `data_entrada` = NEW.`data_entrada`, `data_fabrica` = NEW.`data_fabrica`, `data_validade` = NEW.`data_validade`, `qtd` = NEW.`qtd`, `estado` = NEW.`estado`, `fk_armazem` = NEW.`fk_armazem` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`lote` (`id`, `fk_produto`, `numero_lote`, `data_entrada`, `data_fabrica`, `data_validade`, `qtd`, `estado`, `fk_armazem`) VALUES (NEW.`id`, NEW.`fk_produto`, NEW.`numero_lote`, NEW.`data_entrada`, NEW.`data_fabrica`, NEW.`data_validade`, NEW.`qtd`, NEW.`estado`, NEW.`fk_armazem`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `fk_produto` = VALUES(`fk_produto`), `numero_lote` = VALUES(`numero_lote`), `data_entrada` = VALUES(`data_entrada`), `data_fabrica` = VALUES(`data_fabrica`), `data_validade` = VALUES(`data_validade`), `qtd` = VALUES(`qtd`), `estado` = VALUES(`estado`), `fk_armazem` = VALUES(`fk_armazem`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_lote_ad` AFTER DELETE ON `kitanda_db_1`.`lote` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`lote` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_lote_movimento_ai` AFTER INSERT ON `kitanda_db_1`.`lote_movimento` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`lote_movimento` (`id`, `fk_lote`, `fk_operacao`, `tipo`, `qtd`, `qtd_devolvida`, `data_mov`) VALUES (NEW.`id`, NEW.`fk_lote`, NEW.`fk_operacao`, NEW.`tipo`, NEW.`qtd`, NEW.`qtd_devolvida`, NEW.`data_mov`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `fk_lote` = VALUES(`fk_lote`), `fk_operacao` = VALUES(`fk_operacao`), `tipo` = VALUES(`tipo`), `qtd` = VALUES(`qtd`), `qtd_devolvida` = VALUES(`qtd_devolvida`), `data_mov` = VALUES(`data_mov`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_lote_movimento_au` AFTER UPDATE ON `kitanda_db_1`.`lote_movimento` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`lote_movimento` SET `id` = NEW.`id`, `fk_lote` = NEW.`fk_lote`, `fk_operacao` = NEW.`fk_operacao`, `tipo` = NEW.`tipo`, `qtd` = NEW.`qtd`, `qtd_devolvida` = NEW.`qtd_devolvida`, `data_mov` = NEW.`data_mov` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`lote_movimento` (`id`, `fk_lote`, `fk_operacao`, `tipo`, `qtd`, `qtd_devolvida`, `data_mov`) VALUES (NEW.`id`, NEW.`fk_lote`, NEW.`fk_operacao`, NEW.`tipo`, NEW.`qtd`, NEW.`qtd_devolvida`, NEW.`data_mov`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `fk_lote` = VALUES(`fk_lote`), `fk_operacao` = VALUES(`fk_operacao`), `tipo` = VALUES(`tipo`), `qtd` = VALUES(`qtd`), `qtd_devolvida` = VALUES(`qtd_devolvida`), `data_mov` = VALUES(`data_mov`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_lote_movimento_ad` AFTER DELETE ON `kitanda_db_1`.`lote_movimento` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`lote_movimento` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_marca_ai` AFTER INSERT ON `kitanda_db_1`.`marca` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`marca` (`pk_marca`, `designacao`) VALUES (NEW.`pk_marca`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_marca` = VALUES(`pk_marca`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_marca_au` AFTER UPDATE ON `kitanda_db_1`.`marca` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`marca` SET `pk_marca` = NEW.`pk_marca`, `designacao` = NEW.`designacao` WHERE `pk_marca` = OLD.`pk_marca`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`marca` (`pk_marca`, `designacao`) VALUES (NEW.`pk_marca`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_marca` = VALUES(`pk_marca`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_marca_ad` AFTER DELETE ON `kitanda_db_1`.`marca` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`marca` WHERE `pk_marca` = OLD.`pk_marca`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_master_table_ai` AFTER INSERT ON `kitanda_db_1`.`master_table` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`master_table` (`pk_master_table`, `designcao`, `fk_master_table`) VALUES (NEW.`pk_master_table`, NEW.`designcao`, NEW.`fk_master_table`) ON DUPLICATE KEY UPDATE `pk_master_table` = VALUES(`pk_master_table`), `designcao` = VALUES(`designcao`), `fk_master_table` = VALUES(`fk_master_table`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_master_table_au` AFTER UPDATE ON `kitanda_db_1`.`master_table` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`master_table` SET `pk_master_table` = NEW.`pk_master_table`, `designcao` = NEW.`designcao`, `fk_master_table` = NEW.`fk_master_table` WHERE `pk_master_table` = OLD.`pk_master_table`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`master_table` (`pk_master_table`, `designcao`, `fk_master_table`) VALUES (NEW.`pk_master_table`, NEW.`designcao`, NEW.`fk_master_table`) ON DUPLICATE KEY UPDATE `pk_master_table` = VALUES(`pk_master_table`), `designcao` = VALUES(`designcao`), `fk_master_table` = VALUES(`fk_master_table`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_master_table_ad` AFTER DELETE ON `kitanda_db_1`.`master_table` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`master_table` WHERE `pk_master_table` = OLD.`pk_master_table`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_modalidade_ai` AFTER INSERT ON `kitanda_db_1`.`modalidade` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`modalidade` (`pk_modalidade`, `designacao`, `dias_uteis_trabalho`) VALUES (NEW.`pk_modalidade`, NEW.`designacao`, NEW.`dias_uteis_trabalho`) ON DUPLICATE KEY UPDATE `pk_modalidade` = VALUES(`pk_modalidade`), `designacao` = VALUES(`designacao`), `dias_uteis_trabalho` = VALUES(`dias_uteis_trabalho`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_modalidade_au` AFTER UPDATE ON `kitanda_db_1`.`modalidade` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`modalidade` SET `pk_modalidade` = NEW.`pk_modalidade`, `designacao` = NEW.`designacao`, `dias_uteis_trabalho` = NEW.`dias_uteis_trabalho` WHERE `pk_modalidade` = OLD.`pk_modalidade`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`modalidade` (`pk_modalidade`, `designacao`, `dias_uteis_trabalho`) VALUES (NEW.`pk_modalidade`, NEW.`designacao`, NEW.`dias_uteis_trabalho`) ON DUPLICATE KEY UPDATE `pk_modalidade` = VALUES(`pk_modalidade`), `designacao` = VALUES(`designacao`), `dias_uteis_trabalho` = VALUES(`dias_uteis_trabalho`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_modalidade_ad` AFTER DELETE ON `kitanda_db_1`.`modalidade` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`modalidade` WHERE `pk_modalidade` = OLD.`pk_modalidade`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_modelo_ai` AFTER INSERT ON `kitanda_db_1`.`modelo` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`modelo` (`pk_modelo`, `designacao`, `fk_marca`) VALUES (NEW.`pk_modelo`, NEW.`designacao`, NEW.`fk_marca`) ON DUPLICATE KEY UPDATE `pk_modelo` = VALUES(`pk_modelo`), `designacao` = VALUES(`designacao`), `fk_marca` = VALUES(`fk_marca`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_modelo_au` AFTER UPDATE ON `kitanda_db_1`.`modelo` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`modelo` SET `pk_modelo` = NEW.`pk_modelo`, `designacao` = NEW.`designacao`, `fk_marca` = NEW.`fk_marca` WHERE `pk_modelo` = OLD.`pk_modelo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`modelo` (`pk_modelo`, `designacao`, `fk_marca`) VALUES (NEW.`pk_modelo`, NEW.`designacao`, NEW.`fk_marca`) ON DUPLICATE KEY UPDATE `pk_modelo` = VALUES(`pk_modelo`), `designacao` = VALUES(`designacao`), `fk_marca` = VALUES(`fk_marca`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_modelo_ad` AFTER DELETE ON `kitanda_db_1`.`modelo` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`modelo` WHERE `pk_modelo` = OLD.`pk_modelo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_moeda_ai` AFTER INSERT ON `kitanda_db_1`.`moeda` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`moeda` (`pk_moeda`, `designacao`, `abreviacao`) VALUES (NEW.`pk_moeda`, NEW.`designacao`, NEW.`abreviacao`) ON DUPLICATE KEY UPDATE `pk_moeda` = VALUES(`pk_moeda`), `designacao` = VALUES(`designacao`), `abreviacao` = VALUES(`abreviacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_moeda_au` AFTER UPDATE ON `kitanda_db_1`.`moeda` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`moeda` SET `pk_moeda` = NEW.`pk_moeda`, `designacao` = NEW.`designacao`, `abreviacao` = NEW.`abreviacao` WHERE `pk_moeda` = OLD.`pk_moeda`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`moeda` (`pk_moeda`, `designacao`, `abreviacao`) VALUES (NEW.`pk_moeda`, NEW.`designacao`, NEW.`abreviacao`) ON DUPLICATE KEY UPDATE `pk_moeda` = VALUES(`pk_moeda`), `designacao` = VALUES(`designacao`), `abreviacao` = VALUES(`abreviacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_moeda_ad` AFTER DELETE ON `kitanda_db_1`.`moeda` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`moeda` WHERE `pk_moeda` = OLD.`pk_moeda`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_movimentacao_ai` AFTER INSERT ON `kitanda_db_1`.`movimentacao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`movimentacao` (`id`, `produto_id`, `data_mov`, `tipo`, `documento`, `quantidade_anterior`, `quantidade`, `quantidade_actual`, `valor_unitario`, `valor_total`, `custo_medio`, `usuario_id`, `armazem_id`) VALUES (NEW.`id`, NEW.`produto_id`, NEW.`data_mov`, NEW.`tipo`, NEW.`documento`, NEW.`quantidade_anterior`, NEW.`quantidade`, NEW.`quantidade_actual`, NEW.`valor_unitario`, NEW.`valor_total`, NEW.`custo_medio`, NEW.`usuario_id`, NEW.`armazem_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `produto_id` = VALUES(`produto_id`), `data_mov` = VALUES(`data_mov`), `tipo` = VALUES(`tipo`), `documento` = VALUES(`documento`), `quantidade_anterior` = VALUES(`quantidade_anterior`), `quantidade` = VALUES(`quantidade`), `quantidade_actual` = VALUES(`quantidade_actual`), `valor_unitario` = VALUES(`valor_unitario`), `valor_total` = VALUES(`valor_total`), `custo_medio` = VALUES(`custo_medio`), `usuario_id` = VALUES(`usuario_id`), `armazem_id` = VALUES(`armazem_id`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_movimentacao_au` AFTER UPDATE ON `kitanda_db_1`.`movimentacao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`movimentacao` SET `id` = NEW.`id`, `produto_id` = NEW.`produto_id`, `data_mov` = NEW.`data_mov`, `tipo` = NEW.`tipo`, `documento` = NEW.`documento`, `quantidade_anterior` = NEW.`quantidade_anterior`, `quantidade` = NEW.`quantidade`, `quantidade_actual` = NEW.`quantidade_actual`, `valor_unitario` = NEW.`valor_unitario`, `valor_total` = NEW.`valor_total`, `custo_medio` = NEW.`custo_medio`, `usuario_id` = NEW.`usuario_id`, `armazem_id` = NEW.`armazem_id` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`movimentacao` (`id`, `produto_id`, `data_mov`, `tipo`, `documento`, `quantidade_anterior`, `quantidade`, `quantidade_actual`, `valor_unitario`, `valor_total`, `custo_medio`, `usuario_id`, `armazem_id`) VALUES (NEW.`id`, NEW.`produto_id`, NEW.`data_mov`, NEW.`tipo`, NEW.`documento`, NEW.`quantidade_anterior`, NEW.`quantidade`, NEW.`quantidade_actual`, NEW.`valor_unitario`, NEW.`valor_total`, NEW.`custo_medio`, NEW.`usuario_id`, NEW.`armazem_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `produto_id` = VALUES(`produto_id`), `data_mov` = VALUES(`data_mov`), `tipo` = VALUES(`tipo`), `documento` = VALUES(`documento`), `quantidade_anterior` = VALUES(`quantidade_anterior`), `quantidade` = VALUES(`quantidade`), `quantidade_actual` = VALUES(`quantidade_actual`), `valor_unitario` = VALUES(`valor_unitario`), `valor_total` = VALUES(`valor_total`), `custo_medio` = VALUES(`custo_medio`), `usuario_id` = VALUES(`usuario_id`), `armazem_id` = VALUES(`armazem_id`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_movimentacao_ad` AFTER DELETE ON `kitanda_db_1`.`movimentacao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`movimentacao` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_multa_servico_ai` AFTER INSERT ON `kitanda_db_1`.`multa_servico` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`multa_servico` (`id`, `day_start`, `day_end`, `valor`, `data_registro`, `produto_id`, `usuario_id`) VALUES (NEW.`id`, NEW.`day_start`, NEW.`day_end`, NEW.`valor`, NEW.`data_registro`, NEW.`produto_id`, NEW.`usuario_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `day_start` = VALUES(`day_start`), `day_end` = VALUES(`day_end`), `valor` = VALUES(`valor`), `data_registro` = VALUES(`data_registro`), `produto_id` = VALUES(`produto_id`), `usuario_id` = VALUES(`usuario_id`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_multa_servico_au` AFTER UPDATE ON `kitanda_db_1`.`multa_servico` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`multa_servico` SET `id` = NEW.`id`, `day_start` = NEW.`day_start`, `day_end` = NEW.`day_end`, `valor` = NEW.`valor`, `data_registro` = NEW.`data_registro`, `produto_id` = NEW.`produto_id`, `usuario_id` = NEW.`usuario_id` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`multa_servico` (`id`, `day_start`, `day_end`, `valor`, `data_registro`, `produto_id`, `usuario_id`) VALUES (NEW.`id`, NEW.`day_start`, NEW.`day_end`, NEW.`valor`, NEW.`data_registro`, NEW.`produto_id`, NEW.`usuario_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `day_start` = VALUES(`day_start`), `day_end` = VALUES(`day_end`), `valor` = VALUES(`valor`), `data_registro` = VALUES(`data_registro`), `produto_id` = VALUES(`produto_id`), `usuario_id` = VALUES(`usuario_id`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_multa_servico_ad` AFTER DELETE ON `kitanda_db_1`.`multa_servico` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`multa_servico` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_ai` AFTER INSERT ON `kitanda_db_1`.`notas` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`notas` (`pk_nota`, `tipo_nota`, `cod_nota`, `ref_cod_nota`, `dataNota`, `total_venda`, `performance`, `credito`, `valor_entregue`, `troco`, `hora`, `nome_cliente`, `status_eliminado`, `desconto_total`, `total_iva`, `total_geral`, `cod_fact`, `assinatura`, `hash_cod`, `obs`, `ref_cod_fact`, `total_por_extenso`, `idBanco`, `codigo_usuario`, `codigo_cliente`, `idArmazemFK`, `fk_documento`, `fk_ano_economico`, `fk_cambio`, `ref_fact_data`, `estado`, `desconto_comercial`, `desconto_finaceiro`, `local_carga`, `local_descarga`, `motivo`, `total_incidencia`, `cliente_nif`, `total_incidencia_isento`, `ref_data_fact`, `total_nota`) VALUES (NEW.`pk_nota`, NEW.`tipo_nota`, NEW.`cod_nota`, NEW.`ref_cod_nota`, NEW.`dataNota`, NEW.`total_venda`, NEW.`performance`, NEW.`credito`, NEW.`valor_entregue`, NEW.`troco`, NEW.`hora`, NEW.`nome_cliente`, NEW.`status_eliminado`, NEW.`desconto_total`, NEW.`total_iva`, NEW.`total_geral`, NEW.`cod_fact`, NEW.`assinatura`, NEW.`hash_cod`, NEW.`obs`, NEW.`ref_cod_fact`, NEW.`total_por_extenso`, NEW.`idBanco`, NEW.`codigo_usuario`, NEW.`codigo_cliente`, NEW.`idArmazemFK`, NEW.`fk_documento`, NEW.`fk_ano_economico`, NEW.`fk_cambio`, NEW.`ref_fact_data`, NEW.`estado`, NEW.`desconto_comercial`, NEW.`desconto_finaceiro`, NEW.`local_carga`, NEW.`local_descarga`, NEW.`motivo`, NEW.`total_incidencia`, NEW.`cliente_nif`, NEW.`total_incidencia_isento`, NEW.`ref_data_fact`, NEW.`total_nota`) ON DUPLICATE KEY UPDATE `pk_nota` = VALUES(`pk_nota`), `tipo_nota` = VALUES(`tipo_nota`), `cod_nota` = VALUES(`cod_nota`), `ref_cod_nota` = VALUES(`ref_cod_nota`), `dataNota` = VALUES(`dataNota`), `total_venda` = VALUES(`total_venda`), `performance` = VALUES(`performance`), `credito` = VALUES(`credito`), `valor_entregue` = VALUES(`valor_entregue`), `troco` = VALUES(`troco`), `hora` = VALUES(`hora`), `nome_cliente` = VALUES(`nome_cliente`), `status_eliminado` = VALUES(`status_eliminado`), `desconto_total` = VALUES(`desconto_total`), `total_iva` = VALUES(`total_iva`), `total_geral` = VALUES(`total_geral`), `cod_fact` = VALUES(`cod_fact`), `assinatura` = VALUES(`assinatura`), `hash_cod` = VALUES(`hash_cod`), `obs` = VALUES(`obs`), `ref_cod_fact` = VALUES(`ref_cod_fact`), `total_por_extenso` = VALUES(`total_por_extenso`), `idBanco` = VALUES(`idBanco`), `codigo_usuario` = VALUES(`codigo_usuario`), `codigo_cliente` = VALUES(`codigo_cliente`), `idArmazemFK` = VALUES(`idArmazemFK`), `fk_documento` = VALUES(`fk_documento`), `fk_ano_economico` = VALUES(`fk_ano_economico`), `fk_cambio` = VALUES(`fk_cambio`), `ref_fact_data` = VALUES(`ref_fact_data`), `estado` = VALUES(`estado`), `desconto_comercial` = VALUES(`desconto_comercial`), `desconto_finaceiro` = VALUES(`desconto_finaceiro`), `local_carga` = VALUES(`local_carga`), `local_descarga` = VALUES(`local_descarga`), `motivo` = VALUES(`motivo`), `total_incidencia` = VALUES(`total_incidencia`), `cliente_nif` = VALUES(`cliente_nif`), `total_incidencia_isento` = VALUES(`total_incidencia_isento`), `ref_data_fact` = VALUES(`ref_data_fact`), `total_nota` = VALUES(`total_nota`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_au` AFTER UPDATE ON `kitanda_db_1`.`notas` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`notas` SET `pk_nota` = NEW.`pk_nota`, `tipo_nota` = NEW.`tipo_nota`, `cod_nota` = NEW.`cod_nota`, `ref_cod_nota` = NEW.`ref_cod_nota`, `dataNota` = NEW.`dataNota`, `total_venda` = NEW.`total_venda`, `performance` = NEW.`performance`, `credito` = NEW.`credito`, `valor_entregue` = NEW.`valor_entregue`, `troco` = NEW.`troco`, `hora` = NEW.`hora`, `nome_cliente` = NEW.`nome_cliente`, `status_eliminado` = NEW.`status_eliminado`, `desconto_total` = NEW.`desconto_total`, `total_iva` = NEW.`total_iva`, `total_geral` = NEW.`total_geral`, `cod_fact` = NEW.`cod_fact`, `assinatura` = NEW.`assinatura`, `hash_cod` = NEW.`hash_cod`, `obs` = NEW.`obs`, `ref_cod_fact` = NEW.`ref_cod_fact`, `total_por_extenso` = NEW.`total_por_extenso`, `idBanco` = NEW.`idBanco`, `codigo_usuario` = NEW.`codigo_usuario`, `codigo_cliente` = NEW.`codigo_cliente`, `idArmazemFK` = NEW.`idArmazemFK`, `fk_documento` = NEW.`fk_documento`, `fk_ano_economico` = NEW.`fk_ano_economico`, `fk_cambio` = NEW.`fk_cambio`, `ref_fact_data` = NEW.`ref_fact_data`, `estado` = NEW.`estado`, `desconto_comercial` = NEW.`desconto_comercial`, `desconto_finaceiro` = NEW.`desconto_finaceiro`, `local_carga` = NEW.`local_carga`, `local_descarga` = NEW.`local_descarga`, `motivo` = NEW.`motivo`, `total_incidencia` = NEW.`total_incidencia`, `cliente_nif` = NEW.`cliente_nif`, `total_incidencia_isento` = NEW.`total_incidencia_isento`, `ref_data_fact` = NEW.`ref_data_fact`, `total_nota` = NEW.`total_nota` WHERE `pk_nota` = OLD.`pk_nota`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`notas` (`pk_nota`, `tipo_nota`, `cod_nota`, `ref_cod_nota`, `dataNota`, `total_venda`, `performance`, `credito`, `valor_entregue`, `troco`, `hora`, `nome_cliente`, `status_eliminado`, `desconto_total`, `total_iva`, `total_geral`, `cod_fact`, `assinatura`, `hash_cod`, `obs`, `ref_cod_fact`, `total_por_extenso`, `idBanco`, `codigo_usuario`, `codigo_cliente`, `idArmazemFK`, `fk_documento`, `fk_ano_economico`, `fk_cambio`, `ref_fact_data`, `estado`, `desconto_comercial`, `desconto_finaceiro`, `local_carga`, `local_descarga`, `motivo`, `total_incidencia`, `cliente_nif`, `total_incidencia_isento`, `ref_data_fact`, `total_nota`) VALUES (NEW.`pk_nota`, NEW.`tipo_nota`, NEW.`cod_nota`, NEW.`ref_cod_nota`, NEW.`dataNota`, NEW.`total_venda`, NEW.`performance`, NEW.`credito`, NEW.`valor_entregue`, NEW.`troco`, NEW.`hora`, NEW.`nome_cliente`, NEW.`status_eliminado`, NEW.`desconto_total`, NEW.`total_iva`, NEW.`total_geral`, NEW.`cod_fact`, NEW.`assinatura`, NEW.`hash_cod`, NEW.`obs`, NEW.`ref_cod_fact`, NEW.`total_por_extenso`, NEW.`idBanco`, NEW.`codigo_usuario`, NEW.`codigo_cliente`, NEW.`idArmazemFK`, NEW.`fk_documento`, NEW.`fk_ano_economico`, NEW.`fk_cambio`, NEW.`ref_fact_data`, NEW.`estado`, NEW.`desconto_comercial`, NEW.`desconto_finaceiro`, NEW.`local_carga`, NEW.`local_descarga`, NEW.`motivo`, NEW.`total_incidencia`, NEW.`cliente_nif`, NEW.`total_incidencia_isento`, NEW.`ref_data_fact`, NEW.`total_nota`) ON DUPLICATE KEY UPDATE `pk_nota` = VALUES(`pk_nota`), `tipo_nota` = VALUES(`tipo_nota`), `cod_nota` = VALUES(`cod_nota`), `ref_cod_nota` = VALUES(`ref_cod_nota`), `dataNota` = VALUES(`dataNota`), `total_venda` = VALUES(`total_venda`), `performance` = VALUES(`performance`), `credito` = VALUES(`credito`), `valor_entregue` = VALUES(`valor_entregue`), `troco` = VALUES(`troco`), `hora` = VALUES(`hora`), `nome_cliente` = VALUES(`nome_cliente`), `status_eliminado` = VALUES(`status_eliminado`), `desconto_total` = VALUES(`desconto_total`), `total_iva` = VALUES(`total_iva`), `total_geral` = VALUES(`total_geral`), `cod_fact` = VALUES(`cod_fact`), `assinatura` = VALUES(`assinatura`), `hash_cod` = VALUES(`hash_cod`), `obs` = VALUES(`obs`), `ref_cod_fact` = VALUES(`ref_cod_fact`), `total_por_extenso` = VALUES(`total_por_extenso`), `idBanco` = VALUES(`idBanco`), `codigo_usuario` = VALUES(`codigo_usuario`), `codigo_cliente` = VALUES(`codigo_cliente`), `idArmazemFK` = VALUES(`idArmazemFK`), `fk_documento` = VALUES(`fk_documento`), `fk_ano_economico` = VALUES(`fk_ano_economico`), `fk_cambio` = VALUES(`fk_cambio`), `ref_fact_data` = VALUES(`ref_fact_data`), `estado` = VALUES(`estado`), `desconto_comercial` = VALUES(`desconto_comercial`), `desconto_finaceiro` = VALUES(`desconto_finaceiro`), `local_carga` = VALUES(`local_carga`), `local_descarga` = VALUES(`local_descarga`), `motivo` = VALUES(`motivo`), `total_incidencia` = VALUES(`total_incidencia`), `cliente_nif` = VALUES(`cliente_nif`), `total_incidencia_isento` = VALUES(`total_incidencia_isento`), `ref_data_fact` = VALUES(`ref_data_fact`), `total_nota` = VALUES(`total_nota`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_ad` AFTER DELETE ON `kitanda_db_1`.`notas` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`notas` WHERE `pk_nota` = OLD.`pk_nota`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_compras_ai` AFTER INSERT ON `kitanda_db_1`.`notas_compras` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`notas_compras` (`pk_nota_compras`, `cod_nota`, `ref_cod_nota`, `dataNota`, `total_compra`, `performance`, `credito`, `hora`, `status_eliminado`, `desconto_total`, `total_iva`, `total_geral`, `cod_fact`, `assinatura`, `hash_cod`, `obs`, `ref_cod_fact`, `total_por_extenso`, `idBanco`, `codigo_usuario`, `fk_fornecedor`, `fk_documento`, `fk_ano_economico`, `ref_fact_data`, `estado`, `desconto_comercial`, `desconto_financeiro`, `motivo`, `total_incidencia`, `total_incidencia_isento`, `ref_data_fact`, `idArmazemFK`) VALUES (NEW.`pk_nota_compras`, NEW.`cod_nota`, NEW.`ref_cod_nota`, NEW.`dataNota`, NEW.`total_compra`, NEW.`performance`, NEW.`credito`, NEW.`hora`, NEW.`status_eliminado`, NEW.`desconto_total`, NEW.`total_iva`, NEW.`total_geral`, NEW.`cod_fact`, NEW.`assinatura`, NEW.`hash_cod`, NEW.`obs`, NEW.`ref_cod_fact`, NEW.`total_por_extenso`, NEW.`idBanco`, NEW.`codigo_usuario`, NEW.`fk_fornecedor`, NEW.`fk_documento`, NEW.`fk_ano_economico`, NEW.`ref_fact_data`, NEW.`estado`, NEW.`desconto_comercial`, NEW.`desconto_financeiro`, NEW.`motivo`, NEW.`total_incidencia`, NEW.`total_incidencia_isento`, NEW.`ref_data_fact`, NEW.`idArmazemFK`) ON DUPLICATE KEY UPDATE `pk_nota_compras` = VALUES(`pk_nota_compras`), `cod_nota` = VALUES(`cod_nota`), `ref_cod_nota` = VALUES(`ref_cod_nota`), `dataNota` = VALUES(`dataNota`), `total_compra` = VALUES(`total_compra`), `performance` = VALUES(`performance`), `credito` = VALUES(`credito`), `hora` = VALUES(`hora`), `status_eliminado` = VALUES(`status_eliminado`), `desconto_total` = VALUES(`desconto_total`), `total_iva` = VALUES(`total_iva`), `total_geral` = VALUES(`total_geral`), `cod_fact` = VALUES(`cod_fact`), `assinatura` = VALUES(`assinatura`), `hash_cod` = VALUES(`hash_cod`), `obs` = VALUES(`obs`), `ref_cod_fact` = VALUES(`ref_cod_fact`), `total_por_extenso` = VALUES(`total_por_extenso`), `idBanco` = VALUES(`idBanco`), `codigo_usuario` = VALUES(`codigo_usuario`), `fk_fornecedor` = VALUES(`fk_fornecedor`), `fk_documento` = VALUES(`fk_documento`), `fk_ano_economico` = VALUES(`fk_ano_economico`), `ref_fact_data` = VALUES(`ref_fact_data`), `estado` = VALUES(`estado`), `desconto_comercial` = VALUES(`desconto_comercial`), `desconto_financeiro` = VALUES(`desconto_financeiro`), `motivo` = VALUES(`motivo`), `total_incidencia` = VALUES(`total_incidencia`), `total_incidencia_isento` = VALUES(`total_incidencia_isento`), `ref_data_fact` = VALUES(`ref_data_fact`), `idArmazemFK` = VALUES(`idArmazemFK`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_compras_au` AFTER UPDATE ON `kitanda_db_1`.`notas_compras` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`notas_compras` SET `pk_nota_compras` = NEW.`pk_nota_compras`, `cod_nota` = NEW.`cod_nota`, `ref_cod_nota` = NEW.`ref_cod_nota`, `dataNota` = NEW.`dataNota`, `total_compra` = NEW.`total_compra`, `performance` = NEW.`performance`, `credito` = NEW.`credito`, `hora` = NEW.`hora`, `status_eliminado` = NEW.`status_eliminado`, `desconto_total` = NEW.`desconto_total`, `total_iva` = NEW.`total_iva`, `total_geral` = NEW.`total_geral`, `cod_fact` = NEW.`cod_fact`, `assinatura` = NEW.`assinatura`, `hash_cod` = NEW.`hash_cod`, `obs` = NEW.`obs`, `ref_cod_fact` = NEW.`ref_cod_fact`, `total_por_extenso` = NEW.`total_por_extenso`, `idBanco` = NEW.`idBanco`, `codigo_usuario` = NEW.`codigo_usuario`, `fk_fornecedor` = NEW.`fk_fornecedor`, `fk_documento` = NEW.`fk_documento`, `fk_ano_economico` = NEW.`fk_ano_economico`, `ref_fact_data` = NEW.`ref_fact_data`, `estado` = NEW.`estado`, `desconto_comercial` = NEW.`desconto_comercial`, `desconto_financeiro` = NEW.`desconto_financeiro`, `motivo` = NEW.`motivo`, `total_incidencia` = NEW.`total_incidencia`, `total_incidencia_isento` = NEW.`total_incidencia_isento`, `ref_data_fact` = NEW.`ref_data_fact`, `idArmazemFK` = NEW.`idArmazemFK` WHERE `pk_nota_compras` = OLD.`pk_nota_compras`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`notas_compras` (`pk_nota_compras`, `cod_nota`, `ref_cod_nota`, `dataNota`, `total_compra`, `performance`, `credito`, `hora`, `status_eliminado`, `desconto_total`, `total_iva`, `total_geral`, `cod_fact`, `assinatura`, `hash_cod`, `obs`, `ref_cod_fact`, `total_por_extenso`, `idBanco`, `codigo_usuario`, `fk_fornecedor`, `fk_documento`, `fk_ano_economico`, `ref_fact_data`, `estado`, `desconto_comercial`, `desconto_financeiro`, `motivo`, `total_incidencia`, `total_incidencia_isento`, `ref_data_fact`, `idArmazemFK`) VALUES (NEW.`pk_nota_compras`, NEW.`cod_nota`, NEW.`ref_cod_nota`, NEW.`dataNota`, NEW.`total_compra`, NEW.`performance`, NEW.`credito`, NEW.`hora`, NEW.`status_eliminado`, NEW.`desconto_total`, NEW.`total_iva`, NEW.`total_geral`, NEW.`cod_fact`, NEW.`assinatura`, NEW.`hash_cod`, NEW.`obs`, NEW.`ref_cod_fact`, NEW.`total_por_extenso`, NEW.`idBanco`, NEW.`codigo_usuario`, NEW.`fk_fornecedor`, NEW.`fk_documento`, NEW.`fk_ano_economico`, NEW.`ref_fact_data`, NEW.`estado`, NEW.`desconto_comercial`, NEW.`desconto_financeiro`, NEW.`motivo`, NEW.`total_incidencia`, NEW.`total_incidencia_isento`, NEW.`ref_data_fact`, NEW.`idArmazemFK`) ON DUPLICATE KEY UPDATE `pk_nota_compras` = VALUES(`pk_nota_compras`), `cod_nota` = VALUES(`cod_nota`), `ref_cod_nota` = VALUES(`ref_cod_nota`), `dataNota` = VALUES(`dataNota`), `total_compra` = VALUES(`total_compra`), `performance` = VALUES(`performance`), `credito` = VALUES(`credito`), `hora` = VALUES(`hora`), `status_eliminado` = VALUES(`status_eliminado`), `desconto_total` = VALUES(`desconto_total`), `total_iva` = VALUES(`total_iva`), `total_geral` = VALUES(`total_geral`), `cod_fact` = VALUES(`cod_fact`), `assinatura` = VALUES(`assinatura`), `hash_cod` = VALUES(`hash_cod`), `obs` = VALUES(`obs`), `ref_cod_fact` = VALUES(`ref_cod_fact`), `total_por_extenso` = VALUES(`total_por_extenso`), `idBanco` = VALUES(`idBanco`), `codigo_usuario` = VALUES(`codigo_usuario`), `fk_fornecedor` = VALUES(`fk_fornecedor`), `fk_documento` = VALUES(`fk_documento`), `fk_ano_economico` = VALUES(`fk_ano_economico`), `ref_fact_data` = VALUES(`ref_fact_data`), `estado` = VALUES(`estado`), `desconto_comercial` = VALUES(`desconto_comercial`), `desconto_financeiro` = VALUES(`desconto_financeiro`), `motivo` = VALUES(`motivo`), `total_incidencia` = VALUES(`total_incidencia`), `total_incidencia_isento` = VALUES(`total_incidencia_isento`), `ref_data_fact` = VALUES(`ref_data_fact`), `idArmazemFK` = VALUES(`idArmazemFK`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_compras_ad` AFTER DELETE ON `kitanda_db_1`.`notas_compras` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`notas_compras` WHERE `pk_nota_compras` = OLD.`pk_nota_compras`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_item_compras_ai` AFTER INSERT ON `kitanda_db_1`.`notas_item_compras` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`notas_item_compras` (`pk_notas_item`, `quantidade`, `valor_iva`, `motivo_isensao`, `desconto`, `total`, `fk_compras`, `fk_produto`, `fk_preco`, `fk_nota_compras`, `codigo_isencao`) VALUES (NEW.`pk_notas_item`, NEW.`quantidade`, NEW.`valor_iva`, NEW.`motivo_isensao`, NEW.`desconto`, NEW.`total`, NEW.`fk_compras`, NEW.`fk_produto`, NEW.`fk_preco`, NEW.`fk_nota_compras`, NEW.`codigo_isencao`) ON DUPLICATE KEY UPDATE `pk_notas_item` = VALUES(`pk_notas_item`), `quantidade` = VALUES(`quantidade`), `valor_iva` = VALUES(`valor_iva`), `motivo_isensao` = VALUES(`motivo_isensao`), `desconto` = VALUES(`desconto`), `total` = VALUES(`total`), `fk_compras` = VALUES(`fk_compras`), `fk_produto` = VALUES(`fk_produto`), `fk_preco` = VALUES(`fk_preco`), `fk_nota_compras` = VALUES(`fk_nota_compras`), `codigo_isencao` = VALUES(`codigo_isencao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_item_compras_au` AFTER UPDATE ON `kitanda_db_1`.`notas_item_compras` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`notas_item_compras` SET `pk_notas_item` = NEW.`pk_notas_item`, `quantidade` = NEW.`quantidade`, `valor_iva` = NEW.`valor_iva`, `motivo_isensao` = NEW.`motivo_isensao`, `desconto` = NEW.`desconto`, `total` = NEW.`total`, `fk_compras` = NEW.`fk_compras`, `fk_produto` = NEW.`fk_produto`, `fk_preco` = NEW.`fk_preco`, `fk_nota_compras` = NEW.`fk_nota_compras`, `codigo_isencao` = NEW.`codigo_isencao` WHERE `pk_notas_item` = OLD.`pk_notas_item`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`notas_item_compras` (`pk_notas_item`, `quantidade`, `valor_iva`, `motivo_isensao`, `desconto`, `total`, `fk_compras`, `fk_produto`, `fk_preco`, `fk_nota_compras`, `codigo_isencao`) VALUES (NEW.`pk_notas_item`, NEW.`quantidade`, NEW.`valor_iva`, NEW.`motivo_isensao`, NEW.`desconto`, NEW.`total`, NEW.`fk_compras`, NEW.`fk_produto`, NEW.`fk_preco`, NEW.`fk_nota_compras`, NEW.`codigo_isencao`) ON DUPLICATE KEY UPDATE `pk_notas_item` = VALUES(`pk_notas_item`), `quantidade` = VALUES(`quantidade`), `valor_iva` = VALUES(`valor_iva`), `motivo_isensao` = VALUES(`motivo_isensao`), `desconto` = VALUES(`desconto`), `total` = VALUES(`total`), `fk_compras` = VALUES(`fk_compras`), `fk_produto` = VALUES(`fk_produto`), `fk_preco` = VALUES(`fk_preco`), `fk_nota_compras` = VALUES(`fk_nota_compras`), `codigo_isencao` = VALUES(`codigo_isencao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_notas_item_compras_ad` AFTER DELETE ON `kitanda_db_1`.`notas_item_compras` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`notas_item_compras` WHERE `pk_notas_item` = OLD.`pk_notas_item`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_pagamento_subsidio_feria_natal_ai` AFTER INSERT ON `kitanda_db_1`.`pagamento_subsidio_feria_natal` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`pagamento_subsidio_feria_natal` (`pk_pagamento_subsidio_feria_natal`, `data_hora`, `percentagem`, `valor`, `salario_base`, `tipo_subsideo`, `fk_funcionario`, `fk_usuario`, `forma_pagamento`, `desconto_irt`) VALUES (NEW.`pk_pagamento_subsidio_feria_natal`, NEW.`data_hora`, NEW.`percentagem`, NEW.`valor`, NEW.`salario_base`, NEW.`tipo_subsideo`, NEW.`fk_funcionario`, NEW.`fk_usuario`, NEW.`forma_pagamento`, NEW.`desconto_irt`) ON DUPLICATE KEY UPDATE `pk_pagamento_subsidio_feria_natal` = VALUES(`pk_pagamento_subsidio_feria_natal`), `data_hora` = VALUES(`data_hora`), `percentagem` = VALUES(`percentagem`), `valor` = VALUES(`valor`), `salario_base` = VALUES(`salario_base`), `tipo_subsideo` = VALUES(`tipo_subsideo`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`), `forma_pagamento` = VALUES(`forma_pagamento`), `desconto_irt` = VALUES(`desconto_irt`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_pagamento_subsidio_feria_natal_au` AFTER UPDATE ON `kitanda_db_1`.`pagamento_subsidio_feria_natal` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`pagamento_subsidio_feria_natal` SET `pk_pagamento_subsidio_feria_natal` = NEW.`pk_pagamento_subsidio_feria_natal`, `data_hora` = NEW.`data_hora`, `percentagem` = NEW.`percentagem`, `valor` = NEW.`valor`, `salario_base` = NEW.`salario_base`, `tipo_subsideo` = NEW.`tipo_subsideo`, `fk_funcionario` = NEW.`fk_funcionario`, `fk_usuario` = NEW.`fk_usuario`, `forma_pagamento` = NEW.`forma_pagamento`, `desconto_irt` = NEW.`desconto_irt` WHERE `pk_pagamento_subsidio_feria_natal` = OLD.`pk_pagamento_subsidio_feria_natal`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`pagamento_subsidio_feria_natal` (`pk_pagamento_subsidio_feria_natal`, `data_hora`, `percentagem`, `valor`, `salario_base`, `tipo_subsideo`, `fk_funcionario`, `fk_usuario`, `forma_pagamento`, `desconto_irt`) VALUES (NEW.`pk_pagamento_subsidio_feria_natal`, NEW.`data_hora`, NEW.`percentagem`, NEW.`valor`, NEW.`salario_base`, NEW.`tipo_subsideo`, NEW.`fk_funcionario`, NEW.`fk_usuario`, NEW.`forma_pagamento`, NEW.`desconto_irt`) ON DUPLICATE KEY UPDATE `pk_pagamento_subsidio_feria_natal` = VALUES(`pk_pagamento_subsidio_feria_natal`), `data_hora` = VALUES(`data_hora`), `percentagem` = VALUES(`percentagem`), `valor` = VALUES(`valor`), `salario_base` = VALUES(`salario_base`), `tipo_subsideo` = VALUES(`tipo_subsideo`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`), `forma_pagamento` = VALUES(`forma_pagamento`), `desconto_irt` = VALUES(`desconto_irt`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_pagamento_subsidio_feria_natal_ad` AFTER DELETE ON `kitanda_db_1`.`pagamento_subsidio_feria_natal` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`pagamento_subsidio_feria_natal` WHERE `pk_pagamento_subsidio_feria_natal` = OLD.`pk_pagamento_subsidio_feria_natal`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_pedido_feria_ai` AFTER INSERT ON `kitanda_db_1`.`pedido_feria` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`pedido_feria` (`pk_pedido_feria`, `data_inicio`, `data_fim`, `dias_ferias`, `data_registro`, `fk_funcionario`, `fk_usuario`) VALUES (NEW.`pk_pedido_feria`, NEW.`data_inicio`, NEW.`data_fim`, NEW.`dias_ferias`, NEW.`data_registro`, NEW.`fk_funcionario`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_pedido_feria` = VALUES(`pk_pedido_feria`), `data_inicio` = VALUES(`data_inicio`), `data_fim` = VALUES(`data_fim`), `dias_ferias` = VALUES(`dias_ferias`), `data_registro` = VALUES(`data_registro`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_pedido_feria_au` AFTER UPDATE ON `kitanda_db_1`.`pedido_feria` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`pedido_feria` SET `pk_pedido_feria` = NEW.`pk_pedido_feria`, `data_inicio` = NEW.`data_inicio`, `data_fim` = NEW.`data_fim`, `dias_ferias` = NEW.`dias_ferias`, `data_registro` = NEW.`data_registro`, `fk_funcionario` = NEW.`fk_funcionario`, `fk_usuario` = NEW.`fk_usuario` WHERE `pk_pedido_feria` = OLD.`pk_pedido_feria`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`pedido_feria` (`pk_pedido_feria`, `data_inicio`, `data_fim`, `dias_ferias`, `data_registro`, `fk_funcionario`, `fk_usuario`) VALUES (NEW.`pk_pedido_feria`, NEW.`data_inicio`, NEW.`data_fim`, NEW.`dias_ferias`, NEW.`data_registro`, NEW.`fk_funcionario`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_pedido_feria` = VALUES(`pk_pedido_feria`), `data_inicio` = VALUES(`data_inicio`), `data_fim` = VALUES(`data_fim`), `dias_ferias` = VALUES(`dias_ferias`), `data_registro` = VALUES(`data_registro`), `fk_funcionario` = VALUES(`fk_funcionario`), `fk_usuario` = VALUES(`fk_usuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_pedido_feria_ad` AFTER DELETE ON `kitanda_db_1`.`pedido_feria` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`pedido_feria` WHERE `pk_pedido_feria` = OLD.`pk_pedido_feria`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_previo_aviso_ai` AFTER INSERT ON `kitanda_db_1`.`previo_aviso` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`previo_aviso` (`pk_previo_aviso`, `data_previo`, `descricao`, `fk_usuario`, `fk_funcionario`, `numero_dias_aviso`) VALUES (NEW.`pk_previo_aviso`, NEW.`data_previo`, NEW.`descricao`, NEW.`fk_usuario`, NEW.`fk_funcionario`, NEW.`numero_dias_aviso`) ON DUPLICATE KEY UPDATE `pk_previo_aviso` = VALUES(`pk_previo_aviso`), `data_previo` = VALUES(`data_previo`), `descricao` = VALUES(`descricao`), `fk_usuario` = VALUES(`fk_usuario`), `fk_funcionario` = VALUES(`fk_funcionario`), `numero_dias_aviso` = VALUES(`numero_dias_aviso`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_previo_aviso_au` AFTER UPDATE ON `kitanda_db_1`.`previo_aviso` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`previo_aviso` SET `pk_previo_aviso` = NEW.`pk_previo_aviso`, `data_previo` = NEW.`data_previo`, `descricao` = NEW.`descricao`, `fk_usuario` = NEW.`fk_usuario`, `fk_funcionario` = NEW.`fk_funcionario`, `numero_dias_aviso` = NEW.`numero_dias_aviso` WHERE `pk_previo_aviso` = OLD.`pk_previo_aviso`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`previo_aviso` (`pk_previo_aviso`, `data_previo`, `descricao`, `fk_usuario`, `fk_funcionario`, `numero_dias_aviso`) VALUES (NEW.`pk_previo_aviso`, NEW.`data_previo`, NEW.`descricao`, NEW.`fk_usuario`, NEW.`fk_funcionario`, NEW.`numero_dias_aviso`) ON DUPLICATE KEY UPDATE `pk_previo_aviso` = VALUES(`pk_previo_aviso`), `data_previo` = VALUES(`data_previo`), `descricao` = VALUES(`descricao`), `fk_usuario` = VALUES(`fk_usuario`), `fk_funcionario` = VALUES(`fk_funcionario`), `numero_dias_aviso` = VALUES(`numero_dias_aviso`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_previo_aviso_ad` AFTER DELETE ON `kitanda_db_1`.`previo_aviso` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`previo_aviso` WHERE `pk_previo_aviso` = OLD.`pk_previo_aviso`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_productos_ai` AFTER INSERT ON `kitanda_db_1`.`productos` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`productos` (`codigo_produto`, `consumo`, `preco`, `lugar`, `qtd`, `mesa`, `usuario`, `data_hora`) VALUES (NEW.`codigo_produto`, NEW.`consumo`, NEW.`preco`, NEW.`lugar`, NEW.`qtd`, NEW.`mesa`, NEW.`usuario`, NEW.`data_hora`) ON DUPLICATE KEY UPDATE `codigo_produto` = VALUES(`codigo_produto`), `consumo` = VALUES(`consumo`), `preco` = VALUES(`preco`), `lugar` = VALUES(`lugar`), `qtd` = VALUES(`qtd`), `mesa` = VALUES(`mesa`), `usuario` = VALUES(`usuario`), `data_hora` = VALUES(`data_hora`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_productos_au` AFTER UPDATE ON `kitanda_db_1`.`productos` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`productos` SET `codigo_produto` = NEW.`codigo_produto`, `consumo` = NEW.`consumo`, `preco` = NEW.`preco`, `lugar` = NEW.`lugar`, `qtd` = NEW.`qtd`, `mesa` = NEW.`mesa`, `usuario` = NEW.`usuario`, `data_hora` = NEW.`data_hora` WHERE `codigo_produto` = OLD.`codigo_produto`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`productos` (`codigo_produto`, `consumo`, `preco`, `lugar`, `qtd`, `mesa`, `usuario`, `data_hora`) VALUES (NEW.`codigo_produto`, NEW.`consumo`, NEW.`preco`, NEW.`lugar`, NEW.`qtd`, NEW.`mesa`, NEW.`usuario`, NEW.`data_hora`) ON DUPLICATE KEY UPDATE `codigo_produto` = VALUES(`codigo_produto`), `consumo` = VALUES(`consumo`), `preco` = VALUES(`preco`), `lugar` = VALUES(`lugar`), `qtd` = VALUES(`qtd`), `mesa` = VALUES(`mesa`), `usuario` = VALUES(`usuario`), `data_hora` = VALUES(`data_hora`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_productos_ad` AFTER DELETE ON `kitanda_db_1`.`productos` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`productos` WHERE `codigo_produto` = OLD.`codigo_produto`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produto_imposto_ai` AFTER INSERT ON `kitanda_db_1`.`produto_imposto` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`produto_imposto` (`pk_produto_imposto`, `fk_produto`, `fk_imposto`) VALUES (NEW.`pk_produto_imposto`, NEW.`fk_produto`, NEW.`fk_imposto`) ON DUPLICATE KEY UPDATE `pk_produto_imposto` = VALUES(`pk_produto_imposto`), `fk_produto` = VALUES(`fk_produto`), `fk_imposto` = VALUES(`fk_imposto`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produto_imposto_au` AFTER UPDATE ON `kitanda_db_1`.`produto_imposto` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`produto_imposto` SET `pk_produto_imposto` = NEW.`pk_produto_imposto`, `fk_produto` = NEW.`fk_produto`, `fk_imposto` = NEW.`fk_imposto` WHERE `pk_produto_imposto` = OLD.`pk_produto_imposto`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`produto_imposto` (`pk_produto_imposto`, `fk_produto`, `fk_imposto`) VALUES (NEW.`pk_produto_imposto`, NEW.`fk_produto`, NEW.`fk_imposto`) ON DUPLICATE KEY UPDATE `pk_produto_imposto` = VALUES(`pk_produto_imposto`), `fk_produto` = VALUES(`fk_produto`), `fk_imposto` = VALUES(`fk_imposto`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produto_imposto_ad` AFTER DELETE ON `kitanda_db_1`.`produto_imposto` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`produto_imposto` WHERE `pk_produto_imposto` = OLD.`pk_produto_imposto`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produto_isento_ai` AFTER INSERT ON `kitanda_db_1`.`produto_isento` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`produto_isento` (`pk_produto_isento`, `fk_produto`, `fk_produtos_motivos_isensao`) VALUES (NEW.`pk_produto_isento`, NEW.`fk_produto`, NEW.`fk_produtos_motivos_isensao`) ON DUPLICATE KEY UPDATE `pk_produto_isento` = VALUES(`pk_produto_isento`), `fk_produto` = VALUES(`fk_produto`), `fk_produtos_motivos_isensao` = VALUES(`fk_produtos_motivos_isensao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produto_isento_au` AFTER UPDATE ON `kitanda_db_1`.`produto_isento` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`produto_isento` SET `pk_produto_isento` = NEW.`pk_produto_isento`, `fk_produto` = NEW.`fk_produto`, `fk_produtos_motivos_isensao` = NEW.`fk_produtos_motivos_isensao` WHERE `pk_produto_isento` = OLD.`pk_produto_isento`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`produto_isento` (`pk_produto_isento`, `fk_produto`, `fk_produtos_motivos_isensao`) VALUES (NEW.`pk_produto_isento`, NEW.`fk_produto`, NEW.`fk_produtos_motivos_isensao`) ON DUPLICATE KEY UPDATE `pk_produto_isento` = VALUES(`pk_produto_isento`), `fk_produto` = VALUES(`fk_produto`), `fk_produtos_motivos_isensao` = VALUES(`fk_produtos_motivos_isensao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produto_isento_ad` AFTER DELETE ON `kitanda_db_1`.`produto_isento` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`produto_isento` WHERE `pk_produto_isento` = OLD.`pk_produto_isento`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produtos_motivos_isensao_ai` AFTER INSERT ON `kitanda_db_1`.`produtos_motivos_isensao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`produtos_motivos_isensao` (`pk_produtos_motivos_isensao`, `regime`, `descricao`, `codigo_regime`) VALUES (NEW.`pk_produtos_motivos_isensao`, NEW.`regime`, NEW.`descricao`, NEW.`codigo_regime`) ON DUPLICATE KEY UPDATE `pk_produtos_motivos_isensao` = VALUES(`pk_produtos_motivos_isensao`), `regime` = VALUES(`regime`), `descricao` = VALUES(`descricao`), `codigo_regime` = VALUES(`codigo_regime`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produtos_motivos_isensao_au` AFTER UPDATE ON `kitanda_db_1`.`produtos_motivos_isensao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`produtos_motivos_isensao` SET `pk_produtos_motivos_isensao` = NEW.`pk_produtos_motivos_isensao`, `regime` = NEW.`regime`, `descricao` = NEW.`descricao`, `codigo_regime` = NEW.`codigo_regime` WHERE `pk_produtos_motivos_isensao` = OLD.`pk_produtos_motivos_isensao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`produtos_motivos_isensao` (`pk_produtos_motivos_isensao`, `regime`, `descricao`, `codigo_regime`) VALUES (NEW.`pk_produtos_motivos_isensao`, NEW.`regime`, NEW.`descricao`, NEW.`codigo_regime`) ON DUPLICATE KEY UPDATE `pk_produtos_motivos_isensao` = VALUES(`pk_produtos_motivos_isensao`), `regime` = VALUES(`regime`), `descricao` = VALUES(`descricao`), `codigo_regime` = VALUES(`codigo_regime`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_produtos_motivos_isensao_ad` AFTER DELETE ON `kitanda_db_1`.`produtos_motivos_isensao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`produtos_motivos_isensao` WHERE `pk_produtos_motivos_isensao` = OLD.`pk_produtos_motivos_isensao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_promocao_ai` AFTER INSERT ON `kitanda_db_1`.`promocao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`promocao` (`pk_promocao`, `percentagem`, `data`, `hora`, `retalho_groso`, `fk_usuario`) VALUES (NEW.`pk_promocao`, NEW.`percentagem`, NEW.`data`, NEW.`hora`, NEW.`retalho_groso`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_promocao` = VALUES(`pk_promocao`), `percentagem` = VALUES(`percentagem`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `retalho_groso` = VALUES(`retalho_groso`), `fk_usuario` = VALUES(`fk_usuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_promocao_au` AFTER UPDATE ON `kitanda_db_1`.`promocao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`promocao` SET `pk_promocao` = NEW.`pk_promocao`, `percentagem` = NEW.`percentagem`, `data` = NEW.`data`, `hora` = NEW.`hora`, `retalho_groso` = NEW.`retalho_groso`, `fk_usuario` = NEW.`fk_usuario` WHERE `pk_promocao` = OLD.`pk_promocao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`promocao` (`pk_promocao`, `percentagem`, `data`, `hora`, `retalho_groso`, `fk_usuario`) VALUES (NEW.`pk_promocao`, NEW.`percentagem`, NEW.`data`, NEW.`hora`, NEW.`retalho_groso`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_promocao` = VALUES(`pk_promocao`), `percentagem` = VALUES(`percentagem`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `retalho_groso` = VALUES(`retalho_groso`), `fk_usuario` = VALUES(`fk_usuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_promocao_ad` AFTER DELETE ON `kitanda_db_1`.`promocao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`promocao` WHERE `pk_promocao` = OLD.`pk_promocao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_recibo_rh_ai` AFTER INSERT ON `kitanda_db_1`.`recibo_rh` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`recibo_rh` (`pk_recibo_rh`, `data_hora`, `total_remuneracao`, `total_desconto`, `total_pago`, `fk_forma_pagamento`, `fk_funcionario`, `vencimento`, `dias_trabalhado`, `fk_fecho_periodo`, `periodo`) VALUES (NEW.`pk_recibo_rh`, NEW.`data_hora`, NEW.`total_remuneracao`, NEW.`total_desconto`, NEW.`total_pago`, NEW.`fk_forma_pagamento`, NEW.`fk_funcionario`, NEW.`vencimento`, NEW.`dias_trabalhado`, NEW.`fk_fecho_periodo`, NEW.`periodo`) ON DUPLICATE KEY UPDATE `pk_recibo_rh` = VALUES(`pk_recibo_rh`), `data_hora` = VALUES(`data_hora`), `total_remuneracao` = VALUES(`total_remuneracao`), `total_desconto` = VALUES(`total_desconto`), `total_pago` = VALUES(`total_pago`), `fk_forma_pagamento` = VALUES(`fk_forma_pagamento`), `fk_funcionario` = VALUES(`fk_funcionario`), `vencimento` = VALUES(`vencimento`), `dias_trabalhado` = VALUES(`dias_trabalhado`), `fk_fecho_periodo` = VALUES(`fk_fecho_periodo`), `periodo` = VALUES(`periodo`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_recibo_rh_au` AFTER UPDATE ON `kitanda_db_1`.`recibo_rh` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`recibo_rh` SET `pk_recibo_rh` = NEW.`pk_recibo_rh`, `data_hora` = NEW.`data_hora`, `total_remuneracao` = NEW.`total_remuneracao`, `total_desconto` = NEW.`total_desconto`, `total_pago` = NEW.`total_pago`, `fk_forma_pagamento` = NEW.`fk_forma_pagamento`, `fk_funcionario` = NEW.`fk_funcionario`, `vencimento` = NEW.`vencimento`, `dias_trabalhado` = NEW.`dias_trabalhado`, `fk_fecho_periodo` = NEW.`fk_fecho_periodo`, `periodo` = NEW.`periodo` WHERE `pk_recibo_rh` = OLD.`pk_recibo_rh`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`recibo_rh` (`pk_recibo_rh`, `data_hora`, `total_remuneracao`, `total_desconto`, `total_pago`, `fk_forma_pagamento`, `fk_funcionario`, `vencimento`, `dias_trabalhado`, `fk_fecho_periodo`, `periodo`) VALUES (NEW.`pk_recibo_rh`, NEW.`data_hora`, NEW.`total_remuneracao`, NEW.`total_desconto`, NEW.`total_pago`, NEW.`fk_forma_pagamento`, NEW.`fk_funcionario`, NEW.`vencimento`, NEW.`dias_trabalhado`, NEW.`fk_fecho_periodo`, NEW.`periodo`) ON DUPLICATE KEY UPDATE `pk_recibo_rh` = VALUES(`pk_recibo_rh`), `data_hora` = VALUES(`data_hora`), `total_remuneracao` = VALUES(`total_remuneracao`), `total_desconto` = VALUES(`total_desconto`), `total_pago` = VALUES(`total_pago`), `fk_forma_pagamento` = VALUES(`fk_forma_pagamento`), `fk_funcionario` = VALUES(`fk_funcionario`), `vencimento` = VALUES(`vencimento`), `dias_trabalhado` = VALUES(`dias_trabalhado`), `fk_fecho_periodo` = VALUES(`fk_fecho_periodo`), `periodo` = VALUES(`periodo`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_recibo_rh_ad` AFTER DELETE ON `kitanda_db_1`.`recibo_rh` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`recibo_rh` WHERE `pk_recibo_rh` = OLD.`pk_recibo_rh`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_referencias_ai` AFTER INSERT ON `kitanda_db_1`.`referencias` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`referencias` (`id`, `cod_barra`, `produto_id`, `obs`, `data_referencia`, `usuario_id`) VALUES (NEW.`id`, NEW.`cod_barra`, NEW.`produto_id`, NEW.`obs`, NEW.`data_referencia`, NEW.`usuario_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `cod_barra` = VALUES(`cod_barra`), `produto_id` = VALUES(`produto_id`), `obs` = VALUES(`obs`), `data_referencia` = VALUES(`data_referencia`), `usuario_id` = VALUES(`usuario_id`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_referencias_au` AFTER UPDATE ON `kitanda_db_1`.`referencias` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`referencias` SET `id` = NEW.`id`, `cod_barra` = NEW.`cod_barra`, `produto_id` = NEW.`produto_id`, `obs` = NEW.`obs`, `data_referencia` = NEW.`data_referencia`, `usuario_id` = NEW.`usuario_id` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`referencias` (`id`, `cod_barra`, `produto_id`, `obs`, `data_referencia`, `usuario_id`) VALUES (NEW.`id`, NEW.`cod_barra`, NEW.`produto_id`, NEW.`obs`, NEW.`data_referencia`, NEW.`usuario_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `cod_barra` = VALUES(`cod_barra`), `produto_id` = VALUES(`produto_id`), `obs` = VALUES(`obs`), `data_referencia` = VALUES(`data_referencia`), `usuario_id` = VALUES(`usuario_id`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_referencias_ad` AFTER DELETE ON `kitanda_db_1`.`referencias` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`referencias` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_regime_ai` AFTER INSERT ON `kitanda_db_1`.`regime` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`regime` (`pk_regime`, `designacao`, `valor`) VALUES (NEW.`pk_regime`, NEW.`designacao`, NEW.`valor`) ON DUPLICATE KEY UPDATE `pk_regime` = VALUES(`pk_regime`), `designacao` = VALUES(`designacao`), `valor` = VALUES(`valor`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_regime_au` AFTER UPDATE ON `kitanda_db_1`.`regime` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`regime` SET `pk_regime` = NEW.`pk_regime`, `designacao` = NEW.`designacao`, `valor` = NEW.`valor` WHERE `pk_regime` = OLD.`pk_regime`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`regime` (`pk_regime`, `designacao`, `valor`) VALUES (NEW.`pk_regime`, NEW.`designacao`, NEW.`valor`) ON DUPLICATE KEY UPDATE `pk_regime` = VALUES(`pk_regime`), `designacao` = VALUES(`designacao`), `valor` = VALUES(`valor`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_regime_ad` AFTER DELETE ON `kitanda_db_1`.`regime` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`regime` WHERE `pk_regime` = OLD.`pk_regime`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_relatorio_mensal_ai` AFTER INSERT ON `kitanda_db_1`.`relatorio_mensal` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`relatorio_mensal` (`pk_relatorio_mensal`, `data_relatorio`, `carne_bovina`, `carne_suina`, `carne_caprina`, `carne_ovina`, `aves`, `ovos`, `enchidos`, `sal`, `temperos`, `peixes`, `bebidas`, `bebida`) VALUES (NEW.`pk_relatorio_mensal`, NEW.`data_relatorio`, NEW.`carne_bovina`, NEW.`carne_suina`, NEW.`carne_caprina`, NEW.`carne_ovina`, NEW.`aves`, NEW.`ovos`, NEW.`enchidos`, NEW.`sal`, NEW.`temperos`, NEW.`peixes`, NEW.`bebidas`, NEW.`bebida`) ON DUPLICATE KEY UPDATE `pk_relatorio_mensal` = VALUES(`pk_relatorio_mensal`), `data_relatorio` = VALUES(`data_relatorio`), `carne_bovina` = VALUES(`carne_bovina`), `carne_suina` = VALUES(`carne_suina`), `carne_caprina` = VALUES(`carne_caprina`), `carne_ovina` = VALUES(`carne_ovina`), `aves` = VALUES(`aves`), `ovos` = VALUES(`ovos`), `enchidos` = VALUES(`enchidos`), `sal` = VALUES(`sal`), `temperos` = VALUES(`temperos`), `peixes` = VALUES(`peixes`), `bebidas` = VALUES(`bebidas`), `bebida` = VALUES(`bebida`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_relatorio_mensal_au` AFTER UPDATE ON `kitanda_db_1`.`relatorio_mensal` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`relatorio_mensal` SET `pk_relatorio_mensal` = NEW.`pk_relatorio_mensal`, `data_relatorio` = NEW.`data_relatorio`, `carne_bovina` = NEW.`carne_bovina`, `carne_suina` = NEW.`carne_suina`, `carne_caprina` = NEW.`carne_caprina`, `carne_ovina` = NEW.`carne_ovina`, `aves` = NEW.`aves`, `ovos` = NEW.`ovos`, `enchidos` = NEW.`enchidos`, `sal` = NEW.`sal`, `temperos` = NEW.`temperos`, `peixes` = NEW.`peixes`, `bebidas` = NEW.`bebidas`, `bebida` = NEW.`bebida` WHERE `pk_relatorio_mensal` = OLD.`pk_relatorio_mensal`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`relatorio_mensal` (`pk_relatorio_mensal`, `data_relatorio`, `carne_bovina`, `carne_suina`, `carne_caprina`, `carne_ovina`, `aves`, `ovos`, `enchidos`, `sal`, `temperos`, `peixes`, `bebidas`, `bebida`) VALUES (NEW.`pk_relatorio_mensal`, NEW.`data_relatorio`, NEW.`carne_bovina`, NEW.`carne_suina`, NEW.`carne_caprina`, NEW.`carne_ovina`, NEW.`aves`, NEW.`ovos`, NEW.`enchidos`, NEW.`sal`, NEW.`temperos`, NEW.`peixes`, NEW.`bebidas`, NEW.`bebida`) ON DUPLICATE KEY UPDATE `pk_relatorio_mensal` = VALUES(`pk_relatorio_mensal`), `data_relatorio` = VALUES(`data_relatorio`), `carne_bovina` = VALUES(`carne_bovina`), `carne_suina` = VALUES(`carne_suina`), `carne_caprina` = VALUES(`carne_caprina`), `carne_ovina` = VALUES(`carne_ovina`), `aves` = VALUES(`aves`), `ovos` = VALUES(`ovos`), `enchidos` = VALUES(`enchidos`), `sal` = VALUES(`sal`), `temperos` = VALUES(`temperos`), `peixes` = VALUES(`peixes`), `bebidas` = VALUES(`bebidas`), `bebida` = VALUES(`bebida`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_relatorio_mensal_ad` AFTER DELETE ON `kitanda_db_1`.`relatorio_mensal` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`relatorio_mensal` WHERE `pk_relatorio_mensal` = OLD.`pk_relatorio_mensal`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_retencao_ai` AFTER INSERT ON `kitanda_db_1`.`retencao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`retencao` (`pk_retencao`, `descricao`, `taxa`, `data_hora`) VALUES (NEW.`pk_retencao`, NEW.`descricao`, NEW.`taxa`, NEW.`data_hora`) ON DUPLICATE KEY UPDATE `pk_retencao` = VALUES(`pk_retencao`), `descricao` = VALUES(`descricao`), `taxa` = VALUES(`taxa`), `data_hora` = VALUES(`data_hora`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_retencao_au` AFTER UPDATE ON `kitanda_db_1`.`retencao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`retencao` SET `pk_retencao` = NEW.`pk_retencao`, `descricao` = NEW.`descricao`, `taxa` = NEW.`taxa`, `data_hora` = NEW.`data_hora` WHERE `pk_retencao` = OLD.`pk_retencao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`retencao` (`pk_retencao`, `descricao`, `taxa`, `data_hora`) VALUES (NEW.`pk_retencao`, NEW.`descricao`, NEW.`taxa`, NEW.`data_hora`) ON DUPLICATE KEY UPDATE `pk_retencao` = VALUES(`pk_retencao`), `descricao` = VALUES(`descricao`), `taxa` = VALUES(`taxa`), `data_hora` = VALUES(`data_hora`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_retencao_ad` AFTER DELETE ON `kitanda_db_1`.`retencao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`retencao` WHERE `pk_retencao` = OLD.`pk_retencao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_saidas_tesouraria_ai` AFTER INSERT ON `kitanda_db_1`.`saidas_tesouraria` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`saidas_tesouraria` (`pk_saidas_tesouraria`, `descricao`, `valor`, `data_saida`, `hora_saida`, `fk_usuario`, `fk_banco`) VALUES (NEW.`pk_saidas_tesouraria`, NEW.`descricao`, NEW.`valor`, NEW.`data_saida`, NEW.`hora_saida`, NEW.`fk_usuario`, NEW.`fk_banco`) ON DUPLICATE KEY UPDATE `pk_saidas_tesouraria` = VALUES(`pk_saidas_tesouraria`), `descricao` = VALUES(`descricao`), `valor` = VALUES(`valor`), `data_saida` = VALUES(`data_saida`), `hora_saida` = VALUES(`hora_saida`), `fk_usuario` = VALUES(`fk_usuario`), `fk_banco` = VALUES(`fk_banco`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_saidas_tesouraria_au` AFTER UPDATE ON `kitanda_db_1`.`saidas_tesouraria` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`saidas_tesouraria` SET `pk_saidas_tesouraria` = NEW.`pk_saidas_tesouraria`, `descricao` = NEW.`descricao`, `valor` = NEW.`valor`, `data_saida` = NEW.`data_saida`, `hora_saida` = NEW.`hora_saida`, `fk_usuario` = NEW.`fk_usuario`, `fk_banco` = NEW.`fk_banco` WHERE `pk_saidas_tesouraria` = OLD.`pk_saidas_tesouraria`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`saidas_tesouraria` (`pk_saidas_tesouraria`, `descricao`, `valor`, `data_saida`, `hora_saida`, `fk_usuario`, `fk_banco`) VALUES (NEW.`pk_saidas_tesouraria`, NEW.`descricao`, NEW.`valor`, NEW.`data_saida`, NEW.`hora_saida`, NEW.`fk_usuario`, NEW.`fk_banco`) ON DUPLICATE KEY UPDATE `pk_saidas_tesouraria` = VALUES(`pk_saidas_tesouraria`), `descricao` = VALUES(`descricao`), `valor` = VALUES(`valor`), `data_saida` = VALUES(`data_saida`), `hora_saida` = VALUES(`hora_saida`), `fk_usuario` = VALUES(`fk_usuario`), `fk_banco` = VALUES(`fk_banco`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_saidas_tesouraria_ad` AFTER DELETE ON `kitanda_db_1`.`saidas_tesouraria` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`saidas_tesouraria` WHERE `pk_saidas_tesouraria` = OLD.`pk_saidas_tesouraria`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_seccao_ai` AFTER INSERT ON `kitanda_db_1`.`seccao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`seccao` (`pk_seccao`, `descricao`) VALUES (NEW.`pk_seccao`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_seccao` = VALUES(`pk_seccao`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_seccao_au` AFTER UPDATE ON `kitanda_db_1`.`seccao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`seccao` SET `pk_seccao` = NEW.`pk_seccao`, `descricao` = NEW.`descricao` WHERE `pk_seccao` = OLD.`pk_seccao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`seccao` (`pk_seccao`, `descricao`) VALUES (NEW.`pk_seccao`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_seccao` = VALUES(`pk_seccao`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_seccao_ad` AFTER DELETE ON `kitanda_db_1`.`seccao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`seccao` WHERE `pk_seccao` = OLD.`pk_seccao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_servico_retencao_ai` AFTER INSERT ON `kitanda_db_1`.`servico_retencao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`servico_retencao` (`pk_servico_retencao`, `fk_produto`, `fk_retencao`) VALUES (NEW.`pk_servico_retencao`, NEW.`fk_produto`, NEW.`fk_retencao`) ON DUPLICATE KEY UPDATE `pk_servico_retencao` = VALUES(`pk_servico_retencao`), `fk_produto` = VALUES(`fk_produto`), `fk_retencao` = VALUES(`fk_retencao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_servico_retencao_au` AFTER UPDATE ON `kitanda_db_1`.`servico_retencao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`servico_retencao` SET `pk_servico_retencao` = NEW.`pk_servico_retencao`, `fk_produto` = NEW.`fk_produto`, `fk_retencao` = NEW.`fk_retencao` WHERE `pk_servico_retencao` = OLD.`pk_servico_retencao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`servico_retencao` (`pk_servico_retencao`, `fk_produto`, `fk_retencao`) VALUES (NEW.`pk_servico_retencao`, NEW.`fk_produto`, NEW.`fk_retencao`) ON DUPLICATE KEY UPDATE `pk_servico_retencao` = VALUES(`pk_servico_retencao`), `fk_produto` = VALUES(`fk_produto`), `fk_retencao` = VALUES(`fk_retencao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_servico_retencao_ad` AFTER DELETE ON `kitanda_db_1`.`servico_retencao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`servico_retencao` WHERE `pk_servico_retencao` = OLD.`pk_servico_retencao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_stock_diario_ai` AFTER INSERT ON `kitanda_db_1`.`stock_diario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`stock_diario` (`id`, `produto_id`, `quantidade`, `data_registro`, `armazem_id`) VALUES (NEW.`id`, NEW.`produto_id`, NEW.`quantidade`, NEW.`data_registro`, NEW.`armazem_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `produto_id` = VALUES(`produto_id`), `quantidade` = VALUES(`quantidade`), `data_registro` = VALUES(`data_registro`), `armazem_id` = VALUES(`armazem_id`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_stock_diario_au` AFTER UPDATE ON `kitanda_db_1`.`stock_diario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`stock_diario` SET `id` = NEW.`id`, `produto_id` = NEW.`produto_id`, `quantidade` = NEW.`quantidade`, `data_registro` = NEW.`data_registro`, `armazem_id` = NEW.`armazem_id` WHERE `id` = OLD.`id`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`stock_diario` (`id`, `produto_id`, `quantidade`, `data_registro`, `armazem_id`) VALUES (NEW.`id`, NEW.`produto_id`, NEW.`quantidade`, NEW.`data_registro`, NEW.`armazem_id`) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `produto_id` = VALUES(`produto_id`), `quantidade` = VALUES(`quantidade`), `data_registro` = VALUES(`data_registro`), `armazem_id` = VALUES(`armazem_id`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_stock_diario_ad` AFTER DELETE ON `kitanda_db_1`.`stock_diario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`stock_diario` WHERE `id` = OLD.`id`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_acerto_ai` AFTER INSERT ON `kitanda_db_1`.`tb_acerto` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_acerto` (`idAcerto`, `data_acerto`, `idProduto`, `idUsuario`, `idArmazemFK`, `quantidade`) VALUES (NEW.`idAcerto`, NEW.`data_acerto`, NEW.`idProduto`, NEW.`idUsuario`, NEW.`idArmazemFK`, NEW.`quantidade`) ON DUPLICATE KEY UPDATE `idAcerto` = VALUES(`idAcerto`), `data_acerto` = VALUES(`data_acerto`), `idProduto` = VALUES(`idProduto`), `idUsuario` = VALUES(`idUsuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `quantidade` = VALUES(`quantidade`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_acerto_au` AFTER UPDATE ON `kitanda_db_1`.`tb_acerto` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_acerto` SET `idAcerto` = NEW.`idAcerto`, `data_acerto` = NEW.`data_acerto`, `idProduto` = NEW.`idProduto`, `idUsuario` = NEW.`idUsuario`, `idArmazemFK` = NEW.`idArmazemFK`, `quantidade` = NEW.`quantidade` WHERE `idAcerto` = OLD.`idAcerto`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_acerto` (`idAcerto`, `data_acerto`, `idProduto`, `idUsuario`, `idArmazemFK`, `quantidade`) VALUES (NEW.`idAcerto`, NEW.`data_acerto`, NEW.`idProduto`, NEW.`idUsuario`, NEW.`idArmazemFK`, NEW.`quantidade`) ON DUPLICATE KEY UPDATE `idAcerto` = VALUES(`idAcerto`), `data_acerto` = VALUES(`data_acerto`), `idProduto` = VALUES(`idProduto`), `idUsuario` = VALUES(`idUsuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `quantidade` = VALUES(`quantidade`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_acerto_ad` AFTER DELETE ON `kitanda_db_1`.`tb_acerto` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_acerto` WHERE `idAcerto` = OLD.`idAcerto`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_adiantamento_ai` AFTER INSERT ON `kitanda_db_1`.`tb_adiantamento` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_adiantamento` (`idAdiantamentoFK`, `valor_adiantado`, `descricao`, `data`, `hora`, `idFuncionarioFK`) VALUES (NEW.`idAdiantamentoFK`, NEW.`valor_adiantado`, NEW.`descricao`, NEW.`data`, NEW.`hora`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idAdiantamentoFK` = VALUES(`idAdiantamentoFK`), `valor_adiantado` = VALUES(`valor_adiantado`), `descricao` = VALUES(`descricao`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_adiantamento_au` AFTER UPDATE ON `kitanda_db_1`.`tb_adiantamento` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_adiantamento` SET `idAdiantamentoFK` = NEW.`idAdiantamentoFK`, `valor_adiantado` = NEW.`valor_adiantado`, `descricao` = NEW.`descricao`, `data` = NEW.`data`, `hora` = NEW.`hora`, `idFuncionarioFK` = NEW.`idFuncionarioFK` WHERE `idAdiantamentoFK` = OLD.`idAdiantamentoFK`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_adiantamento` (`idAdiantamentoFK`, `valor_adiantado`, `descricao`, `data`, `hora`, `idFuncionarioFK`) VALUES (NEW.`idAdiantamentoFK`, NEW.`valor_adiantado`, NEW.`descricao`, NEW.`data`, NEW.`hora`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idAdiantamentoFK` = VALUES(`idAdiantamentoFK`), `valor_adiantado` = VALUES(`valor_adiantado`), `descricao` = VALUES(`descricao`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_adiantamento_ad` AFTER DELETE ON `kitanda_db_1`.`tb_adiantamento` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_adiantamento` WHERE `idAdiantamentoFK` = OLD.`idAdiantamentoFK`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_ano_ai` AFTER INSERT ON `kitanda_db_1`.`tb_ano` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_ano` (`idAno`, `descrisao`) VALUES (NEW.`idAno`, NEW.`descrisao`) ON DUPLICATE KEY UPDATE `idAno` = VALUES(`idAno`), `descrisao` = VALUES(`descrisao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_ano_au` AFTER UPDATE ON `kitanda_db_1`.`tb_ano` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_ano` SET `idAno` = NEW.`idAno`, `descrisao` = NEW.`descrisao` WHERE `idAno` = OLD.`idAno`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_ano` (`idAno`, `descrisao`) VALUES (NEW.`idAno`, NEW.`descrisao`) ON DUPLICATE KEY UPDATE `idAno` = VALUES(`idAno`), `descrisao` = VALUES(`descrisao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_ano_ad` AFTER DELETE ON `kitanda_db_1`.`tb_ano` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_ano` WHERE `idAno` = OLD.`idAno`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_armazem_ai` AFTER INSERT ON `kitanda_db_1`.`tb_armazem` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_armazem` (`codigo`, `designacao`) VALUES (NEW.`codigo`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_armazem_au` AFTER UPDATE ON `kitanda_db_1`.`tb_armazem` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_armazem` SET `codigo` = NEW.`codigo`, `designacao` = NEW.`designacao` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_armazem` (`codigo`, `designacao`) VALUES (NEW.`codigo`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_armazem_ad` AFTER DELETE ON `kitanda_db_1`.`tb_armazem` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_armazem` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_balanco_ai` AFTER INSERT ON `kitanda_db_1`.`tb_balanco` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_balanco` (`pk_balanco`, `data_balanco`, `valor_banco_outros`, `valor_banco_caixa`, `valor_banco_stock`) VALUES (NEW.`pk_balanco`, NEW.`data_balanco`, NEW.`valor_banco_outros`, NEW.`valor_banco_caixa`, NEW.`valor_banco_stock`) ON DUPLICATE KEY UPDATE `pk_balanco` = VALUES(`pk_balanco`), `data_balanco` = VALUES(`data_balanco`), `valor_banco_outros` = VALUES(`valor_banco_outros`), `valor_banco_caixa` = VALUES(`valor_banco_caixa`), `valor_banco_stock` = VALUES(`valor_banco_stock`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_balanco_au` AFTER UPDATE ON `kitanda_db_1`.`tb_balanco` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_balanco` SET `pk_balanco` = NEW.`pk_balanco`, `data_balanco` = NEW.`data_balanco`, `valor_banco_outros` = NEW.`valor_banco_outros`, `valor_banco_caixa` = NEW.`valor_banco_caixa`, `valor_banco_stock` = NEW.`valor_banco_stock` WHERE `pk_balanco` = OLD.`pk_balanco`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_balanco` (`pk_balanco`, `data_balanco`, `valor_banco_outros`, `valor_banco_caixa`, `valor_banco_stock`) VALUES (NEW.`pk_balanco`, NEW.`data_balanco`, NEW.`valor_banco_outros`, NEW.`valor_banco_caixa`, NEW.`valor_banco_stock`) ON DUPLICATE KEY UPDATE `pk_balanco` = VALUES(`pk_balanco`), `data_balanco` = VALUES(`data_balanco`), `valor_banco_outros` = VALUES(`valor_banco_outros`), `valor_banco_caixa` = VALUES(`valor_banco_caixa`), `valor_banco_stock` = VALUES(`valor_banco_stock`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_balanco_ad` AFTER DELETE ON `kitanda_db_1`.`tb_balanco` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_balanco` WHERE `pk_balanco` = OLD.`pk_balanco`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_banco_ai` AFTER INSERT ON `kitanda_db_1`.`tb_banco` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_banco` (`idBanco`, `descrisao`, `numero`, `saldo_banco`) VALUES (NEW.`idBanco`, NEW.`descrisao`, NEW.`numero`, NEW.`saldo_banco`) ON DUPLICATE KEY UPDATE `idBanco` = VALUES(`idBanco`), `descrisao` = VALUES(`descrisao`), `numero` = VALUES(`numero`), `saldo_banco` = VALUES(`saldo_banco`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_banco_au` AFTER UPDATE ON `kitanda_db_1`.`tb_banco` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_banco` SET `idBanco` = NEW.`idBanco`, `descrisao` = NEW.`descrisao`, `numero` = NEW.`numero`, `saldo_banco` = NEW.`saldo_banco` WHERE `idBanco` = OLD.`idBanco`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_banco` (`idBanco`, `descrisao`, `numero`, `saldo_banco`) VALUES (NEW.`idBanco`, NEW.`descrisao`, NEW.`numero`, NEW.`saldo_banco`) ON DUPLICATE KEY UPDATE `idBanco` = VALUES(`idBanco`), `descrisao` = VALUES(`descrisao`), `numero` = VALUES(`numero`), `saldo_banco` = VALUES(`saldo_banco`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_banco_ad` AFTER DELETE ON `kitanda_db_1`.`tb_banco` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_banco` WHERE `idBanco` = OLD.`idBanco`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_cliente_ai` AFTER INSERT ON `kitanda_db_1`.`tb_cliente` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_cliente` (`codigo`, `nome`, `morada`, `telefone`, `nif`, `email`, `pais`, `pais_ISO`, `percentagem_desconto`) VALUES (NEW.`codigo`, NEW.`nome`, NEW.`morada`, NEW.`telefone`, NEW.`nif`, NEW.`email`, NEW.`pais`, NEW.`pais_ISO`, NEW.`percentagem_desconto`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `nome` = VALUES(`nome`), `morada` = VALUES(`morada`), `telefone` = VALUES(`telefone`), `nif` = VALUES(`nif`), `email` = VALUES(`email`), `pais` = VALUES(`pais`), `pais_ISO` = VALUES(`pais_ISO`), `percentagem_desconto` = VALUES(`percentagem_desconto`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_cliente_au` AFTER UPDATE ON `kitanda_db_1`.`tb_cliente` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_cliente` SET `codigo` = NEW.`codigo`, `nome` = NEW.`nome`, `morada` = NEW.`morada`, `telefone` = NEW.`telefone`, `nif` = NEW.`nif`, `email` = NEW.`email`, `pais` = NEW.`pais`, `pais_ISO` = NEW.`pais_ISO`, `percentagem_desconto` = NEW.`percentagem_desconto` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_cliente` (`codigo`, `nome`, `morada`, `telefone`, `nif`, `email`, `pais`, `pais_ISO`, `percentagem_desconto`) VALUES (NEW.`codigo`, NEW.`nome`, NEW.`morada`, NEW.`telefone`, NEW.`nif`, NEW.`email`, NEW.`pais`, NEW.`pais_ISO`, NEW.`percentagem_desconto`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `nome` = VALUES(`nome`), `morada` = VALUES(`morada`), `telefone` = VALUES(`telefone`), `nif` = VALUES(`nif`), `email` = VALUES(`email`), `pais` = VALUES(`pais`), `pais_ISO` = VALUES(`pais_ISO`), `percentagem_desconto` = VALUES(`percentagem_desconto`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_cliente_ad` AFTER DELETE ON `kitanda_db_1`.`tb_cliente` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_cliente` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_clientes_encomenda_ai` AFTER INSERT ON `kitanda_db_1`.`tb_clientes_encomenda` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_clientes_encomenda` (`idCliente`, `nome_cliente`, `telefone`, `endereco`, `saldo`, `email`, `credito`) VALUES (NEW.`idCliente`, NEW.`nome_cliente`, NEW.`telefone`, NEW.`endereco`, NEW.`saldo`, NEW.`email`, NEW.`credito`) ON DUPLICATE KEY UPDATE `idCliente` = VALUES(`idCliente`), `nome_cliente` = VALUES(`nome_cliente`), `telefone` = VALUES(`telefone`), `endereco` = VALUES(`endereco`), `saldo` = VALUES(`saldo`), `email` = VALUES(`email`), `credito` = VALUES(`credito`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_clientes_encomenda_au` AFTER UPDATE ON `kitanda_db_1`.`tb_clientes_encomenda` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_clientes_encomenda` SET `idCliente` = NEW.`idCliente`, `nome_cliente` = NEW.`nome_cliente`, `telefone` = NEW.`telefone`, `endereco` = NEW.`endereco`, `saldo` = NEW.`saldo`, `email` = NEW.`email`, `credito` = NEW.`credito` WHERE `idCliente` = OLD.`idCliente`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_clientes_encomenda` (`idCliente`, `nome_cliente`, `telefone`, `endereco`, `saldo`, `email`, `credito`) VALUES (NEW.`idCliente`, NEW.`nome_cliente`, NEW.`telefone`, NEW.`endereco`, NEW.`saldo`, NEW.`email`, NEW.`credito`) ON DUPLICATE KEY UPDATE `idCliente` = VALUES(`idCliente`), `nome_cliente` = VALUES(`nome_cliente`), `telefone` = VALUES(`telefone`), `endereco` = VALUES(`endereco`), `saldo` = VALUES(`saldo`), `email` = VALUES(`email`), `credito` = VALUES(`credito`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_clientes_encomenda_ad` AFTER DELETE ON `kitanda_db_1`.`tb_clientes_encomenda` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_clientes_encomenda` WHERE `idCliente` = OLD.`idCliente`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_conta_ai` AFTER INSERT ON `kitanda_db_1`.`tb_conta` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_conta` (`idContaPK`, `numero_conta`, `saldo_conta`, `idBancoFK`, `iban`, `idFuncionarioFK`) VALUES (NEW.`idContaPK`, NEW.`numero_conta`, NEW.`saldo_conta`, NEW.`idBancoFK`, NEW.`iban`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idContaPK` = VALUES(`idContaPK`), `numero_conta` = VALUES(`numero_conta`), `saldo_conta` = VALUES(`saldo_conta`), `idBancoFK` = VALUES(`idBancoFK`), `iban` = VALUES(`iban`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_conta_au` AFTER UPDATE ON `kitanda_db_1`.`tb_conta` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_conta` SET `idContaPK` = NEW.`idContaPK`, `numero_conta` = NEW.`numero_conta`, `saldo_conta` = NEW.`saldo_conta`, `idBancoFK` = NEW.`idBancoFK`, `iban` = NEW.`iban`, `idFuncionarioFK` = NEW.`idFuncionarioFK` WHERE `idContaPK` = OLD.`idContaPK`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_conta` (`idContaPK`, `numero_conta`, `saldo_conta`, `idBancoFK`, `iban`, `idFuncionarioFK`) VALUES (NEW.`idContaPK`, NEW.`numero_conta`, NEW.`saldo_conta`, NEW.`idBancoFK`, NEW.`iban`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idContaPK` = VALUES(`idContaPK`), `numero_conta` = VALUES(`numero_conta`), `saldo_conta` = VALUES(`saldo_conta`), `idBancoFK` = VALUES(`idBancoFK`), `iban` = VALUES(`iban`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_conta_ad` AFTER DELETE ON `kitanda_db_1`.`tb_conta` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_conta` WHERE `idContaPK` = OLD.`idContaPK`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_dados_instituicao_ai` AFTER INSERT ON `kitanda_db_1`.`tb_dados_instituicao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_dados_instituicao` (`idDadosInsitiuicao`, `nome`, `telefone`, `enderecos`, `email`, `nif`, `cont`, `conta_bancaria1`, `conta_bancaria2`, `conta_bancaria3`, `conta_bancaria4`, `conta_bancaria5`, `conta_bancaria6`, `director_geral`, `numero_vias`, `impressora`, `foco`, `docpadrao`, `desactivarvias`, `desconto_financeiro`, `ano_economico`, `vizualisar_stock`, `transtorno`, `correio_caixa`, `negocio`, `obs_ft`, `prazo_ft`, `local_carregamento`, `conta_bancaria7`, `conta_bancaria8`, `slogan`, `obs_devolucao`, `teclado`, `data_licenca`, `regime`, `regime_contrato`, `config_armazens`, `usar_dois_precos`, `impressora_cozinha`, `chave_mestre`, `data_fecho`, `tesouraria`, `rh`, `comercial`, `janela_servico`, `impressora_sala`, `prazo_proforma`, `desactivar_lugares`, `hora_comeco_venda`, `hora_termino_venda`, `tipo_fecho_caixa`, `enviar_email`, `stock_consulta`, `tipo_ficha_tecnica`, `posto`, `impressora_caixa`, `segundo_monitor`, `alterar_preco`, `tela_venda`) VALUES (NEW.`idDadosInsitiuicao`, NEW.`nome`, NEW.`telefone`, NEW.`enderecos`, NEW.`email`, NEW.`nif`, NEW.`cont`, NEW.`conta_bancaria1`, NEW.`conta_bancaria2`, NEW.`conta_bancaria3`, NEW.`conta_bancaria4`, NEW.`conta_bancaria5`, NEW.`conta_bancaria6`, NEW.`director_geral`, NEW.`numero_vias`, NEW.`impressora`, NEW.`foco`, NEW.`docpadrao`, NEW.`desactivarvias`, NEW.`desconto_financeiro`, NEW.`ano_economico`, NEW.`vizualisar_stock`, NEW.`transtorno`, NEW.`correio_caixa`, NEW.`negocio`, NEW.`obs_ft`, NEW.`prazo_ft`, NEW.`local_carregamento`, NEW.`conta_bancaria7`, NEW.`conta_bancaria8`, NEW.`slogan`, NEW.`obs_devolucao`, NEW.`teclado`, NEW.`data_licenca`, NEW.`regime`, NEW.`regime_contrato`, NEW.`config_armazens`, NEW.`usar_dois_precos`, NEW.`impressora_cozinha`, NEW.`chave_mestre`, NEW.`data_fecho`, NEW.`tesouraria`, NEW.`rh`, NEW.`comercial`, NEW.`janela_servico`, NEW.`impressora_sala`, NEW.`prazo_proforma`, NEW.`desactivar_lugares`, NEW.`hora_comeco_venda`, NEW.`hora_termino_venda`, NEW.`tipo_fecho_caixa`, NEW.`enviar_email`, NEW.`stock_consulta`, NEW.`tipo_ficha_tecnica`, NEW.`posto`, NEW.`impressora_caixa`, NEW.`segundo_monitor`, NEW.`alterar_preco`, NEW.`tela_venda`) ON DUPLICATE KEY UPDATE `idDadosInsitiuicao` = VALUES(`idDadosInsitiuicao`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `enderecos` = VALUES(`enderecos`), `email` = VALUES(`email`), `nif` = VALUES(`nif`), `cont` = VALUES(`cont`), `conta_bancaria1` = VALUES(`conta_bancaria1`), `conta_bancaria2` = VALUES(`conta_bancaria2`), `conta_bancaria3` = VALUES(`conta_bancaria3`), `conta_bancaria4` = VALUES(`conta_bancaria4`), `conta_bancaria5` = VALUES(`conta_bancaria5`), `conta_bancaria6` = VALUES(`conta_bancaria6`), `director_geral` = VALUES(`director_geral`), `numero_vias` = VALUES(`numero_vias`), `impressora` = VALUES(`impressora`), `foco` = VALUES(`foco`), `docpadrao` = VALUES(`docpadrao`), `desactivarvias` = VALUES(`desactivarvias`), `desconto_financeiro` = VALUES(`desconto_financeiro`), `ano_economico` = VALUES(`ano_economico`), `vizualisar_stock` = VALUES(`vizualisar_stock`), `transtorno` = VALUES(`transtorno`), `correio_caixa` = VALUES(`correio_caixa`), `negocio` = VALUES(`negocio`), `obs_ft` = VALUES(`obs_ft`), `prazo_ft` = VALUES(`prazo_ft`), `local_carregamento` = VALUES(`local_carregamento`), `conta_bancaria7` = VALUES(`conta_bancaria7`), `conta_bancaria8` = VALUES(`conta_bancaria8`), `slogan` = VALUES(`slogan`), `obs_devolucao` = VALUES(`obs_devolucao`), `teclado` = VALUES(`teclado`), `data_licenca` = VALUES(`data_licenca`), `regime` = VALUES(`regime`), `regime_contrato` = VALUES(`regime_contrato`), `config_armazens` = VALUES(`config_armazens`), `usar_dois_precos` = VALUES(`usar_dois_precos`), `impressora_cozinha` = VALUES(`impressora_cozinha`), `chave_mestre` = VALUES(`chave_mestre`), `data_fecho` = VALUES(`data_fecho`), `tesouraria` = VALUES(`tesouraria`), `rh` = VALUES(`rh`), `comercial` = VALUES(`comercial`), `janela_servico` = VALUES(`janela_servico`), `impressora_sala` = VALUES(`impressora_sala`), `prazo_proforma` = VALUES(`prazo_proforma`), `desactivar_lugares` = VALUES(`desactivar_lugares`), `hora_comeco_venda` = VALUES(`hora_comeco_venda`), `hora_termino_venda` = VALUES(`hora_termino_venda`), `tipo_fecho_caixa` = VALUES(`tipo_fecho_caixa`), `enviar_email` = VALUES(`enviar_email`), `stock_consulta` = VALUES(`stock_consulta`), `tipo_ficha_tecnica` = VALUES(`tipo_ficha_tecnica`), `posto` = VALUES(`posto`), `impressora_caixa` = VALUES(`impressora_caixa`), `segundo_monitor` = VALUES(`segundo_monitor`), `alterar_preco` = VALUES(`alterar_preco`), `tela_venda` = VALUES(`tela_venda`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_dados_instituicao_au` AFTER UPDATE ON `kitanda_db_1`.`tb_dados_instituicao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_dados_instituicao` SET `idDadosInsitiuicao` = NEW.`idDadosInsitiuicao`, `nome` = NEW.`nome`, `telefone` = NEW.`telefone`, `enderecos` = NEW.`enderecos`, `email` = NEW.`email`, `nif` = NEW.`nif`, `cont` = NEW.`cont`, `conta_bancaria1` = NEW.`conta_bancaria1`, `conta_bancaria2` = NEW.`conta_bancaria2`, `conta_bancaria3` = NEW.`conta_bancaria3`, `conta_bancaria4` = NEW.`conta_bancaria4`, `conta_bancaria5` = NEW.`conta_bancaria5`, `conta_bancaria6` = NEW.`conta_bancaria6`, `director_geral` = NEW.`director_geral`, `numero_vias` = NEW.`numero_vias`, `impressora` = NEW.`impressora`, `foco` = NEW.`foco`, `docpadrao` = NEW.`docpadrao`, `desactivarvias` = NEW.`desactivarvias`, `desconto_financeiro` = NEW.`desconto_financeiro`, `ano_economico` = NEW.`ano_economico`, `vizualisar_stock` = NEW.`vizualisar_stock`, `transtorno` = NEW.`transtorno`, `correio_caixa` = NEW.`correio_caixa`, `negocio` = NEW.`negocio`, `obs_ft` = NEW.`obs_ft`, `prazo_ft` = NEW.`prazo_ft`, `local_carregamento` = NEW.`local_carregamento`, `conta_bancaria7` = NEW.`conta_bancaria7`, `conta_bancaria8` = NEW.`conta_bancaria8`, `slogan` = NEW.`slogan`, `obs_devolucao` = NEW.`obs_devolucao`, `teclado` = NEW.`teclado`, `data_licenca` = NEW.`data_licenca`, `regime` = NEW.`regime`, `regime_contrato` = NEW.`regime_contrato`, `config_armazens` = NEW.`config_armazens`, `usar_dois_precos` = NEW.`usar_dois_precos`, `impressora_cozinha` = NEW.`impressora_cozinha`, `chave_mestre` = NEW.`chave_mestre`, `data_fecho` = NEW.`data_fecho`, `tesouraria` = NEW.`tesouraria`, `rh` = NEW.`rh`, `comercial` = NEW.`comercial`, `janela_servico` = NEW.`janela_servico`, `impressora_sala` = NEW.`impressora_sala`, `prazo_proforma` = NEW.`prazo_proforma`, `desactivar_lugares` = NEW.`desactivar_lugares`, `hora_comeco_venda` = NEW.`hora_comeco_venda`, `hora_termino_venda` = NEW.`hora_termino_venda`, `tipo_fecho_caixa` = NEW.`tipo_fecho_caixa`, `enviar_email` = NEW.`enviar_email`, `stock_consulta` = NEW.`stock_consulta`, `tipo_ficha_tecnica` = NEW.`tipo_ficha_tecnica`, `posto` = NEW.`posto`, `impressora_caixa` = NEW.`impressora_caixa`, `segundo_monitor` = NEW.`segundo_monitor`, `alterar_preco` = NEW.`alterar_preco`, `tela_venda` = NEW.`tela_venda` WHERE `idDadosInsitiuicao` = OLD.`idDadosInsitiuicao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_dados_instituicao` (`idDadosInsitiuicao`, `nome`, `telefone`, `enderecos`, `email`, `nif`, `cont`, `conta_bancaria1`, `conta_bancaria2`, `conta_bancaria3`, `conta_bancaria4`, `conta_bancaria5`, `conta_bancaria6`, `director_geral`, `numero_vias`, `impressora`, `foco`, `docpadrao`, `desactivarvias`, `desconto_financeiro`, `ano_economico`, `vizualisar_stock`, `transtorno`, `correio_caixa`, `negocio`, `obs_ft`, `prazo_ft`, `local_carregamento`, `conta_bancaria7`, `conta_bancaria8`, `slogan`, `obs_devolucao`, `teclado`, `data_licenca`, `regime`, `regime_contrato`, `config_armazens`, `usar_dois_precos`, `impressora_cozinha`, `chave_mestre`, `data_fecho`, `tesouraria`, `rh`, `comercial`, `janela_servico`, `impressora_sala`, `prazo_proforma`, `desactivar_lugares`, `hora_comeco_venda`, `hora_termino_venda`, `tipo_fecho_caixa`, `enviar_email`, `stock_consulta`, `tipo_ficha_tecnica`, `posto`, `impressora_caixa`, `segundo_monitor`, `alterar_preco`, `tela_venda`) VALUES (NEW.`idDadosInsitiuicao`, NEW.`nome`, NEW.`telefone`, NEW.`enderecos`, NEW.`email`, NEW.`nif`, NEW.`cont`, NEW.`conta_bancaria1`, NEW.`conta_bancaria2`, NEW.`conta_bancaria3`, NEW.`conta_bancaria4`, NEW.`conta_bancaria5`, NEW.`conta_bancaria6`, NEW.`director_geral`, NEW.`numero_vias`, NEW.`impressora`, NEW.`foco`, NEW.`docpadrao`, NEW.`desactivarvias`, NEW.`desconto_financeiro`, NEW.`ano_economico`, NEW.`vizualisar_stock`, NEW.`transtorno`, NEW.`correio_caixa`, NEW.`negocio`, NEW.`obs_ft`, NEW.`prazo_ft`, NEW.`local_carregamento`, NEW.`conta_bancaria7`, NEW.`conta_bancaria8`, NEW.`slogan`, NEW.`obs_devolucao`, NEW.`teclado`, NEW.`data_licenca`, NEW.`regime`, NEW.`regime_contrato`, NEW.`config_armazens`, NEW.`usar_dois_precos`, NEW.`impressora_cozinha`, NEW.`chave_mestre`, NEW.`data_fecho`, NEW.`tesouraria`, NEW.`rh`, NEW.`comercial`, NEW.`janela_servico`, NEW.`impressora_sala`, NEW.`prazo_proforma`, NEW.`desactivar_lugares`, NEW.`hora_comeco_venda`, NEW.`hora_termino_venda`, NEW.`tipo_fecho_caixa`, NEW.`enviar_email`, NEW.`stock_consulta`, NEW.`tipo_ficha_tecnica`, NEW.`posto`, NEW.`impressora_caixa`, NEW.`segundo_monitor`, NEW.`alterar_preco`, NEW.`tela_venda`) ON DUPLICATE KEY UPDATE `idDadosInsitiuicao` = VALUES(`idDadosInsitiuicao`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `enderecos` = VALUES(`enderecos`), `email` = VALUES(`email`), `nif` = VALUES(`nif`), `cont` = VALUES(`cont`), `conta_bancaria1` = VALUES(`conta_bancaria1`), `conta_bancaria2` = VALUES(`conta_bancaria2`), `conta_bancaria3` = VALUES(`conta_bancaria3`), `conta_bancaria4` = VALUES(`conta_bancaria4`), `conta_bancaria5` = VALUES(`conta_bancaria5`), `conta_bancaria6` = VALUES(`conta_bancaria6`), `director_geral` = VALUES(`director_geral`), `numero_vias` = VALUES(`numero_vias`), `impressora` = VALUES(`impressora`), `foco` = VALUES(`foco`), `docpadrao` = VALUES(`docpadrao`), `desactivarvias` = VALUES(`desactivarvias`), `desconto_financeiro` = VALUES(`desconto_financeiro`), `ano_economico` = VALUES(`ano_economico`), `vizualisar_stock` = VALUES(`vizualisar_stock`), `transtorno` = VALUES(`transtorno`), `correio_caixa` = VALUES(`correio_caixa`), `negocio` = VALUES(`negocio`), `obs_ft` = VALUES(`obs_ft`), `prazo_ft` = VALUES(`prazo_ft`), `local_carregamento` = VALUES(`local_carregamento`), `conta_bancaria7` = VALUES(`conta_bancaria7`), `conta_bancaria8` = VALUES(`conta_bancaria8`), `slogan` = VALUES(`slogan`), `obs_devolucao` = VALUES(`obs_devolucao`), `teclado` = VALUES(`teclado`), `data_licenca` = VALUES(`data_licenca`), `regime` = VALUES(`regime`), `regime_contrato` = VALUES(`regime_contrato`), `config_armazens` = VALUES(`config_armazens`), `usar_dois_precos` = VALUES(`usar_dois_precos`), `impressora_cozinha` = VALUES(`impressora_cozinha`), `chave_mestre` = VALUES(`chave_mestre`), `data_fecho` = VALUES(`data_fecho`), `tesouraria` = VALUES(`tesouraria`), `rh` = VALUES(`rh`), `comercial` = VALUES(`comercial`), `janela_servico` = VALUES(`janela_servico`), `impressora_sala` = VALUES(`impressora_sala`), `prazo_proforma` = VALUES(`prazo_proforma`), `desactivar_lugares` = VALUES(`desactivar_lugares`), `hora_comeco_venda` = VALUES(`hora_comeco_venda`), `hora_termino_venda` = VALUES(`hora_termino_venda`), `tipo_fecho_caixa` = VALUES(`tipo_fecho_caixa`), `enviar_email` = VALUES(`enviar_email`), `stock_consulta` = VALUES(`stock_consulta`), `tipo_ficha_tecnica` = VALUES(`tipo_ficha_tecnica`), `posto` = VALUES(`posto`), `impressora_caixa` = VALUES(`impressora_caixa`), `segundo_monitor` = VALUES(`segundo_monitor`), `alterar_preco` = VALUES(`alterar_preco`), `tela_venda` = VALUES(`tela_venda`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_dados_instituicao_ad` AFTER DELETE ON `kitanda_db_1`.`tb_dados_instituicao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_dados_instituicao` WHERE `idDadosInsitiuicao` = OLD.`idDadosInsitiuicao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_departamento_ai` AFTER INSERT ON `kitanda_db_1`.`tb_departamento` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_departamento` (`pk_departamento`, `designacao`) VALUES (NEW.`pk_departamento`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_departamento` = VALUES(`pk_departamento`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_departamento_au` AFTER UPDATE ON `kitanda_db_1`.`tb_departamento` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_departamento` SET `pk_departamento` = NEW.`pk_departamento`, `designacao` = NEW.`designacao` WHERE `pk_departamento` = OLD.`pk_departamento`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_departamento` (`pk_departamento`, `designacao`) VALUES (NEW.`pk_departamento`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_departamento` = VALUES(`pk_departamento`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_departamento_ad` AFTER DELETE ON `kitanda_db_1`.`tb_departamento` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_departamento` WHERE `pk_departamento` = OLD.`pk_departamento`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_desconto_ai` AFTER INSERT ON `kitanda_db_1`.`tb_desconto` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_desconto` (`idDesconto`, `data`, `hora`, `quantidade`, `fk_produto`, `fk_cliente`, `fk_usuario`, `valor`) VALUES (NEW.`idDesconto`, NEW.`data`, NEW.`hora`, NEW.`quantidade`, NEW.`fk_produto`, NEW.`fk_cliente`, NEW.`fk_usuario`, NEW.`valor`) ON DUPLICATE KEY UPDATE `idDesconto` = VALUES(`idDesconto`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `quantidade` = VALUES(`quantidade`), `fk_produto` = VALUES(`fk_produto`), `fk_cliente` = VALUES(`fk_cliente`), `fk_usuario` = VALUES(`fk_usuario`), `valor` = VALUES(`valor`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_desconto_au` AFTER UPDATE ON `kitanda_db_1`.`tb_desconto` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_desconto` SET `idDesconto` = NEW.`idDesconto`, `data` = NEW.`data`, `hora` = NEW.`hora`, `quantidade` = NEW.`quantidade`, `fk_produto` = NEW.`fk_produto`, `fk_cliente` = NEW.`fk_cliente`, `fk_usuario` = NEW.`fk_usuario`, `valor` = NEW.`valor` WHERE `idDesconto` = OLD.`idDesconto`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_desconto` (`idDesconto`, `data`, `hora`, `quantidade`, `fk_produto`, `fk_cliente`, `fk_usuario`, `valor`) VALUES (NEW.`idDesconto`, NEW.`data`, NEW.`hora`, NEW.`quantidade`, NEW.`fk_produto`, NEW.`fk_cliente`, NEW.`fk_usuario`, NEW.`valor`) ON DUPLICATE KEY UPDATE `idDesconto` = VALUES(`idDesconto`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `quantidade` = VALUES(`quantidade`), `fk_produto` = VALUES(`fk_produto`), `fk_cliente` = VALUES(`fk_cliente`), `fk_usuario` = VALUES(`fk_usuario`), `valor` = VALUES(`valor`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_desconto_ad` AFTER DELETE ON `kitanda_db_1`.`tb_desconto` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_desconto` WHERE `idDesconto` = OLD.`idDesconto`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_encomenda_ai` AFTER INSERT ON `kitanda_db_1`.`tb_encomenda` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_encomenda` (`idEncomenda`, `data_encomenda`, `data_entrega_prevista`, `idCliente`, `total_encomenda`, `status_entrega`, `obs`, `idUsuario`) VALUES (NEW.`idEncomenda`, NEW.`data_encomenda`, NEW.`data_entrega_prevista`, NEW.`idCliente`, NEW.`total_encomenda`, NEW.`status_entrega`, NEW.`obs`, NEW.`idUsuario`) ON DUPLICATE KEY UPDATE `idEncomenda` = VALUES(`idEncomenda`), `data_encomenda` = VALUES(`data_encomenda`), `data_entrega_prevista` = VALUES(`data_entrega_prevista`), `idCliente` = VALUES(`idCliente`), `total_encomenda` = VALUES(`total_encomenda`), `status_entrega` = VALUES(`status_entrega`), `obs` = VALUES(`obs`), `idUsuario` = VALUES(`idUsuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_encomenda_au` AFTER UPDATE ON `kitanda_db_1`.`tb_encomenda` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_encomenda` SET `idEncomenda` = NEW.`idEncomenda`, `data_encomenda` = NEW.`data_encomenda`, `data_entrega_prevista` = NEW.`data_entrega_prevista`, `idCliente` = NEW.`idCliente`, `total_encomenda` = NEW.`total_encomenda`, `status_entrega` = NEW.`status_entrega`, `obs` = NEW.`obs`, `idUsuario` = NEW.`idUsuario` WHERE `idEncomenda` = OLD.`idEncomenda`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_encomenda` (`idEncomenda`, `data_encomenda`, `data_entrega_prevista`, `idCliente`, `total_encomenda`, `status_entrega`, `obs`, `idUsuario`) VALUES (NEW.`idEncomenda`, NEW.`data_encomenda`, NEW.`data_entrega_prevista`, NEW.`idCliente`, NEW.`total_encomenda`, NEW.`status_entrega`, NEW.`obs`, NEW.`idUsuario`) ON DUPLICATE KEY UPDATE `idEncomenda` = VALUES(`idEncomenda`), `data_encomenda` = VALUES(`data_encomenda`), `data_entrega_prevista` = VALUES(`data_entrega_prevista`), `idCliente` = VALUES(`idCliente`), `total_encomenda` = VALUES(`total_encomenda`), `status_entrega` = VALUES(`status_entrega`), `obs` = VALUES(`obs`), `idUsuario` = VALUES(`idUsuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_encomenda_ad` AFTER DELETE ON `kitanda_db_1`.`tb_encomenda` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_encomenda` WHERE `idEncomenda` = OLD.`idEncomenda`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_entrada_ai` AFTER INSERT ON `kitanda_db_1`.`tb_entrada` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_entrada` (`idEntrada`, `data_entrada`, `idUsuario`, `idArmazemFK`, `status_eliminado`, `quantidade`, `idProduto`) VALUES (NEW.`idEntrada`, NEW.`data_entrada`, NEW.`idUsuario`, NEW.`idArmazemFK`, NEW.`status_eliminado`, NEW.`quantidade`, NEW.`idProduto`) ON DUPLICATE KEY UPDATE `idEntrada` = VALUES(`idEntrada`), `data_entrada` = VALUES(`data_entrada`), `idUsuario` = VALUES(`idUsuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `status_eliminado` = VALUES(`status_eliminado`), `quantidade` = VALUES(`quantidade`), `idProduto` = VALUES(`idProduto`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_entrada_au` AFTER UPDATE ON `kitanda_db_1`.`tb_entrada` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_entrada` SET `idEntrada` = NEW.`idEntrada`, `data_entrada` = NEW.`data_entrada`, `idUsuario` = NEW.`idUsuario`, `idArmazemFK` = NEW.`idArmazemFK`, `status_eliminado` = NEW.`status_eliminado`, `quantidade` = NEW.`quantidade`, `idProduto` = NEW.`idProduto` WHERE `idEntrada` = OLD.`idEntrada`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_entrada` (`idEntrada`, `data_entrada`, `idUsuario`, `idArmazemFK`, `status_eliminado`, `quantidade`, `idProduto`) VALUES (NEW.`idEntrada`, NEW.`data_entrada`, NEW.`idUsuario`, NEW.`idArmazemFK`, NEW.`status_eliminado`, NEW.`quantidade`, NEW.`idProduto`) ON DUPLICATE KEY UPDATE `idEntrada` = VALUES(`idEntrada`), `data_entrada` = VALUES(`data_entrada`), `idUsuario` = VALUES(`idUsuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `status_eliminado` = VALUES(`status_eliminado`), `quantidade` = VALUES(`quantidade`), `idProduto` = VALUES(`idProduto`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_entrada_ad` AFTER DELETE ON `kitanda_db_1`.`tb_entrada` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_entrada` WHERE `idEntrada` = OLD.`idEntrada`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_entrada_vasilhame_ai` AFTER INSERT ON `kitanda_db_1`.`tb_entrada_vasilhame` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_entrada_vasilhame` (`pk_entrada_vasilhame`, `data_entrada`, `hora_entrada`, `fk_usuario`, `fk_amazem`) VALUES (NEW.`pk_entrada_vasilhame`, NEW.`data_entrada`, NEW.`hora_entrada`, NEW.`fk_usuario`, NEW.`fk_amazem`) ON DUPLICATE KEY UPDATE `pk_entrada_vasilhame` = VALUES(`pk_entrada_vasilhame`), `data_entrada` = VALUES(`data_entrada`), `hora_entrada` = VALUES(`hora_entrada`), `fk_usuario` = VALUES(`fk_usuario`), `fk_amazem` = VALUES(`fk_amazem`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_entrada_vasilhame_au` AFTER UPDATE ON `kitanda_db_1`.`tb_entrada_vasilhame` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_entrada_vasilhame` SET `pk_entrada_vasilhame` = NEW.`pk_entrada_vasilhame`, `data_entrada` = NEW.`data_entrada`, `hora_entrada` = NEW.`hora_entrada`, `fk_usuario` = NEW.`fk_usuario`, `fk_amazem` = NEW.`fk_amazem` WHERE `pk_entrada_vasilhame` = OLD.`pk_entrada_vasilhame`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_entrada_vasilhame` (`pk_entrada_vasilhame`, `data_entrada`, `hora_entrada`, `fk_usuario`, `fk_amazem`) VALUES (NEW.`pk_entrada_vasilhame`, NEW.`data_entrada`, NEW.`hora_entrada`, NEW.`fk_usuario`, NEW.`fk_amazem`) ON DUPLICATE KEY UPDATE `pk_entrada_vasilhame` = VALUES(`pk_entrada_vasilhame`), `data_entrada` = VALUES(`data_entrada`), `hora_entrada` = VALUES(`hora_entrada`), `fk_usuario` = VALUES(`fk_usuario`), `fk_amazem` = VALUES(`fk_amazem`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_entrada_vasilhame_ad` AFTER DELETE ON `kitanda_db_1`.`tb_entrada_vasilhame` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_entrada_vasilhame` WHERE `pk_entrada_vasilhame` = OLD.`pk_entrada_vasilhame`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_especialidade_ai` AFTER INSERT ON `kitanda_db_1`.`tb_especialidade` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_especialidade` (`pk_especialidade`, `designacao`) VALUES (NEW.`pk_especialidade`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_especialidade` = VALUES(`pk_especialidade`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_especialidade_au` AFTER UPDATE ON `kitanda_db_1`.`tb_especialidade` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_especialidade` SET `pk_especialidade` = NEW.`pk_especialidade`, `designacao` = NEW.`designacao` WHERE `pk_especialidade` = OLD.`pk_especialidade`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_especialidade` (`pk_especialidade`, `designacao`) VALUES (NEW.`pk_especialidade`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_especialidade` = VALUES(`pk_especialidade`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_especialidade_ad` AFTER DELETE ON `kitanda_db_1`.`tb_especialidade` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_especialidade` WHERE `pk_especialidade` = OLD.`pk_especialidade`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_estado_civil_ai` AFTER INSERT ON `kitanda_db_1`.`tb_estado_civil` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_estado_civil` (`pk_estado_civil`, `designacao`) VALUES (NEW.`pk_estado_civil`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_estado_civil` = VALUES(`pk_estado_civil`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_estado_civil_au` AFTER UPDATE ON `kitanda_db_1`.`tb_estado_civil` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_estado_civil` SET `pk_estado_civil` = NEW.`pk_estado_civil`, `designacao` = NEW.`designacao` WHERE `pk_estado_civil` = OLD.`pk_estado_civil`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_estado_civil` (`pk_estado_civil`, `designacao`) VALUES (NEW.`pk_estado_civil`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_estado_civil` = VALUES(`pk_estado_civil`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_estado_civil_ad` AFTER DELETE ON `kitanda_db_1`.`tb_estado_civil` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_estado_civil` WHERE `pk_estado_civil` = OLD.`pk_estado_civil`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_estorno_ai` AFTER INSERT ON `kitanda_db_1`.`tb_estorno` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_estorno` (`pk_estorno`, `data_estorno`, `fk_usuario`, `idArmazemFK`, `hora_estorno`, `status_eliminado`, `obs`) VALUES (NEW.`pk_estorno`, NEW.`data_estorno`, NEW.`fk_usuario`, NEW.`idArmazemFK`, NEW.`hora_estorno`, NEW.`status_eliminado`, NEW.`obs`) ON DUPLICATE KEY UPDATE `pk_estorno` = VALUES(`pk_estorno`), `data_estorno` = VALUES(`data_estorno`), `fk_usuario` = VALUES(`fk_usuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `hora_estorno` = VALUES(`hora_estorno`), `status_eliminado` = VALUES(`status_eliminado`), `obs` = VALUES(`obs`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_estorno_au` AFTER UPDATE ON `kitanda_db_1`.`tb_estorno` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_estorno` SET `pk_estorno` = NEW.`pk_estorno`, `data_estorno` = NEW.`data_estorno`, `fk_usuario` = NEW.`fk_usuario`, `idArmazemFK` = NEW.`idArmazemFK`, `hora_estorno` = NEW.`hora_estorno`, `status_eliminado` = NEW.`status_eliminado`, `obs` = NEW.`obs` WHERE `pk_estorno` = OLD.`pk_estorno`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_estorno` (`pk_estorno`, `data_estorno`, `fk_usuario`, `idArmazemFK`, `hora_estorno`, `status_eliminado`, `obs`) VALUES (NEW.`pk_estorno`, NEW.`data_estorno`, NEW.`fk_usuario`, NEW.`idArmazemFK`, NEW.`hora_estorno`, NEW.`status_eliminado`, NEW.`obs`) ON DUPLICATE KEY UPDATE `pk_estorno` = VALUES(`pk_estorno`), `data_estorno` = VALUES(`data_estorno`), `fk_usuario` = VALUES(`fk_usuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `hora_estorno` = VALUES(`hora_estorno`), `status_eliminado` = VALUES(`status_eliminado`), `obs` = VALUES(`obs`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_estorno_ad` AFTER DELETE ON `kitanda_db_1`.`tb_estorno` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_estorno` WHERE `pk_estorno` = OLD.`pk_estorno`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_falta_ai` AFTER INSERT ON `kitanda_db_1`.`tb_falta` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_falta` (`idFaltaPK`, `n_falta`, `data`, `hora`, `descricao_falta`, `idFuncionarioFK`, `justificada_injustificada`, `descricao_justificativa`, `data_justificativa`) VALUES (NEW.`idFaltaPK`, NEW.`n_falta`, NEW.`data`, NEW.`hora`, NEW.`descricao_falta`, NEW.`idFuncionarioFK`, NEW.`justificada_injustificada`, NEW.`descricao_justificativa`, NEW.`data_justificativa`) ON DUPLICATE KEY UPDATE `idFaltaPK` = VALUES(`idFaltaPK`), `n_falta` = VALUES(`n_falta`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `descricao_falta` = VALUES(`descricao_falta`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`), `justificada_injustificada` = VALUES(`justificada_injustificada`), `descricao_justificativa` = VALUES(`descricao_justificativa`), `data_justificativa` = VALUES(`data_justificativa`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_falta_au` AFTER UPDATE ON `kitanda_db_1`.`tb_falta` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_falta` SET `idFaltaPK` = NEW.`idFaltaPK`, `n_falta` = NEW.`n_falta`, `data` = NEW.`data`, `hora` = NEW.`hora`, `descricao_falta` = NEW.`descricao_falta`, `idFuncionarioFK` = NEW.`idFuncionarioFK`, `justificada_injustificada` = NEW.`justificada_injustificada`, `descricao_justificativa` = NEW.`descricao_justificativa`, `data_justificativa` = NEW.`data_justificativa` WHERE `idFaltaPK` = OLD.`idFaltaPK`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_falta` (`idFaltaPK`, `n_falta`, `data`, `hora`, `descricao_falta`, `idFuncionarioFK`, `justificada_injustificada`, `descricao_justificativa`, `data_justificativa`) VALUES (NEW.`idFaltaPK`, NEW.`n_falta`, NEW.`data`, NEW.`hora`, NEW.`descricao_falta`, NEW.`idFuncionarioFK`, NEW.`justificada_injustificada`, NEW.`descricao_justificativa`, NEW.`data_justificativa`) ON DUPLICATE KEY UPDATE `idFaltaPK` = VALUES(`idFaltaPK`), `n_falta` = VALUES(`n_falta`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `descricao_falta` = VALUES(`descricao_falta`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`), `justificada_injustificada` = VALUES(`justificada_injustificada`), `descricao_justificativa` = VALUES(`descricao_justificativa`), `data_justificativa` = VALUES(`data_justificativa`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_falta_ad` AFTER DELETE ON `kitanda_db_1`.`tb_falta` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_falta` WHERE `idFaltaPK` = OLD.`idFaltaPK`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_forma_pagamento_ai` AFTER INSERT ON `kitanda_db_1`.`tb_forma_pagamento` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_forma_pagamento` (`pk_forma_pagamento`, `descricao`) VALUES (NEW.`pk_forma_pagamento`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_forma_pagamento` = VALUES(`pk_forma_pagamento`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_forma_pagamento_au` AFTER UPDATE ON `kitanda_db_1`.`tb_forma_pagamento` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_forma_pagamento` SET `pk_forma_pagamento` = NEW.`pk_forma_pagamento`, `descricao` = NEW.`descricao` WHERE `pk_forma_pagamento` = OLD.`pk_forma_pagamento`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_forma_pagamento` (`pk_forma_pagamento`, `descricao`) VALUES (NEW.`pk_forma_pagamento`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_forma_pagamento` = VALUES(`pk_forma_pagamento`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_forma_pagamento_ad` AFTER DELETE ON `kitanda_db_1`.`tb_forma_pagamento` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_forma_pagamento` WHERE `pk_forma_pagamento` = OLD.`pk_forma_pagamento`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_fornecedor_ai` AFTER INSERT ON `kitanda_db_1`.`tb_fornecedor` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_fornecedor` (`codigo`, `nome`, `telefone`, `email`, `site`, `endereco`, `nif`, `fk_regime`) VALUES (NEW.`codigo`, NEW.`nome`, NEW.`telefone`, NEW.`email`, NEW.`site`, NEW.`endereco`, NEW.`nif`, NEW.`fk_regime`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `email` = VALUES(`email`), `site` = VALUES(`site`), `endereco` = VALUES(`endereco`), `nif` = VALUES(`nif`), `fk_regime` = VALUES(`fk_regime`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_fornecedor_au` AFTER UPDATE ON `kitanda_db_1`.`tb_fornecedor` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_fornecedor` SET `codigo` = NEW.`codigo`, `nome` = NEW.`nome`, `telefone` = NEW.`telefone`, `email` = NEW.`email`, `site` = NEW.`site`, `endereco` = NEW.`endereco`, `nif` = NEW.`nif`, `fk_regime` = NEW.`fk_regime` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_fornecedor` (`codigo`, `nome`, `telefone`, `email`, `site`, `endereco`, `nif`, `fk_regime`) VALUES (NEW.`codigo`, NEW.`nome`, NEW.`telefone`, NEW.`email`, NEW.`site`, NEW.`endereco`, NEW.`nif`, NEW.`fk_regime`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `email` = VALUES(`email`), `site` = VALUES(`site`), `endereco` = VALUES(`endereco`), `nif` = VALUES(`nif`), `fk_regime` = VALUES(`fk_regime`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_fornecedor_ad` AFTER DELETE ON `kitanda_db_1`.`tb_fornecedor` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_fornecedor` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_funcao_ai` AFTER INSERT ON `kitanda_db_1`.`tb_funcao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_funcao` (`pk_funcao`, `designacao`) VALUES (NEW.`pk_funcao`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_funcao` = VALUES(`pk_funcao`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_funcao_au` AFTER UPDATE ON `kitanda_db_1`.`tb_funcao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_funcao` SET `pk_funcao` = NEW.`pk_funcao`, `designacao` = NEW.`designacao` WHERE `pk_funcao` = OLD.`pk_funcao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_funcao` (`pk_funcao`, `designacao`) VALUES (NEW.`pk_funcao`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_funcao` = VALUES(`pk_funcao`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_funcao_ad` AFTER DELETE ON `kitanda_db_1`.`tb_funcao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_funcao` WHERE `pk_funcao` = OLD.`pk_funcao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_funcionario_ai` AFTER INSERT ON `kitanda_db_1`.`tb_funcionario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_funcionario` (`idFuncionario`, `numero_funcionario`, `nome`, `telefone`, `morada`, `user_name`, `password`, `habilitacao_literaria`, `dias_instituicao`, `fk_funcao`, `fk_departamento`, `fk_grau_academico`, `fk_especialidade`, `nif`, `data_nascimento`, `docID`, `ndocID`, `data_emissao_docID`, `data_validade_docID`, `fk_estado_civil`, `idStatusFK`, `fkUsuario`, `fk_modalidade`, `fk_empresa`, `sexo`, `n_seguranca_social`, `desconto_seguranca_social`, `data_inicio_contrato`, `data_fim_contrato`, `telefone_1`, `tipo_contrato`, `duracao_contrato`, `data_contrato`, `conta_fechada`, `motivo_fecho`, `photo`, `telefone_2`, `forma_pagamento`, `activo`, `data_validade_bi`, `nbi`, `fk_forma_pagamento`) VALUES (NEW.`idFuncionario`, NEW.`numero_funcionario`, NEW.`nome`, NEW.`telefone`, NEW.`morada`, NEW.`user_name`, NEW.`password`, NEW.`habilitacao_literaria`, NEW.`dias_instituicao`, NEW.`fk_funcao`, NEW.`fk_departamento`, NEW.`fk_grau_academico`, NEW.`fk_especialidade`, NEW.`nif`, NEW.`data_nascimento`, NEW.`docID`, NEW.`ndocID`, NEW.`data_emissao_docID`, NEW.`data_validade_docID`, NEW.`fk_estado_civil`, NEW.`idStatusFK`, NEW.`fkUsuario`, NEW.`fk_modalidade`, NEW.`fk_empresa`, NEW.`sexo`, NEW.`n_seguranca_social`, NEW.`desconto_seguranca_social`, NEW.`data_inicio_contrato`, NEW.`data_fim_contrato`, NEW.`telefone_1`, NEW.`tipo_contrato`, NEW.`duracao_contrato`, NEW.`data_contrato`, NEW.`conta_fechada`, NEW.`motivo_fecho`, NEW.`photo`, NEW.`telefone_2`, NEW.`forma_pagamento`, NEW.`activo`, NEW.`data_validade_bi`, NEW.`nbi`, NEW.`fk_forma_pagamento`) ON DUPLICATE KEY UPDATE `idFuncionario` = VALUES(`idFuncionario`), `numero_funcionario` = VALUES(`numero_funcionario`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `morada` = VALUES(`morada`), `user_name` = VALUES(`user_name`), `password` = VALUES(`password`), `habilitacao_literaria` = VALUES(`habilitacao_literaria`), `dias_instituicao` = VALUES(`dias_instituicao`), `fk_funcao` = VALUES(`fk_funcao`), `fk_departamento` = VALUES(`fk_departamento`), `fk_grau_academico` = VALUES(`fk_grau_academico`), `fk_especialidade` = VALUES(`fk_especialidade`), `nif` = VALUES(`nif`), `data_nascimento` = VALUES(`data_nascimento`), `docID` = VALUES(`docID`), `ndocID` = VALUES(`ndocID`), `data_emissao_docID` = VALUES(`data_emissao_docID`), `data_validade_docID` = VALUES(`data_validade_docID`), `fk_estado_civil` = VALUES(`fk_estado_civil`), `idStatusFK` = VALUES(`idStatusFK`), `fkUsuario` = VALUES(`fkUsuario`), `fk_modalidade` = VALUES(`fk_modalidade`), `fk_empresa` = VALUES(`fk_empresa`), `sexo` = VALUES(`sexo`), `n_seguranca_social` = VALUES(`n_seguranca_social`), `desconto_seguranca_social` = VALUES(`desconto_seguranca_social`), `data_inicio_contrato` = VALUES(`data_inicio_contrato`), `data_fim_contrato` = VALUES(`data_fim_contrato`), `telefone_1` = VALUES(`telefone_1`), `tipo_contrato` = VALUES(`tipo_contrato`), `duracao_contrato` = VALUES(`duracao_contrato`), `data_contrato` = VALUES(`data_contrato`), `conta_fechada` = VALUES(`conta_fechada`), `motivo_fecho` = VALUES(`motivo_fecho`), `photo` = VALUES(`photo`), `telefone_2` = VALUES(`telefone_2`), `forma_pagamento` = VALUES(`forma_pagamento`), `activo` = VALUES(`activo`), `data_validade_bi` = VALUES(`data_validade_bi`), `nbi` = VALUES(`nbi`), `fk_forma_pagamento` = VALUES(`fk_forma_pagamento`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_funcionario_au` AFTER UPDATE ON `kitanda_db_1`.`tb_funcionario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_funcionario` SET `idFuncionario` = NEW.`idFuncionario`, `numero_funcionario` = NEW.`numero_funcionario`, `nome` = NEW.`nome`, `telefone` = NEW.`telefone`, `morada` = NEW.`morada`, `user_name` = NEW.`user_name`, `password` = NEW.`password`, `habilitacao_literaria` = NEW.`habilitacao_literaria`, `dias_instituicao` = NEW.`dias_instituicao`, `fk_funcao` = NEW.`fk_funcao`, `fk_departamento` = NEW.`fk_departamento`, `fk_grau_academico` = NEW.`fk_grau_academico`, `fk_especialidade` = NEW.`fk_especialidade`, `nif` = NEW.`nif`, `data_nascimento` = NEW.`data_nascimento`, `docID` = NEW.`docID`, `ndocID` = NEW.`ndocID`, `data_emissao_docID` = NEW.`data_emissao_docID`, `data_validade_docID` = NEW.`data_validade_docID`, `fk_estado_civil` = NEW.`fk_estado_civil`, `idStatusFK` = NEW.`idStatusFK`, `fkUsuario` = NEW.`fkUsuario`, `fk_modalidade` = NEW.`fk_modalidade`, `fk_empresa` = NEW.`fk_empresa`, `sexo` = NEW.`sexo`, `n_seguranca_social` = NEW.`n_seguranca_social`, `desconto_seguranca_social` = NEW.`desconto_seguranca_social`, `data_inicio_contrato` = NEW.`data_inicio_contrato`, `data_fim_contrato` = NEW.`data_fim_contrato`, `telefone_1` = NEW.`telefone_1`, `tipo_contrato` = NEW.`tipo_contrato`, `duracao_contrato` = NEW.`duracao_contrato`, `data_contrato` = NEW.`data_contrato`, `conta_fechada` = NEW.`conta_fechada`, `motivo_fecho` = NEW.`motivo_fecho`, `photo` = NEW.`photo`, `telefone_2` = NEW.`telefone_2`, `forma_pagamento` = NEW.`forma_pagamento`, `activo` = NEW.`activo`, `data_validade_bi` = NEW.`data_validade_bi`, `nbi` = NEW.`nbi`, `fk_forma_pagamento` = NEW.`fk_forma_pagamento` WHERE `idFuncionario` = OLD.`idFuncionario`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_funcionario` (`idFuncionario`, `numero_funcionario`, `nome`, `telefone`, `morada`, `user_name`, `password`, `habilitacao_literaria`, `dias_instituicao`, `fk_funcao`, `fk_departamento`, `fk_grau_academico`, `fk_especialidade`, `nif`, `data_nascimento`, `docID`, `ndocID`, `data_emissao_docID`, `data_validade_docID`, `fk_estado_civil`, `idStatusFK`, `fkUsuario`, `fk_modalidade`, `fk_empresa`, `sexo`, `n_seguranca_social`, `desconto_seguranca_social`, `data_inicio_contrato`, `data_fim_contrato`, `telefone_1`, `tipo_contrato`, `duracao_contrato`, `data_contrato`, `conta_fechada`, `motivo_fecho`, `photo`, `telefone_2`, `forma_pagamento`, `activo`, `data_validade_bi`, `nbi`, `fk_forma_pagamento`) VALUES (NEW.`idFuncionario`, NEW.`numero_funcionario`, NEW.`nome`, NEW.`telefone`, NEW.`morada`, NEW.`user_name`, NEW.`password`, NEW.`habilitacao_literaria`, NEW.`dias_instituicao`, NEW.`fk_funcao`, NEW.`fk_departamento`, NEW.`fk_grau_academico`, NEW.`fk_especialidade`, NEW.`nif`, NEW.`data_nascimento`, NEW.`docID`, NEW.`ndocID`, NEW.`data_emissao_docID`, NEW.`data_validade_docID`, NEW.`fk_estado_civil`, NEW.`idStatusFK`, NEW.`fkUsuario`, NEW.`fk_modalidade`, NEW.`fk_empresa`, NEW.`sexo`, NEW.`n_seguranca_social`, NEW.`desconto_seguranca_social`, NEW.`data_inicio_contrato`, NEW.`data_fim_contrato`, NEW.`telefone_1`, NEW.`tipo_contrato`, NEW.`duracao_contrato`, NEW.`data_contrato`, NEW.`conta_fechada`, NEW.`motivo_fecho`, NEW.`photo`, NEW.`telefone_2`, NEW.`forma_pagamento`, NEW.`activo`, NEW.`data_validade_bi`, NEW.`nbi`, NEW.`fk_forma_pagamento`) ON DUPLICATE KEY UPDATE `idFuncionario` = VALUES(`idFuncionario`), `numero_funcionario` = VALUES(`numero_funcionario`), `nome` = VALUES(`nome`), `telefone` = VALUES(`telefone`), `morada` = VALUES(`morada`), `user_name` = VALUES(`user_name`), `password` = VALUES(`password`), `habilitacao_literaria` = VALUES(`habilitacao_literaria`), `dias_instituicao` = VALUES(`dias_instituicao`), `fk_funcao` = VALUES(`fk_funcao`), `fk_departamento` = VALUES(`fk_departamento`), `fk_grau_academico` = VALUES(`fk_grau_academico`), `fk_especialidade` = VALUES(`fk_especialidade`), `nif` = VALUES(`nif`), `data_nascimento` = VALUES(`data_nascimento`), `docID` = VALUES(`docID`), `ndocID` = VALUES(`ndocID`), `data_emissao_docID` = VALUES(`data_emissao_docID`), `data_validade_docID` = VALUES(`data_validade_docID`), `fk_estado_civil` = VALUES(`fk_estado_civil`), `idStatusFK` = VALUES(`idStatusFK`), `fkUsuario` = VALUES(`fkUsuario`), `fk_modalidade` = VALUES(`fk_modalidade`), `fk_empresa` = VALUES(`fk_empresa`), `sexo` = VALUES(`sexo`), `n_seguranca_social` = VALUES(`n_seguranca_social`), `desconto_seguranca_social` = VALUES(`desconto_seguranca_social`), `data_inicio_contrato` = VALUES(`data_inicio_contrato`), `data_fim_contrato` = VALUES(`data_fim_contrato`), `telefone_1` = VALUES(`telefone_1`), `tipo_contrato` = VALUES(`tipo_contrato`), `duracao_contrato` = VALUES(`duracao_contrato`), `data_contrato` = VALUES(`data_contrato`), `conta_fechada` = VALUES(`conta_fechada`), `motivo_fecho` = VALUES(`motivo_fecho`), `photo` = VALUES(`photo`), `telefone_2` = VALUES(`telefone_2`), `forma_pagamento` = VALUES(`forma_pagamento`), `activo` = VALUES(`activo`), `data_validade_bi` = VALUES(`data_validade_bi`), `nbi` = VALUES(`nbi`), `fk_forma_pagamento` = VALUES(`fk_forma_pagamento`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_funcionario_ad` AFTER DELETE ON `kitanda_db_1`.`tb_funcionario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_funcionario` WHERE `idFuncionario` = OLD.`idFuncionario`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_grau_academico_ai` AFTER INSERT ON `kitanda_db_1`.`tb_grau_academico` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_grau_academico` (`pk_grau_academico`, `designacao`) VALUES (NEW.`pk_grau_academico`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_grau_academico` = VALUES(`pk_grau_academico`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_grau_academico_au` AFTER UPDATE ON `kitanda_db_1`.`tb_grau_academico` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_grau_academico` SET `pk_grau_academico` = NEW.`pk_grau_academico`, `designacao` = NEW.`designacao` WHERE `pk_grau_academico` = OLD.`pk_grau_academico`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_grau_academico` (`pk_grau_academico`, `designacao`) VALUES (NEW.`pk_grau_academico`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_grau_academico` = VALUES(`pk_grau_academico`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_grau_academico_ad` AFTER DELETE ON `kitanda_db_1`.`tb_grau_academico` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_grau_academico` WHERE `pk_grau_academico` = OLD.`pk_grau_academico`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_desconto_funcionario_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_desconto_funcionario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_desconto_funcionario` (`idItemDescontoFuncionarioPK`, `valor_desconto`, `idTipoDescontoFK`, `idFuncionarioFK`) VALUES (NEW.`idItemDescontoFuncionarioPK`, NEW.`valor_desconto`, NEW.`idTipoDescontoFK`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idItemDescontoFuncionarioPK` = VALUES(`idItemDescontoFuncionarioPK`), `valor_desconto` = VALUES(`valor_desconto`), `idTipoDescontoFK` = VALUES(`idTipoDescontoFK`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_desconto_funcionario_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_desconto_funcionario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_desconto_funcionario` SET `idItemDescontoFuncionarioPK` = NEW.`idItemDescontoFuncionarioPK`, `valor_desconto` = NEW.`valor_desconto`, `idTipoDescontoFK` = NEW.`idTipoDescontoFK`, `idFuncionarioFK` = NEW.`idFuncionarioFK` WHERE `idItemDescontoFuncionarioPK` = OLD.`idItemDescontoFuncionarioPK`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_desconto_funcionario` (`idItemDescontoFuncionarioPK`, `valor_desconto`, `idTipoDescontoFK`, `idFuncionarioFK`) VALUES (NEW.`idItemDescontoFuncionarioPK`, NEW.`valor_desconto`, NEW.`idTipoDescontoFK`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idItemDescontoFuncionarioPK` = VALUES(`idItemDescontoFuncionarioPK`), `valor_desconto` = VALUES(`valor_desconto`), `idTipoDescontoFK` = VALUES(`idTipoDescontoFK`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_desconto_funcionario_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_desconto_funcionario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_desconto_funcionario` WHERE `idItemDescontoFuncionarioPK` = OLD.`idItemDescontoFuncionarioPK`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_encomenda_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_encomenda` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_encomenda` (`idEncomenda`, `idProduto`, `total`, `quantidade`, `codigo`) VALUES (NEW.`idEncomenda`, NEW.`idProduto`, NEW.`total`, NEW.`quantidade`, NEW.`codigo`) ON DUPLICATE KEY UPDATE `idEncomenda` = VALUES(`idEncomenda`), `idProduto` = VALUES(`idProduto`), `total` = VALUES(`total`), `quantidade` = VALUES(`quantidade`), `codigo` = VALUES(`codigo`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_encomenda_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_encomenda` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_encomenda` SET `idEncomenda` = NEW.`idEncomenda`, `idProduto` = NEW.`idProduto`, `total` = NEW.`total`, `quantidade` = NEW.`quantidade`, `codigo` = NEW.`codigo` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_encomenda` (`idEncomenda`, `idProduto`, `total`, `quantidade`, `codigo`) VALUES (NEW.`idEncomenda`, NEW.`idProduto`, NEW.`total`, NEW.`quantidade`, NEW.`codigo`) ON DUPLICATE KEY UPDATE `idEncomenda` = VALUES(`idEncomenda`), `idProduto` = VALUES(`idProduto`), `total` = VALUES(`total`), `quantidade` = VALUES(`quantidade`), `codigo` = VALUES(`codigo`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_encomenda_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_encomenda` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_encomenda` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_entrada_vasilhame_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_entrada_vasilhame` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_entrada_vasilhame` (`pk_item_entrada_vasilhame`, `qtd`, `fk_vasilhame`, `fk_entrada_vasilhame`) VALUES (NEW.`pk_item_entrada_vasilhame`, NEW.`qtd`, NEW.`fk_vasilhame`, NEW.`fk_entrada_vasilhame`) ON DUPLICATE KEY UPDATE `pk_item_entrada_vasilhame` = VALUES(`pk_item_entrada_vasilhame`), `qtd` = VALUES(`qtd`), `fk_vasilhame` = VALUES(`fk_vasilhame`), `fk_entrada_vasilhame` = VALUES(`fk_entrada_vasilhame`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_entrada_vasilhame_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_entrada_vasilhame` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_entrada_vasilhame` SET `pk_item_entrada_vasilhame` = NEW.`pk_item_entrada_vasilhame`, `qtd` = NEW.`qtd`, `fk_vasilhame` = NEW.`fk_vasilhame`, `fk_entrada_vasilhame` = NEW.`fk_entrada_vasilhame` WHERE `pk_item_entrada_vasilhame` = OLD.`pk_item_entrada_vasilhame`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_entrada_vasilhame` (`pk_item_entrada_vasilhame`, `qtd`, `fk_vasilhame`, `fk_entrada_vasilhame`) VALUES (NEW.`pk_item_entrada_vasilhame`, NEW.`qtd`, NEW.`fk_vasilhame`, NEW.`fk_entrada_vasilhame`) ON DUPLICATE KEY UPDATE `pk_item_entrada_vasilhame` = VALUES(`pk_item_entrada_vasilhame`), `qtd` = VALUES(`qtd`), `fk_vasilhame` = VALUES(`fk_vasilhame`), `fk_entrada_vasilhame` = VALUES(`fk_entrada_vasilhame`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_entrada_vasilhame_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_entrada_vasilhame` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_entrada_vasilhame` WHERE `pk_item_entrada_vasilhame` = OLD.`pk_item_entrada_vasilhame`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_entradas_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_entradas` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_entradas` (`id_item_entradas`, `id_produto`, `quantidade`, `fk_entradas`) VALUES (NEW.`id_item_entradas`, NEW.`id_produto`, NEW.`quantidade`, NEW.`fk_entradas`) ON DUPLICATE KEY UPDATE `id_item_entradas` = VALUES(`id_item_entradas`), `id_produto` = VALUES(`id_produto`), `quantidade` = VALUES(`quantidade`), `fk_entradas` = VALUES(`fk_entradas`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_entradas_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_entradas` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_entradas` SET `id_item_entradas` = NEW.`id_item_entradas`, `id_produto` = NEW.`id_produto`, `quantidade` = NEW.`quantidade`, `fk_entradas` = NEW.`fk_entradas` WHERE `id_item_entradas` = OLD.`id_item_entradas`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_entradas` (`id_item_entradas`, `id_produto`, `quantidade`, `fk_entradas`) VALUES (NEW.`id_item_entradas`, NEW.`id_produto`, NEW.`quantidade`, NEW.`fk_entradas`) ON DUPLICATE KEY UPDATE `id_item_entradas` = VALUES(`id_item_entradas`), `id_produto` = VALUES(`id_produto`), `quantidade` = VALUES(`quantidade`), `fk_entradas` = VALUES(`fk_entradas`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_entradas_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_entradas` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_entradas` WHERE `id_item_entradas` = OLD.`id_item_entradas`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_estorno_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_estorno` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_estorno` (`codigo`, `fk_estorno`, `fk_produtos`, `quantidade`) VALUES (NEW.`codigo`, NEW.`fk_estorno`, NEW.`fk_produtos`, NEW.`quantidade`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `fk_estorno` = VALUES(`fk_estorno`), `fk_produtos` = VALUES(`fk_produtos`), `quantidade` = VALUES(`quantidade`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_estorno_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_estorno` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_estorno` SET `codigo` = NEW.`codigo`, `fk_estorno` = NEW.`fk_estorno`, `fk_produtos` = NEW.`fk_produtos`, `quantidade` = NEW.`quantidade` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_estorno` (`codigo`, `fk_estorno`, `fk_produtos`, `quantidade`) VALUES (NEW.`codigo`, NEW.`fk_estorno`, NEW.`fk_produtos`, NEW.`quantidade`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `fk_estorno` = VALUES(`fk_estorno`), `fk_produtos` = VALUES(`fk_produtos`), `quantidade` = VALUES(`quantidade`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_estorno_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_estorno` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_estorno` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_pedidos_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_pedidos` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_pedidos` (`pk_item_pedidos`, `fk_lugares`, `fk_produtos`, `qtd`, `fk_pedidos`, `status_convertido`, `total_item`, `status_enviado`, `status_efectuado`, `data_entrega`, `obs`, `preco`) VALUES (NEW.`pk_item_pedidos`, NEW.`fk_lugares`, NEW.`fk_produtos`, NEW.`qtd`, NEW.`fk_pedidos`, NEW.`status_convertido`, NEW.`total_item`, NEW.`status_enviado`, NEW.`status_efectuado`, NEW.`data_entrega`, NEW.`obs`, NEW.`preco`) ON DUPLICATE KEY UPDATE `pk_item_pedidos` = VALUES(`pk_item_pedidos`), `fk_lugares` = VALUES(`fk_lugares`), `fk_produtos` = VALUES(`fk_produtos`), `qtd` = VALUES(`qtd`), `fk_pedidos` = VALUES(`fk_pedidos`), `status_convertido` = VALUES(`status_convertido`), `total_item` = VALUES(`total_item`), `status_enviado` = VALUES(`status_enviado`), `status_efectuado` = VALUES(`status_efectuado`), `data_entrega` = VALUES(`data_entrega`), `obs` = VALUES(`obs`), `preco` = VALUES(`preco`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_pedidos_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_pedidos` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_pedidos` SET `pk_item_pedidos` = NEW.`pk_item_pedidos`, `fk_lugares` = NEW.`fk_lugares`, `fk_produtos` = NEW.`fk_produtos`, `qtd` = NEW.`qtd`, `fk_pedidos` = NEW.`fk_pedidos`, `status_convertido` = NEW.`status_convertido`, `total_item` = NEW.`total_item`, `status_enviado` = NEW.`status_enviado`, `status_efectuado` = NEW.`status_efectuado`, `data_entrega` = NEW.`data_entrega`, `obs` = NEW.`obs`, `preco` = NEW.`preco` WHERE `pk_item_pedidos` = OLD.`pk_item_pedidos`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_pedidos` (`pk_item_pedidos`, `fk_lugares`, `fk_produtos`, `qtd`, `fk_pedidos`, `status_convertido`, `total_item`, `status_enviado`, `status_efectuado`, `data_entrega`, `obs`, `preco`) VALUES (NEW.`pk_item_pedidos`, NEW.`fk_lugares`, NEW.`fk_produtos`, NEW.`qtd`, NEW.`fk_pedidos`, NEW.`status_convertido`, NEW.`total_item`, NEW.`status_enviado`, NEW.`status_efectuado`, NEW.`data_entrega`, NEW.`obs`, NEW.`preco`) ON DUPLICATE KEY UPDATE `pk_item_pedidos` = VALUES(`pk_item_pedidos`), `fk_lugares` = VALUES(`fk_lugares`), `fk_produtos` = VALUES(`fk_produtos`), `qtd` = VALUES(`qtd`), `fk_pedidos` = VALUES(`fk_pedidos`), `status_convertido` = VALUES(`status_convertido`), `total_item` = VALUES(`total_item`), `status_enviado` = VALUES(`status_enviado`), `status_efectuado` = VALUES(`status_efectuado`), `data_entrega` = VALUES(`data_entrega`), `obs` = VALUES(`obs`), `preco` = VALUES(`preco`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_pedidos_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_pedidos` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_pedidos` WHERE `pk_item_pedidos` = OLD.`pk_item_pedidos`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_permissao_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_permissao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_permissao` (`idItemPermissao`, `idUsuario`, `idPermissao`, `status`, `modulo`) VALUES (NEW.`idItemPermissao`, NEW.`idUsuario`, NEW.`idPermissao`, NEW.`status`, NEW.`modulo`) ON DUPLICATE KEY UPDATE `idItemPermissao` = VALUES(`idItemPermissao`), `idUsuario` = VALUES(`idUsuario`), `idPermissao` = VALUES(`idPermissao`), `status` = VALUES(`status`), `modulo` = VALUES(`modulo`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_permissao_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_permissao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_permissao` SET `idItemPermissao` = NEW.`idItemPermissao`, `idUsuario` = NEW.`idUsuario`, `idPermissao` = NEW.`idPermissao`, `status` = NEW.`status`, `modulo` = NEW.`modulo` WHERE `idItemPermissao` = OLD.`idItemPermissao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_permissao` (`idItemPermissao`, `idUsuario`, `idPermissao`, `status`, `modulo`) VALUES (NEW.`idItemPermissao`, NEW.`idUsuario`, NEW.`idPermissao`, NEW.`status`, NEW.`modulo`) ON DUPLICATE KEY UPDATE `idItemPermissao` = VALUES(`idItemPermissao`), `idUsuario` = VALUES(`idUsuario`), `idPermissao` = VALUES(`idPermissao`), `status` = VALUES(`status`), `modulo` = VALUES(`modulo`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_permissao_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_permissao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_permissao` WHERE `idItemPermissao` = OLD.`idItemPermissao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_pro_forma_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_pro_forma` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_pro_forma` (`pk_item_pro_forma`, `qtd`, `fk_produto`, `fk_pro_forma`, `fk_preco`) VALUES (NEW.`pk_item_pro_forma`, NEW.`qtd`, NEW.`fk_produto`, NEW.`fk_pro_forma`, NEW.`fk_preco`) ON DUPLICATE KEY UPDATE `pk_item_pro_forma` = VALUES(`pk_item_pro_forma`), `qtd` = VALUES(`qtd`), `fk_produto` = VALUES(`fk_produto`), `fk_pro_forma` = VALUES(`fk_pro_forma`), `fk_preco` = VALUES(`fk_preco`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_pro_forma_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_pro_forma` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_pro_forma` SET `pk_item_pro_forma` = NEW.`pk_item_pro_forma`, `qtd` = NEW.`qtd`, `fk_produto` = NEW.`fk_produto`, `fk_pro_forma` = NEW.`fk_pro_forma`, `fk_preco` = NEW.`fk_preco` WHERE `pk_item_pro_forma` = OLD.`pk_item_pro_forma`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_pro_forma` (`pk_item_pro_forma`, `qtd`, `fk_produto`, `fk_pro_forma`, `fk_preco`) VALUES (NEW.`pk_item_pro_forma`, NEW.`qtd`, NEW.`fk_produto`, NEW.`fk_pro_forma`, NEW.`fk_preco`) ON DUPLICATE KEY UPDATE `pk_item_pro_forma` = VALUES(`pk_item_pro_forma`), `qtd` = VALUES(`qtd`), `fk_produto` = VALUES(`fk_produto`), `fk_pro_forma` = VALUES(`fk_pro_forma`), `fk_preco` = VALUES(`fk_preco`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_pro_forma_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_pro_forma` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_pro_forma` WHERE `pk_item_pro_forma` = OLD.`pk_item_pro_forma`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_saida_vasilhame_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_saida_vasilhame` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_saida_vasilhame` (`pk_item_saida_vasilhame`, `fk_vasihame`, `fk_saida_vazilhame`, `qtd`) VALUES (NEW.`pk_item_saida_vasilhame`, NEW.`fk_vasihame`, NEW.`fk_saida_vazilhame`, NEW.`qtd`) ON DUPLICATE KEY UPDATE `pk_item_saida_vasilhame` = VALUES(`pk_item_saida_vasilhame`), `fk_vasihame` = VALUES(`fk_vasihame`), `fk_saida_vazilhame` = VALUES(`fk_saida_vazilhame`), `qtd` = VALUES(`qtd`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_saida_vasilhame_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_saida_vasilhame` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_saida_vasilhame` SET `pk_item_saida_vasilhame` = NEW.`pk_item_saida_vasilhame`, `fk_vasihame` = NEW.`fk_vasihame`, `fk_saida_vazilhame` = NEW.`fk_saida_vazilhame`, `qtd` = NEW.`qtd` WHERE `pk_item_saida_vasilhame` = OLD.`pk_item_saida_vasilhame`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_saida_vasilhame` (`pk_item_saida_vasilhame`, `fk_vasihame`, `fk_saida_vazilhame`, `qtd`) VALUES (NEW.`pk_item_saida_vasilhame`, NEW.`fk_vasihame`, NEW.`fk_saida_vazilhame`, NEW.`qtd`) ON DUPLICATE KEY UPDATE `pk_item_saida_vasilhame` = VALUES(`pk_item_saida_vasilhame`), `fk_vasihame` = VALUES(`fk_vasihame`), `fk_saida_vazilhame` = VALUES(`fk_saida_vazilhame`), `qtd` = VALUES(`qtd`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_saida_vasilhame_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_saida_vasilhame` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_saida_vasilhame` WHERE `pk_item_saida_vasilhame` = OLD.`pk_item_saida_vasilhame`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_saidas_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_saidas` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_saidas` (`codigo`, `fk_saidas_produtos`, `fk_produtos`, `quantidade`, `preco_compra`, `factor_conversao`, `quantidade_retalho`) VALUES (NEW.`codigo`, NEW.`fk_saidas_produtos`, NEW.`fk_produtos`, NEW.`quantidade`, NEW.`preco_compra`, NEW.`factor_conversao`, NEW.`quantidade_retalho`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `fk_saidas_produtos` = VALUES(`fk_saidas_produtos`), `fk_produtos` = VALUES(`fk_produtos`), `quantidade` = VALUES(`quantidade`), `preco_compra` = VALUES(`preco_compra`), `factor_conversao` = VALUES(`factor_conversao`), `quantidade_retalho` = VALUES(`quantidade_retalho`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_saidas_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_saidas` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_saidas` SET `codigo` = NEW.`codigo`, `fk_saidas_produtos` = NEW.`fk_saidas_produtos`, `fk_produtos` = NEW.`fk_produtos`, `quantidade` = NEW.`quantidade`, `preco_compra` = NEW.`preco_compra`, `factor_conversao` = NEW.`factor_conversao`, `quantidade_retalho` = NEW.`quantidade_retalho` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_saidas` (`codigo`, `fk_saidas_produtos`, `fk_produtos`, `quantidade`, `preco_compra`, `factor_conversao`, `quantidade_retalho`) VALUES (NEW.`codigo`, NEW.`fk_saidas_produtos`, NEW.`fk_produtos`, NEW.`quantidade`, NEW.`preco_compra`, NEW.`factor_conversao`, NEW.`quantidade_retalho`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `fk_saidas_produtos` = VALUES(`fk_saidas_produtos`), `fk_produtos` = VALUES(`fk_produtos`), `quantidade` = VALUES(`quantidade`), `preco_compra` = VALUES(`preco_compra`), `factor_conversao` = VALUES(`factor_conversao`), `quantidade_retalho` = VALUES(`quantidade_retalho`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_saidas_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_saidas` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_saidas` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_subsidios_funcionario_ai` AFTER INSERT ON `kitanda_db_1`.`tb_item_subsidios_funcionario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_item_subsidios_funcionario` (`idItemSubsidiosFuncionario`, `valor_subsidio`, `idSubsidioFK`, `idFuncionarioFK`) VALUES (NEW.`idItemSubsidiosFuncionario`, NEW.`valor_subsidio`, NEW.`idSubsidioFK`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idItemSubsidiosFuncionario` = VALUES(`idItemSubsidiosFuncionario`), `valor_subsidio` = VALUES(`valor_subsidio`), `idSubsidioFK` = VALUES(`idSubsidioFK`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_subsidios_funcionario_au` AFTER UPDATE ON `kitanda_db_1`.`tb_item_subsidios_funcionario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_item_subsidios_funcionario` SET `idItemSubsidiosFuncionario` = NEW.`idItemSubsidiosFuncionario`, `valor_subsidio` = NEW.`valor_subsidio`, `idSubsidioFK` = NEW.`idSubsidioFK`, `idFuncionarioFK` = NEW.`idFuncionarioFK` WHERE `idItemSubsidiosFuncionario` = OLD.`idItemSubsidiosFuncionario`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_item_subsidios_funcionario` (`idItemSubsidiosFuncionario`, `valor_subsidio`, `idSubsidioFK`, `idFuncionarioFK`) VALUES (NEW.`idItemSubsidiosFuncionario`, NEW.`valor_subsidio`, NEW.`idSubsidioFK`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idItemSubsidiosFuncionario` = VALUES(`idItemSubsidiosFuncionario`), `valor_subsidio` = VALUES(`valor_subsidio`), `idSubsidioFK` = VALUES(`idSubsidioFK`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_item_subsidios_funcionario_ad` AFTER DELETE ON `kitanda_db_1`.`tb_item_subsidios_funcionario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_item_subsidios_funcionario` WHERE `idItemSubsidiosFuncionario` = OLD.`idItemSubsidiosFuncionario`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_local_ai` AFTER INSERT ON `kitanda_db_1`.`tb_local` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_local` (`codigo`, `designacao`) VALUES (NEW.`codigo`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_local_au` AFTER UPDATE ON `kitanda_db_1`.`tb_local` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_local` SET `codigo` = NEW.`codigo`, `designacao` = NEW.`designacao` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_local` (`codigo`, `designacao`) VALUES (NEW.`codigo`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_local_ad` AFTER DELETE ON `kitanda_db_1`.`tb_local` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_local` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_lugares_ai` AFTER INSERT ON `kitanda_db_1`.`tb_lugares` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_lugares` (`pk_lugares`, `designacao`) VALUES (NEW.`pk_lugares`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_lugares` = VALUES(`pk_lugares`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_lugares_au` AFTER UPDATE ON `kitanda_db_1`.`tb_lugares` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_lugares` SET `pk_lugares` = NEW.`pk_lugares`, `designacao` = NEW.`designacao` WHERE `pk_lugares` = OLD.`pk_lugares`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_lugares` (`pk_lugares`, `designacao`) VALUES (NEW.`pk_lugares`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_lugares` = VALUES(`pk_lugares`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_lugares_ad` AFTER DELETE ON `kitanda_db_1`.`tb_lugares` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_lugares` WHERE `pk_lugares` = OLD.`pk_lugares`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_mes_rh_ai` AFTER INSERT ON `kitanda_db_1`.`tb_mes_rh` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_mes_rh` (`pk_mes_rh`, `descricao`) VALUES (NEW.`pk_mes_rh`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_mes_rh` = VALUES(`pk_mes_rh`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_mes_rh_au` AFTER UPDATE ON `kitanda_db_1`.`tb_mes_rh` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_mes_rh` SET `pk_mes_rh` = NEW.`pk_mes_rh`, `descricao` = NEW.`descricao` WHERE `pk_mes_rh` = OLD.`pk_mes_rh`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_mes_rh` (`pk_mes_rh`, `descricao`) VALUES (NEW.`pk_mes_rh`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `pk_mes_rh` = VALUES(`pk_mes_rh`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_mes_rh_ad` AFTER DELETE ON `kitanda_db_1`.`tb_mes_rh` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_mes_rh` WHERE `pk_mes_rh` = OLD.`pk_mes_rh`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_mesas_ai` AFTER INSERT ON `kitanda_db_1`.`tb_mesas` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_mesas` (`pk_mesas`, `designacao`) VALUES (NEW.`pk_mesas`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_mesas` = VALUES(`pk_mesas`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_mesas_au` AFTER UPDATE ON `kitanda_db_1`.`tb_mesas` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_mesas` SET `pk_mesas` = NEW.`pk_mesas`, `designacao` = NEW.`designacao` WHERE `pk_mesas` = OLD.`pk_mesas`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_mesas` (`pk_mesas`, `designacao`) VALUES (NEW.`pk_mesas`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_mesas` = VALUES(`pk_mesas`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_mesas_ad` AFTER DELETE ON `kitanda_db_1`.`tb_mesas` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_mesas` WHERE `pk_mesas` = OLD.`pk_mesas`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_municipio_ai` AFTER INSERT ON `kitanda_db_1`.`tb_municipio` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_municipio` (`idMunicipio`, `descrisao`, `idProvincia`) VALUES (NEW.`idMunicipio`, NEW.`descrisao`, NEW.`idProvincia`) ON DUPLICATE KEY UPDATE `idMunicipio` = VALUES(`idMunicipio`), `descrisao` = VALUES(`descrisao`), `idProvincia` = VALUES(`idProvincia`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_municipio_au` AFTER UPDATE ON `kitanda_db_1`.`tb_municipio` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_municipio` SET `idMunicipio` = NEW.`idMunicipio`, `descrisao` = NEW.`descrisao`, `idProvincia` = NEW.`idProvincia` WHERE `idMunicipio` = OLD.`idMunicipio`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_municipio` (`idMunicipio`, `descrisao`, `idProvincia`) VALUES (NEW.`idMunicipio`, NEW.`descrisao`, NEW.`idProvincia`) ON DUPLICATE KEY UPDATE `idMunicipio` = VALUES(`idMunicipio`), `descrisao` = VALUES(`descrisao`), `idProvincia` = VALUES(`idProvincia`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_municipio_ad` AFTER DELETE ON `kitanda_db_1`.`tb_municipio` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_municipio` WHERE `idMunicipio` = OLD.`idMunicipio`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_operacao_sistema_ai` AFTER INSERT ON `kitanda_db_1`.`tb_operacao_sistema` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_operacao_sistema` (`pk_operacao_sistema`, `data_abertura`, `hora_abertura`, `data_feicho`, `hora_feicho`, `fk_usuario`) VALUES (NEW.`pk_operacao_sistema`, NEW.`data_abertura`, NEW.`hora_abertura`, NEW.`data_feicho`, NEW.`hora_feicho`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_operacao_sistema` = VALUES(`pk_operacao_sistema`), `data_abertura` = VALUES(`data_abertura`), `hora_abertura` = VALUES(`hora_abertura`), `data_feicho` = VALUES(`data_feicho`), `hora_feicho` = VALUES(`hora_feicho`), `fk_usuario` = VALUES(`fk_usuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_operacao_sistema_au` AFTER UPDATE ON `kitanda_db_1`.`tb_operacao_sistema` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_operacao_sistema` SET `pk_operacao_sistema` = NEW.`pk_operacao_sistema`, `data_abertura` = NEW.`data_abertura`, `hora_abertura` = NEW.`hora_abertura`, `data_feicho` = NEW.`data_feicho`, `hora_feicho` = NEW.`hora_feicho`, `fk_usuario` = NEW.`fk_usuario` WHERE `pk_operacao_sistema` = OLD.`pk_operacao_sistema`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_operacao_sistema` (`pk_operacao_sistema`, `data_abertura`, `hora_abertura`, `data_feicho`, `hora_feicho`, `fk_usuario`) VALUES (NEW.`pk_operacao_sistema`, NEW.`data_abertura`, NEW.`hora_abertura`, NEW.`data_feicho`, NEW.`hora_feicho`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_operacao_sistema` = VALUES(`pk_operacao_sistema`), `data_abertura` = VALUES(`data_abertura`), `hora_abertura` = VALUES(`hora_abertura`), `data_feicho` = VALUES(`data_feicho`), `hora_feicho` = VALUES(`hora_feicho`), `fk_usuario` = VALUES(`fk_usuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_operacao_sistema_ad` AFTER DELETE ON `kitanda_db_1`.`tb_operacao_sistema` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_operacao_sistema` WHERE `pk_operacao_sistema` = OLD.`pk_operacao_sistema`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pais_ai` AFTER INSERT ON `kitanda_db_1`.`tb_pais` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_pais` (`idPais`, `descrisao`) VALUES (NEW.`idPais`, NEW.`descrisao`) ON DUPLICATE KEY UPDATE `idPais` = VALUES(`idPais`), `descrisao` = VALUES(`descrisao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pais_au` AFTER UPDATE ON `kitanda_db_1`.`tb_pais` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_pais` SET `idPais` = NEW.`idPais`, `descrisao` = NEW.`descrisao` WHERE `idPais` = OLD.`idPais`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_pais` (`idPais`, `descrisao`) VALUES (NEW.`idPais`, NEW.`descrisao`) ON DUPLICATE KEY UPDATE `idPais` = VALUES(`idPais`), `descrisao` = VALUES(`descrisao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pais_ad` AFTER DELETE ON `kitanda_db_1`.`tb_pais` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_pais` WHERE `idPais` = OLD.`idPais`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_parametros_ai` AFTER INSERT ON `kitanda_db_1`.`tb_parametros` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_parametros` (`codigo`, `Designacao`) VALUES (NEW.`codigo`, NEW.`Designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `Designacao` = VALUES(`Designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_parametros_au` AFTER UPDATE ON `kitanda_db_1`.`tb_parametros` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_parametros` SET `codigo` = NEW.`codigo`, `Designacao` = NEW.`Designacao` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_parametros` (`codigo`, `Designacao`) VALUES (NEW.`codigo`, NEW.`Designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `Designacao` = VALUES(`Designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_parametros_ad` AFTER DELETE ON `kitanda_db_1`.`tb_parametros` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_parametros` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pedido_ai` AFTER INSERT ON `kitanda_db_1`.`tb_pedido` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_pedido` (`pk_pedido`, `data_pedido`, `hora_pedido`, `fk_mesas`, `status_pedido`, `facturado`, `deposito`, `valor_em_falta`) VALUES (NEW.`pk_pedido`, NEW.`data_pedido`, NEW.`hora_pedido`, NEW.`fk_mesas`, NEW.`status_pedido`, NEW.`facturado`, NEW.`deposito`, NEW.`valor_em_falta`) ON DUPLICATE KEY UPDATE `pk_pedido` = VALUES(`pk_pedido`), `data_pedido` = VALUES(`data_pedido`), `hora_pedido` = VALUES(`hora_pedido`), `fk_mesas` = VALUES(`fk_mesas`), `status_pedido` = VALUES(`status_pedido`), `facturado` = VALUES(`facturado`), `deposito` = VALUES(`deposito`), `valor_em_falta` = VALUES(`valor_em_falta`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pedido_au` AFTER UPDATE ON `kitanda_db_1`.`tb_pedido` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_pedido` SET `pk_pedido` = NEW.`pk_pedido`, `data_pedido` = NEW.`data_pedido`, `hora_pedido` = NEW.`hora_pedido`, `fk_mesas` = NEW.`fk_mesas`, `status_pedido` = NEW.`status_pedido`, `facturado` = NEW.`facturado`, `deposito` = NEW.`deposito`, `valor_em_falta` = NEW.`valor_em_falta` WHERE `pk_pedido` = OLD.`pk_pedido`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_pedido` (`pk_pedido`, `data_pedido`, `hora_pedido`, `fk_mesas`, `status_pedido`, `facturado`, `deposito`, `valor_em_falta`) VALUES (NEW.`pk_pedido`, NEW.`data_pedido`, NEW.`hora_pedido`, NEW.`fk_mesas`, NEW.`status_pedido`, NEW.`facturado`, NEW.`deposito`, NEW.`valor_em_falta`) ON DUPLICATE KEY UPDATE `pk_pedido` = VALUES(`pk_pedido`), `data_pedido` = VALUES(`data_pedido`), `hora_pedido` = VALUES(`hora_pedido`), `fk_mesas` = VALUES(`fk_mesas`), `status_pedido` = VALUES(`status_pedido`), `facturado` = VALUES(`facturado`), `deposito` = VALUES(`deposito`), `valor_em_falta` = VALUES(`valor_em_falta`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pedido_ad` AFTER DELETE ON `kitanda_db_1`.`tb_pedido` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_pedido` WHERE `pk_pedido` = OLD.`pk_pedido`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_permissao_ai` AFTER INSERT ON `kitanda_db_1`.`tb_permissao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_permissao` (`idPermissao`, `descricao`) VALUES (NEW.`idPermissao`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `idPermissao` = VALUES(`idPermissao`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_permissao_au` AFTER UPDATE ON `kitanda_db_1`.`tb_permissao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_permissao` SET `idPermissao` = NEW.`idPermissao`, `descricao` = NEW.`descricao` WHERE `idPermissao` = OLD.`idPermissao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_permissao` (`idPermissao`, `descricao`) VALUES (NEW.`idPermissao`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `idPermissao` = VALUES(`idPermissao`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_permissao_ad` AFTER DELETE ON `kitanda_db_1`.`tb_permissao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_permissao` WHERE `idPermissao` = OLD.`idPermissao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_preco_ai` AFTER INSERT ON `kitanda_db_1`.`tb_preco` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_preco` (`pk_preco`, `data`, `hora`, `preco_compra`, `percentagem_ganho`, `fk_produto`, `fk_usuario`, `preco_medio`, `preco_venda`, `qtd_baixo`, `qtd_alto`, `preco_anterior`, `retalho`) VALUES (NEW.`pk_preco`, NEW.`data`, NEW.`hora`, NEW.`preco_compra`, NEW.`percentagem_ganho`, NEW.`fk_produto`, NEW.`fk_usuario`, NEW.`preco_medio`, NEW.`preco_venda`, NEW.`qtd_baixo`, NEW.`qtd_alto`, NEW.`preco_anterior`, NEW.`retalho`) ON DUPLICATE KEY UPDATE `pk_preco` = VALUES(`pk_preco`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `preco_compra` = VALUES(`preco_compra`), `percentagem_ganho` = VALUES(`percentagem_ganho`), `fk_produto` = VALUES(`fk_produto`), `fk_usuario` = VALUES(`fk_usuario`), `preco_medio` = VALUES(`preco_medio`), `preco_venda` = VALUES(`preco_venda`), `qtd_baixo` = VALUES(`qtd_baixo`), `qtd_alto` = VALUES(`qtd_alto`), `preco_anterior` = VALUES(`preco_anterior`), `retalho` = VALUES(`retalho`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_preco_au` AFTER UPDATE ON `kitanda_db_1`.`tb_preco` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_preco` SET `pk_preco` = NEW.`pk_preco`, `data` = NEW.`data`, `hora` = NEW.`hora`, `preco_compra` = NEW.`preco_compra`, `percentagem_ganho` = NEW.`percentagem_ganho`, `fk_produto` = NEW.`fk_produto`, `fk_usuario` = NEW.`fk_usuario`, `preco_medio` = NEW.`preco_medio`, `preco_venda` = NEW.`preco_venda`, `qtd_baixo` = NEW.`qtd_baixo`, `qtd_alto` = NEW.`qtd_alto`, `preco_anterior` = NEW.`preco_anterior`, `retalho` = NEW.`retalho` WHERE `pk_preco` = OLD.`pk_preco`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_preco` (`pk_preco`, `data`, `hora`, `preco_compra`, `percentagem_ganho`, `fk_produto`, `fk_usuario`, `preco_medio`, `preco_venda`, `qtd_baixo`, `qtd_alto`, `preco_anterior`, `retalho`) VALUES (NEW.`pk_preco`, NEW.`data`, NEW.`hora`, NEW.`preco_compra`, NEW.`percentagem_ganho`, NEW.`fk_produto`, NEW.`fk_usuario`, NEW.`preco_medio`, NEW.`preco_venda`, NEW.`qtd_baixo`, NEW.`qtd_alto`, NEW.`preco_anterior`, NEW.`retalho`) ON DUPLICATE KEY UPDATE `pk_preco` = VALUES(`pk_preco`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `preco_compra` = VALUES(`preco_compra`), `percentagem_ganho` = VALUES(`percentagem_ganho`), `fk_produto` = VALUES(`fk_produto`), `fk_usuario` = VALUES(`fk_usuario`), `preco_medio` = VALUES(`preco_medio`), `preco_venda` = VALUES(`preco_venda`), `qtd_baixo` = VALUES(`qtd_baixo`), `qtd_alto` = VALUES(`qtd_alto`), `preco_anterior` = VALUES(`preco_anterior`), `retalho` = VALUES(`retalho`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_preco_ad` AFTER DELETE ON `kitanda_db_1`.`tb_preco` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_preco` WHERE `pk_preco` = OLD.`pk_preco`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pro_forma_ai` AFTER INSERT ON `kitanda_db_1`.`tb_pro_forma` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_pro_forma` (`pk_pro_forma`, `data`, `hora`, `fk_cliente`, `fk_usuario`, `total`) VALUES (NEW.`pk_pro_forma`, NEW.`data`, NEW.`hora`, NEW.`fk_cliente`, NEW.`fk_usuario`, NEW.`total`) ON DUPLICATE KEY UPDATE `pk_pro_forma` = VALUES(`pk_pro_forma`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `fk_cliente` = VALUES(`fk_cliente`), `fk_usuario` = VALUES(`fk_usuario`), `total` = VALUES(`total`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pro_forma_au` AFTER UPDATE ON `kitanda_db_1`.`tb_pro_forma` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_pro_forma` SET `pk_pro_forma` = NEW.`pk_pro_forma`, `data` = NEW.`data`, `hora` = NEW.`hora`, `fk_cliente` = NEW.`fk_cliente`, `fk_usuario` = NEW.`fk_usuario`, `total` = NEW.`total` WHERE `pk_pro_forma` = OLD.`pk_pro_forma`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_pro_forma` (`pk_pro_forma`, `data`, `hora`, `fk_cliente`, `fk_usuario`, `total`) VALUES (NEW.`pk_pro_forma`, NEW.`data`, NEW.`hora`, NEW.`fk_cliente`, NEW.`fk_usuario`, NEW.`total`) ON DUPLICATE KEY UPDATE `pk_pro_forma` = VALUES(`pk_pro_forma`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `fk_cliente` = VALUES(`fk_cliente`), `fk_usuario` = VALUES(`fk_usuario`), `total` = VALUES(`total`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_pro_forma_ad` AFTER DELETE ON `kitanda_db_1`.`tb_pro_forma` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_pro_forma` WHERE `pk_pro_forma` = OLD.`pk_pro_forma`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_produto_ai` AFTER INSERT ON `kitanda_db_1`.`tb_produto` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_produto` (`codigo`, `designacao`, `preco`, `cod_Tipo_Produto`, `fk_modelo`, `fk_grupo`, `photo`, `status_iva`, `cozinha`, `cod_pai`, `unidade_compra`, `cod_fornecedores`, `data_fabrico`, `data_expiracao`, `codBarra`, `status`, `data_entrada`, `stocavel`, `preco_venda`, `percentagem_desconto`, `quantidade_desconto`, `codigo_manual`, `cod_unidade`, `cod_local`, `cod_user_create`, `user_create`, `cod_user_update`, `user_update`, `factor_conversao`, `controla_lote`) VALUES (NEW.`codigo`, NEW.`designacao`, NEW.`preco`, NEW.`cod_Tipo_Produto`, NEW.`fk_modelo`, NEW.`fk_grupo`, NEW.`photo`, NEW.`status_iva`, NEW.`cozinha`, NEW.`cod_pai`, NEW.`unidade_compra`, NEW.`cod_fornecedores`, NEW.`data_fabrico`, NEW.`data_expiracao`, NEW.`codBarra`, NEW.`status`, NEW.`data_entrada`, NEW.`stocavel`, NEW.`preco_venda`, NEW.`percentagem_desconto`, NEW.`quantidade_desconto`, NEW.`codigo_manual`, NEW.`cod_unidade`, NEW.`cod_local`, NEW.`cod_user_create`, NEW.`user_create`, NEW.`cod_user_update`, NEW.`user_update`, NEW.`factor_conversao`, NEW.`controla_lote`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`), `preco` = VALUES(`preco`), `cod_Tipo_Produto` = VALUES(`cod_Tipo_Produto`), `fk_modelo` = VALUES(`fk_modelo`), `fk_grupo` = VALUES(`fk_grupo`), `photo` = VALUES(`photo`), `status_iva` = VALUES(`status_iva`), `cozinha` = VALUES(`cozinha`), `cod_pai` = VALUES(`cod_pai`), `unidade_compra` = VALUES(`unidade_compra`), `cod_fornecedores` = VALUES(`cod_fornecedores`), `data_fabrico` = VALUES(`data_fabrico`), `data_expiracao` = VALUES(`data_expiracao`), `codBarra` = VALUES(`codBarra`), `status` = VALUES(`status`), `data_entrada` = VALUES(`data_entrada`), `stocavel` = VALUES(`stocavel`), `preco_venda` = VALUES(`preco_venda`), `percentagem_desconto` = VALUES(`percentagem_desconto`), `quantidade_desconto` = VALUES(`quantidade_desconto`), `codigo_manual` = VALUES(`codigo_manual`), `cod_unidade` = VALUES(`cod_unidade`), `cod_local` = VALUES(`cod_local`), `cod_user_create` = VALUES(`cod_user_create`), `user_create` = VALUES(`user_create`), `cod_user_update` = VALUES(`cod_user_update`), `user_update` = VALUES(`user_update`), `factor_conversao` = VALUES(`factor_conversao`), `controla_lote` = VALUES(`controla_lote`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_produto_au` AFTER UPDATE ON `kitanda_db_1`.`tb_produto` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_produto` SET `codigo` = NEW.`codigo`, `designacao` = NEW.`designacao`, `preco` = NEW.`preco`, `cod_Tipo_Produto` = NEW.`cod_Tipo_Produto`, `fk_modelo` = NEW.`fk_modelo`, `fk_grupo` = NEW.`fk_grupo`, `photo` = NEW.`photo`, `status_iva` = NEW.`status_iva`, `cozinha` = NEW.`cozinha`, `cod_pai` = NEW.`cod_pai`, `unidade_compra` = NEW.`unidade_compra`, `cod_fornecedores` = NEW.`cod_fornecedores`, `data_fabrico` = NEW.`data_fabrico`, `data_expiracao` = NEW.`data_expiracao`, `codBarra` = NEW.`codBarra`, `status` = NEW.`status`, `data_entrada` = NEW.`data_entrada`, `stocavel` = NEW.`stocavel`, `preco_venda` = NEW.`preco_venda`, `percentagem_desconto` = NEW.`percentagem_desconto`, `quantidade_desconto` = NEW.`quantidade_desconto`, `codigo_manual` = NEW.`codigo_manual`, `cod_unidade` = NEW.`cod_unidade`, `cod_local` = NEW.`cod_local`, `cod_user_create` = NEW.`cod_user_create`, `user_create` = NEW.`user_create`, `cod_user_update` = NEW.`cod_user_update`, `user_update` = NEW.`user_update`, `factor_conversao` = NEW.`factor_conversao`, `controla_lote` = NEW.`controla_lote` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_produto` (`codigo`, `designacao`, `preco`, `cod_Tipo_Produto`, `fk_modelo`, `fk_grupo`, `photo`, `status_iva`, `cozinha`, `cod_pai`, `unidade_compra`, `cod_fornecedores`, `data_fabrico`, `data_expiracao`, `codBarra`, `status`, `data_entrada`, `stocavel`, `preco_venda`, `percentagem_desconto`, `quantidade_desconto`, `codigo_manual`, `cod_unidade`, `cod_local`, `cod_user_create`, `user_create`, `cod_user_update`, `user_update`, `factor_conversao`, `controla_lote`) VALUES (NEW.`codigo`, NEW.`designacao`, NEW.`preco`, NEW.`cod_Tipo_Produto`, NEW.`fk_modelo`, NEW.`fk_grupo`, NEW.`photo`, NEW.`status_iva`, NEW.`cozinha`, NEW.`cod_pai`, NEW.`unidade_compra`, NEW.`cod_fornecedores`, NEW.`data_fabrico`, NEW.`data_expiracao`, NEW.`codBarra`, NEW.`status`, NEW.`data_entrada`, NEW.`stocavel`, NEW.`preco_venda`, NEW.`percentagem_desconto`, NEW.`quantidade_desconto`, NEW.`codigo_manual`, NEW.`cod_unidade`, NEW.`cod_local`, NEW.`cod_user_create`, NEW.`user_create`, NEW.`cod_user_update`, NEW.`user_update`, NEW.`factor_conversao`, NEW.`controla_lote`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`), `preco` = VALUES(`preco`), `cod_Tipo_Produto` = VALUES(`cod_Tipo_Produto`), `fk_modelo` = VALUES(`fk_modelo`), `fk_grupo` = VALUES(`fk_grupo`), `photo` = VALUES(`photo`), `status_iva` = VALUES(`status_iva`), `cozinha` = VALUES(`cozinha`), `cod_pai` = VALUES(`cod_pai`), `unidade_compra` = VALUES(`unidade_compra`), `cod_fornecedores` = VALUES(`cod_fornecedores`), `data_fabrico` = VALUES(`data_fabrico`), `data_expiracao` = VALUES(`data_expiracao`), `codBarra` = VALUES(`codBarra`), `status` = VALUES(`status`), `data_entrada` = VALUES(`data_entrada`), `stocavel` = VALUES(`stocavel`), `preco_venda` = VALUES(`preco_venda`), `percentagem_desconto` = VALUES(`percentagem_desconto`), `quantidade_desconto` = VALUES(`quantidade_desconto`), `codigo_manual` = VALUES(`codigo_manual`), `cod_unidade` = VALUES(`cod_unidade`), `cod_local` = VALUES(`cod_local`), `cod_user_create` = VALUES(`cod_user_create`), `user_create` = VALUES(`user_create`), `cod_user_update` = VALUES(`cod_user_update`), `user_update` = VALUES(`user_update`), `factor_conversao` = VALUES(`factor_conversao`), `controla_lote` = VALUES(`controla_lote`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_produto_ad` AFTER DELETE ON `kitanda_db_1`.`tb_produto` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_produto` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_profissao_ai` AFTER INSERT ON `kitanda_db_1`.`tb_profissao` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_profissao` (`idProfissao`, `descricao`) VALUES (NEW.`idProfissao`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `idProfissao` = VALUES(`idProfissao`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_profissao_au` AFTER UPDATE ON `kitanda_db_1`.`tb_profissao` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_profissao` SET `idProfissao` = NEW.`idProfissao`, `descricao` = NEW.`descricao` WHERE `idProfissao` = OLD.`idProfissao`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_profissao` (`idProfissao`, `descricao`) VALUES (NEW.`idProfissao`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `idProfissao` = VALUES(`idProfissao`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_profissao_ad` AFTER DELETE ON `kitanda_db_1`.`tb_profissao` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_profissao` WHERE `idProfissao` = OLD.`idProfissao`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_provincia_ai` AFTER INSERT ON `kitanda_db_1`.`tb_provincia` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_provincia` (`idProvincia`, `descrisao`, `idPais`) VALUES (NEW.`idProvincia`, NEW.`descrisao`, NEW.`idPais`) ON DUPLICATE KEY UPDATE `idProvincia` = VALUES(`idProvincia`), `descrisao` = VALUES(`descrisao`), `idPais` = VALUES(`idPais`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_provincia_au` AFTER UPDATE ON `kitanda_db_1`.`tb_provincia` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_provincia` SET `idProvincia` = NEW.`idProvincia`, `descrisao` = NEW.`descrisao`, `idPais` = NEW.`idPais` WHERE `idProvincia` = OLD.`idProvincia`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_provincia` (`idProvincia`, `descrisao`, `idPais`) VALUES (NEW.`idProvincia`, NEW.`descrisao`, NEW.`idPais`) ON DUPLICATE KEY UPDATE `idProvincia` = VALUES(`idProvincia`), `descrisao` = VALUES(`descrisao`), `idPais` = VALUES(`idPais`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_provincia_ad` AFTER DELETE ON `kitanda_db_1`.`tb_provincia` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_provincia` WHERE `idProvincia` = OLD.`idProvincia`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_saida_vasilhame_ai` AFTER INSERT ON `kitanda_db_1`.`tb_saida_vasilhame` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_saida_vasilhame` (`pk_saida_vasilhame`, `data_saida`, `hora_saida`, `fk_armazem`, `fk_usuario`, `nome_funcionario`) VALUES (NEW.`pk_saida_vasilhame`, NEW.`data_saida`, NEW.`hora_saida`, NEW.`fk_armazem`, NEW.`fk_usuario`, NEW.`nome_funcionario`) ON DUPLICATE KEY UPDATE `pk_saida_vasilhame` = VALUES(`pk_saida_vasilhame`), `data_saida` = VALUES(`data_saida`), `hora_saida` = VALUES(`hora_saida`), `fk_armazem` = VALUES(`fk_armazem`), `fk_usuario` = VALUES(`fk_usuario`), `nome_funcionario` = VALUES(`nome_funcionario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_saida_vasilhame_au` AFTER UPDATE ON `kitanda_db_1`.`tb_saida_vasilhame` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_saida_vasilhame` SET `pk_saida_vasilhame` = NEW.`pk_saida_vasilhame`, `data_saida` = NEW.`data_saida`, `hora_saida` = NEW.`hora_saida`, `fk_armazem` = NEW.`fk_armazem`, `fk_usuario` = NEW.`fk_usuario`, `nome_funcionario` = NEW.`nome_funcionario` WHERE `pk_saida_vasilhame` = OLD.`pk_saida_vasilhame`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_saida_vasilhame` (`pk_saida_vasilhame`, `data_saida`, `hora_saida`, `fk_armazem`, `fk_usuario`, `nome_funcionario`) VALUES (NEW.`pk_saida_vasilhame`, NEW.`data_saida`, NEW.`hora_saida`, NEW.`fk_armazem`, NEW.`fk_usuario`, NEW.`nome_funcionario`) ON DUPLICATE KEY UPDATE `pk_saida_vasilhame` = VALUES(`pk_saida_vasilhame`), `data_saida` = VALUES(`data_saida`), `hora_saida` = VALUES(`hora_saida`), `fk_armazem` = VALUES(`fk_armazem`), `fk_usuario` = VALUES(`fk_usuario`), `nome_funcionario` = VALUES(`nome_funcionario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_saida_vasilhame_ad` AFTER DELETE ON `kitanda_db_1`.`tb_saida_vasilhame` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_saida_vasilhame` WHERE `pk_saida_vasilhame` = OLD.`pk_saida_vasilhame`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_saidas_produtos_ai` AFTER INSERT ON `kitanda_db_1`.`tb_saidas_produtos` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_saidas_produtos` (`pk_saidas_produtos`, `data_saida`, `fk_usuario`, `idArmazemFK`, `hora_saida`, `status_eliminado`, `obs`, `nome_funcionario`, `documento`, `destino_id`, `armazem_destino`) VALUES (NEW.`pk_saidas_produtos`, NEW.`data_saida`, NEW.`fk_usuario`, NEW.`idArmazemFK`, NEW.`hora_saida`, NEW.`status_eliminado`, NEW.`obs`, NEW.`nome_funcionario`, NEW.`documento`, NEW.`destino_id`, NEW.`armazem_destino`) ON DUPLICATE KEY UPDATE `pk_saidas_produtos` = VALUES(`pk_saidas_produtos`), `data_saida` = VALUES(`data_saida`), `fk_usuario` = VALUES(`fk_usuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `hora_saida` = VALUES(`hora_saida`), `status_eliminado` = VALUES(`status_eliminado`), `obs` = VALUES(`obs`), `nome_funcionario` = VALUES(`nome_funcionario`), `documento` = VALUES(`documento`), `destino_id` = VALUES(`destino_id`), `armazem_destino` = VALUES(`armazem_destino`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_saidas_produtos_au` AFTER UPDATE ON `kitanda_db_1`.`tb_saidas_produtos` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_saidas_produtos` SET `pk_saidas_produtos` = NEW.`pk_saidas_produtos`, `data_saida` = NEW.`data_saida`, `fk_usuario` = NEW.`fk_usuario`, `idArmazemFK` = NEW.`idArmazemFK`, `hora_saida` = NEW.`hora_saida`, `status_eliminado` = NEW.`status_eliminado`, `obs` = NEW.`obs`, `nome_funcionario` = NEW.`nome_funcionario`, `documento` = NEW.`documento`, `destino_id` = NEW.`destino_id`, `armazem_destino` = NEW.`armazem_destino` WHERE `pk_saidas_produtos` = OLD.`pk_saidas_produtos`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_saidas_produtos` (`pk_saidas_produtos`, `data_saida`, `fk_usuario`, `idArmazemFK`, `hora_saida`, `status_eliminado`, `obs`, `nome_funcionario`, `documento`, `destino_id`, `armazem_destino`) VALUES (NEW.`pk_saidas_produtos`, NEW.`data_saida`, NEW.`fk_usuario`, NEW.`idArmazemFK`, NEW.`hora_saida`, NEW.`status_eliminado`, NEW.`obs`, NEW.`nome_funcionario`, NEW.`documento`, NEW.`destino_id`, NEW.`armazem_destino`) ON DUPLICATE KEY UPDATE `pk_saidas_produtos` = VALUES(`pk_saidas_produtos`), `data_saida` = VALUES(`data_saida`), `fk_usuario` = VALUES(`fk_usuario`), `idArmazemFK` = VALUES(`idArmazemFK`), `hora_saida` = VALUES(`hora_saida`), `status_eliminado` = VALUES(`status_eliminado`), `obs` = VALUES(`obs`), `nome_funcionario` = VALUES(`nome_funcionario`), `documento` = VALUES(`documento`), `destino_id` = VALUES(`destino_id`), `armazem_destino` = VALUES(`armazem_destino`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_saidas_produtos_ad` AFTER DELETE ON `kitanda_db_1`.`tb_saidas_produtos` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_saidas_produtos` WHERE `pk_saidas_produtos` = OLD.`pk_saidas_produtos`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_salario_ai` AFTER INSERT ON `kitanda_db_1`.`tb_salario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_salario` (`idSalarioFK`, `valor_tempo`, `idFuncionarioFK`) VALUES (NEW.`idSalarioFK`, NEW.`valor_tempo`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idSalarioFK` = VALUES(`idSalarioFK`), `valor_tempo` = VALUES(`valor_tempo`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_salario_au` AFTER UPDATE ON `kitanda_db_1`.`tb_salario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_salario` SET `idSalarioFK` = NEW.`idSalarioFK`, `valor_tempo` = NEW.`valor_tempo`, `idFuncionarioFK` = NEW.`idFuncionarioFK` WHERE `idSalarioFK` = OLD.`idSalarioFK`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_salario` (`idSalarioFK`, `valor_tempo`, `idFuncionarioFK`) VALUES (NEW.`idSalarioFK`, NEW.`valor_tempo`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idSalarioFK` = VALUES(`idSalarioFK`), `valor_tempo` = VALUES(`valor_tempo`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_salario_ad` AFTER DELETE ON `kitanda_db_1`.`tb_salario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_salario` WHERE `idSalarioFK` = OLD.`idSalarioFK`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_sexo_ai` AFTER INSERT ON `kitanda_db_1`.`tb_sexo` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_sexo` (`codigo`, `descrisao`, `designacao`) VALUES (NEW.`codigo`, NEW.`descrisao`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `descrisao` = VALUES(`descrisao`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_sexo_au` AFTER UPDATE ON `kitanda_db_1`.`tb_sexo` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_sexo` SET `codigo` = NEW.`codigo`, `descrisao` = NEW.`descrisao`, `designacao` = NEW.`designacao` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_sexo` (`codigo`, `descrisao`, `designacao`) VALUES (NEW.`codigo`, NEW.`descrisao`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `descrisao` = VALUES(`descrisao`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_sexo_ad` AFTER DELETE ON `kitanda_db_1`.`tb_sexo` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_sexo` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_status_ai` AFTER INSERT ON `kitanda_db_1`.`tb_status` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_status` (`idStatus`, `descrisao`) VALUES (NEW.`idStatus`, NEW.`descrisao`) ON DUPLICATE KEY UPDATE `idStatus` = VALUES(`idStatus`), `descrisao` = VALUES(`descrisao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_status_au` AFTER UPDATE ON `kitanda_db_1`.`tb_status` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_status` SET `idStatus` = NEW.`idStatus`, `descrisao` = NEW.`descrisao` WHERE `idStatus` = OLD.`idStatus`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_status` (`idStatus`, `descrisao`) VALUES (NEW.`idStatus`, NEW.`descrisao`) ON DUPLICATE KEY UPDATE `idStatus` = VALUES(`idStatus`), `descrisao` = VALUES(`descrisao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_status_ad` AFTER DELETE ON `kitanda_db_1`.`tb_status` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_status` WHERE `idStatus` = OLD.`idStatus`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_stock_ai` AFTER INSERT ON `kitanda_db_1`.`tb_stock` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_stock` (`codigo`, `cod_produto_codigo`, `dataEntrada`, `quantidade_existente`, `status`, `preco_venda`, `quant_critica`, `quant_baixa`, `quantidade_antiga`, `cod_armazem`, `preco_venda_grosso`, `qtd_grosso`, `preco_venda_fabrica`) VALUES (NEW.`codigo`, NEW.`cod_produto_codigo`, NEW.`dataEntrada`, NEW.`quantidade_existente`, NEW.`status`, NEW.`preco_venda`, NEW.`quant_critica`, NEW.`quant_baixa`, NEW.`quantidade_antiga`, NEW.`cod_armazem`, NEW.`preco_venda_grosso`, NEW.`qtd_grosso`, NEW.`preco_venda_fabrica`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `cod_produto_codigo` = VALUES(`cod_produto_codigo`), `dataEntrada` = VALUES(`dataEntrada`), `quantidade_existente` = VALUES(`quantidade_existente`), `status` = VALUES(`status`), `preco_venda` = VALUES(`preco_venda`), `quant_critica` = VALUES(`quant_critica`), `quant_baixa` = VALUES(`quant_baixa`), `quantidade_antiga` = VALUES(`quantidade_antiga`), `cod_armazem` = VALUES(`cod_armazem`), `preco_venda_grosso` = VALUES(`preco_venda_grosso`), `qtd_grosso` = VALUES(`qtd_grosso`), `preco_venda_fabrica` = VALUES(`preco_venda_fabrica`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_stock_au` AFTER UPDATE ON `kitanda_db_1`.`tb_stock` FOR EACH ROW BEGIN
  IF EXISTS (SELECT 1 FROM `kitanda_db_1`.stock_diario WHERE produto_id = NEW.cod_produto_codigo AND armazem_id = NEW.cod_armazem AND data_registro = CURDATE()) THEN
    UPDATE `kitanda_db_1`.stock_diario SET quantidade = NEW.quantidade_existente WHERE produto_id = NEW.cod_produto_codigo AND armazem_id = NEW.cod_armazem AND data_registro = CURDATE();
  ELSE
    INSERT INTO `kitanda_db_1`.stock_diario (produto_id, quantidade, data_registro, armazem_id) VALUES (NEW.cod_produto_codigo, NEW.quantidade_existente, CURDATE(), NEW.cod_armazem);
  END IF;
  UPDATE `kitanda_db`.`tb_stock` SET `codigo` = NEW.`codigo`, `cod_produto_codigo` = NEW.`cod_produto_codigo`, `dataEntrada` = NEW.`dataEntrada`, `quantidade_existente` = NEW.`quantidade_existente`, `status` = NEW.`status`, `preco_venda` = NEW.`preco_venda`, `quant_critica` = NEW.`quant_critica`, `quant_baixa` = NEW.`quant_baixa`, `quantidade_antiga` = NEW.`quantidade_antiga`, `cod_armazem` = NEW.`cod_armazem`, `preco_venda_grosso` = NEW.`preco_venda_grosso`, `qtd_grosso` = NEW.`qtd_grosso`, `preco_venda_fabrica` = NEW.`preco_venda_fabrica` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_stock` (`codigo`, `cod_produto_codigo`, `dataEntrada`, `quantidade_existente`, `status`, `preco_venda`, `quant_critica`, `quant_baixa`, `quantidade_antiga`, `cod_armazem`, `preco_venda_grosso`, `qtd_grosso`, `preco_venda_fabrica`) VALUES (NEW.`codigo`, NEW.`cod_produto_codigo`, NEW.`dataEntrada`, NEW.`quantidade_existente`, NEW.`status`, NEW.`preco_venda`, NEW.`quant_critica`, NEW.`quant_baixa`, NEW.`quantidade_antiga`, NEW.`cod_armazem`, NEW.`preco_venda_grosso`, NEW.`qtd_grosso`, NEW.`preco_venda_fabrica`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `cod_produto_codigo` = VALUES(`cod_produto_codigo`), `dataEntrada` = VALUES(`dataEntrada`), `quantidade_existente` = VALUES(`quantidade_existente`), `status` = VALUES(`status`), `preco_venda` = VALUES(`preco_venda`), `quant_critica` = VALUES(`quant_critica`), `quant_baixa` = VALUES(`quant_baixa`), `quantidade_antiga` = VALUES(`quantidade_antiga`), `cod_armazem` = VALUES(`cod_armazem`), `preco_venda_grosso` = VALUES(`preco_venda_grosso`), `qtd_grosso` = VALUES(`qtd_grosso`), `preco_venda_fabrica` = VALUES(`preco_venda_fabrica`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_stock_ad` AFTER DELETE ON `kitanda_db_1`.`tb_stock` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_stock` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_stock_mirrow_ai` AFTER INSERT ON `kitanda_db_1`.`tb_stock_mirrow` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_stock_mirrow` (`codigo`, `cod_produto_codigo`, `dataEntrada`, `quantidade_existente`, `status`, `preco_venda`, `quant_critica`, `quant_baixa`, `quantidade_antiga`, `cod_armazem`, `preco_venda_grosso`, `qtd_grosso`, `preco_venda_fabrica`, `data_feicho`) VALUES (NEW.`codigo`, NEW.`cod_produto_codigo`, NEW.`dataEntrada`, NEW.`quantidade_existente`, NEW.`status`, NEW.`preco_venda`, NEW.`quant_critica`, NEW.`quant_baixa`, NEW.`quantidade_antiga`, NEW.`cod_armazem`, NEW.`preco_venda_grosso`, NEW.`qtd_grosso`, NEW.`preco_venda_fabrica`, NEW.`data_feicho`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `cod_produto_codigo` = VALUES(`cod_produto_codigo`), `dataEntrada` = VALUES(`dataEntrada`), `quantidade_existente` = VALUES(`quantidade_existente`), `status` = VALUES(`status`), `preco_venda` = VALUES(`preco_venda`), `quant_critica` = VALUES(`quant_critica`), `quant_baixa` = VALUES(`quant_baixa`), `quantidade_antiga` = VALUES(`quantidade_antiga`), `cod_armazem` = VALUES(`cod_armazem`), `preco_venda_grosso` = VALUES(`preco_venda_grosso`), `qtd_grosso` = VALUES(`qtd_grosso`), `preco_venda_fabrica` = VALUES(`preco_venda_fabrica`), `data_feicho` = VALUES(`data_feicho`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_stock_mirrow_au` AFTER UPDATE ON `kitanda_db_1`.`tb_stock_mirrow` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_stock_mirrow` SET `codigo` = NEW.`codigo`, `cod_produto_codigo` = NEW.`cod_produto_codigo`, `dataEntrada` = NEW.`dataEntrada`, `quantidade_existente` = NEW.`quantidade_existente`, `status` = NEW.`status`, `preco_venda` = NEW.`preco_venda`, `quant_critica` = NEW.`quant_critica`, `quant_baixa` = NEW.`quant_baixa`, `quantidade_antiga` = NEW.`quantidade_antiga`, `cod_armazem` = NEW.`cod_armazem`, `preco_venda_grosso` = NEW.`preco_venda_grosso`, `qtd_grosso` = NEW.`qtd_grosso`, `preco_venda_fabrica` = NEW.`preco_venda_fabrica`, `data_feicho` = NEW.`data_feicho` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_stock_mirrow` (`codigo`, `cod_produto_codigo`, `dataEntrada`, `quantidade_existente`, `status`, `preco_venda`, `quant_critica`, `quant_baixa`, `quantidade_antiga`, `cod_armazem`, `preco_venda_grosso`, `qtd_grosso`, `preco_venda_fabrica`, `data_feicho`) VALUES (NEW.`codigo`, NEW.`cod_produto_codigo`, NEW.`dataEntrada`, NEW.`quantidade_existente`, NEW.`status`, NEW.`preco_venda`, NEW.`quant_critica`, NEW.`quant_baixa`, NEW.`quantidade_antiga`, NEW.`cod_armazem`, NEW.`preco_venda_grosso`, NEW.`qtd_grosso`, NEW.`preco_venda_fabrica`, NEW.`data_feicho`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `cod_produto_codigo` = VALUES(`cod_produto_codigo`), `dataEntrada` = VALUES(`dataEntrada`), `quantidade_existente` = VALUES(`quantidade_existente`), `status` = VALUES(`status`), `preco_venda` = VALUES(`preco_venda`), `quant_critica` = VALUES(`quant_critica`), `quant_baixa` = VALUES(`quant_baixa`), `quantidade_antiga` = VALUES(`quantidade_antiga`), `cod_armazem` = VALUES(`cod_armazem`), `preco_venda_grosso` = VALUES(`preco_venda_grosso`), `qtd_grosso` = VALUES(`qtd_grosso`), `preco_venda_fabrica` = VALUES(`preco_venda_fabrica`), `data_feicho` = VALUES(`data_feicho`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_stock_mirrow_ad` AFTER DELETE ON `kitanda_db_1`.`tb_stock_mirrow` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_stock_mirrow` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_subsidios_ai` AFTER INSERT ON `kitanda_db_1`.`tb_subsidios` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_subsidios` (`idSubsidios`, `descricao`, `incidencia`, `incidencia_inss`) VALUES (NEW.`idSubsidios`, NEW.`descricao`, NEW.`incidencia`, NEW.`incidencia_inss`) ON DUPLICATE KEY UPDATE `idSubsidios` = VALUES(`idSubsidios`), `descricao` = VALUES(`descricao`), `incidencia` = VALUES(`incidencia`), `incidencia_inss` = VALUES(`incidencia_inss`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_subsidios_au` AFTER UPDATE ON `kitanda_db_1`.`tb_subsidios` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_subsidios` SET `idSubsidios` = NEW.`idSubsidios`, `descricao` = NEW.`descricao`, `incidencia` = NEW.`incidencia`, `incidencia_inss` = NEW.`incidencia_inss` WHERE `idSubsidios` = OLD.`idSubsidios`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_subsidios` (`idSubsidios`, `descricao`, `incidencia`, `incidencia_inss`) VALUES (NEW.`idSubsidios`, NEW.`descricao`, NEW.`incidencia`, NEW.`incidencia_inss`) ON DUPLICATE KEY UPDATE `idSubsidios` = VALUES(`idSubsidios`), `descricao` = VALUES(`descricao`), `incidencia` = VALUES(`incidencia`), `incidencia_inss` = VALUES(`incidencia_inss`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_subsidios_ad` AFTER DELETE ON `kitanda_db_1`.`tb_subsidios` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_subsidios` WHERE `idSubsidios` = OLD.`idSubsidios`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tempo_ai` AFTER INSERT ON `kitanda_db_1`.`tb_tempo` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_tempo` (`idTempoPK`, `tempo_dado`, `data`, `hora`, `idFuncionarioFK`) VALUES (NEW.`idTempoPK`, NEW.`tempo_dado`, NEW.`data`, NEW.`hora`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idTempoPK` = VALUES(`idTempoPK`), `tempo_dado` = VALUES(`tempo_dado`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tempo_au` AFTER UPDATE ON `kitanda_db_1`.`tb_tempo` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_tempo` SET `idTempoPK` = NEW.`idTempoPK`, `tempo_dado` = NEW.`tempo_dado`, `data` = NEW.`data`, `hora` = NEW.`hora`, `idFuncionarioFK` = NEW.`idFuncionarioFK` WHERE `idTempoPK` = OLD.`idTempoPK`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_tempo` (`idTempoPK`, `tempo_dado`, `data`, `hora`, `idFuncionarioFK`) VALUES (NEW.`idTempoPK`, NEW.`tempo_dado`, NEW.`data`, NEW.`hora`, NEW.`idFuncionarioFK`) ON DUPLICATE KEY UPDATE `idTempoPK` = VALUES(`idTempoPK`), `tempo_dado` = VALUES(`tempo_dado`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `idFuncionarioFK` = VALUES(`idFuncionarioFK`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tempo_ad` AFTER DELETE ON `kitanda_db_1`.`tb_tempo` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_tempo` WHERE `idTempoPK` = OLD.`idTempoPK`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tipo_produto_ai` AFTER INSERT ON `kitanda_db_1`.`tb_tipo_produto` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_tipo_produto` (`codigo`, `designacao`, `fk_familia`) VALUES (NEW.`codigo`, NEW.`designacao`, NEW.`fk_familia`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`), `fk_familia` = VALUES(`fk_familia`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tipo_produto_au` AFTER UPDATE ON `kitanda_db_1`.`tb_tipo_produto` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_tipo_produto` SET `codigo` = NEW.`codigo`, `designacao` = NEW.`designacao`, `fk_familia` = NEW.`fk_familia` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_tipo_produto` (`codigo`, `designacao`, `fk_familia`) VALUES (NEW.`codigo`, NEW.`designacao`, NEW.`fk_familia`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `designacao` = VALUES(`designacao`), `fk_familia` = VALUES(`fk_familia`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tipo_produto_ad` AFTER DELETE ON `kitanda_db_1`.`tb_tipo_produto` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_tipo_produto` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tipo_usuario_ai` AFTER INSERT ON `kitanda_db_1`.`tb_tipo_usuario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_tipo_usuario` (`idTipoUsuario`, `descricao`) VALUES (NEW.`idTipoUsuario`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `idTipoUsuario` = VALUES(`idTipoUsuario`), `descricao` = VALUES(`descricao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tipo_usuario_au` AFTER UPDATE ON `kitanda_db_1`.`tb_tipo_usuario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_tipo_usuario` SET `idTipoUsuario` = NEW.`idTipoUsuario`, `descricao` = NEW.`descricao` WHERE `idTipoUsuario` = OLD.`idTipoUsuario`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_tipo_usuario` (`idTipoUsuario`, `descricao`) VALUES (NEW.`idTipoUsuario`, NEW.`descricao`) ON DUPLICATE KEY UPDATE `idTipoUsuario` = VALUES(`idTipoUsuario`), `descricao` = VALUES(`descricao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_tipo_usuario_ad` AFTER DELETE ON `kitanda_db_1`.`tb_tipo_usuario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_tipo_usuario` WHERE `idTipoUsuario` = OLD.`idTipoUsuario`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_usuario_ai` AFTER INSERT ON `kitanda_db_1`.`tb_usuario` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_usuario` (`codigo`, `nome`, `senha`, `status`, `dataNascimento`, `telefone`, `email`, `endereco`, `codigo_sexo`, `userName`, `idTipoUsuario`, `sobreNome`, `idStatus`, `dataUltimoAcesso`) VALUES (NEW.`codigo`, NEW.`nome`, NEW.`senha`, NEW.`status`, NEW.`dataNascimento`, NEW.`telefone`, NEW.`email`, NEW.`endereco`, NEW.`codigo_sexo`, NEW.`userName`, NEW.`idTipoUsuario`, NEW.`sobreNome`, NEW.`idStatus`, NEW.`dataUltimoAcesso`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `nome` = VALUES(`nome`), `senha` = VALUES(`senha`), `status` = VALUES(`status`), `dataNascimento` = VALUES(`dataNascimento`), `telefone` = VALUES(`telefone`), `email` = VALUES(`email`), `endereco` = VALUES(`endereco`), `codigo_sexo` = VALUES(`codigo_sexo`), `userName` = VALUES(`userName`), `idTipoUsuario` = VALUES(`idTipoUsuario`), `sobreNome` = VALUES(`sobreNome`), `idStatus` = VALUES(`idStatus`), `dataUltimoAcesso` = VALUES(`dataUltimoAcesso`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_usuario_au` AFTER UPDATE ON `kitanda_db_1`.`tb_usuario` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_usuario` SET `codigo` = NEW.`codigo`, `nome` = NEW.`nome`, `senha` = NEW.`senha`, `status` = NEW.`status`, `dataNascimento` = NEW.`dataNascimento`, `telefone` = NEW.`telefone`, `email` = NEW.`email`, `endereco` = NEW.`endereco`, `codigo_sexo` = NEW.`codigo_sexo`, `userName` = NEW.`userName`, `idTipoUsuario` = NEW.`idTipoUsuario`, `sobreNome` = NEW.`sobreNome`, `idStatus` = NEW.`idStatus`, `dataUltimoAcesso` = NEW.`dataUltimoAcesso` WHERE `codigo` = OLD.`codigo`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_usuario` (`codigo`, `nome`, `senha`, `status`, `dataNascimento`, `telefone`, `email`, `endereco`, `codigo_sexo`, `userName`, `idTipoUsuario`, `sobreNome`, `idStatus`, `dataUltimoAcesso`) VALUES (NEW.`codigo`, NEW.`nome`, NEW.`senha`, NEW.`status`, NEW.`dataNascimento`, NEW.`telefone`, NEW.`email`, NEW.`endereco`, NEW.`codigo_sexo`, NEW.`userName`, NEW.`idTipoUsuario`, NEW.`sobreNome`, NEW.`idStatus`, NEW.`dataUltimoAcesso`) ON DUPLICATE KEY UPDATE `codigo` = VALUES(`codigo`), `nome` = VALUES(`nome`), `senha` = VALUES(`senha`), `status` = VALUES(`status`), `dataNascimento` = VALUES(`dataNascimento`), `telefone` = VALUES(`telefone`), `email` = VALUES(`email`), `endereco` = VALUES(`endereco`), `codigo_sexo` = VALUES(`codigo_sexo`), `userName` = VALUES(`userName`), `idTipoUsuario` = VALUES(`idTipoUsuario`), `sobreNome` = VALUES(`sobreNome`), `idStatus` = VALUES(`idStatus`), `dataUltimoAcesso` = VALUES(`dataUltimoAcesso`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_usuario_ad` AFTER DELETE ON `kitanda_db_1`.`tb_usuario` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_usuario` WHERE `codigo` = OLD.`codigo`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_validade_ai` AFTER INSERT ON `kitanda_db_1`.`tb_validade` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_validade` (`pk_validade`, `data_inicio`, `data_fim`) VALUES (NEW.`pk_validade`, NEW.`data_inicio`, NEW.`data_fim`) ON DUPLICATE KEY UPDATE `pk_validade` = VALUES(`pk_validade`), `data_inicio` = VALUES(`data_inicio`), `data_fim` = VALUES(`data_fim`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_validade_au` AFTER UPDATE ON `kitanda_db_1`.`tb_validade` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_validade` SET `pk_validade` = NEW.`pk_validade`, `data_inicio` = NEW.`data_inicio`, `data_fim` = NEW.`data_fim` WHERE `pk_validade` = OLD.`pk_validade`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_validade` (`pk_validade`, `data_inicio`, `data_fim`) VALUES (NEW.`pk_validade`, NEW.`data_inicio`, NEW.`data_fim`) ON DUPLICATE KEY UPDATE `pk_validade` = VALUES(`pk_validade`), `data_inicio` = VALUES(`data_inicio`), `data_fim` = VALUES(`data_fim`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_validade_ad` AFTER DELETE ON `kitanda_db_1`.`tb_validade` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_validade` WHERE `pk_validade` = OLD.`pk_validade`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_vasilhame_ai` AFTER INSERT ON `kitanda_db_1`.`tb_vasilhame` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tb_vasilhame` (`pk_vasilhame`, `descricao`, `qtd_existente`, `fk_produto`, `fk_armazem`) VALUES (NEW.`pk_vasilhame`, NEW.`descricao`, NEW.`qtd_existente`, NEW.`fk_produto`, NEW.`fk_armazem`) ON DUPLICATE KEY UPDATE `pk_vasilhame` = VALUES(`pk_vasilhame`), `descricao` = VALUES(`descricao`), `qtd_existente` = VALUES(`qtd_existente`), `fk_produto` = VALUES(`fk_produto`), `fk_armazem` = VALUES(`fk_armazem`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_vasilhame_au` AFTER UPDATE ON `kitanda_db_1`.`tb_vasilhame` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tb_vasilhame` SET `pk_vasilhame` = NEW.`pk_vasilhame`, `descricao` = NEW.`descricao`, `qtd_existente` = NEW.`qtd_existente`, `fk_produto` = NEW.`fk_produto`, `fk_armazem` = NEW.`fk_armazem` WHERE `pk_vasilhame` = OLD.`pk_vasilhame`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tb_vasilhame` (`pk_vasilhame`, `descricao`, `qtd_existente`, `fk_produto`, `fk_armazem`) VALUES (NEW.`pk_vasilhame`, NEW.`descricao`, NEW.`qtd_existente`, NEW.`fk_produto`, NEW.`fk_armazem`) ON DUPLICATE KEY UPDATE `pk_vasilhame` = VALUES(`pk_vasilhame`), `descricao` = VALUES(`descricao`), `qtd_existente` = VALUES(`qtd_existente`), `fk_produto` = VALUES(`fk_produto`), `fk_armazem` = VALUES(`fk_armazem`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tb_vasilhame_ad` AFTER DELETE ON `kitanda_db_1`.`tb_vasilhame` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tb_vasilhame` WHERE `pk_vasilhame` = OLD.`pk_vasilhame`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tipo_conta_ai` AFTER INSERT ON `kitanda_db_1`.`tipo_conta` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`tipo_conta` (`pk_tipo_conta`, `designacao`) VALUES (NEW.`pk_tipo_conta`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_tipo_conta` = VALUES(`pk_tipo_conta`), `designacao` = VALUES(`designacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tipo_conta_au` AFTER UPDATE ON `kitanda_db_1`.`tipo_conta` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`tipo_conta` SET `pk_tipo_conta` = NEW.`pk_tipo_conta`, `designacao` = NEW.`designacao` WHERE `pk_tipo_conta` = OLD.`pk_tipo_conta`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`tipo_conta` (`pk_tipo_conta`, `designacao`) VALUES (NEW.`pk_tipo_conta`, NEW.`designacao`) ON DUPLICATE KEY UPDATE `pk_tipo_conta` = VALUES(`pk_tipo_conta`), `designacao` = VALUES(`designacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_tipo_conta_ad` AFTER DELETE ON `kitanda_db_1`.`tipo_conta` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`tipo_conta` WHERE `pk_tipo_conta` = OLD.`pk_tipo_conta`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_transferencia_armazem_ai` AFTER INSERT ON `kitanda_db_1`.`transferencia_armazem` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`transferencia_armazem` (`pk_transferencia_armazem`, `data_hora`, `fk_usuario`, `nome_usuario`) VALUES (NEW.`pk_transferencia_armazem`, NEW.`data_hora`, NEW.`fk_usuario`, NEW.`nome_usuario`) ON DUPLICATE KEY UPDATE `pk_transferencia_armazem` = VALUES(`pk_transferencia_armazem`), `data_hora` = VALUES(`data_hora`), `fk_usuario` = VALUES(`fk_usuario`), `nome_usuario` = VALUES(`nome_usuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_transferencia_armazem_au` AFTER UPDATE ON `kitanda_db_1`.`transferencia_armazem` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`transferencia_armazem` SET `pk_transferencia_armazem` = NEW.`pk_transferencia_armazem`, `data_hora` = NEW.`data_hora`, `fk_usuario` = NEW.`fk_usuario`, `nome_usuario` = NEW.`nome_usuario` WHERE `pk_transferencia_armazem` = OLD.`pk_transferencia_armazem`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`transferencia_armazem` (`pk_transferencia_armazem`, `data_hora`, `fk_usuario`, `nome_usuario`) VALUES (NEW.`pk_transferencia_armazem`, NEW.`data_hora`, NEW.`fk_usuario`, NEW.`nome_usuario`) ON DUPLICATE KEY UPDATE `pk_transferencia_armazem` = VALUES(`pk_transferencia_armazem`), `data_hora` = VALUES(`data_hora`), `fk_usuario` = VALUES(`fk_usuario`), `nome_usuario` = VALUES(`nome_usuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_transferencia_armazem_ad` AFTER DELETE ON `kitanda_db_1`.`transferencia_armazem` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`transferencia_armazem` WHERE `pk_transferencia_armazem` = OLD.`pk_transferencia_armazem`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_transferencia_bancaria_ai` AFTER INSERT ON `kitanda_db_1`.`transferencia_bancaria` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`transferencia_bancaria` (`pk_transferencia`, `data`, `hora`, `fk_banco_origem`, `fk_banco_destino`, `valor`, `fk_usuario`, `descricao`, `nborderaux`) VALUES (NEW.`pk_transferencia`, NEW.`data`, NEW.`hora`, NEW.`fk_banco_origem`, NEW.`fk_banco_destino`, NEW.`valor`, NEW.`fk_usuario`, NEW.`descricao`, NEW.`nborderaux`) ON DUPLICATE KEY UPDATE `pk_transferencia` = VALUES(`pk_transferencia`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `fk_banco_origem` = VALUES(`fk_banco_origem`), `fk_banco_destino` = VALUES(`fk_banco_destino`), `valor` = VALUES(`valor`), `fk_usuario` = VALUES(`fk_usuario`), `descricao` = VALUES(`descricao`), `nborderaux` = VALUES(`nborderaux`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_transferencia_bancaria_au` AFTER UPDATE ON `kitanda_db_1`.`transferencia_bancaria` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`transferencia_bancaria` SET `pk_transferencia` = NEW.`pk_transferencia`, `data` = NEW.`data`, `hora` = NEW.`hora`, `fk_banco_origem` = NEW.`fk_banco_origem`, `fk_banco_destino` = NEW.`fk_banco_destino`, `valor` = NEW.`valor`, `fk_usuario` = NEW.`fk_usuario`, `descricao` = NEW.`descricao`, `nborderaux` = NEW.`nborderaux` WHERE `pk_transferencia` = OLD.`pk_transferencia`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`transferencia_bancaria` (`pk_transferencia`, `data`, `hora`, `fk_banco_origem`, `fk_banco_destino`, `valor`, `fk_usuario`, `descricao`, `nborderaux`) VALUES (NEW.`pk_transferencia`, NEW.`data`, NEW.`hora`, NEW.`fk_banco_origem`, NEW.`fk_banco_destino`, NEW.`valor`, NEW.`fk_usuario`, NEW.`descricao`, NEW.`nborderaux`) ON DUPLICATE KEY UPDATE `pk_transferencia` = VALUES(`pk_transferencia`), `data` = VALUES(`data`), `hora` = VALUES(`hora`), `fk_banco_origem` = VALUES(`fk_banco_origem`), `fk_banco_destino` = VALUES(`fk_banco_destino`), `valor` = VALUES(`valor`), `fk_usuario` = VALUES(`fk_usuario`), `descricao` = VALUES(`descricao`), `nborderaux` = VALUES(`nborderaux`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_transferencia_bancaria_ad` AFTER DELETE ON `kitanda_db_1`.`transferencia_bancaria` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`transferencia_bancaria` WHERE `pk_transferencia` = OLD.`pk_transferencia`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_turno_ai` AFTER INSERT ON `kitanda_db_1`.`turno` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`turno` (`pk_turno`, `data`, `hora_abertura`, `hora_fecho`, `turno`, `fk_usuario`) VALUES (NEW.`pk_turno`, NEW.`data`, NEW.`hora_abertura`, NEW.`hora_fecho`, NEW.`turno`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_turno` = VALUES(`pk_turno`), `data` = VALUES(`data`), `hora_abertura` = VALUES(`hora_abertura`), `hora_fecho` = VALUES(`hora_fecho`), `turno` = VALUES(`turno`), `fk_usuario` = VALUES(`fk_usuario`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_turno_au` AFTER UPDATE ON `kitanda_db_1`.`turno` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`turno` SET `pk_turno` = NEW.`pk_turno`, `data` = NEW.`data`, `hora_abertura` = NEW.`hora_abertura`, `hora_fecho` = NEW.`hora_fecho`, `turno` = NEW.`turno`, `fk_usuario` = NEW.`fk_usuario` WHERE `pk_turno` = OLD.`pk_turno`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`turno` (`pk_turno`, `data`, `hora_abertura`, `hora_fecho`, `turno`, `fk_usuario`) VALUES (NEW.`pk_turno`, NEW.`data`, NEW.`hora_abertura`, NEW.`hora_fecho`, NEW.`turno`, NEW.`fk_usuario`) ON DUPLICATE KEY UPDATE `pk_turno` = VALUES(`pk_turno`), `data` = VALUES(`data`), `hora_abertura` = VALUES(`hora_abertura`), `hora_fecho` = VALUES(`hora_fecho`), `turno` = VALUES(`turno`), `fk_usuario` = VALUES(`fk_usuario`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_turno_ad` AFTER DELETE ON `kitanda_db_1`.`turno` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`turno` WHERE `pk_turno` = OLD.`pk_turno`;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_unidade_ai` AFTER INSERT ON `kitanda_db_1`.`unidade` FOR EACH ROW BEGIN
  INSERT INTO `kitanda_db`.`unidade` (`pk_unidade`, `descricao`, `abreviacao`) VALUES (NEW.`pk_unidade`, NEW.`descricao`, NEW.`abreviacao`) ON DUPLICATE KEY UPDATE `pk_unidade` = VALUES(`pk_unidade`), `descricao` = VALUES(`descricao`), `abreviacao` = VALUES(`abreviacao`);
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_unidade_au` AFTER UPDATE ON `kitanda_db_1`.`unidade` FOR EACH ROW BEGIN
  UPDATE `kitanda_db`.`unidade` SET `pk_unidade` = NEW.`pk_unidade`, `descricao` = NEW.`descricao`, `abreviacao` = NEW.`abreviacao` WHERE `pk_unidade` = OLD.`pk_unidade`;
  IF ROW_COUNT() = 0 THEN
    INSERT INTO `kitanda_db`.`unidade` (`pk_unidade`, `descricao`, `abreviacao`) VALUES (NEW.`pk_unidade`, NEW.`descricao`, NEW.`abreviacao`) ON DUPLICATE KEY UPDATE `pk_unidade` = VALUES(`pk_unidade`), `descricao` = VALUES(`descricao`), `abreviacao` = VALUES(`abreviacao`);
  END IF;
END$$

CREATE TRIGGER `kitanda_db_1`.`esp_unidade_ad` AFTER DELETE ON `kitanda_db_1`.`unidade` FOR EACH ROW BEGIN
  DELETE FROM `kitanda_db`.`unidade` WHERE `pk_unidade` = OLD.`pk_unidade`;
END$$

DELIMITER ;
