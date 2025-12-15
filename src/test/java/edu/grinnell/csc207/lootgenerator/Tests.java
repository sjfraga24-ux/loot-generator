package edu.grinnell.csc207.lootgenerator;

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
    public void (){

        assertEquals(true, ret);
    }

}
