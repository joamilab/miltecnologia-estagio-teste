package br.miltecnologias.estagio.teste;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joamila
 */
public class Colaborador implements Comparable<Colaborador>{
    private String nome;
    private Integer idade;
    private Double salario;
    
    public Colaborador(){}
    
    public Colaborador(String nome, Integer idade, Double salario){
        this.nome = nome;
        this.idade = idade;
        this.salario = salario;
    }
    
    public String getNome(){
        return this.nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public Integer getIdade(){
        return this.idade;
    }
    public void setIdade(Integer idade){
        this.idade = idade;
    }
    
    public Double getSalario(){
        return this.salario;
    }
    public void setSalario(Double salario){
        this.salario = salario;
    }

    @Override
    public int compareTo(Colaborador c) {
        if(this.idade < c.idade){
            return 1;
        }
        else if(this.idade > c.idade){
            return -1;
        }
        return 0;
    }
}
