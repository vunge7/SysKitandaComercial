/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;


import java.sql.Connection;
/**
 *
 * @author Toshiba
 */
public class FolhaSalarioUtil {
    
    
    
    private String cod, nome, conta_bancaria, categoria, nif, iban;
    private double seguranca_social, irt ,valor_tempo, tempo, subsidio, faltas, adiantamentos, renumeracao_liquida;

    public FolhaSalarioUtil() {
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getConta_bancaria() {
        return conta_bancaria;
    }

    public void setConta_bancaria(String conta_bancaria) {
        this.conta_bancaria = conta_bancaria;
    }

    public double getSeguranca_social() {
        return seguranca_social;
    }

    public void setSeguranca_social(double seguranca_social) {
        this.seguranca_social = seguranca_social;
    }

    
    public double getIrt() {
        return irt;
    }

    public void setIrt(double irt) {
        this.irt = irt;
    }

    public double getValor_tempo() {
        return valor_tempo;
    }

    public void setValor_tempo(double valor_tempo) {
        this.valor_tempo = valor_tempo;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public double getSubsidio() {
        return subsidio;
    }

    public void setSubsidio(double subsidio) {
        this.subsidio = subsidio;
    }

    public double getFaltas() {
        return faltas;
    }

    public void setFaltas(double faltas) {
        this.faltas = faltas;
    }

    public double getAdiantamentos() {
        return adiantamentos;
    }

    public void setAdiantamentos(double adiantamentos) {
        this.adiantamentos = adiantamentos;
    }

    public double getRenumeracao_liquida() {
        return renumeracao_liquida;
    }

    public void setRenumeracao_liquida(double renumeracao_liquida) {
        this.renumeracao_liquida = renumeracao_liquida;
    }

    
    public static void main(String[] args) {
        new FolhaSalarioUtil();
    }
    
}
