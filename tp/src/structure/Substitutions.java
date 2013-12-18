package structure;

import java.util.Vector;

public class Substitutions {
	private Vector<Term> E1;//variables
	private static Vector<Term> E2= new Vector<Term>();//cst
	private Vector<Substitution> S;
	
	
	/**
	 * @return l'ensemble de substitutions (vide avant utilisation de la fonction generateAllSub)
	 */
	public Vector<Substitution> getS() {
		return S;
	}

	/**
	 * Constructeur de la classe Substitutions
	 */
	public Substitutions(){
		E1 = new Vector<Term>();
		S = new Vector<Substitution>();
	}
	
	
	/**
	 * Rempli la liste de constantes E2 avec les constantes présentes dans une base de fait et dans les conclusions des règles d'une base de règle
	 * @param bf une base de faits
	 * @param br une base de règles
	 */
	public static void paramE2(FactBase bf,RuleBase br){
		for (Term a:bf.getTerms()){
			if (a.isConstant()){
				E2.add(new Term(a.getLabel(),a.isConstant()));
			}
		}
		for (int i=0;i<br.size();i++){
			for (Term a:br.getRule(i).getTerms()){
				if (!a.isContain(Substitutions.E2)){
					if (a.isConstant()){
						E2.add(new Term(a.getLabel(),a.isConstant()));
					}
				}
			}
		}
	}
	
	
	
	/**
	 * Rempli la liste de variables E1 avec les variables présentes dans la règle passée en paramètre
	 * @param r une règle
	 */
	public void paramE1(Rule r){
		for (Atom a:r.getHypothesis()){
			for (int i=0;i<a.getArity();i++){
				if (a.getArgI(i).isVariable()){
					Term.addTerm(a.getArgI(i),E1);
				}
			}
		}
		for (int i=0;i<r.getConclusion().getArity();i++){
			if (r.getConclusion().getArgI(i).isVariable()){
				Term.addTerm(r.getConclusion().getArgI(i),E1);
			}
		}
	}
	
	
	
	/**
	 * Fonction récursive initialisée par la fonction generateAllSub
	 * @param s une substitution
	 * @param v un vecteur de terme
	 */
	public void generateRec(Substitution s, Vector<Term> v){
		if (v.isEmpty()){
			S.add(s);
		}
		else{
			for (Term t:E2){
				Substitution sub=new Substitution(s);
				CoupleTerms couple=new CoupleTerms(v.firstElement(),t);
				sub.addCouple(couple);
				generateRec(sub, Term.cdr(v));
			}
		}
	}
	
	
	/**
	 * Génère toutes les substitutions possibles
	 */
	public void generateAllSub(){
		Substitution s=new Substitution();
		generateRec(s,E1);
	}
	
	
	public String toString(){
		String s="{\n";
		for (Substitution sub:S){
			s+=sub+"\n";
		}
		s+="\n";
		return s;
	}
	
	

}
