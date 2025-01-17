package gui;

import java.util.Scanner;


public class InAppCli {
	Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		InAppCli cli = new InAppCli();
		cli.displayInterface();
	}
	
	private void displayInterface() {
		displayTitle();
		displayOptions();
		listenInput();
	}
	private void displayTitle() {
		System.out.println("*************************************");
		System.out.println("*                                   *");
		System.out.println("*          Features Model           *");
		System.out.println("*                                   *");
		System.out.println("*************************************");
		System.out.println();
		System.out.println("by Antonio Pedro Fraga, Francisco Rodrigues and Filipa Barroso");
		System.out.println();
		System.out.println();
	}
	
	private void displayOptions() {
		System.out.println("1. Load Feature Model");
		System.out.println("2. Generate valid configs");
		System.out.println("3. Validate config");
		System.out.println("4. Exit");
		System.out.println();
	}
	
	private void listenInput() {
		System.out.println("Insert option followed by enter: ");
		int option = input.nextInt();
		
		if (option < 1 || option > 4){
			System.out.println("Not a valid selection");
			listenInput();
		} else {
			handleSelection(option);
		}
	}
	
	private void handleSelection(int option) {
		switch (option) {
		case 1:
			new GraphGUI("Graph Viewer", option);
			break;
		case 2:
			new GraphGUI("Validate Configuration", option);
			break;
		case 3:
			new GraphGUI("Validate Configuration", option);
			break;
		case 4:
			System.exit(1);
			break;
		}
		
		displayInterface();
	}
}
