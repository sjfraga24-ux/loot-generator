package edu.grinnell.csc207.lootgenerator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class LootGenerator {
    /** The path to the dataset (either the small or large set). */
    private static final String DATA_SET = "data/small";
    public static ArrayList<Monster> monData;
    public static Map<String, Item> itemData;

    public static class Monster{
        String name;
        String type;
        Integer level;
        String itemClass;
        public Monster(String name, String type, String level, String itemClass){
            this.name = name;
            this.type = type;
            this.level = Integer.parseInt(level);
            this.itemClass = itemClass;
        }
    }

    public static class Item{
        String prefix;
        String suffix;
        String name;
        Integer minDef;
        Integer maxDef;

        public Item(String name, String minDef, String maxDef){
            this.name = name;
            this.minDef = Integer.parseInt(minDef);
            this.maxDef = Integer.parseInt(maxDef);
        }
        
    }

    public static void makeMonData(String path){
        Scanner input = new Scanner(path + "/monstats.txt");
        while(input.hasNextLine()){
            Monster temp = new Monster(input.next(), input.next(), input.next(), input.next());
            monData.add(temp);
        }
        input.close();
    } 
    
    public static void makeItemData(String path){
        Scanner input = new Scanner(path + "/.txt");
        while(input.hasNextLine()){
            Item temp = new Item(input.next(), input.next(), input.next());
            itemData.put(temp.name, temp);
        }
        input.close();
    } 


    public static void main(String[] args) {
        System.out.println("This program kills monsters and generates loot!");
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while(running){
            System.out.print("Fight again [y/n]?");
            if(input.next().equals("Y") || input.next().equals("y")){
                //run program
            } else{
                running = false;
            }
        }
        



        input.close();
    }
}
