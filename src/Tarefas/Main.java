/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tarefas;

/**
 *
 * @author Christoph
 */
public class Main {
    
    public static void main(String[] args) {
        ProjetoAG p = new ProjetoAG();
        
        System.out.println(p.toString());
        
     
        
        p.inicializaPopulacaoIntermediaria();
        
        System.out.println("");
        
        System.out.println(p.toString2());
        
        
        
        
    }
    
}
