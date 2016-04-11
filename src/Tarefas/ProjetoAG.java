/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tarefas;

import java.util.Random;

/**
 *
 * @author Christoph
 */
public class ProjetoAG {

    private Integer[][] populacao;
    private Integer[][] populacaoIntermediaria;
    private int pop, rainhas, tamanho, tarefas;
    private double aptidao[];
    
    

    public ProjetoAG() {
        pop = 11;
        tarefas = 10;
        populacao = new Integer[pop][tarefas];
        aptidao = new double[pop];        
    }

    public void inicializaPopulacao() {
        Random r = new Random();
        //gera as tarefas
        for (int i = 0; i < tarefas; i++) {
            populacao[0][i] = r.nextInt(239) + 1;
        }
        //distribui as tarefas
        for (int i = 1; i < pop; i++) {
            for (int j = 0; j < tarefas; j++) {
                populacao[i][j] = r.nextInt(3);
            }
        }
    }

    public void calculaAptidao(Integer[][] ap) {        
        double[] totais = {0, 0, 0};        
        //soma o total de horas para cada pessoa        
        for (int i = 1; i < pop; i++) {
            //soma o total de horas para cada pessoa
            for (int j = 0; j < tarefas; j++) {
                totais[ ap[i][j] ] += ap[0][j];
            }            
            //calcula o desvio padrão da linha
            double m = (totais[0] + totais[1] + totais[2]) / 3;
            double m2 = Math.pow(totais[0] - m, 2) + Math.pow(totais[1] - m, 2) + Math.pow(totais[2] - m, 2);
            double aux = m2 / 3;
            double desvio = Math.sqrt(aux);
            //adiciona o desvio da linha na lista de desvios
            aptidao[i] = desvio;
            totais[0] = 0;
            totais[1] = 0;
            totais[2] = 0;
        }  
    }
    
    public int getMelhorIndice(){        
        double menorDes = aptidao[1];
        int melhorIndice = 1;
        
        for (int i = 1; i < aptidao.length; i++) {
            if (aptidao[i] < menorDes) {
                menorDes = aptidao[i];
                melhorIndice = i;
            }
        }
        return melhorIndice;        
    }
    

    public void geraPopulacaoIntermediaria() {
        populacaoIntermediaria = new Integer[pop][tarefas];
        
        calculaAptidao(populacao);
        int melhorIndice = getMelhorIndice();
        System.out.println("Melhor indice: " + melhorIndice);

        //copia o valor das tarefas e a melhor linha
        for (int i = 0; i < tarefas; i++) {
            populacaoIntermediaria[0][i] = populacao[0][i];
            populacaoIntermediaria[1][i] = populacao[melhorIndice][i];
        }
              
        //popula o resto da população por torneio e cruzamento uniponto
        for (int i = 2; i < pop-1; i++) {
            int indiceMae = selecaoPorTorneio();
            int indicePai = selecaoPorTorneio();            
            cruzamentoUniPonto(indicePai, indiceMae, i);
        }
    }

    public void cruzamentoUniPonto(int indPai, int indMae, int indPop) {
        Random corte = new Random();

        int pontoDeCorte = corte.nextInt(pop - 2) + 1;        
        for (int i = 0; i < pontoDeCorte; i++) {            
            populacaoIntermediaria[indPop][i] = populacao[indMae][i];
            populacaoIntermediaria[indPop + 1][i] = populacao[indPai][i];
        }        
        for (int i = pontoDeCorte; i < pop-1; i++) {            
            populacaoIntermediaria[indPop][i] = populacao[indPai][i];
            populacaoIntermediaria[indPop + 1][i] = populacao[indMae][i];
        }
    }

    public int selecaoPorTorneio() {
        Random geraIndice = new Random();
        int indice1 = geraIndice.nextInt(pop-1) +1;
        int indice2 = geraIndice.nextInt(pop-1) +1;
        
        //System.out.println("I1: " + indice1 + " I2: " +indice2);
        
        if (aptidao[indice1] > aptidao[indice2]) {
            return indice1;
        }
        return indice2;
    }

    public String populacaoToString() {
        String msg = "População: \n";
        for (int i = 0; i < pop; i++) {
            for (int j = 0; j < tarefas; j++) {
                msg = msg + populacao[i][j] + " ";
            }
            msg = msg + "\n";
        }
        return msg;
    }

    public String populacaoIntermediariaToString() {
        String msg = "População Intermediária: \n";
        for (int i = 0; i < pop; i++) {
            for (int j = 0; j < tarefas; j++) {
                msg = msg + populacaoIntermediaria[i][j] + " ";
            }
            msg = msg + "\n";
        }
        return msg;
    }
    
    public String aptidaoToString(){
        String msg = "Aptidao: \n";
        
        for(Double i : aptidao){
            msg = msg + " " + i;
        }
        
        return msg;
    }


    public void algoritmoGenetico(int maxGeracoes) throws InterruptedException {
        System.out.println("Iniciando AG");
        
        int geracoes = 0;
        boolean convergiu = false;
        
        Random r = new Random();
        
        inicializaPopulacao();            
        
        for(int i = 0; i < maxGeracoes; i++){            
            System.out.println("Geração: " + geracoes); 
            System.out.println(aptidaoToString());
            geraPopulacaoIntermediaria();               
            
            if(r.nextInt(4) == 1){
                mutacao();
            }
            
            populacao = populacaoIntermediaria;  
            calculaAptidao(populacao);
           
            System.out.println(populacaoIntermediariaToString());
            geracoes++;
            
        }
        
        
        
    }

    public void mutacao() {
        Random geraPosicao = new Random();
        int linha = geraPosicao.nextInt(pop-2) + 2;
        int coluna = geraPosicao.nextInt(tarefas) ;        
        int novaPosicao = geraPosicao.nextInt(3);
        
        while(populacaoIntermediaria[linha][coluna] == novaPosicao){
            novaPosicao = geraPosicao.nextInt(3);
        }        
        populacaoIntermediaria[linha][coluna] = novaPosicao;
    }

    public void mutacao2() {
        Random geraPosicao = new Random();
        int indCrom = geraPosicao.nextInt(tamanho);
        int indRainha1 = geraPosicao.nextInt(rainhas);
        int indRainha2 = geraPosicao.nextInt(rainhas);
        int aux = populacaoIntermediaria[indCrom][indRainha1];
        populacaoIntermediaria[indCrom][indRainha1] = populacaoIntermediaria[indCrom][indRainha2];
        populacaoIntermediaria[indCrom][indRainha2] = aux;

    }
}