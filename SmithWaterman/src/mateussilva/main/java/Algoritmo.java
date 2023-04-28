package mateussilva.main.java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Algoritmo {

	/* Autor: Mateus José da Silva n° 23 */

	private static int match, mismatch, gap, sizeLinha, sizeColuna; /* variaveis para o calculo do score */
	private static int[][] matrizScore; /* matriz dos scores */
	private static int[][] prevCells; /* matriz das direçoes */
	private static String seq1, seq2; /* sequencias que serão comparadas */
	private static String seq1Inv, seq2Inv; /* alinhamento final */
	static final int left = 1, up = 2, diag = 3, zero = 4; /* Constantes que representam as direções */

	/* metodo que ler o arquivo de entrada. */
	private void lerEntrada() {
		try {
			List<String> infos = new ArrayList<String>();
			String linha;

			BufferedReader entrada = new BufferedReader(new FileReader(".//src//mateussilva//main//java//input.txt"));
			while ((linha = entrada.readLine()) != null) {
				infos.add(linha);
			}
			entrada.close();

			/* atribui as informações lidas as variaveis */
			seq1 = infos.get(0);
			seq2 = infos.get(1);
			sizeLinha = seq1.length();
			sizeColuna = seq2.length();
			match = Integer.parseInt(infos.get(2));
			mismatch = Integer.parseInt(infos.get(3));
			gap = Integer.parseInt(infos.get(4));

		} catch (IOException e) {
			System.err.println("Error: " + e);
		}
	}

	/* metodo que escreve o arquivo de saida. */
	private void escreverSaida() {
		try {
			BufferedWriter saida = new BufferedWriter(new FileWriter(".//src//mateussilva//main//java//output.txt"));
			saida.write(seq1Inv + "\n");
			saida.write(seq2Inv + "\n");
			saida.write(scoreMaximo() + "\n");
			saida.write(match + "\n");
			saida.write(mismatch + "\n");
			saida.write(gap + "");
			saida.close();
		} catch (IOException e) {
			System.err.println("Error: " + e);
		}
	}

	/* cria e calcula os scores de cada celula da matriz */
	private void criarMatriz() {
		if (gap >= 0) {
			throw new Error("O gap deve ser negativo!");
		}

		matrizScore = new int[sizeLinha + 1][sizeColuna + 1];
		prevCells = new int[sizeLinha + 1][sizeColuna + 1];

		int i, j;

		matrizScore[0][0] = 0;
		prevCells[0][0] = zero;

		for (i = 1; i <= sizeLinha; i++) { // primeira coluna
			matrizScore[i][0] = i * gap;
			prevCells[i][0] = up;
		}

		for (j = 1; j <= sizeColuna; j++) { // primeira linha
			matrizScore[0][j] = j * gap;
			prevCells[0][j] = left;
		}

		for (i = 1; i <= sizeLinha; i++) {
			for (j = 1; j <= sizeColuna; j++) {
				/* calcula o valor de cada celula a partir dos scores anteriores */
				int diagScore = matrizScore[i - 1][j - 1] + verificaSimilaridade(i, j);
				int upScore = matrizScore[i - 1][j] + verificaSimilaridade(0, j);
				int leftScore = matrizScore[i][j - 1] + verificaSimilaridade(i, 0);

				/* verifica o maior valor */
				matrizScore[i][j] = Math.max(diagScore, Math.max(upScore, leftScore));

				if (diagScore == matrizScore[i][j]) {
					prevCells[i][j] = diag;
				}
		
				if (leftScore == matrizScore[i][j]) {
					prevCells[i][j] = left;
				}
				if (upScore == matrizScore[i][j]) {
					prevCells[i][j] = up;
				}
			}
		}

		backtrace();
	}

	/* verifica se deu match ou mismatch */
	private static int verificaSimilaridade(int i, int j) {
		if (i == 0 || j == 0) {
			return gap;
		}
		return (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? match : mismatch;
	}

	/* metodo que retorna o maior score da matriz. */
	private static int scoreMaximo() {
		int maxScore = 0;
		for (int i = 1; i <= sizeLinha; i++) {
			for (int j = 1; j <= sizeColuna; j++) {
				if (matrizScore[i][j] > maxScore) {
					maxScore = matrizScore[i][j];
				}
			}
		}
		return maxScore;
	}

	private void printMatrizScore() {
		System.out.println("Matriz de scores: \n");
		for (int i = 0; i <= sizeLinha; i++) {
			for (int j = 0; j <= sizeColuna; j++) {
				System.out.print(matrizScore[i][j] + " ");
			}
			System.out.println();
		}
	}

	private void printMatrizDirecao() {
		System.out.println("\nMatriz de backtrace: \n");
		for (int i = 0; i <= sizeLinha; i++) {
			for (int j = 0; j <= sizeColuna; j++) {
				System.out.print(prevCells[i][j] + " ");
			}
			System.out.println();
		}
	}

	private void backtrace() {

		int linha = sizeLinha, coluna = sizeColuna;
		String s1 = "";
		String s2 = "";

		while (prevCells[linha][coluna] != zero) {
			if (prevCells[linha][coluna] == diag) {
				linha -= 1;
				coluna -= 1;
				s1 += seq1.charAt(linha);
				s2 += seq2.charAt(coluna);
			}
			if (prevCells[linha][coluna] == up) {
				linha -= 1;
				s1 += seq1.charAt(linha);
				s2 += "-";
			}
			if (prevCells[linha][coluna] == left) {
				coluna -= 1;
				s1 += "-";
				s2 += seq2.charAt(coluna);
			}
		}
		seq1Inv = new StringBuilder(s1).reverse().toString();
		seq2Inv = new StringBuilder(s2).reverse().toString();
	}

	public static void main(String[] args) {

		Algoritmo alg = new Algoritmo();

		alg.lerEntrada();
		alg.criarMatriz();
		// alg.printMatrizScore();
		// alg.printMatrizDirecao();
		alg.escreverSaida();

	}

}