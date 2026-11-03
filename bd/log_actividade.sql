-- =============================================================
-- Auditoria: regista as actividades dos utilizadores no sistema
-- (quem criou/alterou/eliminou o quê, quando e em que máquina)
--
-- Relação lógica (sem FOREIGN KEY):
--   log_actividade.cod_usuario -> tb_usuario.codigo
--
-- data_hora não tem DEFAULT porque o MySQL 5.1 não suporta
-- DATETIME DEFAULT CURRENT_TIMESTAMP; é preenchido com NOW() no INSERT
-- (ver util.LogActividadeUtil).
-- =============================================================

CREATE TABLE IF NOT EXISTS log_actividade (
  pk_log_actividade BIGINT       NOT NULL AUTO_INCREMENT,
  data_hora         DATETIME     NOT NULL,
  cod_usuario       INT          NULL,
  nome_usuario      VARCHAR(100) NULL,
  modulo            VARCHAR(50)  NULL,
  formulario        VARCHAR(100) NULL,
  accao             VARCHAR(30)  NULL,
  entidade          VARCHAR(50)  NULL,
  id_registo        VARCHAR(50)  NULL,
  descricao         VARCHAR(500) NULL,
  valor_anterior    TEXT         NULL,
  valor_novo        TEXT         NULL,
  nome_maquina      VARCHAR(75)  NULL,
  ip_maquina        VARCHAR(75)  NULL,
  PRIMARY KEY (pk_log_actividade),
  KEY idx_log_act_data (data_hora),
  KEY idx_log_act_usuario (cod_usuario),
  KEY idx_log_act_entidade (entidade, id_registo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
