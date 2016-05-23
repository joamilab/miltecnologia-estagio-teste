package br.miltecnologias.estagio.teste;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joamila
 */
public class ColaboradorDAO {
    private Connection conexaoBD;
    private Statement stm;
    
    public void conectarComBanco(String usuario, String senha){
        String urlBanco = "jdbc:postgresql://localhost/postgres";
        String nomeDriver = "org.postgresql.Driver";
    
        try {
          Class.forName(nomeDriver).newInstance();
          conexaoBD = DriverManager.getConnection(urlBanco, usuario, senha);
          stm = conexaoBD.createStatement();  
        }
        catch (SQLException ex) {
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }      
    }
    
    public void criarTabela() throws SQLException{
        stm.executeUpdate("create table servidor (nome text, idade integer, salario decimal)");
    }
    
    public void inserirColaboradorNaTabela(Colaborador colaborador) throws SQLException{
        try (PreparedStatement preparedStatement = conexaoBD.prepareStatement("insert into servidor values(?,?,?)")) {
            preparedStatement.setString(1, colaborador.getNome());
            preparedStatement.setInt(2, colaborador.getIdade());
            preparedStatement.setDouble(3, colaborador.getSalario());
            
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
        }
    }
    
    public ArrayList<Colaborador> consultarTabela() throws SQLException{
        ArrayList<Colaborador> servidoresList = new ArrayList();
        ResultSet result = stm.executeQuery("select nome, idade, salario from servidor");
        if(result!=null){
            while(result.next()){
                String nome = result.getString(1);
                Integer idade = result.getInt(2);
                Double salario = result.getDouble(3);
                
                servidoresList.add(new Colaborador(nome, idade, salario));
            }
        }

        return servidoresList;
    }
    
    public Double consultarMenorSalario() throws SQLException{
        ResultSet result = stm.executeQuery("select min(salario) from servidor");
        Double salario = null;
        while(result.next()){
            salario = result.getDouble(1);
        }
        
        return salario;
    }
    
    public ArrayList<Colaborador> consultarPorSalario(Double salario) throws SQLException{
        ArrayList<Colaborador> colabs = new ArrayList<>();  
        String sql = "select * from servidor where salario = " + salario.toString();
        
        ResultSet result = stm.executeQuery(sql);  
        while(result.next()){
            String nome = result.getString(1);
            Integer idade = result.getInt(2);
            Double sal = result.getDouble(3);
            colabs.add(new Colaborador(nome, idade, sal));
        }
        return colabs;
    }
    
    public void atualizarColaboradorNaTabela(Colaborador colaborador) throws SQLException{
        String sql = "update servidor set idade = " + colaborador.getIdade() + ", set salario = " + colaborador.getSalario()
                + " where nome = " + colaborador.getNome();
        stm.executeUpdate(sql);
    }
    
    public void deletarColaboradorNaTabela(String nome) throws SQLException{
        String sql = "delete from servidor where nome = " + nome;
        stm.execute(sql);
    }
    
    public void deletarTodosColaboradores() throws SQLException{
        stm.executeUpdate("delete from servidor");
    }
}
