package util;

import javax.swing.*;

public class OlaMundo
{

    private int id;
    private String nome, sobrenome;

    public OlaMundo( int id, String nome, String sobrenome )
    {
        this.setId( id );
        this.setNome( nome );
        this.setSobreNome( sobrenome );
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public String getNome()
    {
        return this.nome;
    }

    public void setNome( String nome )
    {
        this.nome = nome;
    }

    public String getSobreNome()
    {
        return this.sobrenome;
    }

    public void setSobreNome( String sobrenome )
    {
        this.sobrenome = sobrenome;
    }

    public static void main( String[] args )
    {
//        OlaMundo object_1 = new OlaMundo(1,"Domingos Dala", "Vunge");
//        OlaMundo object_2 = object_1;
//        object_2.setNome("Martinho Luís");
//
//        System.out.println("Eu sou O: " +object_1.getNome());
//        System.out.println("Eu sou O: " +object_2.getNome());
        String texto = "1-Armazem#(2)";

        System.out.println( texto.split( "-" )[ 0 ] );
        System.out.println( texto.split( "-" )[ 1 ].split( "#" )[ 0 ] );

    }

}
