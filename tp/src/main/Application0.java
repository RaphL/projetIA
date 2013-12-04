package main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import structure.Atom;

public class Application0 {

	public static void main(String[] args)throws IOException
	{
		System.out.println("Entrez un chemin de fichier");
		Scanner ch = new Scanner(System.in);
		String chemin= ch.next();
		
		KnowledgeBase KB=new KnowledgeBase(chemin);
		boolean continuer=true;
		System.out.println(KB);
		
		while (continuer){
			System.out.println("Choisissez une action :");
			System.out.println("1 : Saturer la base");
			System.out.println("2 : Saturer la base (homomorphismes)");
			System.out.println("3 : Afficher tous les faits");
			System.out.println("4 : Afficher faits deduits");
			System.out.println("5 : Repondre a une requete");
			System.out.println("6 : Repondre a une requete (homomorphismes)");
			System.out.println("7 : Entrer de nouveaux faits");
			System.out.println("8 : Afficher les regles");
			System.out.println("9 : Quitter");
			
			Scanner sc = new Scanner(System.in);
			int i = sc.nextInt();
			
			switch (i){
			case 1 : KB.instanciation();KB.ForwardChaining();break;
			case 2 : KB.saturer();break;
			case 3 : System.out.println(KB.getBF());break;
			case 4 :KB.afficheFaitsDeduits();break;
			case 5 :ArrayList<Atom> va = KB.requete();
			if (KB.checkReq(va)){
				System.out.println("Cette requete est verifiee par la base de fait saturee");
				
			}else{
				System.out.println("Cette requete n'est pas verifiee par la base de fait saturee");
			}
			break;
			case 6 : ArrayList<Atom> vecta = KB.requete();
			if (KB.checkReqHomomorphismes(vecta)){
				System.out.println("Cette requete est verifiee par la base de fait saturee");
				KB.afficheReqHomomorphismes(vecta);
				
			}else{
				System.out.println("Cette requete n'est pas verifiee par la base de fait saturee");
			}
			break;
			case 7 : KB.ajouterFaits();break;
			case 8 : KB.afficheBR();break;
			case 9 : continuer=false;
			}
		}
	}

}

