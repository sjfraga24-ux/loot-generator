package edu.grinnell.csc207.lootgenerator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import edu.grinnell.csc207.lootgenerator.LootGenerator.Item;

public class Tests {
    @Test
    public void trueDefInRange(){
        Boolean ret = false;
        Item ex = new Item("TestItem", "17", "29");
        ex.setDef();
        if(ex.trueDef <= 29 && ex.trueDef >= 17){
            ret = true;
        }
        assertEquals(true, ret);
    }

    @Test
    public void PreSufBuffExists() throws IOException{
        LootGenerator.makePreData("data/small");
        LootGenerator.makeSufData("data/small");
        LootGenerator.makeTCData("data/small");
        LootGenerator.makeItemData("data/small");
        Item ex = LootGenerator.GenerateBaseItem("Cow (H)");
        boolean ret = false;
        LootGenerator.generateAffix(ex);
        while(ex.prefix.equals("") || ex.suffix.equals("")){
            LootGenerator.generateAffix(ex);
        }
        if(ex.preBuff != 0 && ex.sufBuff != 0){
            ret = true;
        }
        assertEquals(true, ret);
    }

    @Test
    public void ItemsGeneratedSuccessfully() throws IOException{
        LootGenerator.makeTCData("data/large");
        LootGenerator.makeItemData("data/large");
        Item ex = LootGenerator.GenerateBaseItem("Trapped Soul");
        Boolean ret = false;
        if(!ex.name.equals("") && ex.minDef != 0 && ex.maxDef != 0){
            ret = true;
        }
        assertEquals(true, ret);
    }

}
