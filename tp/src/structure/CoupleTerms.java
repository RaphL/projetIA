package structure;

public class CoupleTerms {
	private Term x;
	private Term y;

	public CoupleTerms(Term _x, Term _y){
		x=_x;
		y=_y;
	}
	public Term getTermX() {
		return x;
	}

	public void setTermX(Term x) {
		this.x = x;
	}

	public Term getTermY() {
		return y;
	}

	public void setTermY(Term y) {
		this.y = y;
	}
	
	public String toString(){
		return "("+x +", "+ y+")";
	}
}
