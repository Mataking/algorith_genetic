package M2IAalgoGen;

import java.util.Random;

public class Solution implements Comparable {
	// constantes
	int NB_U = Const.NB_U;
	int N = Const.N, M = Const.M;

	int pt[][] = new int[NB_U][2];

	int u_ctrl[][] = new int[Const.N][Const.M];
	int fe;

	public Solution() {

		for (int u = 0; u < NB_U; u++) {
			pt[u][0] = 0;
			pt[u][1] = 0;
		}

		for (int i = 0; i < N; i++)
			for (int j = 0; j < M; j++)
				u_ctrl[i][j] = 0;

		fe = 0;

	}

	public Solution(Unites uni) {

		int u;
		// définition aléatoire des points de départ
		for (u = 0; u < NB_U; u++) {
			pt[u][0] = (int) (Math.random() * 10000000 % N); // num de ligne du
																// point de
			// départ de l'unité u
			pt[u][1] = (int) (Math.random() * 10000000 % M); // num de colonne
																// du point de
			// départ de l'unité u
		}

	}

	public void cal_mat(Unites uni) {
		int i, j, u, unite_choisie;
		double d, dmin;

		// Generation de l'unite de ctrl entre 0 et NB_U-1 en fct de la distance
		// aux pt
		for (i = 0; i < N; i++)
			for (j = 0; j < M; j++) {
				// initialisation avec unité 0
				unite_choisie = 0;
				dmin = Math.sqrt((float) Math.pow(i - pt[0][0], 2.) + Math.pow(j - pt[0][1], 2.));
				// recherche de l'unité ayant le point de départ le plus proche
				for (u = 1; u < NB_U; u++) {
					d = Math.sqrt((float) Math.pow(i - pt[u][0], 2.) + Math.pow(j - pt[u][1], 2.));
					if (d < dmin) {
						dmin = d;
						unite_choisie = u;
					}
					// en cas d'égalité des distances
					// on choisit l'unité qui a la plus grande capacité
					if ((d == dmin) && (uni.get_capacite(u) > uni.get_capacite(unite_choisie))) {
						dmin = d;
						unite_choisie = u;
					}
				}
				// affectation
				u_ctrl[i][j] = unite_choisie;
			}

	}

	public double cal_var_taille() {
		int i, j, u;
		int nb_cel[] = new int[NB_U];

		// initialisation
		for (u = 0; u < NB_U; u++)
			nb_cel[u] = 0;
		// calcul du nombre de cellules pour chaque unité
		for (i = 0; i < N; i++)
			for (j = 0; j < M; j++) {
				u = u_ctrl[i][j];
				nb_cel[u]++;
			}
		// calcul de la moyenne
		float moy = N * M / NB_U;
		// for (u =0; u<NB_U; u++)
		// cout<<u<<" "<<nb_cel[u]<<endl;
		// cout<<"moy taille "<<moy<<endl;

		// calcul de la variance
		double var = 0.;
		for (u = 0; u < NB_U; u++) {
			var += Math.pow((double) (nb_cel[u] - moy), 2.);
			// cout<<var<<" ";
		}
		var /= NB_U;
		// cout<<"var taille "<<var<<endl;
		return var;

	}

	double cal_var_taux_charge(espace e, Unites uni) {
		int u, i, j;
		double var = 0., moy = 0.;
		double taux_charge[] = new double[NB_U];

		// initialisation de taux_charge
		for (u = 0; u < NB_U; u++) {
			taux_charge[u] = 0;
		}

		// calcul du taux de charge de chaque unité
		for (i = 0; i < N; i++)
			for (j = 0; j < M; j++) {
				u = u_ctrl[i][j];
				taux_charge[u] += e.get_dif(i, j);
			}
		for (u = 0; u < NB_U; u++) {
			// en pourcentage
			taux_charge[u] = (taux_charge[u] / uni.get_capacite(u));
			// cout<<u<<" "<<taux_charge[u]<<endl;
			// test surcharge d'une des unités (taux charge > 100%)
			if (taux_charge[u] > 1) {
				// cout <<"surcharge de l'unite "<<u<<endl;
				return -1.;
			}

		}

		// calcul de la moyenne
		for (u = 0; u < NB_U; u++)
			moy += taux_charge[u];
		moy = moy / NB_U;
		// cout<<"moyenne taux de charge "<<moy<<endl;
		// calcul variance entre taux de charge
		for (u = 0; u < NB_U; u++)
			var += Math.pow((double) (taux_charge[u] - moy), 2.);
		var = var / NB_U;
		// cout<<"variance taux de charge "<<var<<endl;

		return var;
	}

	// il faut que la mat de contrôle soit calcul� avant
	public void cal_fe(espace e, Unites u) {
		double critere_charge, critere_taille;
		int i, j;
		boolean test = true;

		// Generation de l'unite de ctrl entre 0 et NB_U-1 en fct de la distance
		// aux pt de d�part
		cal_mat(u);

		// test point de depart identique fe = 2
		for (i = 0; i < NB_U - 1; i++)
			for (j = i + 1; j < NB_U; j++)
				if ((pt[i][0] == pt[j][0]) && (pt[i][1] == pt[j][1])) {
					// cout<<"2 points de depart identique\n";
					fe = 2;
					test = false;
				}
		if (test) {
			critere_charge = cal_var_taux_charge(e, u);
			if (critere_charge == -1)
				// un des taux de charge est supérieur à 100%
				// fe vaut 1
				fe = 1;
			else {
				critere_taille = cal_var_taille();
				// normalisation
				// div par nombre de cases
				// multi par nombre d'unit�
				critere_taille *= NB_U;
				critere_taille /= (N * M);
				// cout<<"charge "<<critere_charge<<" taille
				// "<<critere_taille<<endl;
				// calcul de fe
				fe = (int) (1. / critere_taille + 1. / critere_charge);
			}

		}
	}

	public Solution mutation(espace e, Unites uni) {

		Solution s = new Solution(uni);
				
		int x = (int) (Math.random()%M);
		int y = (int) (Math.random()%N);
		int hasard = (int) (Math.random()%NB_U);
		
		s.pt[x][y] = hasard;

		return s;
	}

	public boolean equals(Object o) {
		Solution s = (Solution) o;
		int u = 0;
		boolean resu = true;
		if (fe != s.fe)
			resu = false;
		while (resu && u < NB_U) {
			if ((pt[u][0] != s.pt[u][0]) || (pt[u][1] != s.pt[u][1]))
				resu = false; // différent
			u++;
		}

		return resu;
	}

	public int get_fe() {
		return fe;
	}

	public void affiche() {
		int i, j, u;

		// cout <<"\n pt dep\n";
		// for (u=0;u<NB_U;u++)
		// cout <<u<<" "<<pt[u][0]<<" "<<pt[u][1]<<" ";
		// cout<<endl;

		System.out.println("matrice de controle\n");
		for (i = 0; i < N; i++) {
			for (j = 0; j < M; j++) {
				u = u_ctrl[i][j];
				if ((pt[u][0] == i) && (pt[u][1] == j))
					System.out.print("[" + u + "] ");
				else
					System.out.print(" " + u + "  ");
			}
			System.out.println();
		}
		System.out.println("fonction d\'evaluation :" + fe + "\n\n");
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub

		Solution s = (Solution) o;
		if (fe > s.fe)
			return 1;
		if (fe < s.fe)
			return -1;
		if (fe == s.fe)
			return 0;

		return 0;
	}

	// calculer deux enfants this et e2 � partir de deux parents
	public void croisement(Solution p1, Solution p2, Solution e2, espace e, Unites uni) {
		// calcul des tableaux pt et e2.pt � partir de p1.pt et p2.pt � l'aide d'un al�a

		for(int i = 0; i < p1.pt.length; i++)
			for(int j = 0; j< p1.pt[0].length; j++)
				if(Math.random()%2 > 0.5)
				{
					this.pt[i][j] = p1.pt[i][j];
					e2.pt[i][j]= p2.pt[i][j];
				}
				else {
					this.pt[i][j] = p2.pt[i][j];

					e2.pt[i][j]= p1.pt[i][j];
				}

		// calculer la matrice de contrôle

		// �valuer les enfants
		this.cal_fe(e, uni);
		e2.cal_fe(e, uni);


	}

}
