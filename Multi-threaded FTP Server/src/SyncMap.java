import java.util.HashMap;
import java.util.Map;

public class SyncMap {

    Map<String, Boolean> map = new HashMap<>();

    public synchronized void put (String str, boolean bool){
        map.put(str, bool);
    }

    public synchronized boolean get (String id) {
        return map.get(id);
    }

    public synchronized void replace(String id, boolean value){
        map.replace(id, value);
    }

    public synchronized boolean remove(String id){
        boolean last = get(id);
        map.remove(id);
        return last;
    }

    public synchronized boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public Map getMap(){
        return map;
    }

}
