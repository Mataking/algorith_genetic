package M2IAalgoGen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.Iterator;

public class Population {
    
    ArrayList<Solution> pop_sol=new ArrayList<>();    
    //ArrayList<Solution> pop_sol_2=new ArrayList<>();
    
    int P = Const.P;
    
    public void reset(espace e, Unites uni){
        pop_sol=new ArrayList<>();
    }
    
    public Population(espace e, Unites uni) {
        
        int i = 0;
        System.out.print("fe de la generation 0 : ");
        while (i < P) {
            // nouvelle solution
            Solution s = new Solution(uni);
            s.cal_fe(e, uni);
            if (s.get_fe() > 0) {
                System.out.print(s.get_fe() + " ");

                pop_sol.add(s);
                i++;
            }
        }
        System.out.println();
        
    }
    
    void affiche() {
        
        System.out.println("\n\n\ntaille " + pop_sol.size() + "\n");
        
        for (int i = 0; i < pop_sol.size(); i++)
            pop_sol.get(i).affiche();
        
    }
    
    // affiche les fe de chaque individu de la population
    void affiche_fe() {
        System.out.println("fe des " + pop_sol.size() + " solutions :");
        
        for (int i = 0; i < pop_sol.size(); i++)
            System.out.print(pop_sol.get(i).get_fe()+" ");
        System.out.println();
        
    }
    
    public List<Integer> get_fe(){
        List<Integer> result = new ArrayList<Integer>();
        
        for (Solution solution : pop_sol) {
            result.add(solution.get_fe());
        }
        return result;
    }
    
    public Integer calculSomme_fe(){
        Integer result = 0;
        
        
        for (Solution solution : pop_sol) {
            result +=solution.get_fe();
        }
        
        return result;
    }
    // affiche la meilleure solution
    // attention, il faut trier la population avant pour renvoyer la meilleure solution
    void affiche_best() {
        System.out.println("meilleure solution ");
        pop_sol.get(pop_sol.size() - 1).affiche();
    }
    
    int some_fe() {
        int s = 0;
        for (int i = 0; i < P; i++)
            s += pop_sol.get(i).get_fe();
        return s;
    }
    
    // renvoie l'indice d'un des individus
    // chaque individu a un nombre de chance de se reproduire proportionnel � sa fe
    int roue() {
        int i = -1;
        int s = some_fe();
        int a = (int) ((Math.random()*10000000 % s) + 1);
        
        do {
            i++;
            a -= pop_sol.get(i).get_fe();
        } while (a > 0);
        
        return i;
    }
    
    List<Solution> get_two_best (){
        trie();
        
        List <Solution> result = new ArrayList<>();
        result.add(pop_sol.get(pop_sol.size()-1));
        System.out.println("Max :" + pop_sol.get(pop_sol.size()-1).get_fe());
        result.add(pop_sol.get(pop_sol.size()-2));
        System.out.println("Max-1 :" + pop_sol.get(pop_sol.size()-2).get_fe());
        
        return result;
    }
    
    void tournoi(espace e, Unites uni){
        
        Solution e1 = new Solution();
        Solution e2 = new Solution();
        
        List <Solution> result= get_two_best ();
        
        e1.croisement(result.get(0),result.get(1), e2, e, uni);
        
        pop_sol.add(e1);
        pop_sol.add(e2);
    }
    
    // ajouter N enfants issus du croisement de N/2 couple de parents
    void reproduction(espace e, Unites uni) {
        Solution e1 = new Solution();
        Solution e2 = new Solution();

        ArrayList<Integer> pop_vecteur=new ArrayList<Integer>();
        for(int i =0 ; i< pop_sol.size(); i++){
            pop_vecteur.add(this.roue());
        }
        
            for(int j =0; j< pop_vecteur.size(); j+=2){  
                e1.croisement(pop_sol.get(pop_vecteur.get(j)),pop_sol.get(pop_vecteur.get(j+1)), e2, e, uni);

                pop_sol.add(e1);
                pop_sol.add(e2);
            }
    }
    
    
    // ajouter un mutant � la population
    void mutation(espace e, Unites uni) {
        int i;
        i = (int) (Math.random()*10000000 % P);
        Solution pe =pop_sol.get(i);
        
        //        pe.affiche();
        
        pe.mutation(e, uni);
       
        pe.cal_fe(e, uni);
//		pe.affiche();
        pop_sol.add(i,pe);
    }
    
    // trie la population par ordre croissant
    void trie() {
        Collections.sort(pop_sol);
        
    }
    
    // suprime les doublons
    void doublon() {
        int i;
        for (i = 1; i < pop_sol.size(); i++)
            if (pop_sol.get(i).equals(pop_sol.get(i - 1)))
                pop_sol.remove(i - 1);
    }
    
    
    
    // reduit la population � P individus
    void reduire()
    {
        
        while (pop_sol.size()>P)
            pop_sol.remove(0);
    }
    
    
    
}
