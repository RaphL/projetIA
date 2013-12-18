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
	 * @param s la substitution � copier
	 */
	public Substitution(Substitution s){
		setEnsCoupleTerms(new Vector<CoupleTerms>());
		for (CoupleTerms c:s.EnsCoupleTerms){
			EnsCoupleTerms.add(new CoupleTerms(c.getTermX(),c.getTermY()));
		}
	}
	
	/**
	 * Ajoute un couple � la substitution
	 * @param c le couple � ajouter
	 */
	public void addCouple(CoupleTerms c){
		EnsCoupleTerms.add(c);
	}

	
	/**
	 * @param t un terme
	 * @return la constante associ�e au terme t dans la substitution dans le cas o� il est pr�sent, le terme t lui m�me dans le cas contraire
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
	 * Remplace l'ensemble de couples de la substitution par celui pass� en param�tre
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
	 * @return vrai si la substitution est �quivalente � celle pass�e en param�tre
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
	 * @return vrai si la substitution est pr�sente dans le vecteur pass� en param�tre, faux sinon
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
