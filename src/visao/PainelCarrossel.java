/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 18/dez/2025
 * @lastModified 18/dez/2025
 */
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class PainelCarrossel extends JPanel
{

    private final List<Image> imagens;
    private int indiceAtual = 0;

    private float alpha = 1f;

    // === CONTROLES DE TEMPO ===
    private final int tempoParadoMs = 5000;   // ⏱ 3 segundos por imagem
    private final int intervaloTimer = 40;    // FPS da animação
    private final float passoFade = 0.04f;    // suavidade do fade

    private long inicioParado = System.currentTimeMillis();
    private boolean emFadeOut = false;
    private boolean emFadeIn = false;

    public PainelCarrossel( List<Image> imagens )
    {
        this.imagens = imagens;
        setBackground( Color.BLACK );

        Timer timer = new Timer( intervaloTimer, e -> animar() );
        timer.start();
    }

    private void animar()
    {
        long agora = System.currentTimeMillis();

        // ⏸ Tempo parado (imagem visível)
        if ( !emFadeOut && !emFadeIn )
        {
            if ( agora - inicioParado >= tempoParadoMs )
            {
                emFadeOut = true;
            }
            return;
        }

        // 🔽 Fade Out
        if ( emFadeOut )
        {
            alpha -= passoFade;
            if ( alpha <= 0f )
            {
                alpha = 0f;
                emFadeOut = false;

                indiceAtual = ( indiceAtual + 1 ) % imagens.size();
                emFadeIn = true;
            }
        }
        // 🔼 Fade In
        else if ( emFadeIn )
        {
            alpha += passoFade;
            if ( alpha >= 1f )
            {
                alpha = 1f;
                emFadeIn = false;
                inicioParado = System.currentTimeMillis();
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        if ( imagens.isEmpty() )
        {
            return;
        }

        Graphics2D g2 = ( Graphics2D ) g.create();
        g2.setComposite(
                AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha )
        );

        Image img = imagens.get( indiceAtual );
        g2.drawImage( img, 0, 0, getWidth(), getHeight(), this );
        g2.dispose();
    }
}
