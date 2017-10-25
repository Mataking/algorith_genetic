package M2IAalgoGen;
public class Unites {
int []capacite=new int [Const.NB_U];

Unites(int cap_min, int cap_max)
{
	for (int i=0; i<Const.NB_U; i++)		//Generation de capacités entre cap_min et cap_max
        capacite[i] = (int)(cap_min + (Math.random()*10000000  % (cap_max - cap_min)) + 1);

}


public int somme()
{
	int i,s = 0;
	for (i= 0;i<Const.NB_U;i++)
		s += capacite[i];
	return s;
}

public int get_capacite(int uni)
{
	return capacite[uni];
};

void affiche()
{
	for (int i=0;i<Const.NB_U;i++)

System.out.println("unite "+i+" capacite : "+capacite[i]);
};


}
