package br.miltecnologias.estagio.teste;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/**
 *
 * @author Joamila
 */
public class Principal {
    double verba = 0;
    int quantidade = 0;
    int numteste = 1;
    ArrayList<Colaborador> colaboradores = new ArrayList();
    ColaboradorDAO colabdao = new ColaboradorDAO();
    ArrayList<Paragraph> parags = new ArrayList<>();
    
    public void entrarDados(Colaborador colaborador) throws SQLException{
        for(Colaborador colab : colaboradores){
            if(colaborador.getNome().equals(colab.getNome())){
                System.out.println("Colaborador já cadastrado.");
                System.exit(0);
            }
        }
        if(colaborador.getIdade()<18 || colaborador.getIdade()>60){
            System.out.println("O colaborador está fora da faixa etária permitida.");
            System.exit(0);
        }

        if(colaborador.getSalario()<1.00 || colaborador.getSalario()>10000.00){
            System.out.println("O colaborador está fora da faixa salarial permitida.");
            System.exit(0);
        }
        colabdao.inserirColaboradorNaTabela(colaborador);
        colaboradores.add(colaborador);
    }
    
    public void sairDados() throws SQLException, DocumentException{
        Double salariomenor = colabdao.consultarMenorSalario();
        ArrayList<Colaborador> colabs_menor_salario = colabdao.consultarPorSalario(salariomenor);
        
        if(verba >= salariomenor*colabs_menor_salario.size()){
            System.out.println("Teste " + numteste);
            parags.add(new Paragraph("Teste " + numteste));
            numteste += 1;
            for(int j=0; j<colabs_menor_salario.size(); j++){
               System.out.println(colabs_menor_salario.get(j).getNome());
               parags.add(new Paragraph(colabs_menor_salario.get(j).getNome()));
            }
            
            colaboradores.clear();
            colabdao.deletarTodosColaboradores();
            System.out.println("");
        }
        else{
            Collections.sort(colabs_menor_salario);
            int quant = (int) (verba/salariomenor);
            System.out.println("Teste " + numteste);
            parags.add(new Paragraph("Teste " + numteste));
            for(int i=0; i<quant; i++){
                System.out.println(colabs_menor_salario.get(i).getNome());
                parags.add(new Paragraph(colabs_menor_salario.get(i).getNome()));
            }
            
            numteste += 1;
            colaboradores.clear();
            colabdao.deletarTodosColaboradores();
            System.out.println("");
        }
    }
    
    public void gerarPDF() throws DocumentException, FileNotFoundException{
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("E:\\Relatorio_Prefeitura.pdf"));
        document.open();
        
        for(int i=0; i<parags.size(); i++){
            document.add(parags.get(i));
        }
        
        document.close();
    }
    
    public static void main(String args[]) throws SQLException, DocumentException, FileNotFoundException{
        Principal cliente = new Principal();
        Scanner ler = new Scanner(System.in);
        
        cliente.colabdao.conectarComBanco("postgres", "123456");
        cliente.colabdao.criarTabela();
        
        do{
            cliente.verba = ler.nextDouble();
            cliente.quantidade = ler.nextInt();

            for(int i=0; i<cliente.quantidade; i++){
                String nome = ler.next();
                Integer idade = ler.nextInt();
                Double salario = ler.nextDouble();

                cliente.entrarDados(new Colaborador(nome, idade, salario));
            }
            cliente.sairDados();
        }while(cliente.verba!=0 && cliente.quantidade!=0);
        
        cliente.gerarPDF();
    }
}
