# Gera os tickets da lavandaria (Jasper 5): recolha_cliente, recolha_lavandaria, lv_entrega_cliente,
# ticket_recolha_unico e os sub-relatórios sub_recolha_itens / sub_recolha_pagamento.
# Uso: python _gerar_tickets_lavandaria.py <pasta relatorios> [pasta de origem]
# A consulta e os campos de cada relatório principal vêm do .jrxml de origem (por omissão o próprio
# ficheiro da pasta relatorios: os gerados guardam a consulta original).
#
# Regra contra sobreposições: cada elemento começa em y >= y_anterior + altura REAL do anterior
# (alt(h, t): campo()/texto() sobem a altura até caber a letra) e tudo o que vem depois de um campo
# que estica é positionType="Float".
import sys, os, uuid, re
from xml.sax.saxutils import escape

REL = sys.argv[1]  # pasta relatorios
ORIGEM = sys.argv[2] if len(sys.argv) > 2 else REL  # de onde vêm consulta e campos
FONTE = "SansSerif"
PRETO = "#000000"
CINZA = "#555555"


def uid():
    return str(uuid.uuid4())


def caixa(top=0, bottom=0, left=0, right=0, estilo="Solid", w=0.75):
    if not (top or bottom or left or right):
        return ""
    s = '<box>'
    for nome, on in (("topPen", top), ("leftPen", left), ("bottomPen", bottom), ("rightPen", right)):
        if on:
            s += '<%s lineWidth="%s" lineStyle="%s"/>' % (nome, w, estilo)
    return s + '</box>'


def re_(x, y, w, h, float_=False, stretch=False, quando=None, cor=None, fundo=None, remover=False):
    a = 'positionType="Float" ' if float_ else ''
    a += 'stretchType="RelativeToTallestObject" ' if stretch == "rel" else ''
    a += 'isRemoveLineWhenBlank="true" ' if remover else ''
    if fundo:
        a += 'mode="Opaque" backcolor="%s" ' % fundo
    if cor:
        a += 'forecolor="%s" ' % cor
    s = '<reportElement %sx="%d" y="%d" width="%d" height="%d" uuid="%s">' % (a, x, y, w, h, uid())
    if quando:
        s += '<printWhenExpression><![CDATA[%s]]></printWhenExpression>' % quando
    return s + '</reportElement>'


def alt_min(t):
    """Altura mínima de uma linha de texto de t pt (SansSerif)."""
    return int(int(t + 0.5) * 1.32 + 1.5)


def alt(h, t):
    """Altura real com que campo()/texto() desenham um elemento pedido com altura h e letra t."""
    return max(h, alt_min(t))


def fonte(t, negrito=False, italico=False, alin="Left", valin="Middle", html=False):
    m = ' markup="html"' if html else ''
    return ('<textElement textAlignment="%s" verticalAlignment="%s"%s><font fontName="%s" size="%s" isBold="%s" isItalic="%s"/></textElement>'
            % (alin, valin, m, FONTE, int(t + 0.5), "true" if negrito else "false", "true" if italico else "false"))


def campo(x, y, w, h, expr, t=8, negrito=False, italico=False, alin="Left", padrao=None, esticar=False, float_=False,
          quando=None, cor=None, fundo=None, vazio=True, html=False, caixa_="", remover=False, valin="Middle"):
    h = max(h, alt_min(t))
    p = ' pattern="%s"' % padrao if padrao else ''
    st = ' isStretchWithOverflow="true"' if esticar else ''
    bl = ' isBlankWhenNull="true"' if vazio else ''
    if esticar:
        # 1 pt de folga em baixo: o Jasper 5 arredonda a altura esticada para baixo e cortava a última linha
        caixa_ = caixa_.replace('<box>', '<box bottomPadding="1">') if caixa_ else '<box bottomPadding="1"/>'
    return ('<textField%s%s%s>%s%s%s<textFieldExpression><![CDATA[%s]]></textFieldExpression></textField>'
            % (st, bl, p, re_(x, y, w, h, float_, False, quando, cor, fundo, remover), caixa_,
               fonte(t, negrito, italico, alin, valin, html), expr))


def texto(x, y, w, h, s, t=8, negrito=False, italico=False, alin="Left", float_=False, quando=None, cor=None, fundo=None,
          caixa_="", valin="Middle"):
    h = max(h, alt_min(t))
    return ('<staticText>%s%s%s<text><![CDATA[%s]]></text></staticText>'
            % (re_(x, y, w, h, float_, False, quando, cor, fundo), caixa_, fonte(t, negrito, italico, alin, valin), s))


def linha(x, y, w, tracejada=False, float_=False, grossura=0.75, quando=None):
    est = "Dashed" if tracejada else "Solid"
    return ('<line>%s<graphicElement><pen lineWidth="%s" lineStyle="%s"/></graphicElement></line>'
            % (re_(x, y, w, 1, float_, False, quando), grossura, est))


def quadro(x, y, w, h, filhos, float_=False, fundo=None, borda=0, quando=None, cor=None):
    b = caixa(borda, borda, borda, borda, "Solid", borda) if borda else ""
    return '<frame>%s%s%s</frame>' % (re_(x, y, w, h, float_, False, quando, cor, fundo), b, "".join(filhos))


def banda(h, filhos, quebra="Stretch"):
    corpo = "".join(filhos)
    # a banda tem de conter todos os elementos (os de dentro dos quadros têm y relativo: só podem aumentar a conta)
    for m in re.finditer(r'<reportElement [^>]*?y="(\d+)" width="\d+" height="(\d+)"', corpo):
        h = max(h, int(m.group(1)) + int(m.group(2)))
    return '<band height="%d" splitType="%s">%s</band>' % (h, quebra, corpo)


def cabecalho_original(nome):
    """Tudo até <background> (parâmetros, consulta, campos, variáveis) do relatório original."""
    s = open(os.path.join(ORIGEM, nome), encoding="utf-8").read()
    i = s.index("<background>")
    return s[:i]


def gravar(nome, s):
    open(os.path.join(REL, nome), "w", encoding="utf-8", newline="\n").write(s)
    print("gravado", nome)


def novo(nome, largura, altura, margem_esq, margem_dir, corpo_cabecalho, ignorar_paginacao=True):
    col = largura - margem_esq - margem_dir
    return ('<?xml version="1.0" encoding="UTF-8"?>\n'
            '<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" '
            'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
            'xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" '
            'name="%s" pageWidth="%d" pageHeight="%d" columnWidth="%d" leftMargin="%d" rightMargin="%d" topMargin="0" '
            'bottomMargin="8" isIgnorePagination="%s" uuid="%s">\n%s<background><band splitType="Stretch"/></background>'
            % (nome, largura, altura, col, margem_esq, margem_dir, "true" if ignorar_paginacao else "false", uid(), corpo_cabecalho))


# ---------------------------------------------------------------------------------
# medidas (papel de 80 mm: 200 pt de largura, área útil 188)
L = 200
ME, MD = 8, 4
W = L - ME - MD

FMT_MOEDA = "#,##0.00"
DATA_HORA = 'new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format($F{tb_venda_dataVenda})'
DATA = '(%s == null ? "" : new java.text.SimpleDateFormat("dd/MM/yyyy").format(%s))'


def vazio(f):
    return '(%s == null || %s.toString().trim().isEmpty())' % (f, f)


def nao_vazio(f):
    return '!' + vazio(f)


# =================================================================================
# SUB-RELATÓRIO: peças (Qtd | Peça + observação | Valor)
# =================================================================================
sub_itens_cab = '''<parameter name="CODIGO_VENDA" class="java.lang.Integer"/>
<queryString><![CDATA[SELECT
     tb_item_venda.`quantidade` AS qtd,
     tb_produto.`designacao` AS peca,
     tb_item_venda.`obs` AS obs,
     tb_preco.`preco_venda` AS preco,
     tb_item_venda.`data_entrega` AS data_entrega
FROM `tb_item_venda` tb_item_venda
     INNER JOIN `tb_produto` tb_produto ON tb_produto.`codigo` = tb_item_venda.`codigo_produto`
     INNER JOIN `tb_preco` tb_preco ON tb_item_venda.`fk_preco` = tb_preco.`pk_preco`
WHERE tb_item_venda.`codigo_venda` = $P{CODIGO_VENDA}
ORDER BY tb_item_venda.`posicao`, tb_item_venda.`codigo`]]></queryString>
<field name="qtd" class="java.lang.Double"/>
<field name="peca" class="java.lang.String"/>
<field name="obs" class="java.lang.String"/>
<field name="preco" class="java.math.BigDecimal"/>
<field name="data_entrega" class="java.sql.Timestamp"/>
<variable name="PECAS" class="java.lang.Double" calculation="Sum"><variableExpression><![CDATA[$F{qtd}]]></variableExpression></variable>
'''
QW, VW = 20, 56
PW = W - QW - VW
cab_itens = banda(14, [
    texto(0, 0, QW, 12, "Qtd", 7, True, alin="Center"),
    texto(QW, 0, PW, 12, "Peça / Observação", 7, True),
    texto(QW + PW, 0, VW, 12, "Valor (AOA)", 7, True, alin="Right"),
    linha(0, 12, W, grossura=1.0),
])
det_itens = banda(26, [
    campo(0, 2, QW, 11, '$F{qtd}', 8, True, alin="Center", padrao="#,##0.##"),
    campo(QW, 2, PW, 11, '$F{peca}', 8, True, esticar=True),
    campo(QW + PW, 2, VW, 11, '$F{preco}.doubleValue() * $F{qtd}', 8, True, alin="Right", padrao=FMT_MOEDA),
    campo(QW, 2 + alt(11, 8), PW, 10, '$F{obs}', 6.5, italico=True, esticar=True, float_=True, cor=CINZA,
          quando=nao_vazio('$F{obs}'), remover=True),
    linha(0, 2 + alt(11, 8) + alt(10, 6.5) + 1, W, tracejada=True, float_=True, grossura=0.5),
])
sum_itens = banda(16, [
    campo(0, 2, W, 12, '"Total de peças: " + new java.text.DecimalFormat("#,##0.##").format($V{PECAS})', 8, True, alin="Right"),
])
s = novo("sub_recolha_itens", W, 400, 0, 0, sub_itens_cab).replace('bottomMargin="8"', 'bottomMargin="0"')
s += '<columnHeader>%s</columnHeader><detail>%s</detail><summary>%s</summary></jasperReport>\n' % (cab_itens, det_itens, sum_itens)
gravar("sub_recolha_itens.jrxml", s)

# =================================================================================
# SUB-RELATÓRIO: formas de pagamento (Dinheiro ......... 2.000,00)
# =================================================================================
sub_pag_cab = '''<parameter name="CODIGO_VENDA" class="java.lang.Integer"/>
<queryString><![CDATA[SELECT forma_pagamento.`designacao` AS forma, forma_pagamento_item.`valor` AS valor,
     forma_pagamento_item.`referencia` AS referencia
FROM `forma_pagamento` forma_pagamento INNER JOIN `forma_pagamento_item` forma_pagamento_item
     ON forma_pagamento.`pk_forma_pagamento` = forma_pagamento_item.`fk_forma_pagamento`
WHERE forma_pagamento_item.`fk_venda` = $P{CODIGO_VENDA} AND forma_pagamento_item.`valor` > 0]]></queryString>
<field name="forma" class="java.lang.String"/>
<field name="valor" class="java.math.BigDecimal"/>
<field name="referencia" class="java.lang.String"/>
'''
det_pag = banda(13, [
    campo(0, 0, W - 70, 11, '$F{forma} + (' + nao_vazio('$F{referencia}') + ' && !"n/a".equalsIgnoreCase($F{referencia}.trim()) ? " (" + $F{referencia}.trim() + ")" : "")', 7.5),
    campo(W - 70, 0, 70, 11, '$F{valor}', 7.5, alin="Right", padrao=FMT_MOEDA),
])
s = novo("sub_recolha_pagamento", W, 200, 0, 0, sub_pag_cab).replace('bottomMargin="8"', 'bottomMargin="0"')
s += '<detail>%s</detail></jasperReport>\n' % det_pag
gravar("sub_recolha_pagamento.jrxml", s)


def sub(y, nome, h=14):
    return ('<subreport>%s<subreportParameter name="CODIGO_VENDA"><subreportParameterExpression><![CDATA[$P{CODIGO_VENDA}]]>'
            '</subreportParameterExpression></subreportParameter><connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]>'
            '</connectionExpression><subreportExpression><![CDATA[$P{SUBREPORT_DIR} + "%s"]]></subreportExpression></subreport>'
            % (re_(0, y, W, h, True), nome))


# =================================================================================
# CABEÇALHO COMUM (empresa, documento, cliente, data de entrega)
# =================================================================================
def empresa(y, compacto=False):
    e = []
    t = 9 if compacto else 11
    e.append(campo(0, y, W, 0, '$F{tb_dados_instituicao_nome}', t, True, alin="Center", esticar=True, float_=True))
    y += alt(0, t)
    e.append(campo(0, y, W, 0, '$F{tb_dados_instituicao_enderecos}', 6.5, alin="Center", esticar=True, float_=True))
    y += alt(0, 6.5)
    e.append(campo(0, y, W, 0, '"NIF: " + $F{tb_dados_instituicao_nif} + "   ·   Tel.: " + $F{tb_dados_instituicao_telefone}',
                   6.5, alin="Center", esticar=True, float_=True))
    y += alt(0, 6.5)
    if not compacto:
        e.append(campo(0, y, W, 0, '$F{tb_dados_instituicao_email}', 6.5, alin="Center", esticar=True, float_=True,
                       quando=nao_vazio('$F{tb_dados_instituicao_email}'), remover=True))
        y += alt(0, 6.5)
    e.append(linha(0, y + 2, W, tracejada=True, float_=True))
    return e, y + 5


def cliente(y, nif='$F{tb_venda_cliente_nif}'):
    yy = 2
    filhos = [texto(4, yy, W - 8, 0, "CLIENTE", 6, True, cor=CINZA)]
    yy += alt(0, 6)
    filhos.append(campo(4, yy, W - 8, 0, '$F{tb_cliente_nome}', 9, True, esticar=True, float_=True))
    yy += alt(0, 9)
    # só o que existe: "NIF: x · Tel.: y", "NIF: x", "Tel.: y" ou nada (linha removida)
    tem_nif, tem_tel = nao_vazio(nif), nao_vazio('$F{tb_cliente_telefone}')
    filhos.append(campo(4, yy, W - 8, 0, '(%s ? "NIF: " + %s.trim() : "") + (%s ? (%s ? "   ·   " : "") + "Tel.: " + $F{tb_cliente_telefone}.trim() : "")'
                        % (tem_nif, nif, tem_tel, tem_nif),
                        7, esticar=True, float_=True, quando=tem_nif + ' || ' + tem_tel, remover=True))
    yy += alt(0, 7)
    filhos.append(campo(4, yy, W - 8, 0, '$F{tb_cliente_morada}', 7, esticar=True, float_=True,
                        quando=nao_vazio('$F{tb_cliente_morada}') + ' && !"N/A".equalsIgnoreCase($F{tb_cliente_morada}.trim())',
                        remover=True))
    yy += alt(0, 7) + 1
    return quadro(0, y, W, yy, filhos, float_=True, borda=0.75), y + yy + 4


def faixa(y, rotulo, expr, h, t_rot, t_val, rot_w=84):
    """Faixa preta: rótulo à esquerda, valor à direita, sem as caixas se tocarem."""
    filhos = [
        texto(6, 0, rot_w, h, rotulo, t_rot, True, cor="#FFFFFF"),
        campo(6 + rot_w, 0, W - 12 - rot_w, h, expr, t_val, True, alin="Right", cor="#FFFFFF"),
    ]
    h = max(h, alt_min(t_rot), alt_min(t_val))
    return quadro(0, y, W, h, filhos, float_=True, fundo=PRETO, cor=PRETO), y + h


def data_entrega(y, grande=False):
    q, y = faixa(y, "DATA DE ENTREGA", DATA % ('$F{tb_venda_dataVencimento}', '$F{tb_venda_dataVencimento}'),
                 28 if grande else 22, 8, 15 if grande else 12, rot_w=86)
    return q, y + 4


def rodape_validacao(y, extra=""):
    return campo(0, y, W, 0, '($F{tb_venda_assinatura} == null ? "" : $F{tb_venda_assinatura}) + "-Processado por programa validado nº 258/AGT/2020 " + $P{SOFTWARE_NAME}' + extra,
                 6, alin="Center", esticar=True, float_=True), y + alt(0, 6)


def extenso(y):
    # caixa com tracejado em cima e em baixo; o campo estica e a folga de baixo evita cortar a última linha
    return campo(0, y, W, 0, '$F{tb_venda_total_por_extenso}', 7, italico=True, alin="Center", esticar=True, float_=True,
                 caixa_=caixa(1, 1, 0, 0, "Dashed", 0.5)), y + alt(0, 7) + 3


def documento_e_numero(y, grande, designacao):
    t = []
    t.append(campo(0, y + 2, W, 0, designacao, 8, True, alin="Center", esticar=True, float_=True))
    y += 2 + alt(0, 8)
    tn = 20 if grande else 15
    t.append(campo(0, y, W, 0, '$F{tb_venda_cod_fact}', tn, True, alin="Center", float_=True))
    y += alt(0, tn)
    return t, y


def finalizar(nome_ficheiro, titulo, resumo):
    cab = cabecalho_original(nome_ficheiro)
    s = novo(os.path.splitext(nome_ficheiro)[0], L, 842, ME, MD, cab[cab.index(">", cab.index("<jasperReport")) + 1:])
    # sem <detail>: a consulta agrupa por fk_lugares e pode devolver mais de uma linha; as peças
    # (sub-relatório) vão no resumo, que só se imprime uma vez
    s += '<title>%s</title><summary>%s</summary></jasperReport>\n' % (titulo, resumo)
    gravar(nome_ficheiro, s)


# =================================================================================
# TALÃO DO CLIENTE / VIA DA LAVANDARIA
# =================================================================================
def talao(nome_ficheiro, via_lavandaria):
    t = []
    y = 0
    if via_lavandaria:
        t.append(texto(0, 0, W, 0, "VIA DA LAVANDARIA", 8.5, True, alin="Center", fundo=PRETO, cor="#FFFFFF"))
        y = alt(0, 8.5) + 2
    e, y = empresa(y, compacto=via_lavandaria)
    t += e
    d, y = documento_e_numero(y, via_lavandaria,
                              '(' + nao_vazio('$F{documento_designacao}') + ' ? $F{documento_designacao} : ($P{DOCUMENTO} == null ? "" : $P{DOCUMENTO})).toUpperCase()')
    t += d
    t.append(campo(0, y, W, 0, '$P{STATUS_DOCUMENTO}', 6.5, italico=True, alin="Center", float_=True,
                   quando=nao_vazio('$P{STATUS_DOCUMENTO}'), remover=True))
    y += alt(0, 6.5) + 1
    t.append(campo(0, y, W, 0, '"Data da recolha: " + ' + DATA_HORA, 7, float_=True))
    y += alt(0, 7)
    t.append(campo(0, y, W, 0, '"Operador: " + $F{tb_usuario_nome}', 7, esticar=True, float_=True))
    y += alt(0, 7) + 3
    q, y = cliente(y)
    t.append(q)
    q, y = data_entrega(y, grande=via_lavandaria)
    t.append(q)
    titulo = banda(int(y), t)

    r = [sub(0, "sub_recolha_itens.jasper", 16)]
    y = 20
    if not via_lavandaria:
        linhas = [
            ("Total ilíquido", '$F{tb_venda_total_geral}', None),
            ("Desconto comercial", '$F{tb_venda_desconto_comercial}', '$F{tb_venda_desconto_comercial} != null && $F{tb_venda_desconto_comercial}.doubleValue() > 0'),
            ("Desconto do cliente", '$F{tb_venda_desconto_financeiro}', '$F{tb_venda_desconto_financeiro} != null && $F{tb_venda_desconto_financeiro}.doubleValue() > 0'),
            ("IVA", '$F{tb_venda_total_iva}', None),
        ]
        for rot, expr, quando in linhas:
            neg = "Desconto" in rot
            r.append(texto(0, y, W - 84, 0, rot, 7.5, float_=True, quando=quando))
            r.append(campo(W - 84, y, 84, 0, ('"- " + ' if neg else '') + 'new java.text.DecimalFormat("#,##0.00").format(' + expr + ')',
                           7.5, alin="Right", float_=True, quando=quando, remover=True))
            y += alt(0, 7.5)
        y += 3
    # total em destaque
    q, y = faixa(y, "TOTAL A PAGAR" if not via_lavandaria else "TOTAL",
                 'new java.text.DecimalFormat("#,##0.00").format($F{tb_venda_total_venda}) + " " + ($F{moeda_abreviacao} == null ? "AOA" : $F{moeda_abreviacao})',
                 20, 9, 11.5, rot_w=76)
    r.append(q)
    y += 3
    if via_lavandaria:
        # estado do pagamento: FR já pago, FT paga-se na entrega
        r.append(campo(0, y, W, 14, '"FR".equals($F{documento_abreviacao}) ? "PAGO" : "A PAGAR NA ENTREGA"', 9, True,
                       alin="Center", float_=True, caixa_=caixa(1, 1, 1, 1, "Solid", 1)))
        y += alt(14, 9) + 4
    else:
        r.append(texto(0, y, W, 0, "Pagamento", 6.5, True, float_=True, cor=CINZA))
        y += alt(0, 6.5)
        r.append(sub(y, "sub_recolha_pagamento.jasper", 12))
        y += 13
        r.append(texto(0, y, W - 84, 0, "Troco", 8, True, float_=True))
        r.append(campo(W - 84, y, 84, 0, '$F{tb_venda_troco}', 8, True, alin="Right", padrao=FMT_MOEDA, float_=True))
        y += alt(0, 8) + 2
    q, y = extenso(y)
    r.append(q)
    if not via_lavandaria:
        # resumo fiscal
        hc = alt(0, 6.5)
        r.append(quadro(0, y, W, 2 * hc + 1, [
            texto(0, 0, 62, 0, "Incidência", 6.5, True, fundo="#E5E5E5"),
            texto(62, 0, 50, 0, "IVA", 6.5, True, alin="Right", fundo="#E5E5E5"),
            texto(112, 0, W - 112, 0, "  Motivo de isenção", 6.5, True, fundo="#E5E5E5"),
            campo(0, hc + 1, 62, 0, '($F{tb_venda_total_incidencia} == null ? 0d : $F{tb_venda_total_incidencia}.doubleValue()) + ($F{tb_venda_total_incidencia_isento} == null ? 0d : $F{tb_venda_total_incidencia_isento}.doubleValue())',
                  6.5, padrao=FMT_MOEDA),
            campo(62, hc + 1, 50, 0, '$F{tb_venda_total_iva}', 6.5, alin="Right", padrao=FMT_MOEDA),
            campo(112, hc + 1, W - 112, 0, '"  " + ($P{MOTIVO_ISENCAO} == null ? "" : $P{MOTIVO_ISENCAO})', 6.5, esticar=True),
        ], float_=True))
        y += 2 * hc + 4
        r.append(campo(0, y, W, 0, '"Coordenadas bancárias: " + $F{tb_dados_instituicao_conta_bancaria1}', 6.5, alin="Center",
                       esticar=True, float_=True, quando=nao_vazio('$F{tb_dados_instituicao_conta_bancaria1}'), remover=True))
        y += alt(0, 6.5) + 1
        r.append(campo(0, y, W, 0, '"Estimado cliente, verifique e confira a sua roupa antes de sair: não aceitamos reclamações '
                                   'depois da saída da roupa. Não nos responsabilizamos por roupas não levantadas no prazo máximo '
                                   'de 60 dias, nem por nódoas que pela sua natureza já não saem ou por tecidos pouco resistentes. '
                                   'Em caso de dano, respondemos por 50% do valor da peça."',
                       6, alin="Justified", esticar=True, float_=True))
        y += alt(0, 6) + 2
    r.append(linha(0, y + 1, W, tracejada=True, float_=True))
    y += 4
    q, y = rodape_validacao(y)
    r.append(q)
    if not via_lavandaria:
        r.append(texto(0, y + 1, W, 0, "Obrigado pela preferência!", 8.5, True, alin="Center", float_=True))
        y += 1 + alt(0, 8.5)
    finalizar(nome_ficheiro, titulo, banda(int(y), r))


talao("recolha_cliente.jrxml", False)
talao("recolha_lavandaria.jrxml", True)


# =================================================================================
# TALÃO DE ENTREGA (documento EL, impresso ao entregar a roupa)
# =================================================================================
def entrega(nome_ficheiro):
    t = []
    e, y = empresa(0)
    t += e
    d, y = documento_e_numero(y, False,
                              '(' + nao_vazio('$F{documento_designacao}') + ' ? $F{documento_designacao} : "Nota de entrega").toUpperCase()')
    t += d
    t.append(campo(0, y, W, 0, '"Recolha: " + $F{tb_venda_ref_cod_fact}', 8, True, alin="Center", float_=True,
                   quando=nao_vazio('$F{tb_venda_ref_cod_fact}'), remover=True))
    y += alt(0, 8) + 2
    t.append(campo(0, y, W, 0, '"Data da entrega: " + ' + DATA_HORA, 7, float_=True))
    y += alt(0, 7)
    t.append(campo(0, y, W, 0, '"Operador: " + $F{tb_usuario_nome}', 7, esticar=True, float_=True))
    y += alt(0, 7) + 3
    q, y = cliente(y, nif='$F{tb_cliente_nif}')
    t.append(q)
    titulo = banda(int(y), t)

    r = [sub(0, "sub_recolha_itens.jasper", 16)]
    y = 20
    r.append(campo(0, y, W, 0, '"Obs.: " + $F{tb_venda_obs}', 7, italico=True, esticar=True, float_=True,
                   quando=nao_vazio('$F{tb_venda_obs}'), remover=True))
    y += alt(0, 7) + 2
    r.append(texto(0, y, W, 16, "PEÇAS ENTREGUES", 9, True, alin="Center", float_=True, fundo=PRETO, cor="#FFFFFF"))
    y += alt(16, 9) + 14
    # assinatura de quem levanta a roupa
    r.append(linha(20, y, W - 40, float_=True, grossura=0.5))
    r.append(texto(0, y + 1, W, 0, "Recebi as peças acima em boas condições (assinatura)", 6, alin="Center", float_=True, cor=CINZA))
    y += 1 + alt(0, 6) + 3
    r.append(texto(0, y, W, 0, "Este documento não serve de factura.", 7, True, alin="Center", float_=True))
    y += alt(0, 7)
    r.append(linha(0, y + 1, W, tracejada=True, float_=True))
    y += 4
    q, y = rodape_validacao(y)
    r.append(q)
    finalizar(nome_ficheiro, titulo, banda(int(y), r))


entrega("lv_entrega_cliente.jrxml")

# =================================================================================
# ETIQUETA DE CADA PEÇA (vai com a roupa)
# =================================================================================
cab = cabecalho_original("ticket_recolha_unico.jrxml")
cab = cab[cab.index(">", cab.index("<jasperReport")) + 1:]
et = [
    quadro(0, 0, W, 16, [
        campo(5, 0, W - 86, 16, '$F{cod_fact} + "  ·  Peça " + $F{posicao}', 9, True, cor="#FFFFFF"),
        campo(W - 80, 0, 75, 16, '($F{data_entrega} == null ? "" : "Entrega " + new java.text.SimpleDateFormat("dd/MM").format($F{data_entrega}))', 9, True,
              alin="Right", cor="#FFFFFF"),
    ], fundo=PRETO, cor=PRETO),
    # sem sobreposições: só assim o isRemoveLineWhenBlank recolhe as linhas vazias (papel poupado)
    campo(4, 17, W - 8, 10, '$F{designacao_pai}', 6.5, cor=CINZA, quando=nao_vazio('$F{designacao_pai}'), remover=True),
    campo(4, 27, W - 8, 16, '$F{designacao}', 11, True, esticar=True, float_=True),
    campo(4, 43, W - 8, 12, '$F{obs}', 8, italico=True, esticar=True, float_=True, quando=nao_vazio('$F{obs}'), remover=True),
    campo(4, 55, W - 8, 13, '$F{nome_cliente} + (' + nao_vazio('$F{telefone_cliente}') + ' ? "  ·  " + $F{telefone_cliente} : "")',
          8.5, True, esticar=True, float_=True),
    campo(4, 68, W - 8, 9, '"Recebido por " + $F{usuario}', 6, alin="Right", float_=True, cor=CINZA),
    linha(0, 79, W, tracejada=True, float_=True, grossura=1.0),
]
# uma tira contínua com a altura exacta das etiquetas (antes: páginas fixas de 450 pt = papel em branco)
s = novo("ticket_recolha_unico", L, 450, ME, MD, cab)
s += '<detail>%s</detail></jasperReport>\n' % banda(80, et)
gravar("ticket_recolha_unico.jrxml", s)
