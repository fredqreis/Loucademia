package br.com.softblue.loucademia.application.util;

public class StringUtils {
	
	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}
		
		return s.trim().length() == 0;
	}
	
	// Concatenando 'zeros' a esquerda para gerar número de matrícula para alunos com 8 dígitos
	public static String leftZerous(int value, int finalSize) {
		return String.format("%0" + finalSize + "d", value);
	}
	
	public static void main(String[] args) {
		String str = "  ";
		boolean b = StringUtils.isEmpty(str);
		
		System.out.println(b);
		
		int v = 100;
		System.out.println(StringUtils.leftZerous(v, 6));
	}

}
