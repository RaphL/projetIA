package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import structure.Atom;
import structure.FactBase;
import structure.Homomorphismes;
import structure.Rule;
import structure.RuleBase;
import structure.Substitution;
import structure.Substitutions;
import structure.Term;



public class KnowledgeBase {
	private FactBase BF;
	private RuleBase BR;
	private int nbFaits;
	
	
	/**
	 * Constructeur par défaut de la classe KnowledgBase.
	 */
	public KnowledgeBase(){
		BF=new FactBase();
		BR=new RuleBase();
	}
	
	/**
	 * Constructeur de la classe KnowledgeBase.
	 * Construit une base de connaissance à partir d'un fichier formaté.
	 * @param chemin chemin du fichier à charger
	 * @throws IOException
	 */
	public KnowledgeBase(String chemin) throws IOException{
		BufferedReader lectureFichier = new BufferedReader(new FileReader(chemin));
		String s=lectureFichier.readLine();
		if (s==""){
			BF=new FactBase();
		}
		else {
			BF=new FactBase(s);
		}
		BR=new RuleBase();
		s=lectureFichier.readLine();
		while (s!= null){
			BR.addRule(new Rule(s));
			s=lectureFichier.readLine();
		}
		lectureFichier.close();
		nbFaits = BF.size();
		System.out.println("Base de connaissance "+chemin+" chargee");
	}
	
	/**
	 * Fonction de test d'appartenance de l'hypothèse d'une règle à la base de fait en ordre 0
	 * @param r la règle à tester
	 * @return vrai si l'hypothèse de la règle est dans la base de fait, faux sinon
	 */
	private boolean HincluseBF(Rule r){
		boolean incl=false;
		for (Atom a:r.getHypothesis()){
			if (BF.belongsAtom(a)){
				incl=true;
			}else{
				return false;
			}
		}
		return incl;
	}

	/**
	 * Fonction de test d'appartenance de la conclusion d'une règle à une base de fait en ordre 0
	 * @param r la règle à tester
	 * @param f la base de fait 
	 * @return vrai si la conclusion de la règle est dans la base de fait f, faux sinon
	 */
	private boolean CincluseBF(Rule r, FactBase f){
		for (int i=0;i<f.size();i++){
			if (r.getConclusion()==f.getAtoms().get(i)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sature la base de fait en utilisant un algorithme de chaînage avant
	 */
	public void ForwardChaining(){
		boolean fin=false;
		boolean applique[];
		applique=new boolean[BR.size()];
		for (int i=0; i<BR.size(); i++){
			applique[i]=false;
		}
		while (!fin){
			FactBase nouvFaits=new FactBase();
			for (int i=0; i<BR.size();i++){
				if (applique[i]==false){
					if (HincluseBF(BR.getRule(i))){
						applique[i]=true;
						if (!(CincluseBF(BR.getRule(i), BF))&&!(CincluseBF(BR.getRule(i), nouvFaits))){
							nouvFaits.addAtom(BR.getRule(i).getConclusion());
						}
					}
				}
			}
			if (nouvFaits.isEmpty()){
				fin=true;
			}
			else{
				for (Atom a:nouvFaits.getAtoms()){
					if (!BF.belongsAtom(a)){
						BF.addAtom(a);
					}
				}
			}
		}
	}
	
	
	/**
	 * Fonction générant et ajoutant à la base de règle un ensemble de règles d'ordre 0 instanciées à partir d'une règle d'ordre 1 et d'un ensemble de substitutions
	 * @param s l'ensemble de substitutions
	 * @param r la règle à instancier
	 */
	public void genereRule(Substitutions s, Rule r){
		for (int j=0;j<s.getS().size();j++){
			Rule regle=new Rule(r);
			for (Atom a:regle.getHypothesis()){
				for (int i=0;i<a.getArity();i++){	
					Term term=new Term(s.getS().get(j).getConst(a.getArgI(i)).getLabel(),s.getS().get(j).getConst(a.getArgI(i)).isConstant());
					a.setArgI(i,term);
				}
			}
			for (int i=0;i<regle.getConclusion().getArity();i++){
				Term term = new Term(s.getS().get(j).getConst(regle.getConclusion().getArgI(i)).getLabel(),s.getS().get(j).getConst(regle.getConclusion().getArgI(i)).isConstant());
				regle.getConclusion().setArgI(i, term);
			}
			BR.addRule(regle);
		}
	}
	
	/**
	 * Fonction qui transforme la base de règle d'ordre 1 en une base de règle complètement instanciée d'ordre 0
	 */
	public void instanciation(){
		RuleBase oldBR= BR;
		BR = new RuleBase(); 
		int taille=oldBR.size();
		Substitutions.paramE2(BF, oldBR);
		for (int i=0;i<taille;i++){
			Substitutions s=new Substitutions();
			s.paramE1(oldBR.getRule(i));
			s.generateAllSub();
			genereRule(s, oldBR.getRule(i));
		}
	}
	
	/**
	 * Demande à l'utilisateur d'entrer une requête et la stocke dans une liste d'atomes
	 * @return la requête sous forme d'ArrayList<Atom>
	 */
	public ArrayList<Atom> requete(){
		ArrayList<Atom> v=new ArrayList<Atom>();
		System.out.println("Entrez votre requete sous la forme : animal(x);carnivore('loup')");
		Scanner sc = new Scanner(System.in);
		String rq = sc.next();
		StringTokenizer st = new StringTokenizer(rq,";");
   		while(st.hasMoreTokens())
   		{
   			String s = st.nextToken(); // s represente un atome
   			Atom a = new Atom(s);
   			v.add(a);
   		}
   		return v;
	}
	
	/**
	 * Fonction vérifiant l'appartenance d'une requête d'ordre 0 à la base de fait
	 * @param va la requête sous forme de liste d'atomes
	 * @return vrai si la requête est vérifiée par la base de fait saturée, faux sinon
	 */
	public boolean checkReq(ArrayList<Atom> va){
		for(Atom a:va){
			boolean res=false;
			for (Atom f:BF.getAtoms()){
				if (f.equalsA(a)){
					res=true;
				}
			}
			if (res==false){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Fonction vérifiant l'existence d'un homomorphisme d'une requête dans la base de fait saturée
	 * @param va la requête sous forme de liste d'atomes
	 * @return vrai si il existe un homomorphise de la requête dans la base de fait saturée, faux sinon
	 */
	public boolean checkReqHomomorphismes(ArrayList<Atom> va){
		Homomorphismes h = new Homomorphismes(va,BF.getAtoms());
		return h.backtrack();
	}
	
	/**
	 * Fonction retournant l'ensemble des homomorphismes d'une requête dans la base de fait saturée
	 * @param va la requête sous forme de liste d'atomes
	 * @return un vecteur de substitutions, chacune étant un homomorphisme de la requête présent dans la base de fait saturée
	 */
	public Vector<Substitution> reqHomomorphismes(ArrayList<Atom> va){
		Homomorphismes h = new Homomorphismes(va,BF.getAtoms());
		return h.backtrackAll();
	}
	
	/**
	 * Affiche sur la sorte standard l'ensemble des homomorphismes d'une requête présents dans la base de fait saturée
	 * @param va la requête sous forme de liste d'atomes
	 */
	public void afficheReqHomomorphismes(ArrayList<Atom> va){
		Vector<Substitution> v = reqHomomorphismes(va);
		for (Substitution s:v){
			System.out.println(s);
		}
	}
	
	/**
	 * @return la base de fait
	 */
	public FactBase getBF(){
		return BF;
	}
	
	/**
	 * @return la base de règle
	 */
	public RuleBase getBR(){
		return BR;
	}
	
	/**
	 * Ajoute une règle à la base de règle
	 * @param r la règle à ajouter
	 */
	public void addRule(Rule r){
		BR.addRule(r);
	}
	
	/**
	 * Ajoute un fait à la base de fait
	 * @param a le fait à ajouter
	 */
	public void addFact(Atom a){
		BF.addAtom(a);
	}
	
	
	public String toString(){
		String s=BF.toString();
		s+=BR.toString();
		return s;
		
	}
	
	/**
	 * Affiche la base de fait
	 */
	public void afficheBF(){
		System.out.println(BF);
	}
	
	/**
	 * Affiche les faits déduits de la base de connaissance
	 */
	public void afficheFaitsDeduits(){
		System.out.println(BF.afficheSupRang(nbFaits));
	}
	
	/**
	 * Affiche la base de règle
	 */
	public void afficheBR(){
		System.out.println(BR);
	}
	
	/**
	 * Vide la base de faits
	 */
	public void viderBF(){
		BF=new FactBase();
		nbFaits = BF.size();
	}
	
	/**
	 * Fonction permettant de saisir un ensemble de fait sur l'entrée standard et les ajouter à la base de faits
	 */
	public void ajouterFaits(){
		boolean b = true;
		while(b){
			System.out.println("Saisissez un fait, ou 'fin' pour finir");
			Scanner sc = new Scanner(System.in);
			String rq = sc.next();
			if(rq.equals("fin")){
				System.out.println("Ajout(s) terminé(s)");
				b=false;
			}else{
				System.out.println("Fait ajouté : "+rq);
				BF.addFacts(rq);	
			}
		}
	}
	
	/**
	 * Sature la base de fait en utilisant la méthode des homomorphismes
	 */
	public void saturer(){
		int taille=BR.size();
		ArrayList<Vector<Substitution>> tabH = new ArrayList<Vector<Substitution>>();
		for(int i=0;i<taille;i++){
			tabH.add(i, new Vector<Substitution>());
		}
		boolean fin=false;
		Vector<Atom> newFacts;
		while(!fin){
			newFacts = new Vector<Atom>();
			for (int i=0;i<taille;i++){
				Homomorphismes h=new Homomorphismes(BR.getRule(i).getHypothesis(),BF.getAtoms());
				Vector<Substitution> vsub = h.backtrackAll();
				for(Substitution sub:vsub){
					if(!sub.isContain(tabH.get(i))){
						tabH.get(i).add(sub);
						Atom fact = new Atom(BR.getRule(i).getConclusion(),sub);
						if(!BF.belongsAtom(fact)){
							newFacts.add(fact);
						}
					}
				}
			}
			if(newFacts.isEmpty()){
				fin=true;
			}else{
				for(Atom a:newFacts){
					BF.addAtom(a);
				}
			}
		}
	}
	
}
