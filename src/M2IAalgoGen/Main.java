package M2IAalgoGen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    
    public static Unites uni = new Unites(Const.CAP_MIN, Const.CAP_MAX);
    public static espace e = new espace(Const.DIF_MAX);
    
    public static void main(String[] a) throws FileNotFoundException, IOException {
        
        
        uni.affiche();
        e.affiche();
        System.out.println("--------------------------------");
        
        Population p_init = new Population(e, uni);
        Population p1 = new Population(e, uni);
        Population p2 = new Population(e, uni);
        
        p1.reset(e, uni);
        p2.reset(e, uni);
        
        for (int i = 0; i < p_init.pop_sol.size(); i++){
            p1.pop_sol.add(p_init.pop_sol.get(i));
            System.out.println(p1.get_fe() + " ");
                
             p2.pop_sol.add(p_init.pop_sol.get(i));
             System.out.print(p2.get_fe() + " ");
        }

        Map<Integer,List> liste_fonction_r = new HashMap<>();
        ArrayList <Integer> listesomme_r = new ArrayList<>();
        Boolean stop;

        StringBuilder sb_r = new StringBuilder(Const.NB_GEN);
        liste_fonction_r.put(0,p1.get_fe()); 

        sb_r.append("generation_id");
        sb_r.append(',');
        sb_r.append("somme_fe");
        sb_r.append('\n');
        int i =0;  
        sb_r.append(i);
        sb_r.append(',');
        sb_r.append(p1.some_fe());
        sb_r.append('\n');

        while(i < Const.NB_GEN){   
            p1.reproduction(e, uni);
            p1.mutation(e, uni);
            p1.trie();
            p1.doublon();
            p1.trie();
            p1.affiche_fe();
            p1.reduire();                
            liste_fonction_r.put(i+1,p1.get_fe());               
            int somme_r = p1.calculSomme_fe();  

            listesomme_r.add(somme_r);
            if(listesomme_r.size()==3){
                stop = cas_arret(listesomme_r);
                if(stop == true) break;

                listesomme_r=new ArrayList<>();

            } 
            sb_r.append(i+1);
            sb_r.append(',');
            sb_r.append(""+somme_r);
            sb_r.append('\n');
            i++;
        }
                   
        Map<Integer,List> liste_fonction_t = new HashMap<>();
        ArrayList <Integer> listesomme_t = new ArrayList<>();

        StringBuilder sb_t = new StringBuilder(Const.NB_GEN);
        liste_fonction_t.put(0,p2.get_fe());
        
        sb_t.append("generation_id");
        sb_t.append(',');
        sb_t.append("somme_fe");
        sb_t.append('\n');
        int j =0;        
        sb_t.append(j);
        sb_t.append(',');
        sb_t.append(p2.some_fe());
        sb_t.append('\n');
                
        while(j < Const.NB_GEN){
            p2.tournoi(e, uni);
            p2.mutation(e, uni);
            p2.trie();
            p2.doublon();
            p2.trie();
            p2.affiche_fe();
            p2.reduire();
            liste_fonction_t.put(j+1,p2.get_fe());
            int somme_t = p2.calculSomme_fe();
            listesomme_t.add(somme_t);
            if(listesomme_t.size()==3){
                stop = cas_arret(listesomme_t);
                if(stop == true) break;
                listesomme_t=new ArrayList<>();
            }
            sb_t.append(j+1);
            sb_t.append(',');
            sb_t.append(somme_t);
            sb_t.append('\n');
            j++;
        }
                
        FileWriter fileWriter = new FileWriter(new File("output_20_200_R.csv"));
        fileWriter.write(sb_r.toString());                  
        fileWriter.close();
            
        FileWriter fileWriter_2 = new FileWriter(new File("output_20_200_T.csv"));
        fileWriter_2.write(sb_t.toString());                    
        fileWriter_2.close();
    }
    
    
    
    public static Boolean cas_arret(ArrayList<Integer> arg_list){
        
        Boolean resu = false;
        if(arg_list.size()!=3) resu = false;
        else{
            
            if(arg_list.get(0).equals(arg_list.get(1))&& arg_list.get(0).equals(arg_list.get(2)))
                resu = true;
            
        }
        
        
        return resu;
        
    }
    
    static boolean test(espace e, Unites u) {
        float t = (u.somme()) / e.somme();
        System.out.println("somme des capacites = " + u.somme());
        System.out.println("somme des difficultes = " + e.somme());
        if (t < 1.) {
            System.out
                    .println("les capacites sont inferieures aux difficultes\n");
            return false;
        } else {
            System.out.println("les capacites sont " + t
                    + " fois superieures aux difficultes\n");
            return true;
        }
        
    }
    
}
