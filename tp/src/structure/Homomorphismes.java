package structure;

import java.util.ArrayList;
import java.util.Vector;

public class Homomorphismes {
	
	private ArrayList<Atom> A1;//variables
	private ArrayList<Atom> A2;//constantes
	private Vector<Term> E1;//variables
	private Vector<Term> E2;//constantes
	private Vector<Substitution> S;
	
	
	public Homomorphismes(ArrayList<Atom> p1,ArrayList<Atom> p2){
		A1=p1;
		A2=p2;
		S = new Vector<Substitution>();
		E1 = new Vector<Term>();
		E2 = new Vector<Term>();
		paramE1(A1);
		paramE2(A2);
	//	System.out.println("Ceci est mon A1 : "+A1);
	}
	
	public void paramE1(ArrayList<Atom> vAtom){
		for (Atom a:vAtom){
			for (int i=0;i<a.getArity();i++){
				if (a.getArgI(i).isVariable()){
					addTerm(a.getArgI(i),E1);
				}
			}
		}
	}
	
	public void paramE2(ArrayList<Atom> vAtom){
		for (Atom a:vAtom){
			for (int i=0;i<a.getArity();i++){
				if (a.getArgI(i).isConstant()){
					addTerm(a.getArgI(i),E2);
				}
			}
		}
	}
	
	
	public void addTerm(Term t, Vector<Term> vTerm){
		boolean dejaVu=false;
		for (Term t2:vTerm){
			if (t.equalsT(t2)){
				dejaVu=true;
			}
		}
		if (!dejaVu){
			vTerm.add(new Term(t.getLabel(),t.isConstant()));
		}
	}
	
	public Vector<Term> cdr (Vector<Term> s){
		Vector<Term> temp=new  Vector<Term>();
		 for (int i=1; i<s.size();i++){
			 temp.add(s.get(i));
		 }
		 return temp;
	}
	
	public Vector<Substitution> getS() {
		return S;
	}
	
	public Vector<Substitution> backtrackAll(){
		if(E1.isEmpty()){
			for(Atom a:A1){
				boolean res=false;
				if(a.isContain(A2)){
					res=true;
				}
				if(!res){
					return S;
				}
			}
			S.add(new Substitution());
			return S;
		}else{
			backtrackAllRec(new Substitution(),E1);
			return S;
		}
	}
	
	public void backtrackAllRec(Substitution s, Vector<Term> v){
		if (v.isEmpty()){
			S.add(s);
		}
		else{
			for (Term t:E2){
				Substitution sub=new Substitution(s);
				CoupleTerms couple=new CoupleTerms(v.firstElement(),t);
				sub.addSubstitution(couple);
				if(isHomomorphismePartiel(sub)){
					//System.out.println("PARTIEL : bt("+sub+","+cdr(v)+")");
					backtrackAllRec(sub, cdr(v));
				}
			}
		}
	}
	
	public boolean backtrack(){
		return backtrackRec(new Substitution(),E1);
	}
	
	public boolean backtrackRec(Substitution s, Vector<Term> v){
		if (v.isEmpty()){
			return true;
		}
		else{
			for (Term t:E2){
				Substitution sub=new Substitution(s);
				CoupleTerms couple=new CoupleTerms(v.firstElement(),t);
				sub.addSubstitution(couple);
				boolean res = false;
				if(isHomomorphismePartiel(sub)){
					res = backtrackRec(sub, cdr(v));
				}
				if(res){
					return true;
				}
			}
			return false;
		}
	}
	
	
	private boolean goodInstance(Atom a){
		if(a.getNbVar()>0){
			return true;
		}
		for(Atom at:A2){
			if (a.equalsA(at)){
				return true;
			}
		}
		return false;
	}

	private boolean isHomomorphismePartiel(Substitution sub) {
		for(Atom at:A1){
			if(at.getNbVar()<=sub.size()){
				//System.out.println(at+" : Je peux �tre instanci�. "+at.getNbVar()+"<="+sub.size());
				Atom instance = new Atom(at,sub);
				if(!goodInstance(instance)){
					return false;
				}
			}else{
				//System.out.println(at+" : Je peux PAS �tre instanci�. "+at.getNbVar()+"<="+sub.size());
			}
		}
		return true;
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