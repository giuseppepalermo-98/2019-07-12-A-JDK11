package it.polito.tdp.food.model;

public class FoodCalories implements Comparable <FoodCalories> {

	private Food food;
	private Double calorie;
	
	public FoodCalories(Food food, Double calorie) {
		super();
		this.food = food;
		this.calorie = calorie;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Double getCalorie() {
		return calorie;
	}

	public void setCalorie(Double calorie) {
		this.calorie = calorie;
	}

	@Override
	public int compareTo(FoodCalories other) {
		//ORDINA INVERSO DECRESCENTE
		return -this.calorie.compareTo(other.calorie);
	}
	
	
}

