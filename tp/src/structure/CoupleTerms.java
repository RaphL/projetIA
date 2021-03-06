package structure;

public class CoupleTerms {
	private Term x;
	private Term y;

	/**
	 * Constructeur de la classe CoupleTerms
	 * @param _x le premier élément du couple
	 * @param _y le second élément du couple
	 */
	public CoupleTerms(Term _x, Term _y){
		x=_x;
		y=_y;
	}
	
	/**
	 * @return le premier élément du couple
	 */
	public Term getTermX() {
		return x;
	}

	/**
	 * Modifie le premier élément du couple
	 * @param x un terme
	 */
	public void setTermX(Term x) {
		this.x = x;
	}

	/**
	 * @return le second élément du couple
	 */
	public Term getTermY() {
		return y;
	}
	
	/**
	 * Modifie le second élément du couple
	 * @param x un terme
	 */
	public void setTermY(Term y) {
		this.y = y;
	}
	
	public String toString(){
		return "("+x +", "+ y+")";
	}
}
