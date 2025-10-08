


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
/**
 *
 * @author Domingos Dala Vunge & Martinho Canhongo Luis
 */
public class JPAEntityMannagerFactoryUtil
{

//    public static EntityManagerFactory em  =  Persistence.createEntityManagerFactory("SGCMINIMERCADOPU");
    public static EntityManagerFactory em = Persistencia.getEntityManagerFactory( "SGCMINIMERCADOPU" );

    public static void main( String[] args )
    {
//        EntityManagerFactory em = JPAEntityMannagerFactoryUtil.em;
//        UsuarioDao usuarioDao = new UsuarioDao( em );
//        System.out.println(usuarioDao.exist_usuario("dvml", "mavala"));

        leituraFicheiro();

    }

    public static EntityManager createEntityManager()
    {

        try
        {

//                
//                String ip = "localhost";



//                String user = "root";
//                String password = "DoV90x?#";
//                
//                Map map = new HashMap();
//                map.put("javax.persistence.jdbc.url", url); //esta propriedade vai substituir aquela q esta no arquivo
//                map.put("javax.persistence.jdbc.user", user); //esta propriedade vai substituir aquela q esta no arquivo
//                map.put("javax.persistence.jdbc.password", password); //esta propriedade vai substituir aquela q esta no arquivo
////    
//                return  javax.persistence.Persistence.createEntityManagerFactory( "SGCMINIMERCADOPU", map );
//                
            return em.createEntityManager();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }

    }

    public static class Persistencia
    {

        public static EntityManagerFactory getEntityManagerFactory( String PU )
        {

            Vector<String> informacao = leituraFicheiro();

            try
            {

                if ( !informacao.isEmpty() )
                {
                    String ip = informacao.get( 0 );
                    String porta = informacao.get( 1 );


                    String url = "jdbc:mysql://" + ip + ":" + porta + "/kitanda_db_abu?zeroDateTimeBehavior=convertToNull";


                    String user = "root";
                    String password = "DoV90x?#";

                    Map map = new HashMap();
                    map.put( "javax.persistence.jdbc.url", url ); //esta propriedade vai substituir aquela q esta no arquivo
                    map.put( "javax.persistence.jdbc.user", user ); //esta propriedade vai substituir aquela q esta no arquivo
                    map.put( "javax.persistence.jdbc.password", password ); //esta propriedade vai substituir aquela q esta no arquivo
                    map.put( "javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver" ); //esta propriedade vai substituir aquela q esta no arquivo
//    
                    return javax.persistence.Persistence.createEntityManagerFactory( PU, map );
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Erro n. 192 , contacte o fornecedor do sistema" );
                }

                return null;

            }
            catch ( Exception e )
            {
                return null;
            }

        }

    }

    public static Vector<String> leituraFicheiro()
    {

        Vector<String> informacao = new Vector<>();
        try
        {
            File file = new File( "credencial/file.txt" );

            String texto = "";

            Scanner scanner = new Scanner( file );

            while ( scanner.hasNext() )
            {
                informacao.add( scanner.nextLine() );
            }

            System.out.println( texto );

        }
        catch ( FileNotFoundException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( JPAEntityMannagerFactoryUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return informacao;

    }

}
