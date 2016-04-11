package OitoRainhas;


/**
 * Write a description of class AppOitoRainhas here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AppOitoRainhasAG
{
    public static void main(String args[]){
       OitoRainhasAG rainhas = new OitoRainhasAG(8,21);
      
       System.out.println("\fPopulacao Inicial: " + rainhas + "\n");
       rainhas.algoritmoGenetico(100);
    }
}