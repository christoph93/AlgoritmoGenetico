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
    
    public static void main(String[] args) throws InterruptedException {
        ProjetoAG p = new ProjetoAG(20,100, 20, 5);
        
        p.algoritmoGenetico(100);
        
        
    }
    
}
