/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Domingos Dala Vunge
 * Created: 28/06/2023
 */


DELIMITER $$

DROP PROCEDURE IF EXISTS `esvazia_banco_dados` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `esvazia_banco_dados`()
BEGIN

	-- reserva
	delete from reserva where pk_reserva <> 0; -- dependente do produto, pedido
	ALTER TABLE reserva AUTO_INCREMENT = 1 ;
    -- reserva

	delete from alojamento where idAlojamento <> 0; -- dependente do produto, pedido

	ALTER TABLE alojamento AUTO_INCREMENT = 1 ;

    -- tb_item_pedidos







	delete from tb_item_pedidos where pk_item_pedidos <> 0; -- dependente do produto, pedido







	ALTER TABLE tb_item_pedidos AUTO_INCREMENT = 1 ;















	-- tb_pedido







	delete from tb_pedido where pk_pedido <> 0; 







	ALTER TABLE tb_pedido AUTO_INCREMENT = 1 ;















	-- tb_item_venda







	delete from tb_item_venda where codigo <> 0; -- dependente do produto







	ALTER TABLE tb_item_venda AUTO_INCREMENT = 1 ;















	-- tb_item_saidas







	delete from tb_item_saidas where codigo <> 0; -- dependente do produto







	ALTER TABLE tb_item_saidas AUTO_INCREMENT = 1 ;















	-- tb_operacao_sistema







	delete from tb_operacao_sistema where pk_operacao_sistema <> 0;







	ALTER TABLE tb_operacao_sistema AUTO_INCREMENT = 1 ;















	-- tb_preco







	delete from tb_preco where pk_preco <> 0; -- dependente do produto







	ALTER TABLE tb_preco AUTO_INCREMENT = 1 ;















	-- tb_stock







	delete from tb_stock where codigo <> 0; -- dependente do produto







	ALTER TABLE tb_stock AUTO_INCREMENT = 1 ;







	







    -- tb_stock_mirrow







	delete from tb_stock_mirrow where codigo <> 0; -- dependente do produto







	ALTER TABLE tb_stock_mirrow AUTO_INCREMENT = 1 ;







	







    -- produto_isento







	delete from produto_isento where pk_produto_isento <> 0;







	ALTER TABLE produto_isento AUTO_INCREMENT = 1 ;







	







    -- produto_imposto







	delete from produto_imposto where pk_produto_imposto <> 0;







	ALTER TABLE produto_imposto AUTO_INCREMENT = 1 ;















	-- tb_item_pro_forma







	delete from tb_item_pro_forma where pk_item_pro_forma <> 0; -- dependente do produto







	ALTER TABLE tb_item_pro_forma AUTO_INCREMENT = 1 ;















	-- tb_desconto







	delete from tb_desconto where idDesconto <> 0; -- dependente do produto







	ALTER TABLE  tb_desconto AUTO_INCREMENT = 1 ;delete from  tb_acerto where idAcerto <> 0; -- dependente do produto















	-- tb_item_encomenda







	delete from  tb_item_encomenda where idEncomenda <> 0; -- dependente do produto







	ALTER TABLE  tb_item_encomenda AUTO_INCREMENT = 1 ;















	-- tb_item_encomenda







	delete from  item_compras where pk_itm_compras <> 0; -- dependente do produto







	ALTER TABLE  item_compras AUTO_INCREMENT = 1 ;















	-- tb_entrada







	delete from  tb_entrada where idEntrada <> 0;







	ALTER TABLE  tb_entrada AUTO_INCREMENT = 1 ;















	-- tb_item_entrada_vasilhame







	delete from  tb_item_entrada_vasilhame where pk_item_entrada_vasilhame <> 0; 







	ALTER TABLE  tb_item_entrada_vasilhame AUTO_INCREMENT = 1 ;







	







    -- tb_item_saidas







	delete from  tb_item_saidas where codigo <> 0; 







	ALTER TABLE  tb_item_saidas AUTO_INCREMENT = 1 ;







	







    -- tb_saidas_produtos







	delete from  tb_saidas_produtos where pk_saidas_produtos <> 0;







	ALTER TABLE  tb_saidas_produtos AUTO_INCREMENT = 1 ;







	







    -- turno







	delete from  turno where pk_turno <> 0;







	ALTER TABLE  turno AUTO_INCREMENT = 1 ;















	-- notas_item_compras







	delete from  notas_item_compras where pk_notas_item <> 0;







	ALTER TABLE  notas_item_compras AUTO_INCREMENT = 1 ;







	







    -- notas_item







	delete from  notas_item where pk_notas_item <> 0;







	ALTER TABLE  notas_item AUTO_INCREMENT = 1 ;















	-- notas







	delete from  notas where pk_nota <> 0;







	ALTER TABLE  notas AUTO_INCREMENT = 1 ;







    







    -- notas_compras







    delete from  notas_compras where pk_nota_compras <> 0;







	ALTER TABLE  notas_compras AUTO_INCREMENT = 1 ;







    







    -- tb_venda







	delete from  tb_venda where codigo <> 0;







	ALTER TABLE  tb_venda AUTO_INCREMENT = 1 ;















	-- compras







	delete from  compras where pk_compra <> 0;







	ALTER TABLE  compras AUTO_INCREMENT = 1 ;















	-- tb_produto







	delete from  tb_produto where codigo <> 0;







	ALTER TABLE  tb_produto AUTO_INCREMENT = 1 ;























	-- subfamilia







	delete from  tb_tipo_produto where codigo <> 0;







	ALTER TABLE  tb_tipo_produto AUTO_INCREMENT = 1 ;















	-- familia







	delete from familia where pk_familia <> 0;







	ALTER TABLE familia AUTO_INCREMENT = 1 ;







	INSERT INTO familia(designacao) VALUES("Serviços");







    INSERT INTO familia(designacao) VALUES("Produtos");















	-- grupo







	delete from  grupo where pk_grupo <> 0;







	ALTER TABLE  grupo AUTO_INCREMENT = 1 ;







	INSERT INTO grupo(designacao) VALUES("Diferenciado");























	-- modelo







	delete from  modelo where pk_modelo <> 0;







	ALTER TABLE  modelo AUTO_INCREMENT = 1 ;























	-- marca







	delete from  marca where pk_marca <> 0;







	ALTER TABLE  marca AUTO_INCREMENT = 1 ;	















	-- fornecedor







	delete from  tb_fornecedor where codigo <> 0;







	ALTER TABLE  tb_fornecedor AUTO_INCREMENT = 1 ;















	-- cliente







	delete from  tb_cliente where codigo <> 0;







	ALTER TABLE  tb_cliente AUTO_INCREMENT = 1 ;















	







	-- INSERT´S







	INSERT INTO  marca(designacao) VALUES("Diferenciado");







	INSERT INTO  modelo(designacao, fk_marca) VALUES("Diferenciado", 1);







	INSERT INTO  tb_fornecedor(nome, telefone, email, site, endereco, nif, fk_regime) VALUES("Diferenciado", "999-999-999", "N/A", "N/A", "N/A", "999999999", 3, "Singular");







	INSERT INTO  tb_cliente(nome, morada, telefone, nif, email) VALUES("Consumidor Final", "N/A", "999999999", "999999999", "N/A");







    







    







    







    















	-- tb_item_permissao







	delete from  tb_item_permissao where idUsuario <> 15;







	ALTER TABLE  tb_item_permissao AUTO_INCREMENT = 1 ;















	delete from tb_usuario where codigo <> 15;























	/* RESET DOCUMENTOS */

	UPDATE documento SET cod_ultimo_doc = 0 , descricao_ultimo_doc = null , ultima_data = "2020-01-01" WHERE pk_documento > 0;

END $$

DELIMITER ;