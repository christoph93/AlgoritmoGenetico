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
        
        
        //parametros: tamanho da população, número de tarefas, probabilidade(1 em n), mudanças por mutação, porcentagem mínima para convergir (0 a 1);
        ProjetoAG p = new ProjetoAG(20, 100, 10, 5, 0.9);
        
        
        //parametros: max gerações.
        p.algoritmoGenetico(100);
        
        
    }
    
}
