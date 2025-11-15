package edu.grinnell.csc207.lootgenerator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {
    /** The path to the dataset (either the small or large set). */
    private static final String DATA_SET = "data/small";

    public static ArrayList<Monster> monData;
    public static Map<String, Item> itemData;
    public static Map<String, TC> TCData;
    public static Map<Integer, String[]> PreData;
    public static Map<Integer, String[]> SufData;

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
        Integer trueDef;
        Integer sufBuff;
        Integer preBuff;
        String preType;
        String sufType;

        public Item(){}
        
        public Item(String name, String minDef, String maxDef){
            this.name = name;
            this.minDef = Integer.parseInt(minDef);
            this.maxDef = Integer.parseInt(maxDef);
        }

        public void setPre(String prefix){
            this.prefix = prefix;
        }

        public void setSuf(String suffix){
            this.suffix = suffix;
        }

        public void setDef(){
            Random rand = new Random();
            int dif = maxDef - minDef +1;
            trueDef = minDef + rand.nextInt(dif);
        }
        
    }

    public static class TC{
        String lootClass;
        String[] items = new String[3];
        public TC(String lootClass, String item1, String item2, String item3){
            this.lootClass = lootClass;
            items[0] = item1;
            items[1] = item2;
            items[2] = item3;
        }
    }

    /**
     * Stores the monster data from a given file in monData
     * @param path : given file path
     */
    public static void makeMonData(String path){
        Scanner input = new Scanner(path + "/monstats.txt");
        while(input.hasNextLine()){
            Monster temp = new Monster(input.next(), input.next(), input.next(), input.next());
            monData.add(temp);
        }
        input.close();
    } 
    
    /**
     * Stores the item data from a given file in itemData
     * @param path : given file path
     */    
    public static void makeItemData(String path){
        Scanner input = new Scanner(path + "/armor.txt");
        while(input.hasNextLine()){
            Item temp = new Item(input.next(), input.next(), input.next());
            itemData.put(temp.name, temp);
        }
        input.close();
    } 

    /**
     * Stores the Treasure class data from a given file in TCData
     * @param path : given file path
     */    
    public static void makeTCData(String path){
        Scanner input = new Scanner(path + "/TreasureClassEx.txt");
        while(input.hasNextLine()){
            TC temp = new TC(input.next(), input.next(), input.next(), input.next());
            TCData.put(temp.lootClass, temp);
        }
        input.close();
    } 

    public static void makePreData(String path){
        Scanner input = new Scanner(path + "/MagicPrefix.txt");
        int count = 0;
        while(input.hasNextLine()){
            int idx = count;
            String[] data = {input.next(), input.next(), input.next()};
            PreData.put(idx, data);
            count++;
        }
        input.close();
    } 

    public static void makeSufData(String path){
        Scanner input = new Scanner(path + "/MagicSuffix.txt");
        int count = 0;
        while(input.hasNextLine()){
            int idx = count;
            String[] data = {input.next(), input.next(), input.next()};
            SufData.put(idx, data);
            count++;
        }
        input.close();
    } 

    /**
     * Creates a random monster based on the data in monData
     * @return a random monster
     */
    public static Monster getMonster(){
        Random rand = new Random();
        int idx = rand.nextInt(monData.size());
        return monData.get(idx);
    }

    /**
     * Generates a random item within the given Treasure Class
     * @param TC : Treasure Class 
     * @returns the string of a randomly chosen item from the treasure class  
     */
    public static Item GenerateBaseItem(String TC){
        Random rand = new Random();
        if(TCData.containsValue(TC)){
            int idx = rand.nextInt(3);
            Item ret = new Item();
            ret.name = TCData.get(TC).items[idx];
            ret.minDef = itemData.get(ret.name).minDef;
            ret.maxDef = itemData.get(ret.name).maxDef;
            return ret;
        }
        return null;
    }

    public static void generateAffix(Item item){
        Random rand = new Random();
        int chance = rand.nextInt(2);
        int idx = rand.nextInt(PreData.size());
        Integer buffMin;
        Integer buffMax;

        if(chance > 0){
            item.prefix = PreData.get(idx)[0];
            item.preType = PreData.get(idx)[1];
            buffMin = Integer.parseInt(PreData.get(idx)[2]);
            buffMax = Integer.parseInt(PreData.get(idx)[3]);
            int buffDif = buffMax - buffMin +1;
            item.preBuff = buffMin + rand.nextInt(buffDif);
        }else{
            item.prefix = "";
            item.preType = "";
            item.preBuff = 0;
        }

        chance = rand.nextInt(2);
        idx = rand.nextInt(SufData.size());

        if(chance > 0){
            item.suffix = SufData.get(idx)[0];
            item.sufType = SufData.get(idx)[1];
            buffMin = Integer.parseInt(SufData.get(idx)[2]);
            buffMax = Integer.parseInt(SufData.get(idx)[3]);
            int buffDif = buffMax - buffMin +1;
            item.sufBuff = buffMin + rand.nextInt(buffDif);
        }else{
            item.suffix = "";
            item.sufType = "";
            item.sufBuff = 0;
        }
    }

    public static void helper(){
        Monster enemy = getMonster();
        System.out.println("Fighting " + enemy.name);
        System.out.println("You have slain  " + enemy.name + "!");
        System.out.println(enemy.name + "dropped:");
        Item drop = GenerateBaseItem(enemy.itemClass);
        drop.setDef();
        generateAffix(drop);
        System.out.println(drop.prefix + " " + drop.name + " " + drop.suffix);
        System.out.println("Defense: " + drop.trueDef);
        if(drop.prefix != ""){
            System.out.println(drop.preBuff + drop.preType);
        }
        if(drop.suffix != ""){
            System.out.println(drop.sufBuff + drop.sufType);
        }
        
    }

    public static void main(String[] args) {
        System.out.println("This program kills monsters and generates loot!");
        makeItemData(DATA_SET);
        makeMonData(DATA_SET);
        makeSufData(DATA_SET);
        makePreData(DATA_SET);
        makeTCData(DATA_SET);
        Scanner input = new Scanner(System.in);
        boolean running = true;
        String reply;
        helper();
        while(running){
            System.out.print("Fight again [y/n]?");
            reply = input.next();
            if(reply.equals("N") || reply.equals("n")){
                running = false;
            } else if(reply.equals("Y") || reply.equals("y")){
                helper();
            } else{
                System.out.println("Please enter a valid character.");
            }
        }
        



        input.close();
    }
}
