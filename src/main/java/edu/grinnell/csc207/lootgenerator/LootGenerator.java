package edu.grinnell.csc207.lootgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {
    /** The path to the dataset (either the small or large set). */
    private static final String DATA_SET = "data/small";

    public static ArrayList<Monster> monData = new ArrayList<>();
    public static Map<String, Item> itemData = new HashMap<>();
    public static Map<String, TC> TCData = new HashMap<>();
    public static Map<Integer, String[]> PreData = new HashMap<>();
    public static Map<Integer, String[]> SufData = new HashMap<>();

    // A class that stores variables for monsters
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


    // A class that stores methods and variables for items
    public static class Item{
        String prefix = "";
        String suffix = "";
        String name = "";
        int minDef;
        int maxDef;
        int trueDef;
        int sufBuff;
        int preBuff;
        String preType = "";
        String sufType = "";

        public Item(){}
        
        public Item(String name, String minDef, String maxDef){
            this.name = name;
            this.minDef = Integer.parseInt(minDef);
            this.maxDef = Integer.parseInt(maxDef);;
        }

        /**
         * Sets the prefix to the given value
         * @param prefix
         */
        public void setPre(String prefix){
            this.prefix = prefix;
        }

        /**
         * Sets the suffix to the given value
         * @param suffix
         */
        public void setSuf(String suffix){
            this.suffix = suffix;
        }

        /**
         * Finds a def val between minDef and maxDef(inclusive) then sets it to trueDef
         */
        public void setDef(){
            Random rand = new Random();
            int dif = maxDef - minDef +1;
            trueDef = minDef + rand.nextInt(dif);
        }
        
    }

    // A class that stores methods and variables for the Treasure Class
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
     * @throws IOException 
     */
    public static void makeMonData(String path) throws IOException{
        Scanner input = new Scanner(Paths.get(path + "/monstats.txt"));
        input.useDelimiter("\t|   ");
        while(input.hasNext()){
            String name = input.next();
            System.out.println(name);
            String type = input.next();
            System.out.println(type);
            String lvl = input.next().replaceAll("\\s", "");
            System.out.println(lvl);
            String itmClass =  input.nextLine().trim();
            System.out.println(itmClass);
            Monster temp = new Monster(name, type, lvl, itmClass);
            monData.add(temp);
        }
        input.close();
    } 
    
    /**
     * Stores the item data from a given file in itemData
     * @param path : given file path
     * @throws IOException 
     */    
    public static void makeItemData(String path) throws IOException{
        Scanner input = new Scanner(Paths.get(path + "/armor.txt"));
        input.useDelimiter("\t|   ");
        while(input.hasNext()){
            String itmName = input.next();
            String minDef = input.next();
            String maxDef = input.nextLine().replaceAll("\\s", "");
            Item temp = new Item(itmName, minDef, maxDef);
            itemData.put(temp.name, temp);
        }
        input.close();
    } 

    /**
     * Stores the Treasure class data from a given file in TCData
     * @param path : given file path
     * @throws IOException 
     */    
    public static void makeTCData(String path) throws IOException{
        Scanner input = new Scanner(Paths.get(path + "/TreasureClassEx.txt"));
        input.useDelimiter("\t|   ");
        while(input.hasNextLine()){
            String tc = input.next();
            String arm01 = input.next();
            String arm02 = input.next();
            String arm03 = input.nextLine().replaceAll("\t", "");
            TC temp = new TC(tc, arm01, arm02, arm03);
            TCData.put(temp.lootClass, temp);
        }
        input.close();
    } 

    /**
     * Stores the Magic Prefix data from a given file in TCData
     * @param path : given file path
     * @throws IOException 
     */    
    public static void makePreData(String path) throws IOException{
        Scanner input = new Scanner(Paths.get(path + "/MagicPrefix.txt"));
        input.useDelimiter("\t|   ");
        int count = 0;
        while(input.hasNextLine()){
            int idx = count;
            String pre = input.next();
            String stat = input.next();
            String minBuff = input.next().replaceAll("\\s", "");
            String maxBuff = input.nextLine().replaceAll("\\s", "");
            String[] data = {pre, stat, minBuff, maxBuff};
            PreData.put(idx, data);
            count++;
        }
        input.close();
    } 

    /**
     * Stores the Magic Suffix data from a given file in TCData
     * @param path : given file path
     * @throws IOException 
     */    
    public static void makeSufData(String path) throws IOException{
        Scanner input = new Scanner(Paths.get(path + "/MagicSuffix.txt"));
        input.useDelimiter("\t|   ");
        int count = 0;
        while(input.hasNextLine()){
            int idx = count;
            String suf = input.next();
            String stat = input.next();
            String minBuff = input.next().replaceAll("\\s", "");
            String maxBuff = input.nextLine().replaceAll("\\s", "");
            String[] data = {suf, stat, minBuff, maxBuff};
            SufData.put(idx, data);
            count++;
        }
        input.close();
    } 

    /**
     * Creates a random monster based on the data in monData
     * @returns a random monster
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
        int idx = rand.nextInt(3);
        Item ret = new Item();
        if(TCData.containsKey(TC) || itemData.containsKey(TC)){
            if(itemData.containsKey(TC)){
                ret.name = TC;
                ret.minDef = itemData.get(ret.name).minDef;
                ret.maxDef = itemData.get(ret.name).maxDef;
                return ret;
            } else if(TCData.containsKey(TCData.get(TC).items[idx]) || TCData.containsKey(TC)){
                String newTC = TCData.get(TC).items[idx];
                return GenerateBaseItem(newTC);
                
            } else{
                return null;
            }
        } else{
            return null;
        }
    }

    /**
     * Determines whether Item has a prefix or suffix.
     * If it does, then generates a random prefix and/or suffix.
     * @param item : The item that is getting a prefix or suffix
     */
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

    /**
     * A helper function that runs the monster fighting simulation.
     */
    public static void helper(){
        Monster enemy = getMonster();
        System.out.println("Fighting " + enemy.name);
        System.out.println("You have slain " + enemy.name + "!");
        System.out.println(enemy.name + " dropped:");
        Item drop = GenerateBaseItem(enemy.itemClass);
        drop.setDef();
        generateAffix(drop);
        String pre = drop.prefix;
        String suf = drop.suffix;
        String nme = drop.name;
        if(pre.equals("")){
            System.out.println(nme + " " + suf);
        }else{
            System.out.println(pre + " " + nme + " " + suf);
        }
        
        
        System.out.println("Defense: " + drop.trueDef);
        if(drop.prefix != ""){
            System.out.println(drop.preBuff + " " + drop.preType);
        }
        if(drop.suffix != ""){
            System.out.println(drop.sufBuff + " " + drop.sufType);
        }
        
    }

    /**
     * A program that kills monsters and generates loot until the player exits
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
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
