/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.Familia;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FamiliasController implements EntidadeFactory {

    private BDConexao conexao;

    public FamiliasController(BDConexao conexao) {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar(Object object) {
        Familia mesas = (Familia) object;
        String INSERT = "INSERT INTO familia( designacao "
                + ")"
                + " VALUES("
                + "'" + mesas.getDesignacao() + "'"
                + " ) ";

        return conexao.executeUpdate(INSERT);

    }

    @Override
    public boolean actualizar(Object object) {
        return true;
    }

    @Override
    public boolean eliminar(int pk_familia) {
        String DELETE = "DELETE FROM familia WHERE pk_familia = " + pk_familia;
        return conexao.executeUpdate(DELETE);
    }

    @Override
    public List<Familia> listarTodos() {
        String FIND_ALL = "SELECT * FROM familia ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery(FIND_ALL);
        List<Familia> lista = new ArrayList<>();
        Familia familia;
        try {
            while (result.next()) {
                familia = new Familia();
                setResultSetGrupo(result, familia);
                lista.add(familia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Vector<String> getVector() {
        String FIND_ALL = "SELECT * FROM familia ORDER BY pk_familia ASC";
        ResultSet result = conexao.executeQuery(FIND_ALL);
        Vector<String> vector = new Vector<>();
        try {
            while (result.next()) {
                vector.add(result.getString("designacao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vector.add(0, "-- Seleccione --");
        return vector;

    }

    @Override
    public Object findById(int pk_familia) {

        String sql = "SELECT * FROM familia WHERE pk_familia = " + pk_familia;
        ResultSet result = conexao.executeQuery(sql);
        Familia familia = null;
        try {
            if (result.next()) {
                familia = new Familia();
                setResultSetGrupo(result, familia);
            }
        } catch (SQLException e) {
        }

        return familia;

    }

    public Familia getFamiliaByDesignacao(String designacao) {

        String FIND__BY_CODIGO = "SELECT *  FROM familia a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery(FIND__BY_CODIGO);
        Familia familia = null;
        try {
            if (result.next()) {
                familia = new Familia();
                setResultSetGrupo(result, familia);
            }
        } catch (SQLException e) {
        }
        return familia;

    }

    public Familia getFamiliaById(int pk_familia) {

        String FIND_BY_COD_BARRA = "SELECT * FROM familia WHERE pk_familia = " + pk_familia;
        ResultSet result = conexao.executeQuery(FIND_BY_COD_BARRA);
        Familia familia = null;

        try {
            if (result.next()) {
                familia = new Familia();
                setResultSetGrupo(result, familia);
            }
        } catch (SQLException e) {
        }
        return familia;

    }

    private void setResultSetGrupo(ResultSet rs, Familia familia) {
        try {
            familia.setPkFamilia(rs.getInt("pk_familia"));
            familia.setDesignacao(rs.getString("designacao"));
        } catch (SQLException e) {
        }

    }

}
