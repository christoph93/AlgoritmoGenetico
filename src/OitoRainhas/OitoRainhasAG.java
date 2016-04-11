package OitoRainhas;


/**
 * Write a description of class OitoRainhasAG here.
 * 
 * @author Sílvia
 * @version 10/09/2013
 */
import java.util.Random;
public class OitoRainhasAG
{
    private Integer [][]populacao;
    private Integer [][]populacaoIntermediaria;
    private int tamanho, rainhas;
    private Character[][] tabuleiro;
    private Integer aptidao[];
    
    public OitoRainhasAG(int numeroRainhas, int tamanhoPopulacao){
        rainhas = numeroRainhas;
        tamanho = tamanhoPopulacao;
        populacao = new Integer[tamanhoPopulacao][numeroRainhas];     
        tabuleiro = new Character[rainhas][rainhas];
        aptidao = new Integer[tamanho];
        inicializaPopulacao();
    }
    
    public void inicializaPopulacao(){
        Random geraPosicao = new Random();
        for(int i=0; i<tamanho; i++){
          aptidao[i] = 0;
          for(int j=0; j<rainhas; j++)
                populacao[i][j]=geraPosicao.nextInt(rainhas);
        }
    }
    
    public void inicializaPopulacaoIntermediaria(){
        populacaoIntermediaria = new Integer[tamanho][rainhas];
        for(int i=0; i<tamanho; i++){
          for(int j=0; j<rainhas; j++)
                populacaoIntermediaria[i][j]=-1;
        }
    }
    
    public void inicializaTabuleiro(){
        for(int i=0; i<rainhas; i++)
           for(int j=0; j<rainhas; j++){
                tabuleiro[i][j]='-';           
            }
    }
    
    public String toString(){
        String msg = "";
        for(int i=0; i<tamanho; i++){
          for(int j=0; j<rainhas; j++) msg = msg + populacao[i][j] + " ";
          msg = msg + "\n";
        }
        return msg;
    }
    public void decodifica(Integer[] cromossomo ){
         inicializaTabuleiro();
         for(int i=0; i<rainhas; i++){
             int linha = cromossomo[i];
             tabuleiro[linha][i] = '*';
        }
    }
    
    public String exibeSolucao(){
        String msg="";
        int dimensao = rainhas;
        for(int i=0; i<dimensao; i++){
            for(int j=0; j<dimensao; j++){
                msg = msg + tabuleiro[i][j]+" ";
            }
            msg = msg + "\n";
        }
        return msg;
    }
    public int h(){
        int dimensao = rainhas;
        int ataques = 0;
        for(int i=0; i<dimensao; i++)
            for(int j=0; j<dimensao; j++){
                if(tabuleiro[i][j]=='*'){
                    ataques = ataques + contaAtaquePorLinha(i,j);
                    ataques = ataques + contaAtaquePorColuna(i,j);
                    ataques = ataques + contaAtaqueDiagonais(i,j);
                }
            }
        return ataques;
    }
    private int contaAtaquePorLinha(int linha, int coluna){
        int ataques = 0;
         for(int j=coluna+1; j<rainhas; j++)
             if(tabuleiro[linha][j]=='*') ataques++;
        return ataques;
    }
    private int contaAtaquePorColuna(int linha, int coluna){
        int ataques = 0;
        for(int i=linha+1; i<rainhas; i++)
             if(tabuleiro[i][coluna]=='*') ataques++;
        return ataques;
    }
    private int contaAtaqueDiagonais(int linha, int coluna){
        int dimensao = rainhas;
        int ataques = 0;
        for(int i=linha+1,j=coluna+1; i<dimensao && j<dimensao; i++,j++){
            if(tabuleiro[i][j]=='*') ataques++;
        }
        for(int i=linha+1,j=coluna-1; i<dimensao && j>=0; i++,j--){
            if(tabuleiro[i][j]=='*') ataques++;
        }
        return ataques;
    }
    
    public int elitismo(){
        int indice = 0;
        for(int i=1; i<tamanho; i++){
            if(aptidao[i]<aptidao[indice]) indice = i;
        }
        return indice;
    }
    
    public void algoritmoGenetico(int maxGeracoes){
        int geracoes = 0;
        boolean solucaoOtima=false;
        int indSolucaoOtima=0;
        //Implementar o loop para gerações
        while(geracoes<maxGeracoes){
            System.out.println("===> Geracao Atual: " + geracoes);
            //Avaliando
            for(int i=0; i<tamanho; i++){
                decodifica(populacao[i]);
                aptidao[i] = h();
                if(aptidao[i]==0){
                    solucaoOtima=true;
                    indSolucaoOtima = i;
                }
               // System.out.println("Cromossomo: "+ i + " - Aptidao: " + aptidao[i] + "\n" + exibeSolucao() + "\n");
            }
            
            //Pára se já encontrou h()=0 -- objetivo
            if(solucaoOtima) break;
            
            int mesmaMedida = aptidao[1];
            int cont = 0;
            for(int i=0; i<tamanho; i++) if(aptidao[i]==mesmaMedida) cont++;
            if(cont>=tamanho-1){
                System.out.println("Convergiu ...");
                break;
            }
            
            //Preenchendo populacaoIntermediaria
            inicializaPopulacaoIntermediaria();
            int melhor = elitismo();
            //System.out.println("Cromossomo Melhor: " + melhor + " Aptidao - h: " + aptidao[melhor]);
            copiaMelhor(melhor);
            
            for(int i=1; i<tamanho;){
                
                //Seleção por torneio;
                int indiceCromossomoPai = selecaoPorTorneio();
                int indiceCromossomoMae = selecaoPorTorneio();
                
                //Cruzamento
                cruzamentoUniPonto(indiceCromossomoPai, indiceCromossomoMae,i);
                i=i+2;    
            }
           
            //Mutação
            Random possibilidade = new Random();
            if(possibilidade.nextInt(2)==0) {
                //System.out.println("Aplicou mutacao.....");
                //System.out.println(exibePopulacaoIntermediaria());
                int mutantes= (int)(tamanho*0.1);
                while(mutantes>0){
                  //mutacao2();
                  mutacao();
                  mutantes--;
                }
                
            }
            
            //Transforma a população Intermediaria em populacao Atual
            populacao = populacaoIntermediaria;
            
            geracoes++;
        }
        System.out.println("================================================================");
        System.out.println(toString());
        System.out.println("Achou a solução em " + geracoes + " geracoes");
        decodifica(populacao[indSolucaoOtima]);
        System.out.println("Cromossomo: "+ indSolucaoOtima + " - Aptidao: " + aptidao[indSolucaoOtima] + "\n" + exibeSolucao() + "\n");
    }
    
    public int selecaoPorTorneio(){
        Random geraIndice = new Random();
        int indice1 = geraIndice.nextInt(tamanho);
        int indice2 = geraIndice.nextInt(tamanho);
        if(aptidao[indice1]>aptidao[indice2]) return indice1;
        return indice2;
    }
    
    public void cruzamentoUniPonto(int indPai, int indMae, int indPop){
        Random corte = new Random();
       
        //int pontoDeCorte = rainhas/2;
        int pontoDeCorte = corte.nextInt(rainhas-2)+1;
        for(int i=0; i<pontoDeCorte; i++){
            populacaoIntermediaria[indPop][i]=populacao[indMae][i];
            populacaoIntermediaria[indPop+1][i]= populacao[indPai][i];
        }
        for(int i=pontoDeCorte; i<rainhas; i++){
            populacaoIntermediaria[indPop][i]=populacao[indPai][i];
            populacaoIntermediaria[indPop+1][i]=populacao[indMae][i];
        }
        
    }
     
    public void mutacao(){
        Random geraPosicao = new Random();
        int indCrom = geraPosicao.nextInt(tamanho);
        int indRainha = geraPosicao.nextInt(rainhas);
        int novaPosicao = geraPosicao.nextInt(rainhas);
        populacaoIntermediaria[indCrom][indRainha] = novaPosicao;
    }
    
    public void mutacao2(){
        Random geraPosicao = new Random();
        int indCrom = geraPosicao.nextInt(tamanho);
        int indRainha1 = geraPosicao.nextInt(rainhas);
        int indRainha2 = geraPosicao.nextInt(rainhas);
        int aux = populacaoIntermediaria[indCrom][indRainha1];
        populacaoIntermediaria[indCrom][indRainha1] = populacaoIntermediaria[indCrom][indRainha2];
        populacaoIntermediaria[indCrom][indRainha2]=aux;
       
    }
    
    public void copiaMelhor(int i){
          populacaoIntermediaria[0] = populacao[i].clone();    
    }
    
    public String exibePopulacaoIntermediaria(){
        String msg = "";
        for(int i=0; i<tamanho; i++){
            for(int j=0; j<rainhas; j++) msg = msg + populacaoIntermediaria[i][j] + " ";
            msg = msg + "\n";
        }
        return msg;
    }
    
}
