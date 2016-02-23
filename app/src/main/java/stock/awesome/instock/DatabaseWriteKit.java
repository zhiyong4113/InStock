package stock.awesome.instock;


import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class DatabaseWriteKit {

    Firebase database = null;
    String id = null;
    int qty = -1;

    public DatabaseWriteKit(Firebase database) {
        this.database = database;
    }

    public void writeKit(Kit kit) {
        Firebase ref = database.child("kits").child(kit.getKitName());

        HashMap<String, Integer> kitHashMap = kit.getHashMap();
        Map<String, String> newKit = new HashMap<String, String>();

        for (Map.Entry<String, Integer> entry : kitHashMap.entrySet())  {
            id = entry.getKey();
            qty = entry.getValue();

            newKit.put("id", id);
            newKit.put("quantity", Integer.toString(qty));

        }

        ref.setValue(newKit);
    }

}