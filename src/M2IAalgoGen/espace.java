package M2IAalgoGen;
public class espace {

static 	int dif[][]=new int[Const.N][Const.M];

	public espace(int dif_max)
	{
		int i, j;

		for (i=0; i< Const.N; i++) // Generation de difficultés entre 1 et cap_max
			for (j=0; j <Const.M; j++)
			{
				dif[i][j] = (int)(Math.random() *10000000%  dif_max) + 1;
			}



	}


	public int somme()
	{
		int i, j, s=0;

		for (i=0; i< Const.N; i++)
			for (j=0; j <Const.M; j++)
				s+=dif[i][j];
		return s;
		}


	public void affiche()
	{


		int i, j;
		System.out.println("espace : Difficultes des cellules ");
		for (i=0; i< Const.N; i++)
		{
			for (j=0; j <Const.M; j++)
			{
				System.out.print(dif[i][j]);
				System.out.print(" ");
			}
		System.out.println();
		}



	}

 public int get_dif(int i,int j)
 {
	 return dif[i][j];

 }


}