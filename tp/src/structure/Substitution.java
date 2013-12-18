package structure;

import java.util.Vector;

/**
 * @author PAC
 *
 */
public class Substitution {
	private Vector<CoupleTerms> EnsCoupleTerms;
	
	
	/**
	 * Constructeur de la classe Substitution
	 */
	public Substitution(){
		setEnsCoupleTerms(new Vector<CoupleTerms>());
	}
	
	
	/**
	 * Constructeur par copie de la classe Substitution
	 * @param s la substitution à copier
	 */
	public Substitution(Substitution s){
		setEnsCoupleTerms(new Vector<CoupleTerms>());
		for (CoupleTerms c:s.EnsCoupleTerms){
			EnsCoupleTerms.add(new CoupleTerms(c.getTermX(),c.getTermY()));
		}
	}
	
	/**
	 * Ajoute un couple à la substitution
	 * @param c le couple à ajouter
	 */
	public void addCouple(CoupleTerms c){
		EnsCoupleTerms.add(c);
	}

	
	/**
	 * @param t un terme
	 * @return la constante associée au terme t dans la substitution dans le cas où il est présent, le terme t lui même dans le cas contraire
	 */
	public Term getConst(Term t){
		for (int i=0;i<EnsCoupleTerms.size();i++){
			
			Term tx=new Term(EnsCoupleTerms.get(i).getTermX().getLabel(), EnsCoupleTerms.get(i).getTermX().isConstant());
			Term ty=new Term(EnsCoupleTerms.get(i).getTermY().getLabel(), EnsCoupleTerms.get(i).getTermY().isConstant());

			if (tx.getLabel().equals(t.getLabel()) && tx.isConstant()==t.isConstant()){
				return ty;
			}
		}
		
		return t;
	}
	
	/**
	 * @return l'ensemble de couples de la substitution
	 */
	public Vector<CoupleTerms> getEnsCoupleTerms() {
		return EnsCoupleTerms;
	}

	/**
	 * Remplace l'ensemble de couples de la substitution par celui passé en paramètre
	 * @param ensCoupleTerms un ensemble de couples
	 */
	public void setEnsCoupleTerms(Vector<CoupleTerms> ensCoupleTerms) {
		EnsCoupleTerms = ensCoupleTerms;
	}
	
	public String toString(){
		String s="{";
		for (CoupleTerms c:EnsCoupleTerms){
			s+=c;
		}
		s+="}";
		return s;
	}
	
	/**
	 * @return le nombre de couples
	 */
	public int size(){
		return EnsCoupleTerms.size();
	}
	
	/**
	 * @param s une substitution
	 * @return vrai si la substitution est équivalente à celle passée en paramètre
	 */
	public boolean equalS(Substitution s){
		for(CoupleTerms c:EnsCoupleTerms){
			if(!(s.getConst(c.getTermX()).equalsT(c.getTermY()))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param v un vecteur de Substitution
	 * @return vrai si la substitution est présente dans le vecteur passé en paramètre, faux sinon
	 */
	public boolean isContain(Vector<Substitution> v){
		for(Substitution s:v){
			if(equalS(s)){
				return true;
			}
		}
		return false;
		
	}

}
