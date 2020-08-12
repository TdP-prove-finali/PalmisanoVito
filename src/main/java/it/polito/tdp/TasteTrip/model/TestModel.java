package it.polito.tdp.TasteTrip.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		for(String s : m.getNomi()) {
			System.out.println(s);
		}

	}

}
